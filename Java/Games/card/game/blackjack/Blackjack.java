package card.game.blackjack ;

import card.Card ;
import card.deck.Shoe ;

import java.util.Set ;
import java.util.Collections ;
import java.util.List ;
import java.util.LinkedList ;

public class Blackjack
{

    /* Playing objects */
    private BlackjackRules        rules_         = null ;
    private List<BlackjackPlayer> players_       = null ;
    private Shoe                  cardShoe_      = null ;

    private boolean verbose_ = false ;
    private int     hands_   = 0 ;

    public enum Move
    {
        Hit,
        Stand,
        Double,
        Split,
        Surrender,
        Unknown  // Not yet determined
    } ;

    public Blackjack(
        BlackjackRules        rules,
        List<BlackjackPlayer> players
    )
    {
        rules_    = rules ;
        players_  = players ;
        cardShoe_ = new Shoe(rules.getNumberOfDecks()) ;
        cardShoe_.shuffle() ;
    }

    public void setVerbose(boolean verbose) { verbose_ = verbose ; }

    public void play() throws Exception
    {
        while( !cardShoe_.reshuffleNeeded() )
        {
            // Deal - will return a hand for each player and the dealer's hand last
            BlackjackHand[] temphands = deal() ;

            // Take the hands and get them read to play
            //    Last hand is the dealers
            //    Then each player need a list of hands for if/when they split
            BlackjackHand dealerHand = temphands[temphands.length-1] ;
            List<List<BlackjackHand>> hands = new LinkedList<List<BlackjackHand>>() ;
            for(int i = 0 ; i < temphands.length-1 ; ++i)
            {
                hands.add(new LinkedList<BlackjackHand>()) ;
                hands.get(i).add(temphands[i]) ;
            }

            if(verbose_)
            {
                System.out.println("*** (Hand:" + (hands_+1) + ") ***") ;
                System.out.println("    Dealer starts with hand (upcard first): " + dealerHand + " ***" ) ;
            }

            Card dealerUpcard = dealerHand.getCard(0) ;

            // TODO: dealer Insurance
            boolean playersLeft = false ;

            if( dealerHand.getHandValue() != 21 )
            {
                // Index into our player and hand outter array
                int i = 0 ;
                for( BlackjackPlayer p : players_ )
                {
                    if(verbose_)
                    {
                        System.out.println("    (" + p.getName() + ") starts with hand : " + hands.get(i).get(0)) ;
                    }

                    List<BlackjackHand> playerHands = hands.get(i) ;
                    for(int j = 0 ; j < playerHands.size() ; ++j)
                    {
                        // Determine the move
                        Blackjack.Move move = p.getHandDecision(rules_,playerHands.get(j),dealerUpcard) ;

                        if(verbose_)
                        {
                            System.out.print("        ... move = " + move) ;
                        }

                        // Split
                        //   replace the current hand w/ two hands and go again
                        if(Move.Split == move)
                        {
                            BlackjackHand toBeSplitHand = playerHands.remove(j) ;
                            BlackjackHand[] splitHands = new BlackjackHand[2] ;
                            splitHands[0] = new BlackjackHand(toBeSplitHand.getCard(0),cardShoe_.getNextCard()) ;
                            splitHands[1] = new BlackjackHand(toBeSplitHand.getCard(1),cardShoe_.getNextCard()) ;
                            playerHands.add(j,  splitHands[0]) ;
                            playerHands.add(j+1,splitHands[1]) ;
                            --j ;

                            if(verbose_)
                            {
                                System.out.println() ;
                                System.out.println("    (" + p.getName() + ") split resulted with (hand 1) : " + splitHands[0]) ;
                                System.out.println("    (" + p.getName() + ") split resulted with (hand 2) : " + splitHands[1]) ;
                            }
                        }
                        else
                        {
                            while(Move.Unknown   != move &&
                                  Move.Surrender != move &&
                                  Move.Stand     != move &&
                                  !playerHands.get(j).isBusted())
                            {
                                Card hit = cardShoe_.getNextCard() ;
                                if(verbose_)
                                {
                                    System.out.println(" got : " + hit) ;
                                }
                                playerHands.get(j).add(hit) ;

                                // one card on double
                                if(Move.Double == move)
                                {
                                    playerHands.get(j).doubleBet() ;
                                    break ;
                                }

                                // if we didn't bust, get another card
                                if(!playerHands.get(j).isBusted())
                                {
                                    move = p.getHandDecision(rules_,playerHands.get(j),dealerUpcard) ;
                                    if(verbose_)
                                    {
                                        System.out.print("        ... move = " + move) ;
                                    }
                                }
                            }
                            if(Move.Surrender == move)
                            {
                                playerHands.get(j).surrender() ;
                            }
                            if(verbose_)
                            {
                                if(Move.Stand == move || Move.Surrender == move)
                                {
                                    System.out.println() ;
                                }
                            }
                            if(verbose_)
                            {
                                System.out.println("        => ending hand value: " + playerHands.get(j).getHandValue() + (playerHands.get(j).isBusted()? " BUSTED":"")) ;
                            }

                            if(Move.Surrender != move && !playerHands.get(j).isBusted())
                            {
                                playersLeft = true ;
                            }
                        }
                    }
                    ++i ;
                }
            }

            //
            // Dealer hitting
            while( playersLeft &&
                   dealerHand.getHandValue() < 17 ||
                   (dealerHand.getHandValue() == 17 && dealerHand.isSoft()) && rules_.getDealerHitSoft17())
            {
                dealerHand.add(cardShoe_.getNextCard()) ;
            }

            //
            // Record the outcome
            if(verbose_)
            {
                System.out.println("    *** Dealer ends with hand : " + dealerHand + " ***" ) ;
            }
            int i = 0 ;
            for( BlackjackPlayer p : players_ )
            {

                List<BlackjackHand> playerHands = hands.get(i) ;
                List<BlackjackHand.Outcome> outcomes = new LinkedList<BlackjackHand.Outcome>() ;
                double[] weights = new double[playerHands.size()] ;
                for(int j = 0 ; j < playerHands.size() ; ++j)
                {
                    BlackjackHand.Outcome o = determineOutcome(playerHands.get(j),dealerHand) ;
                    if(verbose_)
                    {
                        System.out.println("        (" + p.getName() + ") (hand " + (j+1) + ") ends with hand : " + playerHands.get(j) + " : " + o) ;
                    }

                    // Add outcomes to a list so we can get good statistics
                    outcomes.add(o) ;

                    // Weight is 2   for all doubled hands
                    //           1.5 for all blackjacks
                    //           1   for all others
                    weights[j] = (playerHands.get(j).isDoubled() ? 2 : 1) ;

                    // Only get 3:2 on starting hands
                    if(playerHands.get(j).isBlackjack() &&
                       !dealerHand.isBlackjack()        &&
                       playerHands.size() == 1)
                    {
                        weights[j] = 1.5 ;
                    }
                }
                // record the outcome
                p.getStrategy().recordHandList(playerHands,dealerHand.getCard(0),outcomes,weights) ;
                ++i ;
            }
            if(verbose_)
            {
                System.out.println() ;
            }
            hands_++ ;
        }
    }

    private static BlackjackHand.Outcome determineOutcome(BlackjackHand player, BlackjackHand dealer)
    {
        BlackjackHand.Outcome o = BlackjackHand.Outcome.Push ;
        if( player.isSurrendered() || player.isBusted() || (!dealer.isBusted() && dealer.greaterThan(player)))
        {
            o = BlackjackHand.Outcome.Loss ;
        }
        else if( (player.isBlackjack() && !dealer.isBlackjack()) || dealer.isBusted() || dealer.lessThan(player))
        {
            o = BlackjackHand.Outcome.Win ;
        }
        return o ;
    }

    /*
     * A helper function that deals out the cards
     *   Creates an arrary with the last element being the dealer
     */
    private BlackjackHand[] deal()
    {
        BlackjackHand[] hands = new BlackjackHand[players_.size()+1] ;
        for( int i = 0 ; i < (players_.size()+1) ; ++i )
        {
            hands[i] = new BlackjackHand() ;
            hands[i].add(cardShoe_.getNextCard()) ;
        }
        for( int i = 0 ; i < (players_.size()+1) ; ++i )
        {
            hands[i].add(cardShoe_.getNextCard()) ;
        }
        return hands ;
    }

    public void shuffle()
    {
        cardShoe_.reshuffle() ;
    }
}

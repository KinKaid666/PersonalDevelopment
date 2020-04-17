package card.game.blackjack ;

import card.Card ;
import card.deck.Shoe ;

import java.util.Set ;
import java.util.Collections ;
import java.util.List ;
import java.util.LinkedList ;

public class Blackjack
{

    private BlackjackRules        rules_         = null ;
    private List<BlackjackPlayer> players_       = null ;
    private Shoe                  cardShoe_      = null ;

    /* stats */
    private int hands_  = 0 ;
    private int wins_   = 0 ;
    private int losses_ = 0 ;
    private int pushes_ = 0 ;

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

    public void play()
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

            System.out.println("*** (Hand:" + ((hands_/players_.size())+1) + ") ***") ;
            System.out.println("    Dealer starts with hand (upcard first): " + dealerHand + " ***" ) ;

            Card dealerUpcard = dealerHand.getCard(0) ;

            // TODO: dealer Insurance
            boolean playersLeft = false ;

            if( dealerHand.getHandValue() != 21 )
            {
                // Index into our player and hand outter array
                int i = 0 ;
                for( BlackjackPlayer p : players_ )
                {
                    System.out.println("    (" + p.getName() + ") starts with hand : " + hands.get(i).get(0)) ;

                    List<BlackjackHand> playerHands = hands.get(i) ;
                    for(int j = 0 ; j < playerHands.size() ; ++j)
                    {
                        // Determine the move
                        Blackjack.Move move = p.getHandDecision(rules_,playerHands.get(j),dealerUpcard) ;

                        System.out.print("        ... move = " + move) ;

                        // Split
                        //   replace the current hand w/ two hands and go again
                        if(Move.Split == move)
                        {
                            BlackjackHand toBeSplitHand = playerHands.remove(j) ;
                            BlackjackHand[] splitHands = new BlackjackHand[2] ;
                            splitHands[0] = new BlackjackHand(toBeSplitHand.getCard(0),cardShoe_.getNextCard()) ;
                            splitHands[1] = new BlackjackHand(toBeSplitHand.getCard(1),cardShoe_.getNextCard()) ;
                            playerHands.add(j,splitHands[0]) ;
                            playerHands.add(j,splitHands[1]) ;
                            --j ;
                            System.out.println() ;
                        }
                        else
                        {
                            while(Move.Surrender != move && Move.Stand != move && !playerHands.get(j).busted())
                            {
                                Card hit = cardShoe_.getNextCard() ;
                                System.out.println(" got : " + hit) ;
                                playerHands.get(j).add(hit) ;

                                // one card on double
                                if(Move.Double == move)
                                {
                                    break ;
                                }

                                // if we didn't bust, get another card
                                if(!playerHands.get(j).busted())
                                {
                                    move = p.getHandDecision(rules_,playerHands.get(j),dealerUpcard) ;
                                    System.out.print("        ... move = " + move) ;
                                }
                            }
                            if(Move.Stand == move || Move.Surrender == move)
                                System.out.println() ;
                            System.out.println("        => ending hand value: " + playerHands.get(j).getHandValue() + (playerHands.get(j).busted()? " BUSTED":"")) ;

                            if(Move.Surrender != move && !playerHands.get(j).busted())
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
            int i = 0 ;
            System.out.println("    *** Dealer ends with hand : " + dealerHand + " ***" ) ;
            for( BlackjackPlayer p : players_ )
            {

                List<BlackjackHand> playerHands = hands.get(i) ;
                for(int j = 0 ; j < playerHands.size() ; ++j)
                {
                    ++hands_ ;
                    System.out.print("        (" + p.getName() + ") (hand " + (j+1) + ") ends with hand : " + playerHands.get(j)) ;

                    // Surrender use case
                    if( playerHands.get(j).busted() )
                    {
                        System.out.println( " : loss (busted)" ) ;
                        ++losses_ ;
                    }
                    else if( dealerHand.busted() )
                    {
                        System.out.println( " : win (dealer busted)" ) ;
                        ++wins_ ;
                    }
                    else if( playerHands.get(j).lessThan(dealerHand))
                    {
                        System.out.println( " : loss" ) ;
                        ++losses_ ;
                    }
                    else if( playerHands.get(j).greaterThan(dealerHand) )
                    {
                        System.out.println( " : win" ) ;
                        ++wins_ ;
                    }
                    else if( playerHands.get(j).equals(dealerHand) )
                    {
                        System.out.println( " : push" ) ;
                        ++pushes_ ;
                    }
                    else
                    {
                        System.err.println( " : ???" ) ;
                        System.exit(1) ; // bad
                    }
                }
                ++i ;
            }
            System.out.println() ;
        }
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

    public void printStats()
    {
        System.out.printf("Stats : %,10d hands\n", hands_) ;
        System.out.printf("      : %,10d losses or %02.2f%%\n",losses_,(losses_*100.0/hands_)) ;
        System.out.printf("      : %,10d wins   or %02.2f%%\n",wins_,  (wins_  *100.0/hands_)) ;
        System.out.printf("      : %,10d pushes or %02.2f%%\n",pushes_,(pushes_*100.0/hands_)) ;
    } 

}

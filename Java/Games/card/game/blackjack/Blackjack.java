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
    private int                   numberOfDecks_ = 0 ;
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
        DoubleDown,
        Split,
        Surrender
    } ;

    public Blackjack(
        BlackjackRules        rules,
        List<BlackjackPlayer> players,
        Integer               numberOfDecks
    )
    {
        rules_ = rules ;
        players_ = players ;
        numberOfDecks_ = numberOfDecks ;
        cardShoe_ = new Shoe(numberOfDecks_) ;

        cardShoe_.shuffle() ;
    }

    public void play()
    {
        while( !cardShoe_.reshuffleNeeded() )
        {
            BlackjackHand[] hands = deal() ;
            int i = 0 ;

            //System.out.println("*** (Hand:" + numHands + ") ***") ;
            //System.out.println("    Dealer starts with hand (upcard first): " + hands[hands.length - 1] + " ***" ) ;
            for( BlackjackPlayer p : players_ )
            {
                //System.out.println("    (" + p.getName() + ") starts with hand : " + hands[i]) ;

                // Dumb initial version, only see upcard
                BlackjackHand dealerUpcardHand = new BlackjackHand() ;
                dealerUpcardHand.add(hands[hands.length-1].getCard(0)) ;
                while( hands[i].getHandValue() < 17 && dealerUpcardHand.getHandValue() >= 7)
                {
                    hands[i].add(cardShoe_.getNextCard()) ;
                }
                ++i ;
            }
            // Dumb version
            while( hands[hands.length-1].getHandValue() < 17 )
            {
                hands[hands.length-1].add(cardShoe_.getNextCard()) ;
            }
            i = 0 ;
            //System.out.println("    *** Dealer ends with hand : " + hands[hands.length - 1] + " ***" ) ;
            for( BlackjackPlayer p : players_ )
            {
                ++hands_ ;
                //System.out.print("        (" + p.getName() + ") ends with hand : " + hands[i]) ;
                if( hands[i].busted() )
                {
                    //System.out.println( " : loss (busted)" ) ;
                    ++losses_ ;
                }
                else if( hands[hands.length-1].busted() )
                {
                    //System.out.println( " : win (dealer busted)" ) ;
                    ++wins_ ;
                }
                else if( hands[i].lessThan(hands[hands.length-1]))
                {
                    //System.out.println( " : loss" ) ;
                    ++losses_ ;
                }
                else if( hands[i].greaterThan(hands[hands.length-1]) )
                {
                    //System.out.println( " : win" ) ;
                    ++wins_ ;
                }
                else if( hands[i].equals(hands[hands.length-1]) )
                {
                    //System.out.println( " : push" ) ;
                    ++pushes_ ;
                }
                else
                {
                    System.out.println( " : ???" ) ;
                }
                ++i ;
            }
            //System.out.println() ;
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
        System.out.printf("      : %,10d losses or %02.2f%%\n",losses_,(losses_*1.0/hands_)) ;
        System.out.printf("      : %,10d wins   or %02.2f%%\n",wins_,  (wins_  *1.0/hands_)) ;
        System.out.printf("      : %,10d pushes or %02.2f%%\n",pushes_,(pushes_*1.0/hands_)) ;
    } 

}

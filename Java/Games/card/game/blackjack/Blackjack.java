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
        int numHands = 0 ;
        while( !cardShoe_.reshuffleNeeded() )
        {
            BlackjackHand[] hands = deal() ;
            int i = 0 ;

            System.out.println("*** (Hand: " + numHands + ") Dealer has hand : " + hands[hands.length - 1] + " ***" ) ;
            for( BlackjackPlayer p : players_ )
            {
                System.out.print("\t(" + p.getName() + ") has hand : " + hands[i]) ;

                if( hands[i].lessThan(hands[hands.length-1]) )
                {
                    System.out.println( " : loss" ) ;
                }
                else if( hands[i].greaterThan(hands[hands.length-1]) && !hands[i].busted() )
                {
                    System.out.println( " : win" ) ;
                }
                else if( hands[i].equals(hands[hands.length-1]) )
                {
                    System.out.println( " : push" ) ;
                }
                // Loop for play p
                // while not stay && hand is not busted
                // {
                //     implement move
                // }
                ++i ;
            }
            ++numHands ;
        }
        cardShoe_.shuffle() ;
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
}

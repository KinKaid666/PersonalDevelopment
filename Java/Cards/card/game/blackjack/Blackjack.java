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
        players_ = players_ ;
        numberOfDecks_ = numberOfDecks ;
        cardShoe_ = new Shoe(numberOfDecks_) ;

        shuffle() ;
    }

    public void shuffle()
    {
        cardShoe_.shuffle() ;
    }

    public void play()
    {
        while( deal() && !cardShoe_.reshuffleNeeded() )
        {
            for( BlackjackPlayer p : players_ )
            {
                // Loop for play p
                // while not stay && hand is not busted
                // {
                //     implement move
                // }
            }
        }
        shuffle() ;
    }

    public boolean deal()
    {
        return true ;
    }
}

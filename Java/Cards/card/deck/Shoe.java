package card.deck ;

import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import card.Card ;

public class Shoe extends Deck
{

    private int numberOfDecks_ ;

    /*
    ** Construct a new deck
    */
    public Shoe(int decks)
    {
        numberOfDecks_ = decks ;
        create() ;
    }

    public void create()
    {
        for( int i = 0 ; i < numberOfDecks_ ; ++i )
        {
            addAll((new PokerDeck()).getCards()) ;
        }
    }

    public void reshuffle()
    {
        create() ;
        shuffle() ;
    }

    /*
     * When is a reshuffle needed
     */
    public boolean reshuffleNeeded()
    {
        if(isEmpty())
        {
            return true ;
        }
        return false ;
    }
}

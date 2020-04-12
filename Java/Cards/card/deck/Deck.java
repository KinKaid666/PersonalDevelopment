package card.deck ;

import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import card.Card ;

public abstract class Deck
{

    private List<Card> deck_ = new LinkedList<Card>() ;

    /*
    ** Construct a new deck
    */
    public Deck()
    {
        shuffle() ;
    }

    /*
    ** Should this be called poll?
    */
    public Card getNextCard()
    {
        return deck_.remove(deck_.size() - 1) ;
    }

    /*
    ** Grab all the cards, helpful for multi-deck shoes
    */
    public List<Card> getCards()
    {
        return deck_ ;
    }

    /*
    ** Add one card
    */
    public boolean add(Card card)
    {
        return deck_.add(card) ;
    }

    /*
    ** Add a list of cards
    */
    public boolean addAll(List<Card> cards)
    {
        return deck_.addAll(cards) ;
    }

    public abstract boolean reshuffleNeeded() ;

    public Boolean isEmpty()
    {
        return deck_.isEmpty() ;
    }

    public void shuffle()
    {
        Collections.shuffle(deck_) ;
    }
}

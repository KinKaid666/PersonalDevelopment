package card.deck ;

import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import card.Card ;

public class PokerDeck extends Deck
{

    private List<Card> deck_ = new LinkedList<Card>() ;

    /*
    ** Construct a new deck
    */
    public PokerDeck()
    {
        deck_ = new LinkedList<Card>() ;
        for (Card.Suit suit : Card.Suit.values())
        {
            for (Card.Rank rank : Card.Rank.values())
            {
                add(Card.valueOf(rank, suit));
            }
        }
    }

    public boolean reshuffleNeeded()
    {
        return true ;
    }
}

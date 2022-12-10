package card.deck ;

import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import card.Card ;

public class PokerDeck extends Deck {

    /*
    ** Construct a new deck
    */
    public PokerDeck() {
        super() ;
        //deck_ = new LinkedList<Card>() ;
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                add(Card.valueOf(rank, suit));
            }
        }
        shuffle() ;
    }

    public boolean reshuffleNeeded() {
        return true ;
    }
}

package card.deck ;

import java.util.Collections ;
import java.util.LinkedList ;
import java.util.List ;
import card.Card ;

public class Shoe extends Deck {

    private Integer numberOfDecks_ ;
    private Integer numberOfCards_ ;
    private Double reshuffleAt_ ;
    protected static Double RESHUFFLE_AT_DEFAULT = 0.1 ;

    /*
    ** Construct a new deck
    */
    public Shoe(Integer decks) {
        this(decks,RESHUFFLE_AT_DEFAULT) ;
    }

    /*
    ** Construct a new deck
    */
    public Shoe(Integer decks, Double reshuffleAt) {
        numberOfDecks_ = decks ;
        numberOfCards_ = create() ;
        reshuffleAt_   = reshuffleAt ;
    }

    public Integer getNumberOfDecks() { return numberOfDecks_ ; }

    public Integer create() {
        for( int i = 0 ; i < numberOfDecks_ ; ++i ) {
            addAll((new PokerDeck()).getCards()) ;
        }
        // each poker deck will be shuffled upon creation
        // but we want the shoe shuffled too
        shuffle() ;
        return getCards().size() ;
    }

    public void reshuffle() {
        numberOfCards_ = create() ;
        shuffle() ;
    }

    /*
     * When is a reshuffle needed
     */
    public boolean reshuffleNeeded() {
        // If the deck is empty or we're below the reshuffleAt %
        if(isEmpty() || ((getCards().size()*1.0/numberOfCards_) < reshuffleAt_)) {
            return true ;
        }
        return false ;
    }
}

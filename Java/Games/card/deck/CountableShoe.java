package card.deck ;

import card.Card ;
import card.deck.Shoe ;

public class CountableShoe extends Shoe {
    private Integer count_ = 0 ;

    public CountableShoe(Integer decks) {
        super(decks, Shoe.RESHUFFLE_AT_DEFAULT) ;
    }

    public CountableShoe(Integer decks, Double reshuffleAt) {
        super(decks, reshuffleAt) ;
        //shuffle() ;
    }

    @Override
    public Card getNextCard() {
        Card c = super.getNextCard() ;
        if(c != null) {
            switch(c.getRank()) {
            case Deuce:
            case Three:
            case Four:
            case Five:
            case Six:
                count_++ ;
                break ;
            case Seven:
            case Eight:
            case Nine:
                break ;
            case Ten:
            case Jack:
            case Queen:
            case King:
            case Ace:
                count_-- ;
                break ;

            }
        }
        return c ;
    }

    public Integer getCount() {
        return count_ ;
    }

    public Double getTrueCount() {
        Integer cardsLeft = getNumberOfCardsLeft() ;
        if(cardsLeft == 0) {
            return 0.0 ;
        }

        return (count_ * 1.0) / (cardsLeft / 52.0)  ;
    }
}

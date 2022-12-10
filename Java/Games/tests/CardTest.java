package tests ;

import card.Card ;
import card.game.blackjack.* ;
import card.deck.* ;
import card.* ;

public class CardTest {
    public static void main(String args[]) {

        System.out.println("*** Single Card ***") ;
        Card aceOfSpades = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades) ;
        System.out.println(aceOfSpades) ;

        System.out.println("\n*** PokerDeck ***") ;
        PokerDeck deck = new PokerDeck() ;
        while(!deck.isEmpty()) {
            System.out.println(deck.getNextCard()) ;
        }

        System.out.println("\n*** Shoe 2 decks ***") ;
        Shoe shoe = new Shoe(2) ;
        while(!shoe.isEmpty()) {
            System.out.println(shoe.getNextCard()) ;
        }
    }
}

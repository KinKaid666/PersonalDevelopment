package card ;

import java.util.Map ;
import java.util.EnumMap ;

public class Card implements Comparable<Card> {
    public enum Rank {
        Deuce,
        Three,
        Four,
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Jack,
        Queen,
        King,
        Ace,
    }

    public enum Suit {
        Clubs,
        Diamonds,
        Hearts,
        Spades,
    }

    private static Map<Suit, Map<Rank, Card>> allPossibleCards =
        new EnumMap<Suit, Map<Rank, Card>>(Suit.class);
    static {
        for (Suit suit : Suit.values()) {
            Map<Rank, Card> allRanks = new EnumMap<Rank, Card>(Rank.class);
            for (Rank rank : Rank.values()) {
                allRanks.put(rank, new Card(rank, suit));
            }
            allPossibleCards.put(suit, allRanks);
        }
    }

    /*
    ** Effective java, value of
    */
    public static Card valueOf(Rank rank, Suit suit) {
        return allPossibleCards.get(suit).get(rank);
    }

    private final Rank rank ;
    private final Suit suit ;

    /*
    ** Private constructor so that we don't get more than 1 ace of space, et al
    */
    private Card(Rank rank, Suit suit) {
        this.rank = rank ;
        this.suit = suit ;
    }

    /*
     * Getters
     */
    public Rank getRank() { return rank ; }
    public Suit getSuit() { return suit ; }

    public String toString() {
        return rank + " of " + suit;
    }

    public String getRankString() {
        String rankAsString = "" ;
        switch(rank) {
        case Deuce:
            rankAsString = "2" ;
            break ;
        case Three:
            rankAsString = "3" ;
            break ;
        case Four:
            rankAsString = "4" ;
            break ;
        case Five:
            rankAsString = "5" ;
            break ;
        case Six:
            rankAsString = "6" ;
            break ;
        case Seven:
            rankAsString = "7" ;
            break ;
        case Eight:
            rankAsString = "8" ;
            break ;
        case Nine:
            rankAsString = "9" ;
            break ;
        case Ten:
            rankAsString = "10" ;
            break ;
        case Jack:
            rankAsString = "J" ;
            break ;
        case Queen:
            rankAsString = "Q" ;
            break ;
        case King:
            rankAsString = "K" ;
            break ;
        case Ace:
            rankAsString = "A" ;
            break ;
        }
        return rankAsString ;
    }

    @Override
    public final int compareTo(Card that) {
        return this.rank.ordinal() - that.rank.ordinal() ;
    }

}

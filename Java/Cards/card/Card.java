package card ;

import java.util.Map ;
import java.util.EnumMap ;

public class Card
{
    public enum Rank
    {
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

    public enum Suit
    {
        Clubs,
        Diamonds,
        Hearts,
        Spades,
    }

    private static Map<Suit, Map<Rank, Card>> allPossibleCards =
        new EnumMap<Suit, Map<Rank, Card>>(Suit.class);
    static
    {
        for (Suit suit : Suit.values())
        {
            Map<Rank, Card> allRanks = new EnumMap<Rank, Card>(Rank.class);
            for (Rank rank : Rank.values())
            {
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
    private Card(Rank rank, Suit suit)
    {
        this.rank = rank ;
        this.suit = suit ;
    }

    /*
     * Getters
     */
    public Rank getRank() { return rank ; }
    public Suit getSuit() { return suit ; }

    public String toString()
    {
        return rank + " of " + suit;
    }

}

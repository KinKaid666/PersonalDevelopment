package card.game.blackjack ;

import card.Card ;

import java.util.LinkedList ;
import java.util.List ;

public class BlackjackHand
{

    public enum BlackjackHandValue
    {
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Eleven,
        Twelve,
        Thirteen,
        Fourteen,
        Fifteen,
        Sixteen,
        Seventeen,
        Eighteen,
        Nineteen,
        Twenty,
        SoftThirteen,
        SoftFourteen,
        SoftFifteen,
        SoftSixteen,
        SoftSeventeen,
        SoftEighteen,
        SoftNinteen,
        SoftTwenty,
        PairOfTwos,
        PairOfThree,
        PairOfFours,
        PairOfFives,
        PairOfSixes,
        PairOfSevens,
        PairOfEights,
        PairOfNines,
        PairOfTens,
        Blackjack
    } ;

    private List<Card> cardsInHand_ = null ;

    public BlackjackHand()
    {
        cardsInHand_ = new LinkedList<Card>() ;
    }

    public BlackjackHand(Card one, Card two)
    {
        cardsInHand_ = new LinkedList<Card>() ;
        cardsInHand_.add(one) ;
        cardsInHand_.add(two) ;
    }

    public BlackjackHandValue add(Card card)
    {
        cardsInHand_.add(card) ;
        return getHandValue() ;
    }

    /*
     * compareTo similar to Java's String::compareTo
     */
    public int compareTo(BlackjackHand other)
    {
        return 1 ;
    }

    public BlackjackHandValue getHandValue()
    {
        return BlackjackHandValue.Blackjack ;
    }

    public String toString()
    {
        return "" + getHandValue() ;
    }
}

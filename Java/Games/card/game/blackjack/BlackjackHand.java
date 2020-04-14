package card.game.blackjack ;

import card.Card ;

import java.util.LinkedList ;
import java.util.List ;

public class BlackjackHand
{
    public enum BlackjackHandValue
    {
        Four,
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
        SoftTwelve,
        SoftThirteen,
        SoftFourteen,
        SoftFifteen,
        SoftSixteen,
        SoftSeventeen,
        SoftEighteen,
        SoftNineteen,
        SoftTwenty,
        PairOfTwos,
        PairOfThrees,
        PairOfFours,
        PairOfFives,
        PairOfSixes,
        PairOfSevens,
        PairOfEights,
        PairOfNines,
        PairOfTens,
        Blackjack,
        Busted
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

    public void add(Card card)
    {
        cardsInHand_.add(card) ;
    }

    /*
     * compareTo similar to Java's String::compareTo
     */
    public int compareTo(BlackjackHand other)
    {
        int x = getHandValue(), y = other.getHandValue() ;
        if( x == y ) return 0 ;
        else if (x > y) return 1 ;
        return -1 ;
    }

    public boolean busted()
    {
        return getHandValue() > 21 ;
    }

    public boolean lessThan(BlackjackHand other)
    {
        return compareTo(other) == -1 ;
    }

    public boolean greaterThan(BlackjackHand other)
    {
        return compareTo(other) == 1 ;
    }

    public boolean equals(BlackjackHand other)
    {
        return compareTo(other) == 0 ;
    }

    public boolean isSoft()
    {
        for( Card c : cardsInHand_ )
        {
            if(c.getRank() == Card.Rank.Ace )
            {
                return true ;
            }
        }
        return false ;
    }

    /*
     * getStrategicHandValue is used when determining the players move
     */
    public BlackjackHandValue getStrategicHandValue()
    {
        int value = getHandValue() ;
        switch( value )
        {
            case 4:
                return BlackjackHandValue.PairOfTwos ;
            case 5:
                return BlackjackHandValue.Five ;
            case 6:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Three ) return BlackjackHandValue.PairOfThrees ;
                return BlackjackHandValue.Six ;
            case 7:
                return BlackjackHandValue.Seven ;
            case 8:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Four ) return BlackjackHandValue.PairOfFours ;
                return BlackjackHandValue.Eight ;
            case 9:
                return BlackjackHandValue.Nine ;
            case 10:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Five ) return BlackjackHandValue.PairOfFives ;
                return BlackjackHandValue.Ten ;
            case 11:
                return BlackjackHandValue.Eleven ;
            case 12:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Six ) return BlackjackHandValue.PairOfSixes ;
                if( isSoft() ) return BlackjackHandValue.SoftTwelve ;
                return BlackjackHandValue.Twelve ;
            case 13:
                if( isSoft() ) return BlackjackHandValue.SoftThirteen ;
                return BlackjackHandValue.Thirteen ;
            case 14:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Seven ) return BlackjackHandValue.PairOfSevens ;
                if( isSoft() ) return BlackjackHandValue.SoftFourteen ;
                return BlackjackHandValue.Fourteen ;
            case 15:
                if( isSoft() ) return BlackjackHandValue.SoftFifteen ;
                return BlackjackHandValue.Fifteen ;
            case 16:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Eight ) return BlackjackHandValue.PairOfEights ;
                if( isSoft() ) return BlackjackHandValue.SoftSixteen ;
                return BlackjackHandValue.Sixteen ;
            case 17:
                if( isSoft() ) return BlackjackHandValue.SoftSeventeen ;
                return BlackjackHandValue.Seventeen ;
            case 18:
                if(cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == Card.Rank.Nine ) return BlackjackHandValue.PairOfNines ;
                if( isSoft() ) return BlackjackHandValue.SoftEighteen ;
                return BlackjackHandValue.Eighteen ;
            case 19:
                if( isSoft() ) return BlackjackHandValue.SoftNineteen ;
                return BlackjackHandValue.Nineteen ;
            case 20:
                if(cardsInHand_.size() == 2 && (cardsInHand_.get(0).getRank() == Card.Rank.Ten   ||
                                                cardsInHand_.get(0).getRank() == Card.Rank.Jack  ||
                                                cardsInHand_.get(0).getRank() == Card.Rank.Queen ||
                                                cardsInHand_.get(0).getRank() == Card.Rank.King  ))
                    return BlackjackHandValue.PairOfTens ;
                if( isSoft() ) return BlackjackHandValue.SoftTwenty ;
                return BlackjackHandValue.Twenty ;
            case 21:
                return BlackjackHandValue.Blackjack ;
        }
        return BlackjackHandValue.Busted ;
    }

    /*
     * getHandValue is used when determining the value of the hand for comparison purposes
     */
    public int getHandValue()
    {
        int value = 0 ;
        boolean seenAce = false ;
        for( Card c : cardsInHand_ )
        {
            switch( c.getRank() )
            {
            case Ace:
                value += 1 ;
                seenAce = true ;
                break ;
            case Deuce:
                value += 2 ;
                break ;
            case Three:
                value += 3 ;
                break ;
            case Four:
                value += 4 ;
                break ;
            case Five:
                value += 5 ;
                break ;
            case Six:
                value += 6 ;
                break ;
            case Seven:
                value += 7 ;
                break ;
            case Eight:
                value += 8 ;
                break ;
            case Nine:
                value += 9 ;
                break ;
            case Ten:
            case Jack:
            case Queen:
            case King:
                value += 10 ;
                break ;
            default:
                System.err.println("Unknown card : " + c) ;
                System.exit(1) ;
            }
        }
        /* Handle Soft usecase */
        if( value < 12 && seenAce )
        {
            value += 10 ;
        }
        return value ;
    }

    public String toString()
    {
        int val = getHandValue() ;
        String a = "(" + (val<10?" ":"") + val + ") "+ getStrategicHandValue() ;
        for( int i = 0 ; i < cardsInHand_.size() ; ++i )
        {
            if(i ==0)
            {
                a += " : " ;
            }
            a += cardsInHand_.get(i) ;
            if(i < (cardsInHand_.size()-1) )
            {
                a += ", " ;
            }
        }
        return a ;
    }
}

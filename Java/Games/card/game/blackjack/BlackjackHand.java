package card.game.blackjack ;

import card.Card ;

import java.util.LinkedList ;
import java.util.List ;

public class BlackjackHand
{

    private Integer bet_         = null ;
    private boolean doubled_     = false ;
    private boolean surrendered_ = false ;
    public enum Outcome
    {
        Win,
        Loss,
        Push
    } ;

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
        TwentyOne,
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
        PairOfAces,
        Blackjack,
        Busted
    } ;

    private List<Card> cardsInHand_ = null ;

    /* Non-gambling constructor */
    public BlackjackHand()
    {
        cardsInHand_ = new LinkedList<Card>() ;
    }

    public BlackjackHand( int bet )
    {
        cardsInHand_ = new LinkedList<Card>() ;
        bet_ = bet ;
    }

    public BlackjackHand(Card one, Card two)
    {
        cardsInHand_ = new LinkedList<Card>() ;
        cardsInHand_.add(one) ;
        cardsInHand_.add(two) ;
    }

    public int cardCount()
    {
        return cardsInHand_.size() ;
    }

    public Card getCard(int card)
    {
        return cardsInHand_.get(card) ;
    }

    public boolean isPair()
    {
        return cardsInHand_.size() == 2 && cardsInHand_.get(0).getRank() == cardsInHand_.get(1).getRank() ;
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

    public boolean isBusted()
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

    public boolean isBlackjack()
    {
        return (cardCount() == 2 && getHandValue() == 21) ;
    }

    /*
     * it's soft when one of the aces is counting for 10
     *    -> need 1 or more aces
     *    -> need only 1 ace being worth more than 1
     */
    public boolean isSoft()
    {
        int valMinusAces = getHandValueMinusAces() ;
        int valWithAces  = getHandValue() ;
        int numAces      = 0 ;
        for( Card c : cardsInHand_ )
        {
            if(c.getRank() == Card.Rank.Ace )
            {
                ++numAces ;
            }
        }
        return (numAces > 0) && !(valWithAces == (valMinusAces + numAces)) ;
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
                if( cardsInHand_.size() == 2 && isSoft() ) return BlackjackHandValue.PairOfAces ;
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
                if(cardsInHand_.size() == 2) return BlackjackHandValue.Blackjack ;
                return BlackjackHandValue.TwentyOne ;
        }
        return BlackjackHandValue.Busted ;
    }

    /*
     * getHandValue is used when determining the value of the hand for comparison purposes
     *   hand Value is always the closet to 21 without going over;
     *   therefor always 17 and never 7 when it's A-6
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

    /*
     * getHandValue is used when determining the value of the hand for comparison purposes
     */
    private int getHandValueMinusAces()
    {
        int value = 0 ;
        for( Card c : cardsInHand_ )
        {
            switch( c.getRank() )
            {
            case Ace:
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
        return value ;
    }

    /* Setters */
    public void doubleBet()
    {
        if( bet_ != null )
        {
            bet_ *= 2 ;
        }
        doubled_ = true ;
    }

    public void surrender() { surrendered_ = true ; }

    /* Getters */
    public boolean isSurrendered() { return surrendered_ ; }
    public boolean isDoubled()     { return doubled_     ; }
    public int     bet()           { return bet_         ; }

    public String toString()
    {
        String a = "(" + getHandValue() + ") " + getStrategicHandValue() ;
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

    public static BlackjackHand createHand(BlackjackHand.BlackjackHandValue hv)
    {
        BlackjackHand h ;
        Card c1 = null, c2 = null, extra = null ;
        switch(hv)
        {
            case Five:
                c1 = Card.valueOf(Card.Rank.Deuce,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                break ;
            case Six:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                break ;
            case Seven:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Four, Card.Suit.Spades) ;
                break ;
            case Eight:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Five, Card.Suit.Spades) ;
                break ;
            case Nine:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Six,  Card.Suit.Spades) ;
                break ;
            case Ten:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                break ;
            case Eleven:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                break ;
            case Twelve:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Nine, Card.Suit.Spades) ;
                break ;
            case Thirteen:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Fourteen:
                c1 = Card.valueOf(Card.Rank.Four, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Fifteen:
                c1 = Card.valueOf(Card.Rank.Five, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Sixteen:
                c1 = Card.valueOf(Card.Rank.Six,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Seventeen:
                c1 = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Eighteen:
                c1 = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Nineteen:
                c1 = Card.valueOf(Card.Rank.Nine, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case Twenty:
                c1 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case TwentyOne:
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                c1 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftTwelve:
                c1 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                extra = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;

                break ;
            case SoftThirteen:
                c1 = Card.valueOf(Card.Rank.Deuce,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftFourteen:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftFifteen:
                c1 = Card.valueOf(Card.Rank.Four, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftSixteen:
                c1 = Card.valueOf(Card.Rank.Five, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftSeventeen:
                c1 = Card.valueOf(Card.Rank.Six,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftEighteen:
                c1 = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftNineteen:
                c1 = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case SoftTwenty:
                c1 = Card.valueOf(Card.Rank.Nine, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
            case PairOfTwos:
                c1 = Card.valueOf(Card.Rank.Deuce,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Deuce,Card.Suit.Spades) ;
                break ;
            case PairOfThrees:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                break ;
            case PairOfFours:
                c1 = Card.valueOf(Card.Rank.Four, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Four, Card.Suit.Spades) ;
                break ;
            case PairOfFives:
                c1 = Card.valueOf(Card.Rank.Five, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Five, Card.Suit.Spades) ;
                break ;
            case PairOfSixes:
                c1 = Card.valueOf(Card.Rank.Six,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Six,  Card.Suit.Spades) ;
                break ;
            case PairOfSevens:
                c1 = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                break ;
            case PairOfEights:
                c1 = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                break ;
            case PairOfNines:
                c1 = Card.valueOf(Card.Rank.Nine, Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Nine, Card.Suit.Spades) ;
                break ;
            case PairOfTens:
                c1 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                break ;
            case PairOfAces:
                c1 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                break ;
            case Blackjack:
                c1 = Card.valueOf(Card.Rank.Ten,  Card.Suit.Spades) ;
                c2 = Card.valueOf(Card.Rank.Ace,  Card.Suit.Spades) ;
                break ;
        }
        h = new BlackjackHand(c1,c2) ;
        if(extra != null)
        {
            h.add(extra) ;
        }
        return h ;
    }
}

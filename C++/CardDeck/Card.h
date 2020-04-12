#ifndef __CARD_H__
#define __CARD_H__

#include "Foundation Classes/FCRetainable.h"

#define MINSUIT Card::Spades ;
#define MAXSUIT Card::Diamonds ;
#define MINVALUE Card::Ace ;
#define MAXVALUE Card::King ;

class Card : public FCRetainable
{
public:
    enum Suit
    {
        Spades,
        Clubs,
        Hearts,
        Diamonds
    } ;

    enum Value
    {
        Ace,
        Two,
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
        King
    } ;

    Card( Suit suit, Value value )
        : suit_(suit), value_(value)
    { ; }

    Suit  GetSuit()  { return suit_  ; }
    Value GetValue() { return value_ ; }

private:
    Card() ;

private:
    Suit suit_ ;
    Value value_ ;
} ;

#endif /* __CARD_H__ */

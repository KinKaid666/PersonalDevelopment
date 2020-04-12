#include "CardDeck.h"

int CardDeck::Deck = 52 ;

CardDeck::CardDeck()
{
    for( int i = 0 ; i < Deck ; ++i )
    {
            deck_.push_back(P<Card>(new Card(Card::Suit(i/13),Card::Value(i%13)))) ;
    }
}

void
CardDeck::Shuffle()
{ ; }

P<Card>
CardDeck::GetNextCard()
{ ; }


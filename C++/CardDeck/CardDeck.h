#ifndef __CARDDECK_H__
#define __CARDDECK_H__

#include <vector>

#include "Foundation Classes/FCRetainable.h"
#include "Foundation Classes/FCSmartPointer.h"
#include "Card.h"

class CardDeck : public FCRetainable
{
public:
    static int Deck ;
    CardDeck() ;

    void Shuffle() ;

    P<Card> GetNextCard() ;

private:
    std::vector< P<Card> > deck_ ;
} ;

#endif /* __CARDDECK_H__ */

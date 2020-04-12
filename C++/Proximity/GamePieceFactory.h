#ifndef __GAMEPIECEFACTORY_H__
#define __GAMEPIECEFACTORY_H__

#include "GamePiece.h"

class GamePieceFactory
{
public:
    virtual GamePiece* createGamePiece(GamePiece::Color, int, int, int) = 0 ;
    virtual ~GamePieceFactory() { ; }
} ;

class ProximityGamePieceFactory
 : public GamePieceFactory
{
    virtual GamePiece* createGamePiece(GamePiece::Color color,
                                       int              value,
                                       int              min,
                                       int              max )
    { return new ProximityGamePiece(color, value, min, max ) ; }
    virtual ~ProximityGamePieceFactory() { ; }
} ;

#endif

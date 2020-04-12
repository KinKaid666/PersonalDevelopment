#ifndef __GAMEBOARD_H__
#define __GAMEBOARD_H__

#include <cstdlib>
#include "GamePiece.h"
class GamePiece ;
class GamePieceFactory ;
class GameStrategy ;

class GameBoard
{
public:
    GameBoard( GameStrategy     *strategy,
	       GamePieceFactory *factory,
               unsigned int      rows,
	       unsigned int      columns ) ;
    virtual ~GameBoard() ;

    GamePiece *GetPiece( unsigned int row, unsigned int column ) ;
    int Insert( GamePiece *piece, unsigned int row, unsigned int column ) ;
    int SetPiece( GamePiece *piece, unsigned int row, unsigned int column ) ;

    unsigned int GetRowCount() { return rows_ ; } 
    unsigned int GetColumnCount() { return columns_ ; } 
    bool SpacesLeft() { return pieces_ < (rows_*columns_) ; }

    unsigned int GetTerritoryTotal( GamePiece::Color ) ;
    unsigned int GetArmyTotal( GamePiece::Color ) ;
private:
    GameStrategy *strategy_ ;
    GamePieceFactory *factory_ ;
    unsigned int rows_, columns_ ;

    /* double array of pointers */
    GamePiece ***board_ ;
    int pieces_ ;
} ;

#endif

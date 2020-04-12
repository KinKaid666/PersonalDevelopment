#include <cstdlib>
#include "GameBoard.h"
#include "GamePiece.h"
#include "GameStrategy.h"

GameStrategy::GameStrategy( int neighborAdjustment, int enemyAdjustment )
  : neighborAdjustment_(neighborAdjustment),
    enemyAdjustment_   (enemyAdjustment)
{ ; }

GameStrategy::~GameStrategy()
{ ; }

void GameStrategy::ApplyStrategy( GamePiece *piece, GamePiece *neighbor )
{
    if( piece != NULL && neighbor != NULL )
    {
        if( piece->GetColor() == neighbor->GetColor() )
        {
            neighbor->ApplyAdjustment(neighborAdjustment_) ;
        }
        else if( piece->GetArmies() > neighbor->GetArmies() )
        {
            neighbor->SetColor(piece->GetColor()) ;
            neighbor->ApplyAdjustment(enemyAdjustment_) ;
        }
    }
}

ProximityGameStrategy::ProximityGameStrategy(
    int neighborAdjustment,
    int enemyAdjustment )
  : GameStrategy( neighborAdjustment, enemyAdjustment )
{ ; }

ProximityGameStrategy::~ProximityGameStrategy()
{ ; }

int ProximityGameStrategy::Insert(
    GameBoard    *board,
    GamePiece    *piece,
    unsigned int  row,
    unsigned int  column )
{
    int rc = EXIT_FAILURE ;
    if( (rc = board->SetPiece(piece, row, column)) == EXIT_SUCCESS )
    {
        /*
        ** for all pieces, the board will look like this
        **    0,0   0,1   0,2
        **       1,0   1,1   1,2 
        **    2,0   2,1   2,2
        **       3,0   3,1   3,2 
        */

        /* always apply our strategy to the pieces to our left and right */ 
        ApplyStrategy(piece, board->GetPiece(row  ,column-1)) ;
        ApplyStrategy(piece, board->GetPiece(row  ,column+1)) ;

        /* always apply our strategy to the pieces above and below us */
        ApplyStrategy(piece, board->GetPiece(row-1,column  )) ;
        ApplyStrategy(piece, board->GetPiece(row+1,column  )) ;

        /*
        ** now we need to figure out who else is adjacent to us
        **
        ** even rows, will have have the last two pieces at column + 1
        **  odd rows, will have have the last two pieces at column - 1
        */
        if( row % 2 )
        {
            ApplyStrategy(piece, board->GetPiece(row-1,column+1)) ;
            ApplyStrategy(piece, board->GetPiece(row+1,column+1)) ;
        }
        else
        {
            ApplyStrategy(piece, board->GetPiece(row-1,column-1)) ;
            ApplyStrategy(piece, board->GetPiece(row+1,column-1)) ;
        }
    }
    return rc ;
}

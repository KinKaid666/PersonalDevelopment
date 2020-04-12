#include <cstdlib>
#include "GameStrategy.h"
#include "GameBoard.h"
#include "GamePiece.h"

GameBoard::GameBoard(
    GameStrategy *strategy,
    GamePieceFactory *factory,
    unsigned int rows,
    unsigned int columns
) : strategy_(strategy),
    factory_ (factory),
    rows_    (rows),
    columns_ (columns),
    pieces_  (0)
{
    /* memory set to 0 already */
    board_ = (GamePiece***)calloc(rows_, sizeof(GamePiece*)) ;
    for( int i = 0 ; i < rows ; ++i )
    {
        board_[i] = (GamePiece**)calloc(columns, sizeof(GamePiece*)) ;
    }
}

GameBoard::~GameBoard()
{
    for( int i = 0 ; i < rows_ ; ++i )
    {
        for( int j = 0 ; j < columns_ ; ++j )
        {
            if( NULL != board_[i][j] )
            {
                free(board_[i][j]) ;
            }
        }
        free(board_[i]) ;
    } 
    free(board_) ;
}

GamePiece *GameBoard::GetPiece( unsigned int row, unsigned int column )
{
    if( row >= rows_ || column >= columns_ )
        return NULL ;
    else
        return board_[row][column] ;
}

int GameBoard::Insert( GamePiece *piece, unsigned int row, unsigned int column )
{
    strategy_->Insert(this, piece, row, column) ;
    return EXIT_SUCCESS ;
}

int GameBoard::SetPiece( GamePiece *piece, unsigned int row, unsigned int column )
{
    if( row    >= rows_    ||
        column >= columns_ ||
        NULL != board_[row][column] )
    {
        return EXIT_FAILURE ;
    }
    else
    {
        board_[row][column] = piece ;
        ++pieces_ ;
    }
    return EXIT_SUCCESS ;
}
unsigned int GameBoard::GetTerritoryTotal( GamePiece::Color color )
{
    int total = 0 ;
    for( int i = 0 ; i < rows_ ; ++i )
    {
        for( int j = 0 ; j < columns_ ; ++j )
        {
            if( (NULL != board_[i][j]) &&
                (color == board_[i][j]->GetColor()) )
            {
                total += 1 ;
            }
        }
    } 
    return total ;
}

unsigned int GameBoard::GetArmyTotal( GamePiece::Color color )
{
    int total = 0 ;
    for( int i = 0 ; i < rows_ ; ++i )
    {
        for( int j = 0 ; j < columns_ ; ++j )
        {
            if( (NULL != board_[i][j]) &&
                (color == board_[i][j]->GetColor()) )
            {
                total += board_[i][j]->GetArmies() ;
            }
        }
    } 
    return total ;
}


#ifndef __GAMESTRATEGY_H__
#define __GAMESTRATEGY_H__

class GamePiece ;
class GameBoard ;

class GameStrategy
{
public:
    GameStrategy( int neighborAdjustment, int enemyAdjustment ) ;

    virtual ~GameStrategy() ;
    virtual int Insert( GameBoard    *board,
                        GamePiece    *piece,
                        unsigned int  row,
                        unsigned int  column ) = 0 ;
    void ApplyStrategy( GamePiece *piece, GamePiece *neighbor ) ;

private:
    int neighborAdjustment_, enemyAdjustment_ ;
} ;

class ProximityGameStrategy
  :  public GameStrategy
{
public:
    ProximityGameStrategy( int neighborAdjustment, int enemyAdjustment ) ;
    virtual ~ProximityGameStrategy() ;

    virtual int Insert(GameBoard    *board,
                       GamePiece    *piece,
                       unsigned int  row,
                       unsigned int  column ) ;
} ;

#endif

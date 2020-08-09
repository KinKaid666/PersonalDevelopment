#ifndef __GAMEPIECE_H__
#define __GAMEPIECE_H__

#include <ostream>
using namespace std ;

class GamePiece
{
public:
    enum Color
    {
        RED,
        BLUE
    } ;

    GamePiece( Color color, int value, int min, int max )
      : color_(color), value_(value), min_(min), max_(max)
    { ; }
    virtual ~GamePiece() { ; }

    void ApplyAdjustment( int adj )
    {
        value_ += adj ;
        if( value_ < min_ ) value_ = min_ ;
        if( value_ > max_ ) value_ = max_ ;
    }

    void SetColor( Color color ) { color_ = color ; }

    Color GetColor() { return color_ ; }
    int   GetArmies(){ return value_ ; }

    string asString() ;
    string Print() ;

    /* give me another */
    GamePiece* dup() { return new GamePiece(color_, value_, min_, max_) ; } 

private:
    Color color_ ;
    int value_ ;
    int min_, max_ ;
    friend ostream& operator<<(ostream &o, GamePiece *gp) ;
} ;

class ProximityGamePiece
 : public GamePiece
{
public:
    ProximityGamePiece( GamePiece::Color color, unsigned int value, int min, int max )
      : GamePiece(color, value, min, max)
    { ; }
    virtual ~ProximityGamePiece() { ; }
} ;

#endif

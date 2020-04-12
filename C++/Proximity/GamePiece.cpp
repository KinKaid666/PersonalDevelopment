#include <iomanip>
#include "GamePiece.h"

string GamePiece::asString()
{
    static char num[16] ;
    string tmp ;
    if( color_ == GamePiece::RED )
    {
        tmp += " R" ;
    }
    else if( color_ == GamePiece::BLUE )
    {
        tmp += " B" ;
    }
    else
    {
        tmp += " ?" ;
    }
    sprintf( num, "%2d ", value_ ) ;
    tmp += num ;
    return tmp ;
}

string GamePiece::Print()
{
    static char num[16] ;
    string tmp ;
    if( color_ == GamePiece::RED )
    {
        tmp += " R" ;
    }
    else if( color_ == GamePiece::BLUE )
    {
        tmp += " B" ;
    }
    else
    {
        tmp += " ?" ;
    }
    sprintf( num, "%2d ", value_ ) ;
    tmp += num ;
    return tmp ;
}

ostream& operator<<(ostream &o, GamePiece *gp) 
{

    o << gp->asString() ;
    return o ;
}

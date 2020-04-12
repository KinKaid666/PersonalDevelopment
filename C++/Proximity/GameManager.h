#ifndef __GAMEMANAGER_H__
#define __GAMEMANAGER_H__

class GameManager
{
public:
    GameManager( GameBoard        *board,
		 GameBoardPrinter *printer )
      : board_   (board),
	printer_ (printer)
    { ; }
    virtual ~GameManager()
    { ; }

private:
    GameBoard        *board_ ;
    GameBoardPrinter *printer_ ;

    GamePiece        **pieceSet ;

} ;
#endif

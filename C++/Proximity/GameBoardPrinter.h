#ifndef __GAMEBOARDPRINTER_H__
#define __GAMEBOARDPRINTER_H__

#include <ostream>
#include <iomanip>
#include <sstream>
#include <curses.h>
#include "GameBoard.h"
using namespace std ;

class GameBoardPrinterImpl
{
public:
    GameBoardPrinterImpl( GameBoard *board ) : board_(board) { ; }
    virtual ~GameBoardPrinterImpl() { ; }
    virtual void PrintBoard() = 0 ;
    virtual void puts(const char *) = 0 ;
    virtual void puti(int ) = 0 ;
    virtual void putc(char ) = 0 ;
    virtual string gets() = 0 ;
    virtual bool eof() = 0 ;

protected:
    GameBoard *getBoard() { return board_ ; }
private:
    GameBoard *board_ ;
} ;

class StreamBoardPrinterImpl
  : public GameBoardPrinterImpl
{
public:
    StreamBoardPrinterImpl(GameBoard *board, ostream &ost, istream &ist)
      : GameBoardPrinterImpl(board), ost_(ost), ist_(ist)
    { ; }
    virtual ~StreamBoardPrinterImpl() { ; }
    virtual void PrintBoard()
    {
        /* why would you bother? */
        if( NULL == getBoard() ) return ;

        int num_rows = getBoard()->GetRowCount(),
            num_cols = getBoard()->GetColumnCount() ;
        int row = 1 ;
        char column = 'A' ;
        const char *padding    = "  " ;
        const char *offset     = "   " ;
        const char *empty_cell = "     " ;
        const char cell_boarder = '|' ;

        /* it's header time */
        ost_ << offset << offset ;
        for( int i = 0 ; i < num_cols ; ++i )
            ost_ << "-" << setw(3) << column++ << padding ;
        ost_ << endl ;

        for( int i = 0 ; i < num_rows ; ++i )
        {
            ost_ << setw(3) << row++ << offset ;
            /* extra for getBoard() hex viewing */
            if( i%2 )
                ost_ << offset ;
            ost_ << cell_boarder ;
            for( int j = 0 ; j < num_cols ; ++j )
            {
                if( NULL != getBoard()->GetPiece(i, j) )
                    ost_ << getBoard()->GetPiece(i, j) ;
                else
                    ost_ << empty_cell ;
                ost_ << cell_boarder ;
            }
            ost_ << endl ;
        }
    }

    virtual void puts(const char *str) { ost_ << str ; }
    virtual string gets() { string tmp ; getline(ist_,tmp) ; return tmp ; }
    virtual bool eof() { return ist_.eof() ; }
    virtual void puti(int i) { ost_ << i ; }
    virtual void putc(char c) { ost_ << c ; }
private:
    ostream &ost_ ;
    istream &ist_ ;
} ;

class CursesBoardPrinterImpl
  : public GameBoardPrinterImpl
{
public:
    CursesBoardPrinterImpl(GameBoard *board)
      : GameBoardPrinterImpl(board), eof_(false)
    {
        initscr() ;
        start_color() ;
    }
    virtual ~CursesBoardPrinterImpl() { endwin() ; }
    virtual void PrintBoard()
    {
        /* why would you bother? */
        if( NULL == getBoard() ) return ;

        int num_rows = getBoard()->GetRowCount(),
            num_cols = getBoard()->GetColumnCount() ;
        int row = 1 ;
        char column = 'A' ;
        const char *padding    = "  " ;
        const char *offset     = "   " ;
        const char *empty_cell = "     " ;
        const char cell_boarder = '|' ;

        /* it's header time */
        printw( "%s%s", offset, offset ) ;
        for( int i = 0 ; i < num_cols ; ++i )
            printw( "%s%3c%s", "-", column++, padding ) ;
        printw( "\n" ) ;

        for( int i = 0 ; i < num_rows ; ++i )
        {
            printw( "%3d%s", row++, offset ) ;
            /* extra for getBoard() hex viewing */
            if( i%2 )
                printw( "%s", offset ) ;
            printw( "%c", cell_boarder ) ;
            for( int j = 0 ; j < num_cols ; ++j )
            {
                if( NULL != getBoard()->GetPiece(i, j) )
                    printw( "%s", getBoard()->GetPiece(i, j)->asString().c_str() ) ;
                else
                    printw( "%s", empty_cell ) ;
                printw( "%c", cell_boarder ) ;
            }
            printw( "\n" ) ;
        }
        refresh() ;
    }

    virtual void puts(const char *str) { printw( "%s", str ) ; }
    virtual string gets()
    {
        char str[80] ;
        string a ;

        getstr(str) ;
        a = str ;
        return a ;
    }
    virtual bool eof() { return eof_ ; } 
    virtual void puti(int  i) { printw( "%d", i ) ; }
    virtual void putc(char c) { printw( "%c", c ) ; }
private:
    bool eof_ ;
    int lines ;
    int columns ;
} ;

class GameBoardPrinter
{
public:
    GameBoardPrinter(GameBoard *board, ostream &ost, istream &ist)
    { impl_ = new StreamBoardPrinterImpl(board, ost, ist) ; }

    /* already got an impl, give it to me */
    GameBoardPrinter(GameBoardPrinterImpl *impl) : impl_(impl) { ; }

    ~GameBoardPrinter() { delete impl_ ; } 

    void PrintBoard() { GetImpl()->PrintBoard() ; }
    void puts(const char *str) { GetImpl()->puts(str) ; }
    string gets() { return GetImpl()->gets() ; }
    bool eof() { return GetImpl()->eof() ; }
    void puti(int i) { return GetImpl()->puti(i) ; }
    void putc(char c) { return GetImpl()->putc(c) ; }
protected:
    GameBoardPrinterImpl* GetImpl() { return impl_ ; }
private:
    GameBoardPrinterImpl *impl_ ;
} ;

#endif /* __GAMEBOARDPRINTER_H__ */

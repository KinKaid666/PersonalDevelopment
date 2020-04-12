#include <cstdlib>
#include <iostream>
#include <limits>
#include <time.h>
#include "GamePieceFactory.h"
#include "GameStrategy.h"
#include "GameBoardPrinter.h"
#include "GameBoard.h"
#include "GameManager.h"

#define MAX 20
#define MIN 1
#define USE_ARMY_LOGIC          1
#define USE_TERRITORY_LOGIC     0

#define DEFAULT_ROWS                MAX
#define DEFAULT_COLUMNS             MAX
#define DEFAULT_NEIGHBOR_ADJUSTMENT 1
#define DEFAULT_ENEMY_ADJUSTMENT    1

GamePiece **generatePlayerPieces(
    GamePiece::Color color,
    unsigned int     num_pieces,
    int              min,
    int              max,
    GamePieceFactory *factory )
{
    GamePiece **set = (GamePiece**)calloc(num_pieces, sizeof(GamePiece*)) ;
    for( int i = 0 ; i < num_pieces ; ++i )
    {
        int random = (rand() % (max - min)) + min ;
        set[i] = factory->createGamePiece( color, random, min, max ) ;
    }
    return set ;
}

GamePiece** generateOtherPieces(
    GamePiece **seed,
    GamePiece::Color new_color,
    unsigned int num_pieces
)
{
    GamePiece **set = (GamePiece**)calloc(num_pieces, sizeof(GamePiece*)) ;
    for( int i = 0 ; i < num_pieces ; ++i )
    {
        set[i] = seed[i]->dup() ;
        set[i]->SetColor(new_color) ;
    }

    for( int i = 0 ; i < num_pieces ; ++i )
    {
        int swap = rand() % num_pieces ;
        GamePiece *tmp = set[swap] ;
        set[swap] = set[i] ;
        set[i] = tmp ;
    }
    return set ;
}

void freePieces( GamePiece **set, unsigned int count )
{
    while(count-- > 0)
    {
        free(set[count]) ;
    }
    free(set) ;
}

static void usage(int rc)
{
    fprintf( stderr, "usage: proximity <rows> <columns> <neighbor> <enemy>\n") ;
    fprintf( stderr, "\n" ) ;
    fprintf( stderr, "  rows,columns    The board will be rowsXcolumsn\n" ) ;
    fprintf( stderr, "  neighbor,       The strategy on tackover, or \n"  ) ;
    fprintf( stderr, "     enemy,       when next to your own piece\n"    ) ;
    exit(rc) ;
}

/*
** The way this algorithm works is to try and capture the most VALUE
**
** Value is defined by either armies or territories
**
** Also, we'd rather not occupy spaces that could be of value to us
** later.  That being spaces we could take if we had a higher army count
**
** This works because we weight things we want with a factor of 7, and
** things we don't want with a factor of 1.  This is because if all 6
** spaces around the square in question are occupied by enemies that
** our piece cannot take, we'll leave it for the time being
*/
int gamePieceValue(
    GamePiece *piece,
    GamePiece *neighbor,
    int logic,
    int neighbor_adjustment,
    int enemy_adjustment
)
{
    int value = 0 ;

    if( NULL != piece && NULL != neighbor )
    {
        if( logic == USE_ARMY_LOGIC )
        {
            if(piece->GetColor() != neighbor->GetColor())
            {
               if(piece->GetArmies() > neighbor->GetArmies())
               {
                    value += neighbor->GetArmies() * 7;
                    value += enemy_adjustment * 7 ;
                }
                else
                {
                    if(neighbor->GetArmies() != MAX)
                        value -= 1 ;
                }
            }
            else
            {
                value += neighbor_adjustment * 7 ;
            }

        }
        else if( logic == USE_TERRITORY_LOGIC )
        {
            if(piece->GetColor() != neighbor->GetColor())
            {
                if(piece->GetArmies() > neighbor->GetArmies())
                    value += 1 * 7 ;
                else
                    value -= 1 ;
            }
            else
            {

            }
        }
    }
    return value ;
}

int spaceValue(
    GameBoard           *board,
    unsigned int        row,
    unsigned int        column,
    GamePiece           *piece,
    int                 logic,
    int                 neighbor_adjustment,
    int                 enemy_adjustment
)
{
    /* probably don't want that */
    int value = 0 ;

    /*
    ** Sum-up all the neighboring pieces values
    */
    value += gamePieceValue(piece, board->GetPiece(row  ,column-1),
                  logic, neighbor_adjustment, enemy_adjustment) ;
    value += gamePieceValue(piece, board->GetPiece(row  ,column+1),
                  logic, neighbor_adjustment, enemy_adjustment) ;

    /* always apply our strategy to the pieces above and below us */
    value += gamePieceValue(piece, board->GetPiece(row-1,column  ),
                  logic, neighbor_adjustment, enemy_adjustment) ;
    value += gamePieceValue(piece, board->GetPiece(row+1,column  ),
                  logic, neighbor_adjustment, enemy_adjustment) ;

    /*
    ** now we need to figure out who else is adjacent to us
    **
    ** even rows, will have have the last two pieces at column + 1
    **  odd rows, will have have the last two pieces at column - 1
    */
    if( row % 2 )
    {
        value += gamePieceValue(piece, board->GetPiece(row-1,column+1),
                      logic, neighbor_adjustment, enemy_adjustment) ;
        value += gamePieceValue(piece, board->GetPiece(row+1,column+1),
                      logic, neighbor_adjustment, enemy_adjustment) ;
    }
    else
    {
        value += gamePieceValue(piece, board->GetPiece(row-1,column-1),
                      logic, neighbor_adjustment, enemy_adjustment) ;
        value += gamePieceValue(piece, board->GetPiece(row+1,column-1),
                      logic, neighbor_adjustment, enemy_adjustment) ;
    }
    return value ;
}

/*
** Returns weight found
*/
int determineBestPlay(
    GameBoard           *board,
    unsigned int        row_size,
    unsigned int        column_size,
    GamePiece           *piece,
    int                 neighbor_adjustment,
    int                 enemy_adjustment,
    int                 logic,
    int                 &best_choice_row,
    int                 &best_choice_column
)
{
    best_choice_row = best_choice_column = -1 ;
    int best_value ;
    int current_value = 0 ;

    for( int i = 0 ; i < row_size ; ++i )
    {
        for( int j = 0 ; j < column_size ; ++j )
        {

            /* Make sure no one is home first */
            if(NULL == board->GetPiece(i, j) )
            {
               /* If this is the best space, or if we haven't found any yet */
               if(((current_value = spaceValue( board,
                                             i,
                                             j,
                                             piece,
                                             logic,
                                             neighbor_adjustment,
                                             enemy_adjustment )) > best_value ) ||
                   /* find a better way other than, "haven't selected yet */
                   (best_choice_row == -1))
                {
                    best_choice_row = i ;
                    best_choice_column = j ;
                    best_value = current_value ;
                }
            }
        }
    }
    return best_value ;
}

int main( int ac, char **av )
{
    if( ac > 5 )
    {
        usage(EXIT_FAILURE) ;
    }

    srand(time(NULL)) ;
    unsigned int board_size,
                 player_set_size,
                 rows,
                 columns,
                 neighbor_adjustment,
                 enemy_adjustment ;
    rows                = DEFAULT_ROWS                ;
    columns             = DEFAULT_COLUMNS             ;
    neighbor_adjustment = DEFAULT_NEIGHBOR_ADJUSTMENT ;
    enemy_adjustment    = DEFAULT_ENEMY_ADJUSTMENT    ;

    if(ac > 1)
    {
        rows = atoi(av[1]) ;
    }

    if(ac > 2)
    {
        columns = atoi(av[2]) ;
    }
    if(ac > 3)
    {
        neighbor_adjustment = atoi(av[3]) ;
    }
    if(ac > 4)
    {
        enemy_adjustment = atoi(av[4]) ;
    }
    board_size = rows * columns ;
    player_set_size = (board_size/2) + (board_size%2) ;

    GameStrategy        *strategy  = new ProximityGameStrategy(
                                                        neighbor_adjustment,
                                                        enemy_adjustment) ;
    GamePieceFactory    *factory   = new ProximityGamePieceFactory() ;
    GameBoard           *proximity = new GameBoard(strategy,
                                                   factory,
                                                   rows,
                                                   columns) ;
    /* this will create a stream output */
    GameBoardPrinter *printer ;
    if( getenv("USE_NCURSES") != NULL  )
        printer = new GameBoardPrinter(new CursesBoardPrinterImpl(proximity)) ;
    else
        printer = new GameBoardPrinter(proximity, std::cout, std::cin) ;

    /* Half the size of the board +1, in case it's an odd number */
    GameManager         *manager  = new GameManager( proximity, printer ) ;

    /*
    ** At the beginning of the game we're going to need to generate
    ** all the game pieces
    **
    ** We'll generate all of one players pieces, then duplicate them
    ** for the other side (and randomly distribute them)
    */
    GamePiece **p1 = generatePlayerPieces( GamePiece::RED,
                                           player_set_size, MIN, MAX, factory );
    GamePiece **p2 = generateOtherPieces(p1, GamePiece::BLUE, player_set_size) ;

    int turn = 0 ;
    while( proximity->SpacesLeft() )
    {
        char column ;
        int row ;
        GamePiece *next_piece ;

        int current_player = (turn%2)+1 ;
        /*
        ** Who's turn is it?
        */
        if( turn%2 )
            next_piece = p2[turn/2]->dup() ;
        else
            next_piece = p1[turn/2]->dup() ;

        printer->PrintBoard() ;

        int row_sug, column_sug, weight ;
        weight = determineBestPlay( proximity,
                                    rows,
                                    columns,
                                    next_piece,
                                    neighbor_adjustment,
                                    enemy_adjustment,
                                    USE_ARMY_LOGIC,
                                    row_sug,
                                    column_sug) ;
        if( current_player == 1 )
        {
            printer->puts( "**Player " ) ;
            printer->puti( current_player ) ;
            printer->puts( "**\n" ) ;
            printer->puts( "NEXT PIECE: " ) ;
            printer->puts( next_piece->Print().c_str() ) ;
            printer->puts( "\n" ) ;
            printer->puts( "SUGGESTION = " ) ;
            printer->putc( char(column_sug + 'A') ) ;
            printer->puti( row_sug+1 ) ;
            printer->puts( " weight = " ) ;
            printer->puti( weight ) ;
            printer->puts( "\n" ) ;
            printer->puts( "SELECT LOCATION (ex: A1, ^D to exit) [" ) ;
            printer->putc( char(column_sug + 'A') ) ;
            printer->puti( row_sug+1 ) ;
            printer->puts( "]: " ) ;

            column = char(column_sug + 'A') ;
            row = row_sug + 1;

            string input = printer->gets() ;
            if( !printer->eof() )
            {
                /*
                ** Insert a copy of our piece and set it's color
                */
                int charsEntered = sscanf( input.c_str(), "%c%d", &column, &row ) ;
                if( 0 == charsEntered ||
                    2 == charsEntered ||
                    (EXIT_FAILURE == proximity->Insert(next_piece, row-1, column - 'A')))
                {
                    /* didn't work, don't go anywhere */
                    printer->puts( "**Invalid location**\n" ) ;
                    free(next_piece) ;
                }
                else
                {
                    ++turn ;
                }
            }
            else
            {
                printer->puts( "\n" ) ;
                break ;
            }
        }
        else
        {
            /*
            ** COMPUTERS TURN
            */
            printer->puts( "**Computer Player " ) ;
            printer->puti( current_player ) ;
            printer->puts( "**\n" ) ;
            printer->puts( "NEXT PIECE: " ) ;
            printer->puts( next_piece->Print().c_str() ) ;
            printer->puts( "\n" ) ;
            printer->puts( "SELECTED LOCATION = " ) ;
            printer->putc( char(column_sug + 'A') ) ;
            printer->puti( row_sug+1 ) ;
            printer->puts( " weight = " ) ;
            printer->puti( weight ) ;
            printer->puts( "\n" ) ;
            if( EXIT_FAILURE == proximity->Insert(next_piece, row_sug, column_sug))
            {
                printer->puts( "Internal Error, computer chose invalid location\n" ) ;
                exit(1) ;
            }
            ++turn ;
        }
    }

    int play1_terr   = proximity->GetTerritoryTotal( GamePiece::RED ) ;
    int play2_terr   = proximity->GetTerritoryTotal( GamePiece::BLUE ) ;
    int play1_armies = proximity->GetArmyTotal( GamePiece::RED ) ;
    int play2_armies = proximity->GetArmyTotal( GamePiece::BLUE ) ;

    printer->puts( "== FINAL BOARD STANDINGS ==\n" ) ;
    printer->PrintBoard() ;
    printer->puts( "Player 1 Totals: \n" ) ;
    printer->puts( "\tTerritories: " ) ;
    printer->puti( play1_terr ) ;
    printer->puts( "\n\tArmies:      " ) ;
    printer->puti( play1_armies ) ;
    printer->puts( "\nPlayer 2 Totals: \n" ) ;
    printer->puts( "\tTerritories: " ) ;
    printer->puti( play2_terr ) ;
    printer->puts( "\n\tArmies:      " ) ;
    printer->puti( play2_armies ) ;
    printer->puts( "\n" ) ;

    freePieces(p1, player_set_size) ;
    freePieces(p2, player_set_size) ;
    delete factory ;
    delete strategy ;
    delete proximity ;
    delete printer ;
    delete manager ;
    return EXIT_SUCCESS ;
}

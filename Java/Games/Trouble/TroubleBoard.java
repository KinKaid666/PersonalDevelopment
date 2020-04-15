//
// Trouble Board
//
// The board is a 2d matrix of ints the includes the finish slots, the home rows
//   and the spaces between
package Trouble ;
import java.lang.Exception ;

class TroubleBoard
{
    // Constraints of the game
    private static int MAX_PLAYERS               = 4 ;
    private static int MAX_PIECES                = 4 ;
    private static int EMPTY_POSITION            = MAX_PLAYERS ;
    private static int POSITIONS_BETWEEN_PLAYERS = 7 ;
    private static int BOARD_POSITIONS           = MAX_PLAYERS * POSITIONS_BETWEEN_PLAYERS ;
    private static int MOVE_NOT_POSSIBLE         = -1 ;

    private int _numPlayers ;
    private int _numPieces ;

    private int _positionsPerPlayer ;
    private int _boardSize ;
    private int[] _board ;
    private int[][] _pieceReference ;

    //
    //
    // Create board with appropriate number of players and pieces
    public TroubleBoard(int numberOfPlayers, int piecesPerPlayer) throws Exception
    {
        if(numberOfPlayers > MAX_PLAYERS || numberOfPlayers < 1)
        {
            throw new Exception("Incorrect number of players: "
                                    + numberOfPlayers
                                    + "! Players must be between 1 and 4") ;
        }
        if(piecesPerPlayer > MAX_PIECES  || piecesPerPlayer < 1)
        {
            throw new Exception("Incorrect number of pieces per player: "
                                    + piecesPerPlayer
                                    + "! Pieces must be between 1 and 4") ;
        }
        _numPlayers = numberOfPlayers ;
        _numPieces  = piecesPerPlayer ;

        // each player will need space for home pieces, finish pices, and
        //   those positions on the board between
        _positionsPerPlayer = _numPieces + _numPieces + POSITIONS_BETWEEN_PLAYERS ;
        _boardSize = _positionsPerPlayer * MAX_PLAYERS ;

        _board          = new int[_boardSize] ;
        _pieceReference = new int[_numPlayers][_numPieces] ;
        ClearBoard() ;
    }

    //
    // Start game anew
    public void ClearBoard()
    {
        //
        // Move all players back to home
        for(int i = 0 ; i < _boardSize ; ++i)
        {
            _board[i] = EMPTY_POSITION ;
        }

        for(int i = 0 ; i < _numPlayers ; ++i )
        {
            for(int j = 0 ; j < _numPieces ; ++j)
            {
                int cell = _getCellOffsetForHomePosition(i,j) ;
                _pieceReference[i][j] = cell ;
                _board[cell] = i ;
            }
        }

    }

    //
    // Move player 'player' spaces
    //
    // return: true if piece moved, false otherwise
    public boolean Move(int player, int piece, int spaces)
    {
        int currentCell = _getCellOffsetForPiece(player, piece) ;
        int newCell     = _getCellOffsetForBoardPosition(currentCell, spaces) ;

        // If not possible return error or the places own piece is there
        if(newCell == MOVE_NOT_POSSIBLE ||
           _board[newCell] == player)
        {
            return false ;
        }

        // Send current player home
        if(_board[newCell] != player &&
           _board[newCell] != EMPTY_POSITION)
        {
            int incumbentPlayer = _board[newCell] ;

            // find which piece it is
            int incumbentPiece = -1 ;
            for(int j = 0 ; j < _numPieces ; ++j)
            {
                if(newCell == _pieceReference[incumbentPlayer][j])
                {
                    incumbentPiece = j ;
                    break ;
                }
            }
            // move current piece home
            int homeCell = _getCellOffsetForHomePosition(incumbentPlayer,incumbentPiece) ;

            _pieceReference[incumbentPlayer][incumbentPiece] = homeCell ;
            _board[homeCell] = incumbentPlayer ;
        }

        _board[currentCell] = EMPTY_POSITION ;
        _board[newCell]     = player ;
        _pieceReference[player][piece] = newCell ;
        return true ;
    }

    //
    // Deteremine if player 'player' has won
    public boolean HasPlayerWon(int player)
    {
        boolean won = true ;
        int position = 0 ;

        //
        // Check to see if player has their pieces in the finish cells
        for(int i = 0 ; i < _numPieces ; ++i)
        {
            int cell = _getCellOffsetForFinishPosition(player, i) ;

            if(_board[cell] == EMPTY_POSITION)
            {
                won = false ;
                break ;
            }
        }
        return won ;
    }

    public void Print()
    {
        System.out.println("Board:") ;

        // Print the board as a set of legs
        //     x x x x x x x x x -
        //                       |
        //     - - - - - - - - - -
        //  |
        //   > x x x x x x x x x
        //                       |
        //     - - - - - - - - - -
        //  |
        //   > x x x x x x x x x
        //                       |
        //     - - - - - - - - - -
        //  |
        //   > x x x x x x x x x
        for(int i = 0 ; i < _boardSize ; )
        {
            if((i+1)/_positionsPerPlayer < _numPlayers)
            {
                System.out.print("  player " + (i+1)/_positionsPerPlayer + " finish ") ;
            }
            else
            {
                System.out.print("                  ") ;
            }
            for(int j = 0 ; j < _positionsPerPlayer ; ++j, ++i)
            {
                if( _numPieces == j )
                {
                    System.out.print(" home " ) ;
                }
                else if( (_numPieces*2) == j)
                {
                    System.out.print(" board " ) ;
                }
                System.out.print("[" + (_board[i] == EMPTY_POSITION ? " " : _board[i]) + "]") ;
            }
            System.out.println("\n") ;
        }
    }

    boolean IsHome(int player, int piece)
    {
        if(_board[_getCellOffsetForHomePosition(player,piece)] == player)
        {
            return true ;
        }
        return false ;
    }

    boolean OnBoard(int player, int piece)
    {
        if(_isCellBoard(_getCellOffsetForPiece(player,piece)))
        {
            return true ;
        }
        return false ;
    }

    //
    // THE BOARD IS A UNIQUE DATA STRUCTURE
    // Board is divided into an array per player that looks like this
    //
    // Finish1 Finish2 .. Home1 Home2 .. Board1 Board2 ...
    //

    //
    // Helper function to get home position
    //
    // Finish pieces are the first cells 
    private int _getCellOffsetForHomePosition(int player, int homePosition)
    {
        return (player * _positionsPerPlayer) + homePosition + _numPieces ;
    }

    //
    // Helper function to get finish position
    //
    // Finish pieces are the first cells
    private int _getCellOffsetForFinishPosition(int player, int finishPosition)
    {
        return (player * _positionsPerPlayer) + finishPosition ;
    }

    //
    // Helper function to get next board position from current to current + inc
    private int _getCellOffsetForBoardPosition(int currentCell, int increment)
    {
        int newCell = currentCell ;
        int leg = currentCell / _positionsPerPlayer ;

        // get which player it is
        int player = _board[currentCell] ;

        //
        // Move
        //
        //   First, move out of home
        if(_isCellHome(newCell))
        {
            newCell = (_positionsPerPlayer * player) + (2 * _numPieces) - 1 ;
        }
        //
        // or, if we're in the finish and we'll go paste, error
        else if(_isCellFinish(newCell))
        {
            if((newCell + increment) > (((leg*_positionsPerPlayer) + _numPieces)-1))
            {
                return MOVE_NOT_POSSIBLE ;
            }
        }

        // Move
        newCell += increment ;

        int wrap = 0 ;
        // jump over finish and home cells if we've gone past
        if(newCell >= ((leg+1)*_positionsPerPlayer))
        {
            newCell += (2 * _numPieces) * ((increment/POSITIONS_BETWEEN_PLAYERS) + 1) ;
            // wrap the array
            newCell %= _boardSize ;
            if( newCell < currentCell)
            {
                wrap = 1 ;
            }
        }

        // fine the "end cell" (last cell) for that player
        int endCell = player*_positionsPerPlayer + (2 * _numPieces) - 1 ;

        // go into finish if we've crossed our "end cell"
        if((newCell > endCell && currentCell < newCell && currentCell < endCell && !_isCellHome(currentCell)) ||
           (newCell > endCell && wrap == 1 ))
        {
            // see if it fits
            if((newCell - endCell) > _numPieces)
            {
                return MOVE_NOT_POSSIBLE ;
            }
            else
            {
                newCell -= (2 * _numPieces) ;
            }
        }
        return newCell ;
    }

    //
    // Helper function to get current cell for player X, piece Y
    private int _getCellOffsetForPiece(int player, int piece)
    {
        return _pieceReference[player][piece] ;
    }

    //
    // Helper function to determine if current cell is a home cell
    private boolean _isCellHome(int cell)
    {
        int normalizedCell = cell % _positionsPerPlayer ;
        return normalizedCell >= _numPieces && normalizedCell < (2*_numPieces) ;
    }

    //
    // Helper function to determine if current cell is a finish cell
    private boolean _isCellFinish(int cell)
    {
        int normalizedCell = cell % _positionsPerPlayer ;
        return normalizedCell < _numPieces ;
    }

    //
    // Helper function to determine if current cell is a board cell
    private boolean _isCellBoard(int cell)
    {
        int normalizedCell = cell % _positionsPerPlayer ;
        return normalizedCell >= (2*_numPieces) ;
    }
} ;

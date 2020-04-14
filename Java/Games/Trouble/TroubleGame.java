import java.lang.Exception ;
class TroubleGame
{
    private static int REPEAT_ROLL       = 6 ;
    private static int PIECES_PER_PLAYER = 4 ;
    private int _numPlayers ;
    private int _winner ;
    private int _turn ;
    private int _totalMoves ;
    private boolean _gameOver ;
    private TroubleBoard _board ;
    private TroubleDice  _dice ;

    //
    // Create trouble game
    public TroubleGame(int numPlayers) throws Exception
    {
        _numPlayers = numPlayers ;
        _board = new TroubleBoard(_numPlayers,PIECES_PER_PLAYER) ;
        _dice  = new TroubleDice() ;
        StartGame() ;
    }

    public void StartGame()
    {
        _gameOver = false ;
        _turn       = 0 ;
        _totalMoves = 0 ;
        _board.ClearBoard() ;
    }

    public boolean Advance() throws Exception
    {
        if(_isGameOver())
        {
            throw new Exception("Cannot advance game; player "
                                    + _winner + " won!") ;
        }

        int roll = 0 ;
        do
        {
            //
            // First roll the dice
            roll = _dice.Roll() ;

            //System.out.println("Player " + _turn + " has rolled a : "+ roll ) ;
            //
            // TODO: Pick a more strategic piece
            //
            // simple logic, iterate over the pieces until you can move
            for(int i = 0 ; i < PIECES_PER_PLAYER ; ++i)
            {
                // TODO: Home
                if(roll == REPEAT_ROLL && _board.IsHome(_turn,i))
                {
                    if(_board.Move(_turn, i, 1))
                    {
                        break ;
                    }
                }
                // if the move happened exit
                else if(_board.OnBoard(_turn, i))
                {
                    if(_board.Move(_turn, i, roll))
                    {
                        break ;
                    }
                }
            }
        }
        while(REPEAT_ROLL == roll) ;

        // move to next player
        _turn = (_turn + 1) % _numPlayers ;

        ++_totalMoves ;
        return !_isGameOver() ;
    }

    //
    // Internal helper function to determine if any player has all their pieces
    //     in the homebase
    private boolean _isGameOver()
    {
        for(int i = 0 ; i < _numPlayers && !_gameOver ; ++i )
        {
            if(_board.HasPlayerWon(i))
            {
                _winner = i ;
                _gameOver = true ;
            }
        }
        return _gameOver ;
    }

    public void PrintBoard()
    {
        _board.Print() ;
    }

    public int Winner() throws Exception
    {
        if(!_gameOver)
        {
            throw new Exception("no winner") ;
        }
        return _winner ;
    }

    public int GetMoves()
    {
        return _totalMoves ;
    }
} ;

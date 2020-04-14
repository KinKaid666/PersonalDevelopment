import java.io.* ;
class Trouble
{
    public static void main(String args[])
    {
        int numPlayers = 0 ;

        //
        // Check to make sure we know how many people are playing
        if( args.length != 1 )
        {
            System.err.println("usage: Trouble <number of players>") ;
            System.exit(1) ;
        }

        //
        // Convert argument to an int
        try
        {
            numPlayers = Integer.parseInt(args[0]) ;
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid number of players: " + args[0]) ;
            System.exit(1) ;
        }


        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in)) ;
        String input ;
        TroubleGame game ;
        try
        {
            game = new TroubleGame(numPlayers) ;
            do
            {
                //game.PrintBoard() ;
                //input = inputStream.readLine() ;
            }
            while(true == game.Advance()) ;
            System.out.println("Game over: player " + game.Winner() + " has won in a game with " + game.GetMoves() + ".") ;
            game.PrintBoard() ;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage()) ;
            System.exit(1) ;
        }
    }
} ;

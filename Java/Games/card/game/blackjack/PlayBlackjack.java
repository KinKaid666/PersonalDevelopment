package card.game.blackjack ;

import java.util.List ;
import java.util.LinkedList ;

import card.game.blackjack.* ;
import card.Card ;

class PlayBlackjack
{
    public static void main(String[] args)
    {
        if(!(args.length == 3 || args.length == 4))
        {
            System.err.println("usage: java " + System.getProperty("sun.java.command").split(" ")[0] + " <number of players> <number of shuffles> <strategy filename> [verbose: 1 or default:0]") ;
            System.exit(1) ;
        }
        int numplayers = Integer.parseInt(args[0]) ;
        int games      = Integer.parseInt(args[1]) ;
        String strategy = args[2] ;
        boolean verbose = false ;

        if(args.length == 4 && Integer.parseInt(args[3]) == 1)
        {
            verbose = true ;
        }

        BlackjackRules rules = new BlackjackRules( 8,     // Decks
                                                   true,  // split Aces
                                                   true,  // dealer hit soft 17
                                                   true,  // allow surrender
                                                   true,  // replit pairs
                                                   false, // resplit aces
                                                   true,  // double after splitj
                                                   false, // double on only 10 or 11
                                                   true   // insurance
                                                ) ;
        BlackjackStrategy s = null ;
        try
        {
            s = BlackjackStrategy.createStrategyFromFile(args[2]) ;
            if(verbose)
            {
                s.printStrategy() ;
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage()) ;
            System.exit(1) ;
        }
        List<BlackjackPlayer> players = new LinkedList<BlackjackPlayer>() ;
        for( int i = 0 ; i < numplayers ; ++i )
        {
            BlackjackPlayer p = new BlackjackPlayer("Player " + i, s) ;
            players.add(p) ;
        }

        Blackjack game = new Blackjack(rules, players) ;
        game.setVerbose(verbose) ;
        int progressBar = games / 10 ;
        //System.out.print("progress") ;
        while( games > 0 )
        {
            try
            {
                game.play() ;
                game.shuffle() ;
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage()) ;
                System.exit(1) ;
            }


            // Progress Bar
            // Comment out until we're done w/ the strategy
            //if(progressBar > 0 && (games % progressBar) == 0 ) System.out.print(".") ;
            --games ;
            //System.out.println("Reshuffling " + games + " left") ;

        }
        if(verbose)
        {
            s.printStatistics() ;
        }
        s.printStatisticsCSV() ;
    }
}

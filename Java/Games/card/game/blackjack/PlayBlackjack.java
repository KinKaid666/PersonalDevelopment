package card.game.blackjack ;

import java.util.List ;
import java.util.LinkedList ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.Card ;

class PlayBlackjack
{
    private static Logger logger = Logger.getLogger(PlayBlackjack.class.getName()) ;

    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            System.err.println("usage: java " + System.getProperty("sun.java.command").split(" ")[0] + " <number of shuffles> <strategy filename or omit for Random>") ;
            System.exit(1) ;
        }
        int games       = Integer.parseInt(args[0]) ;
        String strategy = args[1] ;
        BlackjackStrategy s = null ;
        try
        {
            if(args[1].equals("0"))
            {
                s = BlackjackStrategy.createRandom() ;
            }
            else
            {
                s = BlackjackStrategyStatic.createStrategyFromFile(args[2]) ;
                s.printStrategy(System.out) ;
            }
        }
        catch (Exception e)
        {
            System.err.println("caught exception: " + e.getMessage()) ;
            System.exit(1) ;
        }

        BlackjackSimulator sim = new BlackjackSimulator(s,games) ;
        sim.simulate() ;
        s.printStatisticsCSV(System.out) ;
    }
}

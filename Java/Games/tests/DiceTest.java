package tests ;

import dice.Die ;
import dice.Dice ;

import java.util.Map ;

public class DiceTest {
    public static void main(String args[]) {
        if(args.length != 2) {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <number of dice> <number of times>") ;
            System.exit(1) ;
        }
        int numDice  = Integer.parseInt(args[0]) ;
        int numRolls = Integer.parseInt(args[1]) ;

        if(numDice < 0 || numRolls < 0) {
            System.err.println("usage: both arguments most be a positive integer") ;
            System.exit(1) ;
        }
        Dice dice = new Dice(numDice, true) ;

        System.out.printf("Rolling %d dice %,10d times\n", numDice, numRolls) ;
        int progressBar = numRolls / 10 ;
        System.out.print("progress") ;

        for( int i = 0 ; i < numRolls ; ++i ) {
            int x = dice.roll() ;
            //System.out.println("Roll " + i + " = " + x) ;
            if(progressBar > 0 && (i % progressBar) == 0 ) System.out.print(".") ;

        }
        System.out.println() ;

        Map<Integer,Integer> stats = dice.getStatistics() ;
        int max = 6 * numDice ;
        for( int i = numDice ; i <= max ; ++i ) {
            if(stats.containsKey(i)) {
                System.out.printf("%" + String.valueOf(max).length() + "d = %," + (String.valueOf(numRolls).length()+2) + "d / %5.2f%%\n", i, stats.get(i), stats.get(i)*100.00 / numRolls) ;
            } else {
                System.out.printf("%" + String.valueOf(max).length() +"d = %," + (String.valueOf(numRolls).length()+2) + "d / %5.2f%%\n", i, 0, 0.0) ;
            }
        }
    }
}

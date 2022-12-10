package card.game.blackjack.GeneticAlgorithm ;

import java.util.* ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.game.blackjack.GeneticAlgorithm.* ;
import card.Card ;
import java.util.Random ;


public class CompareBlackjackStrategies {
    private static Logger logger = Logger.getLogger(DetermineBlackjackStrategy.class.getName()) ;
    // after how many generations should you see output?
    private static final int OUTPUT_TIMING = 100 ;
    private static Random r = new Random(System.currentTimeMillis()) ;

    private static double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart)) ;
    }

    public static void main(String[] args) {
        if(!(args.length >= 2)) {
            System.err.println("usage: java " + System.getProperty("sun.java.command").split(" ")[0] + " <number of games> <strategy file 1> [<strategy file 2> ...]") ;
            System.exit(1) ;
        }

        // Process arguments
        //    g - # games
        int numGames = 0 ;

        try {
            numGames = Integer.parseInt(args[0]) ;
        } catch (NumberFormatException e) {
            System.err.printf("%s is not a valid number of games!\n", args[0]) ;
        }

        Map<String, Double> strategiesToFitness = new HashMap<String,Double>() ;
        // Create the strategies, initialize all fitness functions with 0
        try {
            for(int i = 1 ; i < args.length ; i++) {
                BlackjackStrategy s = BlackjackStrategyStatic.createStrategyFromFile(args[i]) ;
                BlackjackSimulator sim = new BlackjackSimulator(s, numGames) ;
                sim.simulate() ;
                strategiesToFitness.put(args[i], Double.valueOf(sim.getFitness())) ;
            }
        } catch (Exception e) {
            System.err.println("Failed to create strategy: " + e.getMessage()) ;
        }

        System.out.println("Raw values:") ;
        strategiesToFitness.forEach((key, value) -> {
            System.out.printf("\tStrategy=%s Value=%.3f\n", key, value);
        });

        // Baseline the array such that the worst strategy gets a 0
        Double minFitness = Collections.min(strategiesToFitness.values()) ;
        for(String s : strategiesToFitness.keySet()) {
            strategiesToFitness.put(s, strategiesToFitness.get(s) - minFitness) ;
        }

        System.out.println("Scaled values:") ;
        strategiesToFitness.forEach((key, value) -> {
            System.out.printf("\tStrategy=%s Value=%.3f\n", key, value);
        });
        Double totalFitness = strategiesToFitness.values()
                                                 .stream()
                                                 .mapToDouble(d->d)
                                                 .sum() ;
        // Scale their relative fitness to amplify the best strategies participation
        //    in the mating pool
        for(String s : strategiesToFitness.keySet()) {
            strategiesToFitness.put(s, strategiesToFitness.get(s) / totalFitness) ;
        }

        System.out.println("Normalized values:") ;
        strategiesToFitness.forEach((key, value) -> {
            System.out.printf("\tStrategy=%s Value=%.3f\n", key, value);
        });
    }
}

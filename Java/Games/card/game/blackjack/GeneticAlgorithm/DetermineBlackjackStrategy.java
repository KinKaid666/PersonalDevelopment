package card.game.blackjack.GeneticAlgorithm ;

import java.util.* ;
import java.util.stream.* ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.game.blackjack.GeneticAlgorithm.* ;
import card.Card ;
import java.util.Random ;


public class DetermineBlackjackStrategy {
    private static Logger logger = Logger.getLogger(DetermineBlackjackStrategy.class.getName()) ;
    // after how many generations should you see output?
    private static final int OUTPUT_TIMING = 100 ;
    private static Random r = new Random(System.currentTimeMillis()) ;
    private static final String bookFilename =
        "/Users/ericferguson/Development/Java/Games/card/game/blackjack/Strategies/perfect.stg" ;

    public static void main(String[] args) {
        if(!(args.length == 4)) {
            System.err.println("usage: java " +
                    System.getProperty("sun.java.command").split(" ")[0] +
                    " <number of strategies> <number of hands> " +
                    "<number of generations> <mutation rate>") ;
            System.exit(1) ;
        }

        // Process arguments
        //    n - # of starting strategies
        //    g - # games per strategy
        //    t - # generations
        //    m - mutation rate
        int numStrategies   = Integer.parseInt(args[0]) ;
        int numGames        = Integer.parseInt(args[1]) ;
        int numGenerations  = Integer.parseInt(args[2]) ;
        double mutationRate = Double.parseDouble(args[3]) ;

        System.out.printf("Evolving blackjack: Population = %,d, " +
                          "Games per Strategy = %,d, " +
                          "Generations of evolution = %,d, " +
                          "with a mutation rate of %.2f%%\n",
                              numStrategies,
                              numGames,
                              numGenerations,
                              mutationRate*100.0d) ;

        // setup n random blackjack simulations
        List<BlackjackEvolutionObject> strategies = new ArrayList<BlackjackEvolutionObject>() ;
        for(int i = 0 ; i < numStrategies ; i++) {
            strategies.add(new BlackjackEvolutionObject(BlackjackStrategy.createRandom())) ;
        }
        BlackjackStrategy bestStrategy = null ;

        // loop for g generations
        for(int i = 0 ; i < numGenerations ; i++) {

            // foreach strategies, play blackjack g hands and get it's fitness
            for(int j = 0 ; j < numStrategies ; j++) {
                BlackjackSimulator sim =
                    new BlackjackSimulator(strategies.get(j).getStrategy(), numGames) ;
                sim.simulate() ;
                double fitness = sim.getFitness() ;
                strategies.get(j).setFitness(fitness) ;
            }

            Collections.sort(strategies) ;
            bestStrategy = strategies.get(strategies.size() - 1).getStrategy() ;

            // Create a mating pool
            List<BlackjackEvolutionObject> matingPool = null ;
            if( true ) {
                // Try top 50
                int n = 50 ;
                matingPool = strategies.stream()
                                       .skip(numStrategies - n)
                                       .collect(Collectors.toList()) ;
            } else if ( false ) {
                // Try top 10
                int n = 10 ;
                matingPool = strategies.stream()
                                       .skip(numStrategies - n)
                                       .collect(Collectors.toList()) ;
                // After 100 generations 0.941
            } else if ( false ) {
                // try all
                matingPool = strategies ;
                // After 100 generations 0.857
            }

            // Rescale the fitness the array such that the worst strategy gets a 0
            Double minFitness = matingPool.stream()
                                          .map(v -> v.getFitness())
                                          .mapToDouble(d->d)
                                          .min()
                                          .orElse(0.0d) ;
            for(int j = 0 ; j < matingPool.size() ; j++) {
                matingPool.get(j).setNormalizedFitness(
                        matingPool.get(j).getFitness() - minFitness) ;
            }

            Double totalFitness = matingPool.stream()
                                            .map(v -> v.getNormalizedFitness())
                                            .mapToDouble(d->d)
                                            .sum() ;

            for(int j = 0 ; j < matingPool.size() ; j++) {
                matingPool.get(j).setNormalizedFitness(
                        matingPool.get(j).getNormalizedFitness()/totalFitness) ;
            }

            // generate new strategy population
            ArrayList<BlackjackEvolutionObject> newStrategies =
                new ArrayList<BlackjackEvolutionObject>() ;
            for(int j = 0 ; j < numStrategies ; j++) {
                BlackjackStrategy a = BlackjackEvolutionObject.pickStrategy(matingPool,
                                                                            r.nextDouble()) ;
                BlackjackStrategy b = BlackjackEvolutionObject.pickStrategy(matingPool,
                                                                            r.nextDouble()) ;

                if(a == null || b == null) {
                    System.err.println("Failed to pick a strategy to mate\n") ;
                    System.exit(1) ;
                }
                // crossover creates new strategy object that zeros it's statistics
                BlackjackStrategy c = a.crossover(b) ;

                // random modify the configuration based on the mutation rate
                c.mutate(mutationRate) ;

                // Add it to the new mating pool
                newStrategies.add(new BlackjackEvolutionObject(c)) ;
            }
            //for(BlackjackEvolutionObject bjEO : strategies) {
                //System.out.printf("\t[Gen: %d] BlackjackEvolutionObject => f: %.3f, n: %.3f\n",
                        //i, bjEO.getFitness(), bjEO.getNormalizedFitness()) ;
                //bjEO.getStrategy().printStrategy(System.out) ;
            //}
            strategies = newStrategies ;
            System.out.printf("After %d generations, the max fitness %.3f\n",
                              i+1,
                              bestStrategy.getFitness()) ;
            System.out.println("Best Strategy so far...") ;
            bestStrategy.printStrategy(System.out) ;
        }
        try {
            System.out.printf("Best Strategy vs. Book = %.3f%%\n",
                    bestStrategy.compare(
                        BlackjackStrategyStatic.createStrategyFromFile(bookFilename))*100.0d) ;
        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
        }
    }
}

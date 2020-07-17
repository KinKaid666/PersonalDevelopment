package card.game.blackjack.GeneticAlgorithm ;

import java.util.List ;
import java.util.ArrayList ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.game.blackjack.GeneticAlgorithm.* ;
import card.Card ;
import java.util.Random ;


public class DetermineBlackjackStrategy
{
    private static Logger logger = Logger.getLogger(DetermineBlackjackStrategy.class.getName()) ;
    // after how many generations should you see output?
    private static final int OUTPUT_TIMING = 100 ;
    private static Random r = new Random(System.currentTimeMillis()) ;

    private static double map(double value, double istart, double istop, double ostart, double ostop)
    {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart)) ;
    }

    public static void main(String[] args)
    {
        if(!(args.length == 4))
        {
            System.err.println("usage: java " + System.getProperty("sun.java.command").split(" ")[0] + " <number of strategies> <number of games> <number of generations> <mutation rate>") ;
            System.exit(1) ;
        }

        // Process arguments
        //    n - # of starting strategies
        //    g - # generations
        //    m - mutation rate
        int numStrategies   = Integer.parseInt(args[0]) ;
        int numGames        = Integer.parseInt(args[1]) ;
        int numGenerations  = Integer.parseInt(args[2]) ;
        double mutationRate = Double.parseDouble(args[3]) ;

        System.out.printf("Evolving blackjack: Population = %,d, Games per Strategy = %,d, Generations of evolution = %,d, with a mutation rate of %.2f%%\n",numStrategies,numGames,numGenerations,mutationRate*100.0d) ;
        // setup n random blackjack simulations
        ArrayList<BlackjackEvolutionObject> strategies = new ArrayList<BlackjackEvolutionObject>() ;
        for( int i = 0 ; i < numStrategies ; i++ )
        {
            strategies.add(new BlackjackEvolutionObject(BlackjackStrategy.createRandom())) ;
        }
        BlackjackStrategy bestStrategy = null ;

        // loop for g generations
        for( int i = 0 ; i < numGenerations ; i++ )
        {
            double maxFitness = 0.0d, minFitness = Double.MAX_VALUE ;
            double fitnessSum = 0.0d ;

            // foreach strategies, play blackjack N number and get it's fitness
            for( int j = 0 ; j < numStrategies ; j++ )
            {
                BlackjackSimulator sim = new BlackjackSimulator(strategies.get(j).getStrategy(), numGames) ;
                sim.simulate() ;
                double fitness = sim.getFitness() ;
                strategies.get(j).setFitness(fitness) ;
                if(fitness > maxFitness)
                {
                    maxFitness = fitness ;
                    bestStrategy = strategies.get(j).getStrategy() ;
                }
                if(fitness < minFitness)
                {
                    minFitness = fitness ;
                }
            }

            // Scale their relative fitness to amplify the best strategies participation
            //    in the mating pool
            for( int j = 0 ; j < numStrategies ; j++ )
            {
                double newFitness = Math.pow(strategies.get(j).getFitness() - minFitness,3) ;
                strategies.get(j).setNormalizedFitness(newFitness) ;
                fitnessSum += newFitness ;
            }
            for( int j = 0 ; j < numStrategies ; j++ )
            {
                strategies.get(j).setNormalizedFitness(strategies.get(j).getNormalizedFitness()/fitnessSum) ;
            }

            /* Debug
            for( int j = 0 ; j < numStrategies ; j++ )
            {
                System.out.printf("\t\tStrategy[%d].fitness = %.3f, normalizedFitness = %.3f\n", j, strategies.get(j).getFitness(),strategies.get(j).getNormalizedFitness()) ;
            }
            */

            // generate new strategy population
            ArrayList<BlackjackEvolutionObject> newStrategies = new ArrayList<BlackjackEvolutionObject>() ;
            for( int j = 0 ; j < numStrategies ; j++ )
            {
                BlackjackStrategy a = BlackjackEvolutionObject.pickStrategy(strategies, r.nextDouble()) ;
                BlackjackStrategy b = BlackjackEvolutionObject.pickStrategy(strategies, r.nextDouble()) ;

                // crossover creates new strategy object that zeros it's statistics
                BlackjackStrategy c = a.crossover(b) ;

                // random modify the configuration based on the mutation rate
                c.mutate(mutationRate) ;

                // Add it to the new mating pool
                newStrategies.add(new BlackjackEvolutionObject(c)) ;
            }
            strategies = newStrategies ;
            System.out.printf("After %d generations, the max fitness %.3f\n", i, bestStrategy.getFitness()) ;
        }
        System.out.println("Best Strategy...") ;
        bestStrategy.printStrategy(System.out) ;
        try
        {
            System.out.printf("Best Strategy vs. Book = %.3f%%\n", bestStrategy.compare(BlackjackStrategyStatic.createStrategyFromFile("/Users/ericferguson/PersonalDevelopment/Java/Games/card/game/blackjack/Strategies/perfect.stg"))*100.0d) ;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage()) ;
        }
    }
}

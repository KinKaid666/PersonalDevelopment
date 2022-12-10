package card.game.blackjack.GeneticAlgorithm ;

import java.util.* ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.Card ;

public class BlackjackEvolutionObject implements Comparable<BlackjackEvolutionObject> {
    private BlackjackStrategy s_                 ;
    private double            fitness_           ;
    private double            normalizedFitness_ ;

    public BlackjackEvolutionObject(BlackjackStrategy s,
                                    double            fitness,
                                    double            normalizedFitness) {
        s_                 = s                 ;
        fitness_           = fitness           ;
        normalizedFitness_ = normalizedFitness ;
    }

    public BlackjackEvolutionObject(BlackjackStrategy s) {
        s_                 = s    ;
        fitness_           = 0.0d ;
        normalizedFitness_ = 0.0d ;
    }

    // Getters
    public BlackjackStrategy getStrategy()         { return s_                 ; }
    public double            getFitness()          { return fitness_           ; }
    public double            getNormalizedFitness(){ return normalizedFitness_ ; }

    // Setters
    public void setStrategy         (BlackjackStrategy s){ s_                 = s ; }
    public void setFitness          (double            f){ fitness_           = f ; }
    public void setNormalizedFitness(double            f){ normalizedFitness_ = f ; }

    //
    // Given an array of BlackjackEvolutionObjects, pick the element at pct
    public static BlackjackStrategy pickStrategy(List<BlackjackEvolutionObject> l, double targetPct) {
        if(l.size() == 0) {
            return null ;
        }
        //System.out.printf("\tget object with %.3f in array of size %d\n", targetPct, l.size()) ;
        double cumPct = 0.0d ;
        int i = 0 ;
        while(cumPct <= targetPct) {
            cumPct += l.get(i).getNormalizedFitness() ;
            //System.out.printf("\t after %d, cumPct = %.3f\n", i, cumPct) ;
            i++ ;
        }
        i-- ;
        //System.out.printf("\tPicking %d with fitness of %.3f\n",i, l.get(i).getFitness()) ;
        return l.get(i).getStrategy() ;
    }

    @Override
    public final int compareTo(BlackjackEvolutionObject that) {
        return Double.compare(this.fitness_, that.fitness_) ;
    }
} ;

package dice ;

import java.util.* ;

public class Dice {

    private ArrayList<Die> dice_ = null ;
    private int diceValue_  = 0 ;
    private boolean keepStatistics_ = false ;

    private Map<Integer, Integer> stats_ = null ;

    public Dice(int numDice) {
        init(numDice,false) ;
    }

    public Dice(int numDice, boolean keepStatistics) {
        init(numDice,keepStatistics) ;
    }

    private void init(int numDice, boolean keepStatistics) {
        keepStatistics_ = keepStatistics ;
        dice_ = new ArrayList<Die>(numDice) ;
        for( int i = 0 ; i < numDice ; ++i ) {
            dice_.add(new Die()) ;
        }
    }

    // Roll the dice
    public int roll() {
        int value = 0 ;
        for( Die d : dice_ ) {
            value += d.roll() ;
        }

        diceValue_ = value ;
        recordRoll(value) ;
        return diceValue_ ;
    }

    // Roll x of the dice, used in some games
    public int roll(int x) {
        int value = 0 ;
        for( int i = 0 ; i < x && i < dice_.size() ; i++ ) {
            value += dice_.get(i).roll() ;
        }

        diceValue_ = value ;
        recordRoll(value) ;
        return diceValue_ ;
    }

    private void recordRoll(int value) {
        if(!keepStatistics_) {
            return ;
        }

        if(stats_ == null) {
            stats_ = new HashMap<Integer,Integer>() ;
        }

        if(stats_.containsKey(diceValue_)) {
            Integer currentCount = stats_.get(diceValue_) ;
            stats_.replace(diceValue_, ++currentCount) ;

        } else {
            stats_.put(diceValue_,1) ;
        }
    }

    public Dice clone() {
        Dice clonedDice = new Dice(2) ;
        clonedDice.diceValue_ = this.diceValue_ ;
        clonedDice.dice_ = null ;
        clonedDice.dice_ = new ArrayList<Die>(this.dice_.size()) ;
        for(int i = 0 ; i < this.dice_.size() ; i++) {
            clonedDice.dice_.add(this.dice_.get(i).clone()) ;
        }
        return clonedDice ;
    }


    // Getters
    public int value() { return diceValue_ ; }
    public int value(int die) { return dice_.get(die).value() ; }
    public Map<Integer,Integer> getStatistics() { return stats_ ; }
} ;

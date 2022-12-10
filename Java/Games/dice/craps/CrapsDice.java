package dice.craps ;

import dice.* ;

public class CrapsDice {
    private Dice dice_ = new Dice(2) ;
    private CrapsRoll currentRoll_ = null ;

    public CrapsDice() { }

    public CrapsRoll roll() {
        dice_.roll() ;
        return (currentRoll_ = new CrapsRoll(dice_)) ;
    }
    public CrapsRoll value() { return currentRoll_ ; }
} ;

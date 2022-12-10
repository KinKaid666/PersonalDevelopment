package dice.craps ;

public class ComeBet extends Bet {

    public ComeBet(Double bet) {
        super(bet) ;
    }

    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.isPoint() ;
        } else {
            return roll.value().equals(placePoint_) ;
        }
    }

    public boolean didLose(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.value().equals(Integer.valueOf(7)) ;
        } else {
            return roll.value().equals(Integer.valueOf(7)) ;
        }
    }

    public Double winAmountMultipler(CrapsRoll roll) {
    }
}

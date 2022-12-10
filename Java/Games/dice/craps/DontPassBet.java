package dice.craps ;

public class DontPassBet extends Bet {

    public DontPassBet(Double bet) {
        super(bet) ;
    }

    // Win:
    //    on() == 7
    //    off() == 2, 3 (push 12)
    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.value().equals(Integer.valueOf(7)) ;
        } else {
            return roll.isComeOutCraps() && !roll.value().equals(Integer.valueOf(12)) ;
        }
    }

    // Lose:
    //    on() == Point
    //    off() == 2, 3 (push 12)
    public boolean didLose(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.value().equals(point) ;
        } else {
            return roll.isComeOutNatural() ;
        }
    }

    public Double winAmountMultipler(CrapsRoll roll) {
        return Double.valueOf(1.0) ;
    }
}

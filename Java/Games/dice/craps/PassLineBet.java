package dice.craps ;

public class PassLineBet extends Bet {

    public PassLineBet(Double bet) {
        super(bet) ;
    }

    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return point.equals(roll.value()) ;
        } else {
            return roll.isComeOutNatural() ;
        }
    }

    public boolean didLose(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.value().equals(Integer.valueOf(7)) ;
        } else {
            return roll.isComeOutCraps() ;
        }
    }

    public Double winAmountMultipler(CrapsRoll roll) {
        return Double.valueOf(1.0) ;
    }
}

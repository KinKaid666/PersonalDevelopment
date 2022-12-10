package dice.craps ;

public class FieldBet extends Bet {

    public FieldBet(Double bet) {
        super(bet) ;
    }

    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        return roll.isField() ;
    }

    public boolean didLose(boolean on, Integer point, CrapsRoll roll) {
        return !roll.isField() ;
    }

    public Double winAmountMultipler(CrapsRoll roll) {
        switch(roll.value()) {
        case 2:
        case 12:
            return Double.valueOf(2) ;
        case  3:
        case  4:
        case  9:
        case 10:
        case 11:
            return Double.valueOf(1) ;
        default:
            return Double.valueOf(0) ;
        }
    }
}

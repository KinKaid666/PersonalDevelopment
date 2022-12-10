package dice.craps ;

public class HardwayBet extends Bet {
    private Integer hardwayPoint_ ;

    public HardwayBet(Double bet, Integer point) {
        super(bet) ;
        hardwayPoint_ = point ;
    }

    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        return roll.isHardway() && hardwayPoint_.equals(roll.value()) ;
    }

    public boolean didLose(boolean on, Integer point, CrapsRoll roll) {
        return roll.isHardwayValue() && !roll.isPair() && hardwayPoint_.equals(roll.value()) ;
    }

    public Double winAmountMultipler(CrapsRoll roll) {
        switch(roll.value()) {
        case  4:
        case 10:
            return Double.valueOf(7) ;
        case  6:
        case  8:
            return Double.valueOf(9) ;
        default:
            return Double.valueOf(0) ;
        }
    }
}

package dice.craps ;

public class PlaceBet extends Bet {
    private Integer placePoint_ ;

    public PlaceBet(Double bet, Integer placePoint) {
        super(bet) ;
        placePoint_ = placePoint ;
    }

    public boolean didWin(boolean on, Integer point, CrapsRoll roll) {
        if(on) {
            return roll.value().equals(placePoint_) ;
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
        switch(placePoint_) {
        case 4:
        case 10:
            return Double.valueOf(9)/Double.valueOf(5) ;
        case 5:
        case 9:
            return Double.valueOf(7)/Double.valueOf(5) ;
        case 6:
        case 8:
            return Double.valueOf(7)/Double.valueOf(6) ;
        default:
            return Double.valueOf(0) ;
        }
    }
}

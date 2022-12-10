package dice.craps ;

public class PlayCraps {
    public static void main(String[] args) {
        // New game, 5x odds
        //if(false) {
        CrapsRules rules = new CrapsRules(Integer.valueOf(5)) ;
        Craps c = new Craps(rules) ;
        c.addBet(new PassLineBet(Double.valueOf(10))) ;
        c.addBet(new DontPassBet(Double.valueOf(10))) ;
        c.addBet(new PlaceBet(Double.valueOf(10),Integer.valueOf(6))) ;
        c.addBet(new PlaceBet(Double.valueOf(10),Integer.valueOf(8))) ;
        c.addBet(new FieldBet(Double.valueOf(10))) ;
        c.addBet(new HardwayBet(Double.valueOf(10),Integer.valueOf(6))) ;
        c.play(10) ;
        //}

        // one roll check
        if(false) {
        Bet b = new PassLineBet(Double.valueOf(10)) ;
        CrapsDice dice = new CrapsDice() ;
        CrapsRoll roll ;
        while(true) {
            roll = dice.roll() ;
            if(roll.value() == Integer.valueOf(7)) {
                break ;
            }
        }
        System.out.println("Roll = " + roll) ;
        System.out.println("PassLineBet.didLose(true, 5, 7) = " +
                b.didLose(true, Integer.valueOf(5), roll)) ;
        }
    }
} ;

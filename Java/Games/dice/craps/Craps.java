package dice.craps ;

import java.util.* ;

public class Craps {
    private CrapsDice dice_ ;
    private Integer point_ ;
    private List<Bet> bets_ ;

    public Craps(CrapsRules rules) {
        dice_ = new CrapsDice() ;
        bets_ = new LinkedList<Bet>() ;
    }

    public void addBet(Bet b) {
        bets_.add(b) ;
    }

    /*
     * Play _rounds_ rounds
     */
    public void play(int rounds) {
        while(rounds-- > 0) {
            playRound() ;
        }
    }

    /*
     * Play one round defined as establishing a point
     * then either crapping out or hitting the point
     */
    public void playRound() {
        // Come out / establish point / start round
        while(off()) {
            CrapsRoll roll = dice_.roll() ;
            System.out.println("Come out roll = " + roll) ;

            evaluateBets() ;
            if(roll.isPoint()) {
                point_ = roll.value() ;
                break ;
            }
        }

        // Button on, roll until round ends
        while(on()) {
            CrapsRoll roll = dice_.roll() ;
            System.out.println("\tpoint = " + point() + ", roll = " + roll) ;

            evaluateBets() ;
            if(dice_.value().value().equals(Integer.valueOf(7)) || dice_.value().value().equals(point())) {
                point_ = null ;
                break ;
            }
        }
    }

    // Helper function to pay bets
    private void evaluateBets() {
        if(bets_ != null) {
            for(Bet b: bets_) {
                if(b.isWorking()) {
                    if(b.didWin(on(), point(), dice_.value())) {
                        System.out.printf("\t\tWINNING %s $%.2f\n",
                                              b.getClass().getSimpleName(),
                                              (b.getBet() * b.winAmountMultipler(dice_.value()))) ;
                    }

                    if(b.didLose(on(), point(), dice_.value())) {
                        System.out.printf("\t\tLOSING %s $%.2f\n",
                                              b.getClass().getSimpleName(),
                                              b.getBet()) ;
                    }
                }
            }
        }
    }

    public boolean on()    { return point_ != null ; }
    public boolean off()   { return !on() ; }
    public Integer point() { return point_ ; }
} ;

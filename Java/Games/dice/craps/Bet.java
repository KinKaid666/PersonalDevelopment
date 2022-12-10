package dice.craps ;

public abstract class Bet {
    private Double bet_ ;
    private boolean working_ ;

    public Bet(Double bet) {
        bet_ = bet ;
        working_ = true ;
    }

    public Bet(Double bet, boolean working) {
        bet_ = bet ;
        working_ = working ;
    }

    // getters and setters
    public Double getBet() { return bet_ ; }
    public void setBet(Double bet) { bet_ = bet ; }
    public boolean isWorking() { return working_ ; }
    public void setWorking(boolean working) { working_ = working ; }


    // Three abstract methods for derived classes
    // to implement
    //   1. didWin() -> did the bet win?
    //   2. didLose() -> did the bet lose?
    //   3. winAmountMultipler() -> when the bet wins how much
    public abstract boolean didWin(boolean on, Integer point, CrapsRoll roll) ;
    public abstract boolean didLose(boolean on, Integer point, CrapsRoll roll) ;
    public abstract Double winAmountMultipler(CrapsRoll roll) ;

} ;

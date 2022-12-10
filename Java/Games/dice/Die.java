package dice ;
import java.util.Random ;

public class Die
{
    private static int diceMin_ = 0 ;
    private static int diceMax_ = 6 ;
    private int diceValue_ ;
    private static Random random_ ;

    public Die() {
        if(random_ == null)
            random_ = new Random(System.currentTimeMillis()) ;
        diceValue_ = 0 ;
    }


    //
    // Roll the dice
    public int roll()
    {
        diceValue_ = (int)(random_.nextDouble() * (diceMax_ - diceMin_)) + 1 ;
        return diceValue_ ;
    }

    //
    // Getter
    public int value()
    {
        return diceValue_ ;
    }

    public Die clone() {
        Die clonedDie = new Die() ;
        clonedDie.diceValue_ = this.diceValue_ ;
        clonedDie.random_ = this.random_ ;
        return clonedDie ;
    }
} ;

package dice ;
import java.util.Random ;

public class Die
{
    private static int diceMin_ = 0 ;
    private static int diceMax_ = 6 ;
    private int  diceValue_ ;
    private static Random random_ ;
    private static boolean randomInitialized_ = false ;

    public Die()
    {
        if(!randomInitialized_)
        {
            random_ = new Random(System.currentTimeMillis()) ;
            randomInitialized_ = true ;
        }
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
} ;

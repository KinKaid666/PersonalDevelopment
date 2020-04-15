package dice ;

import java.util.HashMap ;
import java.util.Map ;
import java.util.LinkedList ;
import java.util.List ;

public class Dice
{

    private List<Die> dice_ = null ;
    private int diceValue_  = 0 ;

    private Map<Integer, Integer> stats_ = null ;

    public Dice(int numDice)
    {
        dice_ = new LinkedList<Die>() ;
        for( int i = 0 ; i < numDice ; ++i )
        {
            dice_.add(new Die()) ;
        }
        stats_ = new HashMap<Integer,Integer>() ;
    }


    //
    // Roll the dice
    public int roll()
    {
        int value = 0 ;
        for( Die d : dice_ )
        {
            value += d.roll() ;
        }

        diceValue_ = value ;

        if(stats_.containsKey(diceValue_))
        {
            Integer currentCount = stats_.get(diceValue_) ;
            stats_.replace(diceValue_, ++currentCount) ;

        }
        else
        {
                stats_.put(diceValue_,1) ;
        }
        return diceValue_ ;
    }

    //
    // Getter
    public int value()
    {
        return diceValue_ ;
    }

    public Map<Integer,Integer> getStatistics()
    {
        return stats_ ;
    }
} ;

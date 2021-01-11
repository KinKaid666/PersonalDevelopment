package Trouble ;
import java.util.Random ;
public class TroubleDice
{
    private static int _diceMin = 0 ;
    private static int _diceMax = 6 ;
    private int    _diceValue ;
    private Random _random ;

    public TroubleDice()
    {
        _random = new Random(System.currentTimeMillis()) ;
        _diceValue = 0 ;
    }


    //
    // Roll the dice
    public int Roll()
    {
        _diceValue = (int)(_random.nextDouble() * (_diceMax - _diceMin)) + 1 ;
        return _diceValue ;
    }

    //
    // Getter
    public int Value()
    {
        return _diceValue ;
    }
} ;

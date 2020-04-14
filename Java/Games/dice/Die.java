import java.util.Random ;
class Die
{
    private static int _diceMin = 0 ;
    private static int _diceMax = 6 ;
    private int  _diceValue ;
    private Random _random ;

    public Die()
    {
        _random = new Random(System.currentTimeMillis()) ;
        _diceValue = 0 ;
    }


    //
    // Roll the dice
    public int roll()
    {
        _diceValue = (int)(_random.nextDouble() * (_diceMax - _diceMin)) + 1 ;
        return _diceValue ;
    }

    //
    // Getter
    public int value()
    {
        return _diceValue ;
    }
} ;

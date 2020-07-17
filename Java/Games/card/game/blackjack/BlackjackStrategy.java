package card.game.blackjack ;

import card.Card ;
import java.util.* ;
import java.io.PrintStream ;
import java.util.Random ;

/*
 * Base class
 */
public class BlackjackStrategy
{
    private BlackjackStatistics stats_ = new BlackjackStatistics() ;
    static final Random random_ = new Random(System.currentTimeMillis()) ;

    private HashMap<Integer, HashMap<Card, StrategicMove>>      pairStrategies_ = null ;
    private HashMap<Integer, HashMap<Card, StrategicMove>> softTotalStrategies_ = null ;
    private HashMap<Integer, HashMap<Card, StrategicMove>> hardTotalStrategies_ = null ;
    private HashMap<Integer, HashMap<Card, StrategicMove>> surrenderStrategies_ = null ;

    private boolean takeInsurance_ ;
    private boolean takeEvenMoney_ ;

    public enum StrategicMove
    {
        Hit,
        Stand,
        DoubleElseHit,
        DoubleElseStand,
        Split,
        DontSplit,
        SplitIfDouble,
        Surrender,
        NoSurrender
    } ;

    public BlackjackStrategy(
                    HashMap<Integer, HashMap<Card, StrategicMove>>      pairStrategies,
                    HashMap<Integer, HashMap<Card, StrategicMove>> softTotalStrategies,
                    HashMap<Integer, HashMap<Card, StrategicMove>> hardTotalStrategies,
                    HashMap<Integer, HashMap<Card, StrategicMove>> surrenderStrategies,
                    boolean                                takeInsurance      ,
                    boolean                                takeEvenMoney      )
    {
              pairStrategies_ =      pairStrategies ;
         softTotalStrategies_ = softTotalStrategies ;
         hardTotalStrategies_ = hardTotalStrategies ;
         surrenderStrategies_ = surrenderStrategies ;
         takeInsurance_       = takeInsurance       ;
         takeEvenMoney_       = takeEvenMoney       ;
    }

    /*
     * Forwarding functions to the statistics helper class
     */
    public void recordHandList(List<BlackjackHand>         handList,
                               Card                        c,
                               List<BlackjackHand.Outcome> outcomes,
                               double[]                    weights) throws Exception
    {
        stats_.recordHandList(handList, c, outcomes, weights) ;
    }

    /* Forwarding functions */
    public void printStatistics   (PrintStream o) { stats_.printStatistics(o)   ; }
    public void printStatisticsCSV(PrintStream o) { stats_.printStatisticsCSV(o); }
    public double getFitness() { return stats_.getFitness() ; }

    /*
     * Genetic Algorthim Functions
     */
    //
    // Blend the this and other into a new object
    //   Randomly take one row of data from either object
    //
    public BlackjackStrategy crossover(BlackjackStrategy other)
    {
        BlackjackStrategy s = this.clone() ;
        for(Integer key : s.pairStrategies_.keySet())
        {
            for( Card c : pairStrategies_.get(key).keySet() )
            {
                if(random_.nextBoolean())
                {
                    s.pairStrategies_.get(key).put(c,other.pairStrategies_.get(key).get(c)) ;
                }
            }
        }
        for(Integer key : s.softTotalStrategies_.keySet())
        {
            for( Card c : softTotalStrategies_.get(key).keySet() )
            {
                if(random_.nextBoolean())
                {
                    s.softTotalStrategies_.get(key).put(c,other.softTotalStrategies_.get(key).get(c)) ;
                }
            }
        }
        for(Integer key : s.hardTotalStrategies_.keySet())
        {
            for( Card c : hardTotalStrategies_.get(key).keySet() )
            {
                if(random_.nextBoolean())
                {
                    s.hardTotalStrategies_.get(key).put(c,other.hardTotalStrategies_.get(key).get(c)) ;
                }
            }
        }
        for(Integer key : s.surrenderStrategies_.keySet())
        {
            for( Card c : surrenderStrategies_.get(key).keySet() )
            {
                if(random_.nextBoolean())
                {
                    s.surrenderStrategies_.get(key).put(c,other.surrenderStrategies_.get(key).get(c)) ;
                }
            }
        }
        return s ;
    }


    //
    // Iterate over the configuration and mutate at the learning rate (percentage)
    public void mutate(double mutationRate)
    {
        for(Integer key : pairStrategies_.keySet())
        {
            for( Card c : pairStrategies_.get(key).keySet() )
            {
                if(random_.nextDouble() < mutationRate)
                {
                    StrategicMove m = (random_.nextBoolean() ? StrategicMove.Split : StrategicMove.Stand ) ;
                    pairStrategies_.get(key).put(c,m) ;
                }
            }
        }
        for(Integer key : softTotalStrategies_.keySet())
        {
            for( Card c : softTotalStrategies_.get(key).keySet() )
            {
                if(random_.nextDouble() < mutationRate)
                {
                    StrategicMove[] options = new StrategicMove[]{StrategicMove.DoubleElseHit,StrategicMove.Stand,StrategicMove.Hit} ;
                    StrategicMove m = options[random_.nextInt(3)] ;
                    softTotalStrategies_.get(key).put(c,m) ;
                }
            }
        }
        for(Integer key : hardTotalStrategies_.keySet())
        {
            for( Card c : hardTotalStrategies_.get(key).keySet() )
            {
                if(random_.nextDouble() < mutationRate)
                {
                    StrategicMove[] options = new StrategicMove[]{StrategicMove.DoubleElseHit,StrategicMove.Stand,StrategicMove.Hit} ;
                    StrategicMove m = options[random_.nextInt(3)] ;
                    hardTotalStrategies_.get(key).put(c,m) ;
                }
            }
        }
        for(Integer key : surrenderStrategies_.keySet())
        {
            for( Card c : surrenderStrategies_.get(key).keySet() )
            {
                if(random_.nextDouble() < mutationRate)
                {
                    StrategicMove m = (random_.nextBoolean() ? StrategicMove.Surrender : StrategicMove.NoSurrender) ;
                    surrenderStrategies_.get(key).put(c,m) ;
                }
            }
        }
    }

    // deep Clone, avoid annoying java-ness
    @SuppressWarnings("unchecked")
    public BlackjackStrategy clone()
    {
        // Clone each of the Hashmaps, but the inside Hashmap is still a shallow
        BlackjackStrategy s = new BlackjackStrategy(
                (HashMap<Integer,HashMap<Card,StrategicMove>>)pairStrategies_.clone(),
                (HashMap<Integer,HashMap<Card,StrategicMove>>)softTotalStrategies_.clone(),
                (HashMap<Integer,HashMap<Card,StrategicMove>>)hardTotalStrategies_.clone(),
                (HashMap<Integer,HashMap<Card,StrategicMove>>)surrenderStrategies_.clone(),
                takeInsurance_,
                takeEvenMoney_                ) ;
        for(Integer key : pairStrategies_.keySet())
        {
            s.pairStrategies_.put(key,(HashMap<Card,StrategicMove>)pairStrategies_.get(key).clone()) ;
        }
        for(Integer key : softTotalStrategies_.keySet())
        {
            s.softTotalStrategies_.put(key,(HashMap<Card,StrategicMove>)softTotalStrategies_.get(key).clone()) ;
        }
        for(Integer key : hardTotalStrategies_.keySet())
        {
            s.hardTotalStrategies_.put(key,(HashMap<Card,StrategicMove>)hardTotalStrategies_.get(key).clone()) ;
        }
        for(Integer key : surrenderStrategies_.keySet())
        {
            s.surrenderStrategies_.put(key,(HashMap<Card,StrategicMove>)surrenderStrategies_.get(key).clone()) ;
        }
        return s ;
    }

    /*
     * Printing function
     */
    public void printStrategy(PrintStream o)
    {
        o.println("*** Dumping Configuration" ) ; 
        o.println("    *** Dumping pair strategies" ) ; 
        BlackjackStrategyStatic.printStrategyHelper(pairStrategies_,o) ;
        o.println("    *** Dumping soft totals strategies" ) ; 
        BlackjackStrategyStatic.printStrategyHelper(softTotalStrategies_,o) ;
        o.println("    *** Dumping hard totals strategies" ) ; 
        BlackjackStrategyStatic.printStrategyHelper(hardTotalStrategies_,o) ;
        o.println("    *** Dumping surrender strategies" ) ; 
        BlackjackStrategyStatic.printStrategyHelper(surrenderStrategies_,o) ;
        o.println("    takeInsurance = " + (takeInsurance_? "true":"false")) ;
        o.println("    takeEvenMoney = " + (takeEvenMoney_? "true":"false")) ;
    }

    public static void printStrategyHelper(HashMap<Integer, HashMap<Card, StrategicMove>> m, PrintStream o)
    {
        for(Integer key : m.keySet())
        {
            for( Card c : m.get(key).keySet() )
            {
                o.println("        PlayerHandValue = " + key + ", Dealer Upcard = " + c + ",\tMove = " + m.get(key).get(c)) ;
            }
        }
    }

    public static BlackjackStrategy createRandom()
    {

        // Initialize all the maps and then call super
        HashMap<Integer,HashMap<Card,StrategicMove>>     pairsStrategies = new HashMap<Integer,HashMap<Card,StrategicMove>>() ;
        HashMap<Integer,HashMap<Card,StrategicMove>> softTotalStrategies = new HashMap<Integer,HashMap<Card,StrategicMove>>() ;
        HashMap<Integer,HashMap<Card,StrategicMove>> hardTotalStrategies = new HashMap<Integer,HashMap<Card,StrategicMove>>() ;
        HashMap<Integer,HashMap<Card,StrategicMove>> surrenderStrategies = new HashMap<Integer,HashMap<Card,StrategicMove>>() ;

        try
        {
        // Randomly Initialize Pair values with Split or Not
        //   outter loop = pair value
        for(int i = 1 ; i < 11 ; i++)
        {
            // inner loop = dearler upcard
            for(int j = 2 ; j < 12 ; j++)
            {
                Card c = BlackjackStrategyStatic.getCardForConfigValue(j) ;
                StrategicMove m = (random_.nextBoolean() ? StrategicMove.Split : StrategicMove.Stand ) ;
                if(pairsStrategies.containsKey(i))
                {
                    pairsStrategies.get(i).put(c,m) ;
                }
                else
                {
                    HashMap<Card,StrategicMove> csm = new HashMap<Card,StrategicMove>() ;
                    csm.put(c,m) ;
                    pairsStrategies.put(i,csm) ;
                }
            }
        }

        // Randomly initialize soft totals with stand, hit or double
        //   outter loop = player hand
        for(int i = 12 ; i < 22 ; i++)
        {
            // inner loop = dearler upcard
            for(int j = 2 ; j < 12 ; j++)
            {
                Card c = BlackjackStrategyStatic.getCardForConfigValue(j) ;
                StrategicMove[] options = new StrategicMove[]{StrategicMove.DoubleElseHit,StrategicMove.Stand,StrategicMove.Hit} ;
                StrategicMove m = options[random_.nextInt(3)] ;
                if(softTotalStrategies.containsKey(i))
                {
                    softTotalStrategies.get(i).put(c,m) ;
                }
                else
                {
                    HashMap<Card,StrategicMove> csm = new HashMap<Card,StrategicMove>() ;
                    csm.put(c,m) ;
                    softTotalStrategies.put(i,csm) ;
                }
            }
        }

        // Randomly initialize hard totals with stand, hit or double
        //   outter loop = player hand
        for(int i = 4 ; i < 22 ; i++)
        {
            // inner loop = dearler upcard
            for(int j = 2 ; j < 12 ; j++)
            {
                Card c = BlackjackStrategyStatic.getCardForConfigValue(j) ;
                StrategicMove[] options = new StrategicMove[]{StrategicMove.DoubleElseHit,StrategicMove.Stand,StrategicMove.Hit} ;
                StrategicMove m = options[random_.nextInt(3)] ;
                if(hardTotalStrategies.containsKey(i))
                {
                    hardTotalStrategies.get(i).put(c,m) ;
                }
                else
                {
                    HashMap<Card,StrategicMove> csm = new HashMap<Card,StrategicMove>() ;
                    csm.put(c,m) ;
                    hardTotalStrategies.put(i,csm) ;
                }
            }
        }

        // Randomly initialize surrender with yes or no
        //   outter loop = player hand
        for(int i = 4 ; i < 22 ; i++)
        {
            // inner loop = dearler upcard
            for(int j = 2 ; j < 12 ; j++)
            {
                Card c = BlackjackStrategyStatic.getCardForConfigValue(j) ;
                StrategicMove m = (random_.nextBoolean() ? StrategicMove.Surrender : StrategicMove.NoSurrender) ;
                if(surrenderStrategies.containsKey(i))
                {
                    surrenderStrategies.get(i).put(c,m) ;
                }
                else
                {
                    HashMap<Card,StrategicMove> csm = new HashMap<Card,StrategicMove>() ;
                    csm.put(c,m) ;
                    surrenderStrategies.put(i,csm) ;
                }
            }
        }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage()) ;
            System.exit(1) ;
        }
        boolean takeInsurance = random_.nextBoolean() ;
        boolean takeEvenMoney = random_.nextBoolean() ;

        return new BlackjackStrategy(pairsStrategies,softTotalStrategies,hardTotalStrategies,surrenderStrategies,takeInsurance,takeEvenMoney) ;
    }

    public Blackjack.Move getHandDecision(BlackjackRules r, BlackjackHand h, Card dealerUpcard) throws Exception
    {
        Blackjack.Move move = Blackjack.Move.Unknown ;

        //
        // normalize:
        //    1. We're holding out strategy map only in spades for ease of space
        //    2. All the stragies for 10s are the same, so normalize
        Card normalizedDealerUpcard = Card.valueOf(dealerUpcard.getRank(),Card.Suit.Spades) ;
        if( normalizedDealerUpcard.getRank() == Card.Rank.King  ||
            normalizedDealerUpcard.getRank() == Card.Rank.Queen ||
            normalizedDealerUpcard.getRank() == Card.Rank.Jack  )
        {
            normalizedDealerUpcard = Card.valueOf(Card.Rank.Ten,Card.Suit.Spades) ;
        }

        // If we have a pair, process it
        if(Blackjack.Move.Unknown == move && h.isPair())
        {
            //
            // Figure out which pair we have; config shows one number and 1 = Ace
            Integer pairValue = h.getHandValue() ;
            pairValue = (pairValue == 12 ? pairValue = 1 : pairValue / 2) ;

            BlackjackStrategy.StrategicMove m = pairStrategies_.get(pairValue).get(normalizedDealerUpcard) ;
            switch(m)
            {
                case Split :
                    move = Blackjack.Move.Split ;
                    break ;
                case DontSplit :
                    // intentional
                    break ;
                case SplitIfDouble :
                    if(r.getCanDoubleAfterSplit())
                    {
                        move = Blackjack.Move.Split ;
                    }
                    else
                    {
                        // intentional fall through to hard strategy
                    }
                    break ;
            }
        }

        // Soft Totals
        if(Blackjack.Move.Unknown == move && h.isSoft())
        {
            BlackjackStrategy.StrategicMove m = softTotalStrategies_.get(h.getHandValue()).get(normalizedDealerUpcard) ;
            switch(m)
            {
                case Stand:
                    move = Blackjack.Move.Stand ;
                    break ;
                case Hit:
                    move = Blackjack.Move.Hit ;
                    break ;
                // TODO: check rules
                case DoubleElseHit:
                    if(h.cardCount() == 2)
                    {
                        move = Blackjack.Move.Double ;
                    }
                    else
                    {
                        move = Blackjack.Move.Hit ;
                    }
                    break ;
                case DoubleElseStand:
                    if(h.cardCount() == 2)
                    {
                        move = Blackjack.Move.Double ;
                    }
                    else
                    {
                        move = Blackjack.Move.Stand ;
                    }
                    break ;
            }
        }

        // surrender?
        if(Blackjack.Move.Unknown == move &&
                h.cardCount() == 2 &&
               (r.getAllowEarlySurrender() || r.getAllowLateSurrender()) &&
               surrenderStrategies_.get(h.getHandValue()).get(normalizedDealerUpcard) == BlackjackStrategy.StrategicMove.Surrender)
        {
            move = Blackjack.Move.Surrender ;
        }

        // Then hard totals
        if(Blackjack.Move.Unknown == move)
        {
            BlackjackStrategy.StrategicMove m = hardTotalStrategies_.get(h.getHandValue()).get(normalizedDealerUpcard) ;
            switch(m)
            {
                case Hit:
                    move = Blackjack.Move.Hit ;
                    break ;
                case Stand:
                    move = Blackjack.Move.Stand ;
                    break ;
                case DoubleElseHit:
                    if(h.cardCount() == 2)
                    {
                        move = Blackjack.Move.Double ;
                    }
                    else
                    {
                        move = Blackjack.Move.Hit ;
                    }
                    break ;
            }
        }

        if(Blackjack.Move.Unknown == move)
        {
            throw new Exception("Don't know what to do with " + h + " against a dealer upcard of " + dealerUpcard) ;
        }
        return move ;
    }

    public double compare(BlackjackStrategy other)
    {
        int i = 0, j = 0 ;
        for(Integer key : pairStrategies_.keySet())
        {
            for( Card c : pairStrategies_.get(key).keySet() )
            {
                if(pairStrategies_.get(key).get(c) == other.pairStrategies_.get(key).get(c))
                {
                    i++ ;
                }
                j++ ;
            }
        }
        for(Integer key : softTotalStrategies_.keySet())
        {
            for( Card c : softTotalStrategies_.get(key).keySet() )
            {
                if(softTotalStrategies_.get(key).get(c) == other.softTotalStrategies_.get(key).get(c))
                {
                    i++ ;
                }
                j++ ;
            }
        }
        for(Integer key : hardTotalStrategies_.keySet())
        {
            for( Card c : hardTotalStrategies_.get(key).keySet() )
            {
                if(hardTotalStrategies_.get(key).get(c) == other.hardTotalStrategies_.get(key).get(c))
                {
                    i++ ;
                }
                j++ ;
            }
        }
        for(Integer key : surrenderStrategies_.keySet())
        {
            for( Card c : surrenderStrategies_.get(key).keySet() )
            {
                if(surrenderStrategies_.get(key).get(c) == other.surrenderStrategies_.get(key).get(c))
                {
                    i++ ;
                }
                j++ ;
            }
        }
        return (1.0d * i)/j ;
    }
} ;

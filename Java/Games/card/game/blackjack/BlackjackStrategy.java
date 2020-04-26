package card.game.blackjack ;

import java.lang.Exception ;
import java.io.* ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.List ;
import card.game.blackjack.* ;
import card.Card ;

/*
 * Class that encapsulates our strategy
 *
 * FILE
 * #   PAIRS
 *     Y=Split Pair
 *     N=Don't Split Pair
 *     A=Split if Double After Split Allowed, otherwise do not split
 *   SOFT TOTALS
 *     S=Stand
 *     H=Hit
 *     D=Double if allowed, else Hit
 *     X=Double if allowed, else Stand
 *   HARD TOTALS
 *     S=Stand
 *     H=Hit
 *     D=Double if allowed, else Hit
 *   SURRENDER
 *     C=Surrender (capitulate)
 *     X=Do nothing
 */
// TODO: store each filename into a has so we don't re-create each object
public class BlackjackStrategy
{
    /* keeping the stats tied to the statistics */
    private BlackjackStatistics stats_ = new BlackjackStatistics() ;
    private static Map<String, BlackjackStrategy> strategies_ = new HashMap<String,BlackjackStrategy>() ;

    private final static String      PAIRS_STRING = "Pairs"    ;
    private final static String SOFT_TOTAL_STRING = "SoftTotal" ;
    private final static String HARD_TOTAL_STRING = "HardTotal" ;
    private final static String  SURRENDER_STRING = "Surrender" ;

    private Map<Integer, Map<Card, StrategicMove>>      pairStrategies_ = null ;
    private Map<Integer, Map<Card, StrategicMove>> softTotalStrategies_ = null ;
    private Map<Integer, Map<Card, StrategicMove>> hardTotalStrategies_ = null ;
    private Map<Integer, Map<Card, StrategicMove>> surrenderStrategies_ = null ;

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

    /*
     * The strategy is found in file
     *   Formmat
     *      1: # Comment
     *
     */
    private BlackjackStrategy(Map<Integer, Map<Card, StrategicMove>>      pairStrategies,
                              Map<Integer, Map<Card, StrategicMove>> softTotalStrategies,
                              Map<Integer, Map<Card, StrategicMove>> hardTotalStrategies,
                              Map<Integer, Map<Card, StrategicMove>> surrenderStrategies,
                              boolean                                takeInsurance      ,
                              boolean                                takeEvenMoney      )
    {
             pairStrategies_ =      pairStrategies ;
        softTotalStrategies_ = softTotalStrategies ;
        hardTotalStrategies_ = hardTotalStrategies ;
        surrenderStrategies_ = surrenderStrategies ;
        takeInsurance_ = takeInsurance ;
        takeEvenMoney_ = takeEvenMoney ;
    }

    /*
     * Forwarding functions to the statistics helper class
     */
    public void recordHandList(List<BlackjackHand> handList, Card c, List<BlackjackHand.Outcome> outcomes, double[] weights) throws Exception
    {
        stats_.recordHandList(handList, c, outcomes, weights) ;
    }

    /* Forwarding functions */
    public void printStatistics()    { stats_.printStatistics()    ; }
    public void printStatisticsCSV() { stats_.printStatisticsCSV() ; }

    /*
     * Printing function
     */
    public void printStrategy()
    {
        System.out.println("*** Dumping Configuration" ) ;
        System.out.println("    *** Dumping pair strategies" ) ;
        BlackjackStrategy.printStrategyHelper(pairStrategies_) ;
        System.out.println("    *** Dumping soft totals strategies" ) ;
        BlackjackStrategy.printStrategyHelper(softTotalStrategies_) ;
        System.out.println("    *** Dumping hard totals strategies" ) ;
        BlackjackStrategy.printStrategyHelper(hardTotalStrategies_) ;
        System.out.println("    *** Dumping surrender strategies" ) ;
        BlackjackStrategy.printStrategyHelper(surrenderStrategies_) ;
        System.out.println("    takeInsurance = " + (takeInsurance_? "true":"false")) ;
        System.out.println("    takeEvenMoney = " + (takeEvenMoney_? "true":"false")) ;
    }

    public static void printStrategyHelper(Map<Integer, Map<Card, StrategicMove>> m)
    {
        for(Integer key : m.keySet())
        {
            for( Card c : m.get(key).keySet() )
            {
                System.out.println("        PlayerHandValue = " + key + ", Dealer Upcard = " + c + ",\tMove = " + m.get(key).get(c)) ;
            }
        }
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
        //System.out.print("        ...getHandDecision( hand = " + h.getHandValue() + ", dealer upcard = " + normalizedDealerUpcard + ") = " ) ;

        // Early surrender is before the dealer checks their hold card; skipped
        // Late surrender is after the dealer checks their hold card.
        if(!h.isPair() && r.getAllowLateSurrender() &&
           surrenderStrategies_.get(h.getHandValue()).get(normalizedDealerUpcard) == BlackjackStrategy.StrategicMove.Surrender)
        {
            if(h.cardCount() == 2)
            {
                move = Blackjack.Move.Surrender ;
            }
        }

        // If we have a pair, process it
        if(Blackjack.Move.Surrender != move && h.isPair())
        {
            //
            // Figure out which pair we have; config shows one number and 1 = Ace
            Integer pairValue = h.getHandValue() ;
            pairValue = (pairValue == 12 ? pairValue = 1 : pairValue / 2) ;

            StrategicMove m = pairStrategies_.get(pairValue).get(normalizedDealerUpcard) ;
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

    public static BlackjackStrategy createStrategyFromFile(String filename) throws Exception
    {
        BlackjackStrategy s = null ;

        // first try to find if we've already got this strategy object built
        if(strategies_.containsKey(filename))
        {
            s = strategies_.get(filename) ;
        }
        else
        {
            File sFile = new File(filename) ;
            BufferedReader br = new BufferedReader(new FileReader(sFile)) ;

            String fileContents = new String() ;
            String fileLine ;
            while((fileLine = br.readLine()) != null)
            {
                fileLine = fileLine.replaceAll("\\s", "");
                if(fileLine.indexOf('#') >= 0)
                {
                    fileLine = fileLine.substring(0,fileLine.indexOf('#')) ;
                }
                fileContents += fileLine ;
            }

            String[] pieces = fileContents.split("}") ;
            String pairs = null, soft = null, hard = null, sur  = null;
            for(int i = 0 ; i < pieces.length ; ++i )
            {
                if(pieces[i].contains(PAIRS_STRING))
                {
                    pairs = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                }
                else if(pieces[i].contains(SOFT_TOTAL_STRING))
                {
                    soft = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                }
                else if(pieces[i].contains(HARD_TOTAL_STRING))
                {
                    hard = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                }
                else if(pieces[i].contains(SURRENDER_STRING))
                {
                    sur = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                }
            }
            Map<Integer,Map<Card,StrategicMove>>     pairsStrategies = null ;
            Map<Integer,Map<Card,StrategicMove>> softTotalStrategies = null ;
            Map<Integer,Map<Card,StrategicMove>> hardTotalStrategies = null ;
            Map<Integer,Map<Card,StrategicMove>> surrenderStrategies = null ;
            if( pairs != null )
            {
                pairsStrategies = processMoveConfiguration(pairs) ;
            }
            else
            {
                throw new Exception("Invalid config: missing pairs configuation") ;
            }
            if( soft != null )
            {
                softTotalStrategies = processMoveConfiguration(soft) ;
            }
            else
            {
                throw new Exception("Invalid config: missing soft totals configuration") ;
            }
            if( hard != null )
            {
                hardTotalStrategies = processMoveConfiguration(hard) ;
            }
            else
            {
                throw new Exception("Invalid config: missing hard totals configuration") ;
            }
            if( sur != null )
            {
                surrenderStrategies = processMoveConfiguration(sur) ;
            }
            else
            {
                throw new Exception("Invalid config: missing surrender configuration") ;
            }
            s = new BlackjackStrategy(pairsStrategies,
                                      softTotalStrategies,
                                      hardTotalStrategies,
                                      surrenderStrategies,
                                      false,
                                      false) ;
            strategies_.put(filename,s) ;
        }
        return s ;
    }

    /*
     * Helper function
     */
    private static Map<Integer, Map<Card, StrategicMove>> processMoveConfiguration(String s) throws Exception
    {
        Map<Integer, Map<Card, StrategicMove>> ret = new HashMap<Integer, Map<Card, StrategicMove>>() ;

        String[] lines = s.split(";") ;
        for( int i = 0 ; i < lines.length ; ++i )
        {
            String[] components = lines[i].split(":") ;

            if(components.length != 2)
            {
                throw new Exception("Invalid config line: " + lines[i] + " expecting PlayerCard:Option,Option,...") ;
            }

            Integer playerHandValue = Integer.parseInt(components[0]);
            String [] dealerUpcardOptions = components[1].split(",") ;
            if(dealerUpcardOptions.length != 10)
            {
                throw new Exception("Invalid config line: " + lines[i] + " expecting 10 dealer options") ;
            }

            for( int j = 0 ; j < dealerUpcardOptions.length ; ++j )
            {

                // Insert it into the array, deaer upcard
                if(ret.containsKey(playerHandValue))
                {
                    ret.get(playerHandValue).put(getCardForConfigValue(j+2),getStrategicMoveForConfigValue(dealerUpcardOptions[j].charAt(0))) ;
                }
                else
                {
                    Map<Card, StrategicMove> m = new HashMap<Card,StrategicMove>() ;
                    m.put(getCardForConfigValue(j+2),getStrategicMoveForConfigValue(dealerUpcardOptions[j].charAt(0))) ;
                    ret.put(playerHandValue,m) ;
                }
            }
        }
        return ret ;
    }

    private static Card getCardForConfigValue(Integer i)
    {
        Card c = null ;

        switch(i.intValue())
        {
            case 2:
                c = Card.valueOf(Card.Rank.Deuce,Card.Suit.Spades) ;
                break ;
            case 3:
                c = Card.valueOf(Card.Rank.Three,Card.Suit.Spades) ;
                break ;
            case 4:
                c = Card.valueOf(Card.Rank.Four,Card.Suit.Spades) ;
                break ;
            case 5:
                c = Card.valueOf(Card.Rank.Five,Card.Suit.Spades) ;
                break ;
            case 6:
                c = Card.valueOf(Card.Rank.Six,Card.Suit.Spades) ;
                break ;
            case 7:
                c = Card.valueOf(Card.Rank.Seven,Card.Suit.Spades) ;
                break ;
            case 8:
                c = Card.valueOf(Card.Rank.Eight,Card.Suit.Spades) ;
                break ;
            case 9:
                c = Card.valueOf(Card.Rank.Nine,Card.Suit.Spades) ;
                break ;
            case 10:
                c = Card.valueOf(Card.Rank.Ten,Card.Suit.Spades) ;
                break ;
            case 11:
                c = Card.valueOf(Card.Rank.Ace,Card.Suit.Spades) ;
                break ;
        }
        return c ;
    }

    private static StrategicMove getStrategicMoveForConfigValue(char c)
    {
        switch(c)
        {
            case 'Y':
                return StrategicMove.Split ;
            case 'N':
                return StrategicMove.DontSplit ;
            case 'A':
                return StrategicMove.SplitIfDouble ;
            case 'S':
                return StrategicMove.Stand ;
            case 'H':
                return StrategicMove.Hit ;
            case 'D':
                return StrategicMove.DoubleElseHit ;
            case 'X':
                return StrategicMove.DoubleElseStand ;
            case 'C':
                return StrategicMove.Surrender ;
            case 'F':
                return StrategicMove.NoSurrender ;
        }
        // useless
        System.out.println("MISSED A STRATEGIC CASE") ;
        return StrategicMove.Stand ;
    }
}

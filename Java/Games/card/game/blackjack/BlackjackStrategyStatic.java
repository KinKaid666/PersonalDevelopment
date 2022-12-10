package card.game.blackjack ;

import java.lang.Exception ;
import java.io.* ;
import java.util.HashMap ;
import java.util.List ;
import card.game.blackjack.* ;
import card.Card ;
import java.util.logging.Logger ;

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
public class BlackjackStrategyStatic extends BlackjackStrategy
{
    private static Logger logger = Logger.getLogger(BlackjackStrategy.class.getName()) ;

    protected static HashMap<String, BlackjackStrategy> strategies_ = new HashMap<String,BlackjackStrategy>() ;

    private final static String      PAIRS_STRING = "Pairs"    ;
    private final static String SOFT_TOTAL_STRING = "SoftTotal" ;
    private final static String HARD_TOTAL_STRING = "HardTotal" ;
    private final static String  SURRENDER_STRING = "Surrender" ;

    /*
     * The strategy is found in file
     *   Formmat
     *      1: # Comment
     *
     */
    private BlackjackStrategyStatic(
                    HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>>      pairStrategies,
                    HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> softTotalStrategies,
                    HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> hardTotalStrategies,
                    HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> surrenderStrategies,
                    boolean                                takeInsurance,
                    boolean                                takeEvenMoney) {
        super(pairStrategies,softTotalStrategies,hardTotalStrategies,surrenderStrategies,takeInsurance,takeEvenMoney) ;
    }

    public static BlackjackStrategy createStrategyFromFile(String filename) throws Exception {
        BlackjackStrategy s = null ;

        // first try to find if we've already got this strategy object built
        if(strategies_.containsKey(filename)) {
            s = strategies_.get(filename) ;
        } else {
            File sFile = new File(filename) ;
            BufferedReader br = new BufferedReader(new FileReader(sFile)) ;

            String fileContents = new String() ;
            String fileLine ;
            while((fileLine = br.readLine()) != null) {
                fileLine = fileLine.replaceAll("\\s", "");
                if(fileLine.indexOf('#') >= 0) {
                    fileLine = fileLine.substring(0,fileLine.indexOf('#')) ;
                }
                fileContents += fileLine ;
            }

            String[] pieces = fileContents.split("}") ;
            String pairs = null, soft = null, hard = null, sur  = null;
            for(int i = 0 ; i < pieces.length ; ++i ) {
                if(pieces[i].contains(PAIRS_STRING)) {
                    pairs = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                } else if(pieces[i].contains(SOFT_TOTAL_STRING)) {
                    soft = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                } else if(pieces[i].contains(HARD_TOTAL_STRING)) {
                    hard = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                } else if(pieces[i].contains(SURRENDER_STRING)) {
                    sur = pieces[i].substring(pieces[i].indexOf("{")+1) ;
                }
            }
            HashMap<Integer,HashMap<Card,BlackjackStrategy.StrategicMove>>     pairsStrategies = null ;
            HashMap<Integer,HashMap<Card,BlackjackStrategy.StrategicMove>> softTotalStrategies = null ;
            HashMap<Integer,HashMap<Card,BlackjackStrategy.StrategicMove>> hardTotalStrategies = null ;
            HashMap<Integer,HashMap<Card,BlackjackStrategy.StrategicMove>> surrenderStrategies = null ;
            if( pairs != null ) {
                pairsStrategies = processMoveConfiguration(pairs) ;
            } else {
                throw new Exception("Invalid config: missing pairs configuation") ;
            }
            if( soft != null ) {
                softTotalStrategies = processMoveConfiguration(soft) ;
            } else {
                throw new Exception("Invalid config: missing soft totals configuration") ;
            }
            if( hard != null ) {
                hardTotalStrategies = processMoveConfiguration(hard) ;
            } else {
                throw new Exception("Invalid config: missing hard totals configuration") ;
            }
            if( sur != null ) {
                surrenderStrategies = processMoveConfiguration(sur) ;
            } else {
                throw new Exception("Invalid config: missing surrender configuration") ;
            }
            s = new BlackjackStrategyStatic(pairsStrategies,
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
    private static HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> processMoveConfiguration(String s) throws Exception {
        HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>> ret = new HashMap<Integer, HashMap<Card, BlackjackStrategy.StrategicMove>>() ;

        String[] lines = s.split(";") ;
        for( int i = 0 ; i < lines.length ; ++i ) {
            String[] components = lines[i].split(":") ;

            if(components.length != 2) {
                throw new Exception("Invalid config line: " + lines[i] + " expecting PlayerCard:Option,Option,...;") ;
            }

            Integer playerHandValue = Integer.parseInt(components[0]);
            String [] dealerUpcardOptions = components[1].split(",") ;
            if(dealerUpcardOptions.length != 10) {
                throw new Exception("Invalid config line: " + lines[i] + " expecting 10 dealer options") ;
            }

            for( int j = 0 ; j < dealerUpcardOptions.length ; ++j ) {

                // Insert it into the array, deaer upcard
                if(ret.containsKey(playerHandValue)) {
                    ret.get(playerHandValue).put(getCardForConfigValue(j+2),getStrategicMoveForConfigValue(dealerUpcardOptions[j].charAt(0))) ;
                } else {
                    HashMap<Card, BlackjackStrategy.StrategicMove> m = new HashMap<Card,BlackjackStrategy.StrategicMove>() ;
                    m.put(getCardForConfigValue(j+2),getStrategicMoveForConfigValue(dealerUpcardOptions[j].charAt(0))) ;
                    ret.put(playerHandValue,m) ;
                }
            }
        }
        return ret ;
    }

    public static Card getCardForConfigValue(Integer i) throws Exception {
        Card c = null ;

        switch(i.intValue()) {
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
            default:
                throw new Exception("Unkown card") ;
        }
        return c ;
    }

    public static char getConfigValueForStrategicMove(BlackjackStrategy.StrategicMove m) throws Exception {
        switch(m) {
            case Split:
                return 'Y' ;
            case DontSplit:
                return 'N' ;
            case SplitIfDouble:
                return 'A' ;
            case Stand:;
                return 'S' ;
            case Hit:;
                return 'H' ;
            case DoubleElseHit:
                return 'D' ;
            case DoubleElseStand:
                return 'X' ;
            case Surrender:
                return 'C' ;
            case NoSurrender:
                return 'F' ;
        }
        // useless
        throw new Exception("Unknown") ;
    }
    public static BlackjackStrategy.StrategicMove getStrategicMoveForConfigValue(char c) throws Exception {
        switch(c) {
            case 'Y':
                return BlackjackStrategy.StrategicMove.Split ;
            case 'N':
                return BlackjackStrategy.StrategicMove.DontSplit ;
            case 'A':
                return BlackjackStrategy.StrategicMove.SplitIfDouble ;
            case 'S':
                return BlackjackStrategy.StrategicMove.Stand ;
            case 'H':
                return BlackjackStrategy.StrategicMove.Hit ;
            case 'D':
                return BlackjackStrategy.StrategicMove.DoubleElseHit ;
            case 'X':
                return BlackjackStrategy.StrategicMove.DoubleElseStand ;
            case 'C':
                return BlackjackStrategy.StrategicMove.Surrender ;
            case 'F':
                return BlackjackStrategy.StrategicMove.NoSurrender ;
        }
        // useless
        throw new Exception("Unknown") ;
    }
}

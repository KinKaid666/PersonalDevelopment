package card.game.blackjack ;

import card.Card ;
import card.game.blackjack.* ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.Map ;
import java.util.HashMap ;
import java.util.EnumMap ;
import java.util.logging.Logger ;
import java.io.PrintStream ;

/*
 * Class used to help track statistics
 *
 * One Hand = a starting hand. We aggregate all splits into one result for purposes of statistics
 */
public class BlackjackStatistics
{
    private static Logger logger = Logger.getLogger(BlackjackStatistics.class.getName()) ;

    /* struct to record statitics */
    private class BlackjackStatisticsInfo
    {
        public int    occurrences_    = 0 ;
        public int    wins_           = 0 ;
        public int    losses_         = 0 ;
        public int    pushes_         = 0 ;
        public double weightedwins_   = 0 ;
        public double weightedlosses_ = 0 ;
        public double weightedpushes_ = 0 ;
    }
    private Map<BlackjackHand.BlackjackHandValue, Map<Card.Rank, BlackjackStatisticsInfo>> stats_ = new EnumMap<BlackjackHand.BlackjackHandValue, Map<Card.Rank, BlackjackStatisticsInfo>>(BlackjackHand.BlackjackHandValue.class) ;
    public BlackjackStatistics()
    {
        for(BlackjackHand.BlackjackHandValue v : BlackjackHand.BlackjackHandValue.values())
        {
            // Skip unnecessary starting hands
            if(v == BlackjackHand.BlackjackHandValue.Busted    ||
               v == BlackjackHand.BlackjackHandValue.TwentyOne ||
               v == BlackjackHand.BlackjackHandValue.Twenty    ||
               v == BlackjackHand.BlackjackHandValue.SoftTwelve)
            {
                continue ;
            }

            stats_.put(v, new HashMap<Card.Rank,BlackjackStatisticsInfo>()) ;
            for( Card.Rank r : Card.Rank.values() )
            {
                // for the purposes of statistics we'll treat
                if(r == Card.Rank.Jack  ||
                   r == Card.Rank.Queen ||
                   r == Card.Rank.King )
                {
                    continue ;
                }

                BlackjackStatisticsInfo i = new BlackjackStatisticsInfo() ;
                stats_.get(v).put(r, i) ;
            }
        }
    }

    private void recordHand(BlackjackHand.BlackjackHandValue v, Card.Rank r, BlackjackHand.Outcome o, double weight) throws Exception
    {
        if(stats_.containsKey(v) && stats_.containsKey(r))
        {
            throw new Exception("Recording unknown hand") ;
        }

        Card.Rank normalizedRank = r ;
        if(r == Card.Rank.Jack  ||
           r == Card.Rank.Queen ||
           r == Card.Rank.King )
        {
            r = Card.Rank.Ten ;
        }

        stats_.get(v).get(r).occurrences_++ ;
        switch(o)
        {
            case Win:
                stats_.get(v).get(r).wins_++ ;
                stats_.get(v).get(r).weightedwins_ += 1 * weight ;
                break ;
            case Loss:
                stats_.get(v).get(r).losses_++ ;
                stats_.get(v).get(r).weightedlosses_ += 1 * weight ;
                break ;
            case Push:
                stats_.get(v).get(r).pushes_++ ;
                stats_.get(v).get(r).weightedpushes_ += 1 * weight ;
                break ;
        }
    }

    /*
     * Record a starting hand and the outcome
     */
    private void recordHand(BlackjackHand h, Card c, BlackjackHand.Outcome o, double weight) throws Exception
    {
        if( h.cardCount() < 2 )
        {
            throw new Exception("Unable to record hand with less than two cards") ;
        }
        BlackjackHand startingHand = new BlackjackHand(h.getCard(0),h.getCard(1)) ;
        //System.out.println("recording a " + o + " for " + startingHand + " against a " + c.getRank() + " with weight " + weight) ;
        recordHand(startingHand.getStrategicHandValue(), c.getRank(), o, weight) ;
    }

    /*
     * Record a finished hand (including splits) and the outcome
     */
    public void recordHandList(List<BlackjackHand> handList, Card c, List<BlackjackHand.Outcome> outcomes, double[] weights) throws Exception
    {
        if(handList.size() != outcomes.size() ||
           outcomes.size() != weights.length)
        {
            throw new Exception("Statistics exception: incomplete lists") ;
        }

        if(handList.size() == 1)
        {
            recordHand(handList.get(0),c,outcomes.get(0),weights[0]) ;
        }
        else
        {

            BlackjackHand startingHand = new BlackjackHand(handList.get(0).getCard(0),
                                                           handList.get(1).getCard(0)) ;

            BlackjackHand.Outcome netOutcome = BlackjackHand.Outcome.Push ;
            double netWeight = 0 ;
            for(int i = 0 ; i < handList.size() ; ++i)
            {
                switch(outcomes.get(i))
                {
                    case Win:
                        netWeight += 1 * weights[i] ;
                        break ;
                    case Loss:
                        netWeight -= 1 * weights[i] ;
                        break ;
                    case Push:
                        // nothing
                        break ;
                }
            }
            if(netWeight > 0)
            {
                netOutcome = BlackjackHand.Outcome.Win ;
            }
            else if(netWeight < 0)
            {
                netOutcome = BlackjackHand.Outcome.Loss ;
                netWeight *= -1 ;
            }
            recordHand(startingHand,c,netOutcome,netWeight) ;
        }
    }

    /*
     * Print out how well the strategy worked
     */
    // TODO: Refactor these printing functions
    public void printStatistics(PrintStream o)
    {
        BlackjackStatisticsInfo totals = new BlackjackStatisticsInfo() ;
        for(BlackjackHand.BlackjackHandValue k1 : stats_.keySet())
        {

            BlackjackStatisticsInfo handTotal = new BlackjackStatisticsInfo() ;
            for( Card.Rank k2 : stats_.get(k1).keySet() )
            {
                BlackjackStatisticsInfo i = stats_.get(k1).get(k2) ;
                o.println("    Starting hand " + k1 + " against dealer upcard " + k2) ;
                o.printf("        Frequency       = %,10d\n", i.occurrences_) ;
                o.printf("        Wins            = %,10d   / %6.2f%%\n",  i.wins_          , (i.occurrences_ > 0 ? i.wins_          *100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Losses          = %,10d   / %6.2f%%\n",  i.losses_        , (i.occurrences_ > 0 ? i.losses_        *100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Pushes          = %,10d   / %6.2f%%\n",  i.pushes_        , (i.occurrences_ > 0 ? i.pushes_        *100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Weighted Wins   =   %,10.1f / %6.2f%%\n", i.weightedwins_  , (i.occurrences_ > 0 ? i.weightedwins_  *100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Weighted Losses =   %,10.1f / %6.2f%%\n", i.weightedlosses_, (i.occurrences_ > 0 ? i.weightedlosses_*100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Weighted Pushes =   %,10.1f / %6.2f%%\n", i.weightedpushes_, (i.occurrences_ > 0 ? i.weightedpushes_*100.0 / i.occurrences_ : 0) ) ;
                o.printf("        Weighted Net    =   %,10.1f / %6.2f%%\n", (i.weightedwins_ - i.weightedlosses_), (i.occurrences_ > 0 ? (i.weightedwins_ - i.weightedlosses_)*100.0 / i.occurrences_ : 0) ) ;

                // Sum it up for the row total
                handTotal.occurrences_    += i.occurrences_    ;
                handTotal.wins_           += i.wins_           ;
                handTotal.losses_         += i.losses_         ;
                handTotal.pushes_         += i.pushes_         ;
                handTotal.weightedwins_   += i.weightedwins_   ;
                handTotal.weightedlosses_ += i.weightedlosses_ ;
                handTotal.weightedpushes_ += i.weightedpushes_ ;
                // Sum it up for the total
                totals.occurrences_       += i.occurrences_    ;
                totals.wins_              += i.wins_           ;
                totals.losses_            += i.losses_         ;
                totals.pushes_            += i.pushes_         ;
                totals.weightedwins_      += i.weightedwins_   ;
                totals.weightedlosses_    += i.weightedlosses_ ;
                totals.weightedpushes_    += i.weightedpushes_ ;
            }
            o.println("    TOTAL Starting hand " + k1) ;
            o.printf("        Frequency       = %,10d\n", handTotal.occurrences_) ;
            o.printf("        Wins            = %,10d   / %6.2f%%\n",  handTotal.wins_          , (handTotal.occurrences_ > 0 ? handTotal.wins_          *100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Losses          = %,10d   / %6.2f%%\n",  handTotal.losses_        , (handTotal.occurrences_ > 0 ? handTotal.losses_        *100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Pushes          = %,10d   / %6.2f%%\n",  handTotal.pushes_        , (handTotal.occurrences_ > 0 ? handTotal.pushes_        *100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Weighted Wins   =   %,10.1f / %6.2f%%\n", handTotal.weightedwins_  , (handTotal.occurrences_ > 0 ? handTotal.weightedwins_  *100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Weighted Losses =   %,10.1f / %6.2f%%\n", handTotal.weightedlosses_, (handTotal.occurrences_ > 0 ? handTotal.weightedlosses_*100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Weighted Pushes =   %,10.1f / %6.2f%%\n", handTotal.weightedpushes_, (handTotal.occurrences_ > 0 ? handTotal.weightedpushes_*100.0 / handTotal.occurrences_ : 0) ) ;
            o.printf("        Weighted Net    =   %,10.1f / %6.2f%%\n", (handTotal.weightedwins_ - handTotal.weightedlosses_), (handTotal.occurrences_ > 0 ? (handTotal.weightedwins_ - handTotal.weightedlosses_)*100.0 / handTotal.occurrences_ : 0) ) ;


        }
        o.println("TOTALS") ;
        o.printf("    Frequency       = %,10d\n", totals.occurrences_   ) ;
        o.printf("    Wins            = %,10d   / %6.2f%%\n",  totals.wins_          , (totals.occurrences_ > 0 ? totals.wins_          *100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Losses          = %,10d   / %6.2f%%\n",  totals.losses_        , (totals.occurrences_ > 0 ? totals.losses_        *100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Pushes          = %,10d   / %6.2f%%\n",  totals.pushes_        , (totals.occurrences_ > 0 ? totals.pushes_        *100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Weighted Wins   =   %,10.1f / %6.2f%%\n", totals.weightedwins_  , (totals.occurrences_ > 0 ? totals.weightedwins_  *100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Weighted Losses =   %,10.1f / %6.2f%%\n", totals.weightedlosses_, (totals.occurrences_ > 0 ? totals.weightedlosses_*100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Weighted Pushes =   %,10.1f / %6.2f%%\n", totals.weightedpushes_, (totals.occurrences_ > 0 ? totals.weightedpushes_*100.0 / totals.occurrences_ : 0) ) ;
        o.printf("    Weighted Net    =   %,10.1f / %6.2f%%\n", (totals.weightedwins_ - totals.weightedlosses_), (totals.occurrences_ > 0 ? (totals.weightedwins_ - totals.weightedlosses_)*100.0 / totals.occurrences_ : 0) ) ;
    }

    /*
     * Print out how well the strategy worked
     */
    public void printStatisticsCSV(PrintStream o)
    {
        List<BlackjackStatisticsInfo> dealerHandsTotal = new LinkedList<BlackjackStatisticsInfo>() ;
        BlackjackStatisticsInfo totals = new BlackjackStatisticsInfo() ;
        boolean init = false ;
        o.println("Starting Hand,2,3,4,5,6,7,8,9,10,A,Total" ) ;
        for(BlackjackHand.BlackjackHandValue v : BlackjackHand.BlackjackHandValue.values())
        {
            // Skip unnecessary starting hands
            if(v == BlackjackHand.BlackjackHandValue.Busted    ||
               v == BlackjackHand.BlackjackHandValue.TwentyOne ||
               v == BlackjackHand.BlackjackHandValue.Twenty    ||
               v == BlackjackHand.BlackjackHandValue.SoftTwelve)
            {
                continue ;
            }

            o.print(v) ;
            BlackjackStatisticsInfo handTotal = new BlackjackStatisticsInfo() ;
            int j = 0 ;
            for( Card.Rank r : Card.Rank.values() )
            {

                // for the purposes of statistics we'll treat
                if(r == Card.Rank.Jack  ||
                   r == Card.Rank.Queen ||
                   r == Card.Rank.King )
                {
                    continue ;
                }

                // First time through create the dealer starting hands totals
                if( false == init )
                {
                    BlackjackStatisticsInfo x = new BlackjackStatisticsInfo() ;
                    dealerHandsTotal.add(x) ;
                }

                BlackjackStatisticsInfo info = stats_.get(v).get(r) ;
                o.printf(",%.2f%%", (info.occurrences_ > 0 ? (info.weightedwins_ - info.weightedlosses_)*100.0 / info.occurrences_ : 0) ) ;

                // Sum it up for the row total
                handTotal.occurrences_    += info.occurrences_    ;
                handTotal.wins_           += info.wins_           ;
                handTotal.losses_         += info.losses_         ;
                handTotal.pushes_         += info.pushes_         ;
                handTotal.weightedwins_   += info.weightedwins_   ;
                handTotal.weightedlosses_ += info.weightedlosses_ ;
                handTotal.weightedpushes_ += info.weightedpushes_ ;
                // Sum it up for column total
                dealerHandsTotal.get(j).occurrences_    += info.occurrences_    ;
                dealerHandsTotal.get(j).wins_           += info.wins_           ;
                dealerHandsTotal.get(j).losses_         += info.losses_         ;
                dealerHandsTotal.get(j).pushes_         += info.pushes_         ;
                dealerHandsTotal.get(j).weightedwins_   += info.weightedwins_   ;
                dealerHandsTotal.get(j).weightedlosses_ += info.weightedlosses_ ;
                dealerHandsTotal.get(j).weightedpushes_ += info.weightedpushes_ ;
                // Sum it up for the total
                totals.occurrences_       += info.occurrences_    ;
                totals.wins_              += info.wins_           ;
                totals.losses_            += info.losses_         ;
                totals.pushes_            += info.pushes_         ;
                totals.weightedwins_      += info.weightedwins_   ;
                totals.weightedlosses_    += info.weightedlosses_ ;
                totals.weightedpushes_    += info.weightedpushes_ ;
                ++j ;
            }
            init = true ;
            o.printf(",%.2f%%", (handTotal.occurrences_ > 0 ? (handTotal.weightedwins_ - handTotal.weightedlosses_)*100.0 / handTotal.occurrences_ : 0) ) ;
            o.println() ;
        }
        o.print("ALL") ;
        for(int i = 0 ; i < dealerHandsTotal.size() ; ++i)
        {
            o.printf(",%.2f%%", (dealerHandsTotal.get(i).occurrences_ > 0 ? (dealerHandsTotal.get(i).weightedwins_ - dealerHandsTotal.get(i).weightedlosses_)*100.0 / dealerHandsTotal.get(i).occurrences_ : 0) ) ;
        }
        o.printf(",%.2f%%", (totals.occurrences_ > 0 ? (totals.weightedwins_ - totals.weightedlosses_)*100.0 / totals.occurrences_ : 0) ) ;
        o.println() ;
    }

    // return 0 - infinity
    public double getFitness()
    {
        BlackjackStatisticsInfo totals = new BlackjackStatisticsInfo() ;
        for(BlackjackHand.BlackjackHandValue v : BlackjackHand.BlackjackHandValue.values())
        {
            // Skip unnecessary starting hands
            if(v == BlackjackHand.BlackjackHandValue.Busted    ||
               v == BlackjackHand.BlackjackHandValue.TwentyOne ||
               v == BlackjackHand.BlackjackHandValue.Twenty    ||
               v == BlackjackHand.BlackjackHandValue.SoftTwelve)
            {
                continue ;
            }

            for( Card.Rank r : Card.Rank.values() )
            {

                // for the purposes of statistics we'll treat
                if(r == Card.Rank.Jack  ||
                   r == Card.Rank.Queen ||
                   r == Card.Rank.King )
                {
                    continue ;
                }

                BlackjackStatisticsInfo info = stats_.get(v).get(r) ;
                totals.occurrences_       += info.occurrences_    ;
                totals.wins_              += info.wins_           ;
                totals.losses_            += info.losses_         ;
                totals.pushes_            += info.pushes_         ;
                totals.weightedwins_      += info.weightedwins_   ;
                totals.weightedlosses_    += info.weightedlosses_ ;
                totals.weightedpushes_    += info.weightedpushes_ ;
            }
        }
        return (totals.occurrences_ > 0 ? (totals.weightedwins_ - totals.weightedlosses_)*1.0 / totals.occurrences_ : 0) + 1 ;
    }
}

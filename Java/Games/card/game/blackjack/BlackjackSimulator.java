package card.game.blackjack ;

import java.util.List ;
import java.util.LinkedList ;
import java.util.logging.Logger ;

import card.game.blackjack.* ;
import card.Card ;

public class BlackjackSimulator {
    private static Logger logger = Logger.getLogger(BlackjackSimulator.class.getName()) ;

    private BlackjackStrategy strategy_ ;
    private BlackjackPlayer   player_ ;
    private Blackjack         game_ ;
    private int               numHands_ ;

    public BlackjackSimulator(BlackjackStrategy s, int numHands) {
        strategy_ = s ;
        numHands_ = numHands ;
        player_ = new BlackjackPlayer("",strategy_) ;

        BlackjackRules rules = new BlackjackRules( 8,     // Decks
                                                   true,  // split Aces
                                                   true,  // dealer hit soft 17
                                                   false, // allow early surrender
                                                   true,  // allow late surrender
                                                   true,  // replit pairs
                                                   false, // resplit aces
                                                   true,  // double after splitj
                                                   false, // double on only 10 or 11
                                                   false  // insurance
                                                ) ;

        List<BlackjackPlayer> players = new LinkedList<BlackjackPlayer>() ;
        players.add(player_) ;
        game_ = new Blackjack(rules, players) ;
    }

    public void simulate() {
        while( numHands_ > 0 ) {
            try {
                game_.play() ;
                if( game_.reshuffleNeeded() ) {
                    game_.shuffle() ;
                }
            } catch (Exception e) {
                System.err.println(e.getMessage()) ;
                System.exit(1) ;
            }

            --numHands_ ;
        }
    }

    //
    // Return how well the overall simulation did from 0 to infinity
    public double getFitness() {
        return strategy_.getFitness() ;
    }
}

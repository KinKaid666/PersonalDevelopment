package card.game.blackjack ;

import card.Card ;
import card.game.blackjack.* ;
import java.util.logging.Logger ;

public class BlackjackPlayer {
    private static Logger logger = Logger.getLogger(BlackjackPlayer.class.getName()) ;

    private String            name_ ;
    private BlackjackStrategy strategy_ ;

    public BlackjackPlayer(String            name,
                           BlackjackStrategy strategy) {
        name_ = name ;
        strategy_ = strategy ;
    }

    public Blackjack.Move getHandDecision(BlackjackRules rules, BlackjackHand hand, Card dealerUpcard) throws Exception {
        return strategy_.getHandDecision(rules, hand, dealerUpcard) ;
    }

    public String getName() {
        return name_ ;
    }

    /*
     * Get strategy to clone, for splits hands
     */
    public BlackjackStrategy getStrategy() {
        return strategy_ ;
    }
}

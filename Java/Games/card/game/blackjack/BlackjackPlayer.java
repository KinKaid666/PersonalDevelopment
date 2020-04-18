package card.game.blackjack ;

import card.Card ;
import card.game.blackjack.* ;
public class BlackjackPlayer
{
    private String            name_ ;
    private BlackjackStrategy strategy_ ;

    public BlackjackPlayer(String            name,
                           BlackjackStrategy strategy)
    {
        name_ = name ;
        strategy_ = strategy ;
    }

    public Blackjack.Move getHandDecision(BlackjackRules rules, BlackjackHand hand, Card dealerUpcard) throws Exception
    {
        return strategy_.getHandDecision(rules, hand, dealerUpcard) ;
    }

    public String getName()
    {
        return name_ ;
    }

    /*
     * Get strategy to clone, for splits hands
     */
    public BlackjackStrategy getStrategy()
    {
        return strategy_ ;
    }

    public void printPlayer()
    {
        System.out.println("Announcing Player with name '" + name_ + "'") ;
        strategy_.printStrategy() ;
    }
}

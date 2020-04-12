package card.game.blackjack ;

public class BlackjackPlayer
{
    private String            name_ ;
    private BlackjackRules    rules_ = null ;
    private BlackjackStrategy strategy_ ;
    public BlackjackPlayer(String name, BlackjackStrategy strategy)
    {
        name_ = name ;
        strategy_ = strategy ;
    }

    public Blackjack.Move decision(BlackjackHand hand)
    {
        return Blackjack.Move.Stand ;
    }

    public void setTableRules(BlackjackRules rules)
    {
        rules_ = rules ;
    }

}


import card.game.blackjack.* ;
import card.Card ;

public class BJStrategyTest
{
    public static void main(String args[])
    {
        if(args.length != 1)
        {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <filename of strategy>") ;
            System.exit(1) ;
        }
        try
        {
            BlackjackRules rules = new BlackjackRules( 6,     // Decks
                                                       true,  // split Aces
                                                       true,  // dealer hit soft 17
                                                       false, // allow surrender
                                                       false, // allow late surrender
                                                       true,  // replit pairs
                                                       false, // resplit aces
                                                       true,  // double after splitj
                                                       false, // double on only 10 or 11
                                                       true   // insurance
                                                    ) ;

            BlackjackStrategy s = BlackjackStrategy.createStrategyFromFile( args[0] ) ;
            s.printStrategy() ;

            Card ace = Card.valueOf(Card.Rank.Ace,Card.Suit.Hearts) ;
            Card three = Card.valueOf(Card.Rank.Three,Card.Suit.Hearts) ;
            Card eight = Card.valueOf(Card.Rank.Eight,Card.Suit.Hearts) ;
            Card queen = Card.valueOf(Card.Rank.Queen,Card.Suit.Hearts) ;
            Card dealerCard = Card.valueOf(Card.Rank.Ten,Card.Suit.Hearts) ;
            BlackjackHand h = new BlackjackHand(ace,three) ;
            Blackjack.Move m = s.getHandDecision( rules, h, dealerCard ) ;
            System.out.println("getHandDecision( hand = " + h + ", dealer upcard = " + dealerCard + ") = " + m) ;
            h.add(queen) ;
            System.out.println("getHandDecision( hand = " + h + ", dealer upcard = " + dealerCard + ") = " + m) ;
        }
        catch(Exception e)
        {
            System.out.println("Caught exception: " + e.getMessage()) ;
        }
    }
}

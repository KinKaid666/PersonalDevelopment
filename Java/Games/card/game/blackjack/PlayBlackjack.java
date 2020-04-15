package card.game.blackjack ;

import java.util.List ;
import java.util.LinkedList ;

import card.Card ;

class PlayBlackjack
{
    public static void main(String[] args)
    {
        /*
        Card c1 = Card.valueOf(Card.Rank.Ten, Card.Suit.Spades),
             c2 = Card.valueOf(Card.Rank.Ten, Card.Suit.Spades),
             c3 = Card.valueOf(Card.Rank.Ten, Card.Suit.Spades),
             c4 = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades),
             c5 = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades),
             c6 = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades);
        BlackjackHand h = new BlackjackHand(c1,c2) ;
        h.add(c3) ;
        h.add(c4) ;
        h.add(c5) ;
        h.add(c6) ;
        System.out.println(h) ;
        */

        if(args.length != 2)
        {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <number of players> <number of shuffles>") ;
            System.exit(1) ;
        }
        int numplayers = Integer.parseInt(args[0]) ;
        int games      = Integer.parseInt(args[1]) ;

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
        List<BlackjackPlayer> players = new LinkedList<BlackjackPlayer>() ;
        for( int i = 0 ; i < numplayers ; ++i )
        {
            BlackjackPlayer p = new BlackjackPlayer("Player " + i, rules, new BlackjackStrategy()) ;
            players.add(p) ;
        }

        Blackjack game = new Blackjack(rules, players) ;
        //int progressBar = games / 10 ;
        //System.out.print("progress") ;
        while( games > 0 )
        {
            game.play() ;
            game.shuffle() ;

            // Progress Bar
            // Comment out until we're done w/ the strategy
            //if(progressBar > 0 && (games % progressBar) == 0 ) System.out.print(".") ;
            --games ;
        }
        System.out.println() ;
        game.printStats() ;
    }
}

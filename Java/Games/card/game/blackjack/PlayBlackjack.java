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

        BlackjackRules rules = new BlackjackRules() ;
        List<BlackjackPlayer> players = new LinkedList<BlackjackPlayer>() ;

        if(args.length != 2)
        {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <number of players> <number of shuffles>") ;
            System.exit(1) ;
        }
        int numplayers = Integer.parseInt(args[0]) ;
        int games      = Integer.parseInt(args[1]) ;

        for( int i = 0 ; i < numplayers ; ++i )
        {
            BlackjackPlayer p = new BlackjackPlayer("Player " + i, rules, new BlackjackStrategy()) ;
            players.add(p) ;
        }

        Blackjack game = new Blackjack(rules, players, 6) ;
        int progressBar = games / 10 ;
        while( --games > 0 )
        {
            game.play() ;
            game.shuffle() ;

            // Progress Bar
            if((games % progressBar) == 0 ) System.out.print(".") ;
        }
        System.out.println() ;
        game.printStats() ;
    }
}

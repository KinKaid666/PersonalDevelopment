package card.game.blackjack ;

import java.util.List ;
import java.util.LinkedList ;

import card.Card ;

class PlayBlackjack
{
    public static void main(String[] args)
    {

        /*
        Card c1 = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades),
             c2 = Card.valueOf(Card.Rank.Ace, Card.Suit.Spades) ;
        BlackjackHand h = new BlackjackHand(c1,c2) ;
        System.out.println(h.getHandValue()) ;
        */

        BlackjackRules rules = new BlackjackRules() ;
        List<BlackjackPlayer> players = new LinkedList<BlackjackPlayer>() ;

        if(args.length != 1)
        {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <number of players>") ;
            System.exit(1) ;
        }
        int numplayers = Integer.parseInt(args[0]) ;

        for( int i = 0 ; i < numplayers ; ++i )
        {
            BlackjackPlayer p = new BlackjackPlayer("Eric Ferguson " + i, rules, new BlackjackStrategy()) ;
            players.add(p) ;
        }
        Blackjack game = new Blackjack(rules, players, 6) ;
        game.play() ;
    }
}

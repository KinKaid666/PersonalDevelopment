import card.game.blackjack.* ;
import card.Card ;

import java.io.* ;
import java.util.StringTokenizer ;
import java.util.LinkedList ;
import java.util.List ;

public class SaveTest {
    public static void main(String args[]) {
        try {
            BlackjackStrategy s = BlackjackStrategyStatic.createStrategyFromFile("/Users/ericferguson/Development/Java/Games/card/game/blackjack/Strategies/perfect.stg") ;
            s.printStrategy(System.out) ;
        } catch(Exception e) {
            System.out.println("Load exception: " + e.getMessage()) ;
        }
    }
}

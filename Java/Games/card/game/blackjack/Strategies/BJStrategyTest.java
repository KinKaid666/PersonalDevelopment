
import card.game.blackjack.* ;
import card.Card ;

import java.io.* ;
import java.util.StringTokenizer ;
import java.util.LinkedList ;
import java.util.List ;

public class BJStrategyTest
{
    public static void main(String args[])
    {
        try
        {
            BlackjackRules rules = new BlackjackRules( 6,     // Decks
                                                       true,  // split Aces
                                                       true,  // dealer hit soft 17
                                                       true,  // allow surrender
                                                       true,  // allow late surrender
                                                       true,  // replit pairs
                                                       false, // resplit aces
                                                       true,  // double after splitj
                                                       false, // double on only 10 or 11
                                                       true   // insurance
                                                    ) ;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input ;

            BlackjackStrategy s = null ;
            while(true)
            {
                System.out.print("> ") ;
                input = br.readLine() ;

                // break input into multiple strings
                StringTokenizer st = new StringTokenizer(input, " \n") ;
                List<String> commandArgs = new LinkedList<String>() ;
                while(st.hasMoreTokens())
                {
                    commandArgs.add(st.nextToken()) ;
                }

                boolean needHelp = false ;
                if(commandArgs.size() > 0)
                {
                    switch(commandArgs.get(0))
                    {
                        case "quit":
                        case "exit":
                            System.exit(0) ;
                            break ;
                        case "?":
                        case "help":
                            needHelp = true ;
                            break ;
                        case "load":
                            if(commandArgs.size() == 2)
                            {
                                try
                                {
                                    s = BlackjackStrategy.createStrategyFromFile( commandArgs.get(1) ) ;
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Load exception: " + e.getMessage()) ;
                                }
                            }
                            else
                            {
                                System.err.println("load needs a file") ;
                                needHelp = true ;
                            }
                            break ;
                        case "print":
                            if( s == null )
                            {
                                System.err.println("cannot print until strategy file has been loaded") ;
                            }
                            else
                            {
                                s.printStrategy() ;
                            }
                            break ;
                        default:
                            if(commandArgs.size() == 2)
                            {
                                if( s == null )
                                {
                                    System.err.println("cannot print until strategy file has been loaded") ;
                                }
                                else
                                {
                                    try
                                    {
                                        BlackjackHand h = BlackjackHand.createHand(BlackjackHand.BlackjackHandValue.valueOf(commandArgs.get(0))) ;
                                        Card dealerCard = BlackjackStrategy.getCardForConfigValue(Integer.parseInt(commandArgs.get(1))) ;
                                        Blackjack.Move m = s.getHandDecision( rules, h, dealerCard ) ;
                                        System.out.println("getHandDecision( hand = " + h + ", dealer upcard = " + dealerCard + " ) = " + m) ;
                                    }
                                    catch(Exception e)
                                    {
                                        System.err.println("Bad hand or dealer upcard: " + commandArgs) ;
                                        System.err.println("Possible hands: " + java.util.Arrays.asList(BlackjackHand.BlackjackHandValue.values())) ;
                                        System.err.println("Possible deader upcards: [2,3,4,5,6,7,8,9,10,11]") ;
                                    }
                                }
                            }
                            else
                            {
                                System.err.println("Unknown input") ;
                                needHelp = true ;
                            }
                    }
                    if(needHelp)
                    {
                        System.out.println("Test strategy objects against various inputs") ;
                        System.out.println() ;
                        System.out.println("usage: <Hand> <Dealer upcard>") ;
                        System.out.println("       load <file>: loads strategy file") ;
                        System.out.println("       exit|quit  : exit") ;
                        System.out.println("       help|?     : print this") ;
                        needHelp = false ;
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Caught exception: " + e.getMessage()) ;
        }
    }
}


import dice.Dice ;
import dice.Die ;
import java.util.Map ;
import java.util.HashMap ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.time.Instant ;
import java.time.Duration ;

public class ShutTheBox
{
    //// TODO: Write sumset code
    //private static int[][][] allOptions_ = {
        //// 1
        //{ {1}, },
        //// 2
        //{ {2}, },
        //// 3
        //{ {3},{1,2}, },
        //// 4
        //{ {4},{1,3}, },
        //// 5
        //{ {5},{1,4},{2,3}, },
        //// 6
        //{ {6},{1,5},{2,4},{1,2,3}, },
        //// 7
        //{ {7},{1,6},{2,5},{3,4},{1,2,4}, },
        //// 8
        //{ {8},{1,7},{2,6},{3,5},{1,2,5},{1,3,4}, },
        //// 9
        //{ {9},{1,8},{2,7},{3,6},{4,5},{1,2,6},{1,3,5},{2,3,4}, },
        //// 10
        //{ {10},{1,9},{2,8},{3,7},{4,6},{1,2,7},{1,3,6},{1,4,5},{2,3,4},{1,2,3,4}, },
        //// 11
        //{ {1,10},{2,9},{3,8},{4,7},{5,6},{1,2,8},{1,3,7},{1,4,6},{2,3,6},{2,4,5},{1,2,3,5}, },
        //// 12
        //{ {2,10},{3,9},{4,8},{5,7},{1,2,9},{1,3,8},{1,4,7},{1,5,6},{2,3,7},{2,4,6},{3,4,5},{1,2,3,6},{1,2,4,5}, },
    //} ;
    private static ArrayList< ArrayList< ArrayList<Integer>>> allOptions_ = getSumSets() ;
    private static ArrayList< ArrayList< ArrayList<Integer>>> getSumSets()
    {
        ArrayList<ArrayList<ArrayList<Integer>>> allOptions = new ArrayList<ArrayList<ArrayList<Integer>>>() ;
        ArrayList<Integer> set = new ArrayList<Integer>() ;

        // create array from 1 to 10 for the PIPs
        for(int i = 1 ; i <= 10 ; i++)
        {
            set.add(i) ;
        }

        // create the possible sets
        for(int i = 1 ; i <= 12 ; i++)
        {
            allOptions.add(sumset(set, i)) ;
        }

        // now sort inner lists are sorted by number of elements
        for(int x = 0 ; x < allOptions.size() ; x++)
        {
            for(int i = 0 ; i < allOptions.get(x).size() ; i++)
            {
                for(int j = i + 1 ; j < allOptions.get(x).size() ; j++ )
                {
                    // swap
                    if(allOptions.get(x).get(i).size() > allOptions.get(x).get(j).size())
                    {
                        ArrayList<Integer> temp = allOptions.get(x).get(j) ;
                        allOptions.get(x).remove(j) ;
                        allOptions.get(x).add(j, allOptions.get(x).get(i)) ;
                        allOptions.get(x).remove(i) ;
                        allOptions.get(x).add(i, temp) ;

                    }
                }
            }
        }
        return allOptions ;
    }

    public static ArrayList< ArrayList<Integer>> sumset( ArrayList<Integer> set, Integer sum )
    {
        ArrayList< ArrayList<Integer>> returnSets = new ArrayList< ArrayList<Integer>>() ;
        ArrayList<Integer> tempSet = new ArrayList<Integer>() ;
        __sumset( set, sum, 0, 0, tempSet, returnSets ) ;
        return returnSets ;
    }

    private static void __sumset(ArrayList<Integer> set,
                                 Integer sum,
                                 Integer index,
                                 Integer sumSoFar,
                                 ArrayList<Integer> setSoFar,
                                 ArrayList< ArrayList<Integer>> setsSoFar )
    {
        for( ; index < set.size() ; ++index )
        {
            setSoFar.add(set.get(index)) ;
            sumSoFar += set.get(index) ;
            sum      -= set.get(index) ;

            /* Found one, add it */
            if( sum == 0 )
            {
                setsSoFar.add( new ArrayList<Integer>(setSoFar) ) ;
            }
            else
            {
                __sumset( set, sum, index + 1, sumSoFar, setSoFar, setsSoFar ) ;
            }

            /* Pretend like it never happened and keep going */
            sumSoFar -= set.get(index) ;
            sum      += set.get(index) ;
            setSoFar.remove(setSoFar.lastIndexOf(set.get(index))) ;
        }
    }

    private static int boxSize_ = 10 ;
    private boolean[] box_ = { true, true, true, true, true, true, true, true, true, true } ;
    private Dice dice_ = new Dice(2) ;
    private Integer oneDiceConfig_ ;

    //
    // Create a game of shut the box
    public ShutTheBox()
    {
        oneDiceConfig_ = 0 ;
        reset() ;
    }

    // Create a game of shut the box
    //   oneDiceConfig is when you witch to one die
    //         6 = when 7-10 are closed
    //         5 = when 6-10 are closed
    //         ...
    //         0 = when 1-10 are closed (i.e. never switch
    public ShutTheBox(Integer oneDiceConfig)
    {
        oneDiceConfig_ = oneDiceConfig ;
        reset() ;
    }

    public void reset()
    {
        for(int i = 1 ; i < boxSize_ ; i++ )
        {
            open(i) ;
        }
    }

    public boolean check(int i)
    {
        return box_[i-1] ;
    }

    public void close(int i)
    {
        box_[i-1] = false ;
    }

    public void open(int i)
    {
        box_[i-1] = true ;
    }

    public boolean onlyXOrLess(int x)
    {
        for(int i = boxSize_ ; i > x ; i--)
        {
            if(check(i))
            {
                return false ;
            }
        }
        return true ;
    }

    public int score()
    {
        int score = 0 ;
        for(int i = 1 ; i <= boxSize_ ; i++ )
        {
            if(check(i))
            {
                score += i ;
            }
        }
        return score ;
    }

    public boolean move(int roll)
    {
        ArrayList<ArrayList<Integer>> options = allOptions_.get(roll-1) ;
        for(int i = 0 ; i < options.size() ; i++)
        {
            if(possible(options.get(i)))
            {
                for(int j = 0 ; j < options.get(i).size() ; j++ )
                {
                    close(options.get(i).get(j)) ;
                }
                return true ;
            }
        }
        return false ;
    }

    public String getBoard()
    {
        StringBuilder sb = new StringBuilder() ;
        sb.append("box = ") ;
        for(int i = 1 ; i <= boxSize_ ; i++)
        {
            sb.append(check(i) ? i : " ") ;
            sb.append(" ") ;
        }
        return sb.toString() ;
    }

    public boolean possible(ArrayList<Integer> options)
    {
        for( int i : options )
        {
            if(!check(i))
            {
                return false ;
            }
        }
        return true ;
    }

    public int play()
    {
        int i = 0 ;
        while(true)
        {
            int roll ;
            if(onlyXOrLess(oneDiceConfig_))
            {
                roll = dice_.roll(1) ;
            }
            else
            {
                roll = dice_.roll() ;
            }

            //System.out.printf("Roll = %2d ", roll) ;
            if(!move(roll))
            {
                //System.out.println() ;
                break ;
            }
            //System.out.println(getBoard()) ;
            i++ ;
        }
        return i ;
    }

    public static void main(String[] args)
    {
/*
        {
            int i = 1 ;
            for(arraylist<arraylist<integer>> arrayofoptions : alloptions_ )
            {
                system.out.printf("sumset that total %d from set(1..10)\n", i++) ;

                int j = 1 ;
                for( arraylist<integer> options : arrayofoptions )
                {
                    system.out.printf("\t%d = {", j++) ;
                    for( integer option : options )
                    {
                        system.out.printf("%d,", option) ;
                    }
                    system.out.println("},") ;
                }
            }
        }
*/

        if(args.length != 2)
        {
            System.err.println("usage: " + System.getProperty("sun.java.command") + " <pip value to switch to one die> <number of games to simulate>") ;
            System.exit(1) ;
        }

        Map<Integer,Integer> scores = new HashMap<Integer,Integer>() ;
        Map<Integer,Integer> moves  = new HashMap<Integer,Integer>() ;

        int oneDiceConfig = Integer.parseInt(args[0]) ;
        int numGames      = Integer.parseInt(args[1]) ;
        int games = 0 ;
        int totalScore = 0 ;
        int totalMoves = 0 ;

        Instant start = Instant.now();
        while(games < numGames)
        {
            ShutTheBox game = new ShutTheBox(oneDiceConfig) ;
            Integer currentMoves = game.play() ;
            Integer currentScore = game.score() ;
            totalScore += currentScore ;
            totalMoves += currentMoves ;
            if(scores.containsKey(currentScore))
            {
                Integer currentCount = scores.get(currentScore) ;
                scores.replace(currentScore, ++currentCount) ;
            }
            else
            {
                scores.put(currentScore, 1) ;
            }

            if(moves.containsKey(currentMoves))
            {
                Integer currentCount = moves.get(currentMoves) ;
                moves.replace(currentMoves, ++currentCount) ;
            }
            else
            {
                moves.put(currentScore, 1) ;
            }
            game.reset() ;
            games++ ;
        }
        Instant end = Instant.now();

        int maxScore = 55 ;
        for( int i = 0 ; i <= maxScore ; i++ )
        {
            if(scores.containsKey(i))
            {
                System.out.printf("%" + String.valueOf(maxScore).length() + "d = %," + (String.valueOf(numGames).length()+2) + "d / %5.2f%%\n", i, scores.get(i), scores.get(i)*100.00 / numGames) ;
            }
            else
            {
                System.out.printf("%" + String.valueOf(maxScore).length() +"d = %," + (String.valueOf(numGames).length()+2) + "d / %5.2f%%\n", i, 0, 0.0) ;
            }
        }
        System.out.printf("Timer = %s\n", Duration.between(start, end) ) ;
        System.out.printf("After playing %,d games, average score was %.2f with an average of %.2f moves per game\n",
                numGames,
                (totalScore * 1.0) / games,
                (totalMoves * 1.0) / games) ;
    }
}


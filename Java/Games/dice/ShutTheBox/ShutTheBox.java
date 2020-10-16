
import dice.Dice ;
import java.util.Map ;
import java.util.HashMap ;

public class ShutTheBox
{
    private static int[][][] allOptions_ = {
        // 1
        { {1}, },
        // 2
        { {2},{1,1}, },
        // 3
        { {3},{1,2}, },
        // 4
        { {4},{1,3}, },
        // 5
        { {5},{1,4},{2,3}, },
        // 6
        { {6},{1,5},{2,4},{1,2,3}, },
        // 7
        { {7},{1,6},{2,5},{3,4},{1,2,4}, },
        // 8
        { {8},{1,7},{2,6},{3,5},{1,2,5},{1,3,4}, },
        // 9
        { {9},{1,8},{2,7},{3,6},{4,5},{1,2,6},{1,3,5},{2,3,4}, },
        // 10
        { {10},{1,9},{2,8},{3,7},{4,6},{1,2,7},{1,3,6},{1,4,5},{2,3,4},{1,2,3,4}, },
        // 11
        { {1,10},{2,9},{3,8},{4,7},{5,6},{1,2,8},{1,3,7},{1,4,6},{2,3,6},{2,4,5},{1,2,3,5}, },
        // 12
        { {2,10},{3,9},{4,8},{5,7},{1,2,9},{1,3,8},{1,4,7},{1,5,6},{2,3,7},{2,4,6},{3,4,5},{1,2,3,6},{1,2,4,5}, },
    } ;

    private static int boxSize_ = 10 ;
    private boolean[] box_ = { true, true, true, true, true, true, true, true, true, true } ;
    private Dice dice_ = new Dice(2) ;

    public ShutTheBox()
    {
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

    public int score()
    {
        int score = 0 ;
        for(int i = 1 ; i < boxSize_ ; i++ )
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
        int[][] options = allOptions_[roll-1] ;
        for(int i = 0 ; i < options.length ; i++)
        {
            if(possible(options[i]))
            {
                for(int j = 0 ; j < options[i].length ; j++ )
                {
                    close(options[i][j]) ;
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

    public boolean possible(int[] options)
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
            int roll = dice_.roll() ;
            if(!move(roll))
            {
                break ;
            }
            i++ ;
        }
        return i ;
    }

    public static void main(String[] args)
    {
        ShutTheBox game = new ShutTheBox() ;
        Map<Integer,Integer> scores = new HashMap<Integer,Integer>() ;
        Map<Integer,Integer> moves  = new HashMap<Integer,Integer>() ;

        int games = 0 ;
        int totalScore = 0 ;
        int totalMoves = 0 ;
        while(games < 1_000_000_000)
        {
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

        int maxScore = 55 ;
        for( int i = 1 ; i <= maxScore ; i++ )
        {
            if(scores.containsKey(i))
            {
                System.out.printf("%" + String.valueOf(maxScore).length() + "d = %," + (String.valueOf(games).length()+2) + "d / %5.2f%%\n", i, scores.get(i), scores.get(i)*100.00 / games) ;
            }
            else
            {
                System.out.printf("%" + String.valueOf(maxScore).length() +"d = %," + (String.valueOf(games).length()+2) + "d / %5.2f%%\n", i, 0, 0.0) ;
            }
        }
        System.out.printf("After playing %,d games, average score was %.2f with an average of %.2f moves per game\n",
                games,
                (totalScore * 1.0) / games,
                (totalMoves * 1.0) / games) ;


    }
}


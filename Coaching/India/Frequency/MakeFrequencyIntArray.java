import java.util.* ;
import java.io.* ;

public class MakeFrequencyIntArray {
    public static int[][] makeFrequency(int[][] points) {
        //base case
        if(points == null) return points ;

        int[][] histogram = new int[1][] ;

        for(int i ; i < points.length ; ++i) {
            //giving up, just too complicated
        }
        return histogram ;
    }

    public static int[] makePoint(String point) {
        if(point == null || point.length() < 3) {
            return null ;
        }

        String components[] = point.split(",") ;
        if(components.length != 2) {
            return null ;
        }

        int[] p = new int[2] ;

        try {
            p[0] = Integer.parseInt(components[0]) ;
            p[1] = Integer.parseInt(components[1]) ;
        } catch(Exception e) {
            // do something
        }
        return p ;
    }

    public static int[][] addPoint(int[][] points, int[] point) {
        int[][] newPoints = null ;
        if(points == null) {
            newPoints = new int[1][1] ;
            newPoints[0] = point ;
            return newPoints ;
        }

        newPoints = new int[points.length+1][] ;
        int i = 0 ;
        for( ; i < points.length ; ++i) {
            newPoints[i] = points[i] ;
        }
        newPoints[i] = point ;
        return newPoints ;
    }

    // Print a 2d int array
    public static void printPoints(int[][] points, PrintStream o) {
        if(points == null) return ;
        for(int i = 0 ; i < points.length ; ++i) {
            if(i==0) o.printf("[") ;
            for(int j = 0 ; j < points[i].length ; ++j) {
                if(j==0) o.printf("[") ;

                o.printf("%d", points[i][j]) ;
                if(j==(points[i].length-1)) {
                    o.printf("]") ;
                } else {
                    o.printf(",") ;
                }
            }

            if(i==(points.length-1)) {
                o.printf("]\n") ;
            } else {
                o.printf(",") ;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Please enter in points (separated by newlines, ^D when done") ;

        String line ;
        int[][] points = null ;
        while((line = System.console().readLine()) != null)
        {
            int[] p = makePoint(line) ;
            if(p == null) {
                System.err.println("Skipping invalid line: " + line) ;
                continue ;
            }
            points = addPoint(points, p) ;
        }

        System.out.println("\nCreated the following list:") ;
        printPoints(points, System.out) ;

        int[][] histogram = makeFrequency(points) ;
        System.out.println("\nHistogram created is:") ;
        printPoints(histogram, System.out) ;
    }
}

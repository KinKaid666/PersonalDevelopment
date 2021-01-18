import java.util.* ;

public class MakeFrequency {
    public static Map<Point,Integer> makeFrequency(List<Point> points) {
        Map<Point,Integer> histogram = new HashMap<Point,Integer>() ;
        for(Point p : points) {
            Integer i = histogram.get(p) ;
            if(i == null) {
                histogram.put(p,Integer.valueOf(1)) ;
            } else {
                i++ ;
                histogram.put(p,i) ;
            }
        }
        return histogram ;
    }

    public static void main(String[] args) {
        System.out.println("Please enter in points (separated by newlines, ^D when done") ;

        String line ;
        List<Point> points = new LinkedList<Point>() ;
        while((line = System.console().readLine()) != null)
        {
            Point p = Point.valueOf(line) ;
            if(p == null) {
                System.err.println("Skipping invalid line: " + line) ;
                continue ;
            }
            points.add(p) ;
        }

        System.out.println("\nCreated the following list:") ;
        System.out.println(points) ;

        Map<Point,Integer> histogram = makeFrequency(points) ;
        System.out.println("\nHistogram created is:") ;
        System.out.println(histogram) ;
    }
}

import java.util.* ;

public class Point implements Comparable<Point> {
    private int x_ ;
    private int y_ ;

    public Point(int x, int y) {
        x_ = x ;
        y_ = y ;
    }

    public void setX(int x) { x_ = x ; }
    public void setY(int y) { y_ = y ; }
    public int getX() { return x_ ; }
    public int getY() { return y_ ; }

    public String toString() {
        StringBuffer sb = new StringBuffer() ;
        sb.append("<Point(").append(x_).append(",").append(y_).append(")>") ;
        return sb.toString() ;
    }

    public static Point valueOf(String point) {
        // bad input(s)
        if(point == null || point.length() < 3) {
            return null ;
        }

        String components[] = point.split(",") ;
        if(components.length != 2) {
            return null ;
        }

        int x, y ;
        Point p = null ;
        try {
            x = Integer.parseInt(components[0]) ;
            y = Integer.parseInt(components[1]) ;
            p = new Point(x,y) ;
        } catch(Exception e) {
            // do something
        }
        return p ;
    }

    @Override
    // Not good enough for production, good enough for an interview
    public int hashCode() {
        return x_ << 32 | y_ ;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true ;
        }

        if(!(other instanceof Point)) {
            return false ;
        }

        Point that = (Point)other ;
        return this.x_ == that.x_ && this.y_ == that.y_ ;
    }

    @Override
    public int compareTo(Point that) {
        if(this.x_ < that.x_) {
            return -1 ;
        } else if(this.x_ > that.x_) {
            return 1 ;
        } else {
            if(this.y_ < that.y_) {
                return -1 ;
            } else if(this.y_ > that.y_) {
                return 1 ;
            } else {
                return 0 ;
            }
        }
        // never get here
    }
}


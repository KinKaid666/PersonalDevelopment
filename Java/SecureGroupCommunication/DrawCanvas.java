package SecureGroupCommunication ;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

// Canvas for drawing lines and erasing.
public class DrawCanvas extends Canvas implements
        MouseListener, MouseMotionListener {

    // Internal shape class.
    private class shape {
        private int x1, x2, y1, y2, color, type;

        public shape(int a, int b, int c, int d, int clr) {
            x1 = a;
            y1 = b;
            x2 = c;
            y2 = d;
            color = clr;
            type = 1;
        }

        public shape(int a, int b) {
            x1 = a;
            y1 = b;
            type = 2;
        }

        public int getX1() { return x1; }
        public int getX2() { return x2; }
        public int getY1() { return y1; }
        public int getY2() { return y2; }
        public int color() { return color; }
        public int type()  { return type; }
    }

    // Ints to track mouse dragging.
    private int d1, d2, d3, d4;

    private int currentColor;

    // Shape vector.
    private Vector shapes;

    private boolean pen, erase;

    private Color[] colors = { Color.black, Color.green, Color.blue,
                               Color.red, Color.yellow, Color.orange,
                               Color.gray, Color.magenta, Color.cyan,
                               Color.pink };

    private GroupWhiteBoard parent;

    // Constructor.
    public DrawCanvas(GroupWhiteBoard gwb) {
        parent = gwb;
        addMouseListener( this );
        addMouseMotionListener( this );
        this.setBackground( Color.white );
        this.setSize(200, 300);
        pen = false;
        erase = false;
        d1 = d2 = d3 = d4 = -1;
        currentColor = 0;
        shapes = new Vector();
    }

    // Paint method.
    public void paint( Graphics g ) {

        // Draw each shape.
        for(int i = 0; i < shapes.size(); i++) {
            shape s = (shape) shapes.elementAt(i);
            int type = s.type();

            // Line.
            if(type == 1) {
                g.setColor( colors[s.color()] );
                g.drawLine( s.getX1(), s.getY1(),
                            s.getX2(), s.getY2());
            }
            // Eraser Oval.
            if(type == 2) {
                g.setColor( Color.white );
                g.fillOval( s.getX1(), s.getY1(), 20, 20 );
            }
        }
    }

    // Assign the current color.
    public void setColor(int c) { currentColor = c; }

    // Erase the board, remove all shapes.
    public void eraseAll() {
        shapes.removeAllElements();
        repaint();
    }

    // Add a remote eraser oval.
    public void remoteErase(int rE1, int rE2) {
        shapes.addElement(new shape(rE1, rE2));
        repaint();
    }

    // Add a remote line.
    public void remoteLine(int a, int b, int c, int d, int clr) {
        shapes.addElement(new shape(a, b, c, d, clr));
        repaint();
    }

    // Function tracking methods.
    public void penOff() { pen = false; }
    public void penOn() { pen = true; }
    public void eraseOff() { erase = false; }
    public void eraseOn() { erase = true; }

    // Reset the line tracking coordinates.
    public void mouseReleased( MouseEvent e ) {
        d1 = d2 = d3 = d4 = -1;
    }

    // Track line drawing, and add a new line.
    public void mouseDragged( MouseEvent e ) {
        if(pen) {
            d1 = d3;
            d2 = d4;
            d3 = e.getX();
            d4 = e.getY();
            if(d1 == -1 || d2 == -1) {
                d1 = d3;
                d2 = d4;
            }
            shapes.add(new shape(d1, d2, d3, d4, currentColor));
            parent.sendLine( d1, d2, d3, d4 ,currentColor);
            repaint();
        }
        if(erase) {
            shapes.add(new shape(e.getX() - 10, e.getY() - 10));
            parent.sendErase(e.getX() - 10, e.getY() - 10);
            repaint();
        }
    }

    // Undefined mouse events.
    public void mouseClicked( MouseEvent e ) {}
    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e ) {}
    public void mouseMoved( MouseEvent e ) {}
    public void mousePressed( MouseEvent e ) {}
}

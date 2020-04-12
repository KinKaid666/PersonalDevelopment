import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.geom.*;
import javax.imageio.*;
import java.io.*;

public class ReadTest1 extends JFrame {
    BufferedImage bi;
    int width;
    int height;
    
    public ReadTest1() {
        try {
             bi = ImageIO.read(new File("cards.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
        height = bi.getHeight()+ 100;
        width = bi.getWidth()+ 100;
        setSize(width,height);
    }
    
    public void paint(Graphics g) {
        //setBackground(Color.white);
        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(bi,100,100,null);
        //g2.fill(new Ellipse2D.Double(25,50,255,125));
        
    }
    
    public static void main(String[] args) {
        JFrame f = new ReadTest1();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


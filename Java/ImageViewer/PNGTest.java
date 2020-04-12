import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.File ;
import java.io.IOException ;
import java.awt.image.BufferedImage ;
import javax.imageio.* ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;
import javax.swing.JPanel ;
import java.awt.Dimension ;
import java.awt.GridLayout ;
import java.awt.Graphics2D ;
public class PNGTest extends JFrame
{
    private BufferedImage image = null ;
    private String imageFilename = null ;

    public PNGTest(String s)
    {
        imageFilename = s ;
    }

    @Override
    public void paint(Graphics g)
    {
        try
        {
            image = ImageIO.read(new File(imageFilename)) ;
        }
        catch (IOException e)
        {
            System.out.println("Uncaugh IOException: " + e.toString()) ;
            System.exit(1) ;
        }
        Graphics2D g2 = (Graphics2D)g ;
        g2.drawImage(image, 0, 0, null);
        setSize(image.getWidth(),image.getHeight()) ;
    }


    private static void createAndShowGUI(String s)
    {
        PNGTest frame = new PNGTest(s) ;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String args[])
    {
        if( args.length != 1)
        {
            System.err.println("Usage: ImageDisplay <filename>") ;
            System.exit(1) ;
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args[0]);
            }
        }) ;
    }
}

/*
 * Graphics2D (will go to SVG)
 */

package eu.discoveri.predikt.test.horochart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * A basic test class for writing to a BufferedImage via Graphics2D.  This is
 * used as a reference implementation (we can assume that the output is correct
 * for Java2D, then compare how it looks when using JFreeSVG or OrsonPDF).
 */
public class BufferedImageDemo
{
    /**
     * Starting point for the demo.
     * 
     * @param args  ignored.
     * 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(600, 400, 
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
//        ImageIcon icon = new ImageIcon(BufferedImageDemo.class.getResource("/home/chrispowell/Discoveri/discoveri-logo.png"));
        ImageIcon icon = new ImageIcon("/home/chrispowell/Discoveri/discoveri-logo.png");
        g2.rotate(Math.PI / 12);
        g2.setStroke(new BasicStroke(2.0f));
        g2.setPaint(Color.WHITE);
        g2.fill(new Rectangle(0, 0, 600, 400));
        g2.setPaint(Color.RED);
        g2.draw(new Rectangle(0, 0, 600, 400));
        g2.drawImage(icon.getImage(), 10, 20, null);
        ImageIO.write(image, "png", new File("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/image-test.png"));
    }
}

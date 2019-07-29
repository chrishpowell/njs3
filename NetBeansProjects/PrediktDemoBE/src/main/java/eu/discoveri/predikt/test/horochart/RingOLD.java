/*
 * Draw a ring
 */
package eu.discoveri.predikt.test.horochart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import java.io.File;
import java.io.IOException;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class RingOLD
{
    /**
     * Draw a ring.
     * 
     * @param g2  Graphics 'container'
     * @param scolor Stroke colour
     * @param cx x posn
     * @param cy y posn
     * @param ro radius
     * @param thick ring thickness
     */
    public static void drawHoroRing( Graphics2D g2, Color scolor,
                                     double cx, double cy, double ro, double thick,
                                     double offx, double offy )
    {
        // Not checking for negative thickness etc. 'cos not seeing RingOLD drawn makes this obvious 
        // Docs, btw, are confusing!  The args are: x: top-left, y: top-left; major-axis diameter, minor-axis diameter
        double topleftx = cx-ro+offx, toplefty = cy-ro+offy;
        Ellipse2D outCircle = new Ellipse2D.Double(topleftx, toplefty, ro, ro);
        Ellipse2D inCircle = new Ellipse2D.Double(topleftx+thick/2., toplefty+thick/2., ro-thick, ro-thick);
        
        g2.setPaint(scolor);
        g2.draw(inCircle);
        g2.draw(outCircle);
    }
    
    public static void main(String[] args)
            throws IOException
    {
        SVGGraphics2D g2 = new SVGGraphics2D(110, 110);
        // Make lines smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double CX = 50.d, CY = 50.d, DO = 50.d, THICK = 10.d, OFFX = 0.d, OFFY = 0.d;
        drawHoroRing( g2, Color.BLUE, CX, CY, DO, THICK, OFFX, OFFY );

        SVGUtils.writeToSVG(new File("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/ring-test.svg"), g2.getSVGElement());
    }
}

/*
 * Draw a ring
 */
package eu.discoveri.predikt.graphics;

import eu.discoveri.predikt.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javafx.geometry.Point2D;
import javax.imageio.ImageIO;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

import eu.discoveri.prediktdemobe.Planet;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JFrame;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
//import java.awt.Dimension;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import org.w3c.dom.Element;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Ring1 implements ImageObserver
{
    // Default setup
    private double   cx = 50.d, cy = 50.d,                   // Centre
                     majdo = 50.d, mindo = 50.d,             // Major, minor axes size                  
                     thick = 10.d,                           // Thickness of ring
                     ccircle = 40.d;                         // Centre circle (radius = 50.-40.)
    // Viewbox (NB: assuming majoraxis is x axis)
    private int      vwidth = 200, vheight = 200;            // Viewbox size
    private double   offx = 0.d, offy = 0.d,                 // Offset from display origin
                     originx = cx+offx, originy = cy+offy,   // Ring origin
                     topleftx = originx-majdo,               // Viewport origin x
                     toplefty = originy-mindo;               // Viewport origin y
    
    /**
     * Setup view.
     * 
     * @param viewboxHeight
     * @param viewboxWidth
     * @param ncx centre x coord
     * @param ncy centre y coord
     * @param majorAxis size major axis
     * @param minorAxis size minor axis
     * @param thickness size of ring
     * @param offsetX offset from topleft x
     * @param offsetY offset from toleft y
     * @param centrecircle the centre circle (with logo)
     */
    public Ring1( double viewboxHeight, double viewboxWidth,
                 double ncx, double ncy,
                 double majorAxis, double minorAxis,
                 double thickness, double centrecircle,
                 double offsetX, double offsetY  )
    {
        // Not checking for negative thickness etc. 'cos not seeing Ring drawn makes this obvious
        cx = ncx; cy = ncy;
        majdo = majorAxis; mindo = minorAxis;
        thick = thickness;
        ccircle = centrecircle;
        offx = offsetX; offy = offsetY;
        originx = offsetX+ncx/2.;
        originy = offsetY+ncy/2.;
        
        // Where the view origin in relation to ring origin
        vheight = (int)viewboxHeight; vwidth = (int)viewboxWidth;
        topleftx = offsetX;
        toplefty = offsetY;
    }
    
    /*
     * Coordinate checker
     */
    private void drawCoords( Graphics2D g2 )
    {
        // Coord size
        double WIDTH = 8.d, HEIGHT = 8.d;
        
        /*
         * Draw coords
         */
        //... Topleft
        g2.setPaint(Color.ORANGE);
        g2.setBackground(Color.ORANGE);
        Ellipse2D coord0 = new Ellipse2D.Double(offx, offy, WIDTH, HEIGHT);
        g2.draw(coord0);
        g2.fill(coord0);
        
        //...Ring origin
        g2.setPaint(Color.GREEN);
        g2.setBackground(Color.GREEN);
        Ellipse2D coord1 = new Ellipse2D.Double(originx, originy, WIDTH, HEIGHT);
        //g2.drawString("O", (float)originx, (float)originy);
        g2.draw(coord1);
        g2.fill(coord1);
        
        //...Top of ring
        g2.setPaint(Color.BLUE);
        g2.setBackground(Color.BLUE);
        Ellipse2D coord2 = new Ellipse2D.Double(originx, originy-mindo/2., WIDTH, HEIGHT);
        g2.draw(coord2);
        g2.fill(coord2);
        
        //...Right of ring
        g2.setPaint(Color.MAGENTA);
        g2.setBackground(Color.MAGENTA);
        Ellipse2D coord3 = new Ellipse2D.Double(originx+majdo/2., originy, WIDTH, HEIGHT);
        g2.draw(coord3);
        g2.fill(coord3);
        
        //...Bottom of ring
        g2.setPaint(Color.CYAN);
        g2.setBackground(Color.CYAN);
        Ellipse2D coord4 = new Ellipse2D.Double(originx, originy+mindo/2., WIDTH, HEIGHT);
        g2.draw(coord4);
        g2.fill(coord4);
        
        //...Left of ring
        g2.setPaint(Color.BLACK);
        g2.setBackground(Color.BLACK);
        Ellipse2D coord5 = new Ellipse2D.Double(originx-majdo/2., originy, WIDTH, HEIGHT);
        g2.draw(coord5);
        g2.fill(coord5);
    }
    
    
    /**
     * Draw a ring.
     * 
     * @param g2  Graphics 'container'
     * @param scolorOut Stroke and background colour outer
     * @param scolorIn Stroke and background colour inner
     * @param scolorCent centre circle colour
     * @throws IOException
     */
    public void drawHoroRing( SVGGraphics2D g2, Color scolorOut, Color scolorIn, Color scolorCent )
            throws IOException
    {
        // Docs, btw, are confusing!  The args are: x: top-left, y: top-left; major-axis diameter, minor-axis diameter
        Ellipse2D outCircle = new Ellipse2D.Double(topleftx, toplefty, majdo, mindo);
        Ellipse2D inCircle = new Ellipse2D.Double(topleftx+thick/2., toplefty+thick/2., majdo-thick, mindo-thick);
        Ellipse2D cCircle = new Ellipse2D.Double(topleftx+ccircle/2., toplefty+ccircle/2., majdo-ccircle, mindo-ccircle);

        g2.setPaint(scolorOut);
        g2.draw(outCircle);
        g2.fill(outCircle);
        
        g2.setPaint(scolorIn);
        g2.draw(inCircle);
        g2.fill(inCircle);

        g2.setPaint(scolorCent);
        g2.draw(cCircle);
        g2.fill(cCircle);
        
        // Draw logo
        BufferedImage blogo = ImageIO.read(new File(Constants.RESOURCEPATH+"images/deltat.png"));
        Image logo = blogo.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        g2.drawImage( logo, (int)(11.d+topleftx+ccircle/2.), (int)(11.d+toplefty+ccircle/2.), this );
    }
    
    
    /**
     * Draw the planets (and Sun and Moon etc.)
     * 
     * @param g2
     * @throws IOException
     */
    public void drawPlanets( SVGGraphics2D g2 )
            throws IOException
    {
        // Get the planets
        Map<String,Planet> planets = Planet.getPlanets();
        
        planets.entrySet().stream().map((entry) -> {
            // Get planet posn.
            ephemerisCalc(entry.getValue());
            return entry;
        }).forEachOrdered((entry) -> {
            System.out.println("....> " +entry.getValue().getName() );
        });
        
        // See if any overlap
        planets.entrySet().forEach((entry) -> {
            planets.entrySet().stream().filter((entry2) -> !( entry2.getValue().getOrder() == entry.getValue().getOrder() )).map((entry2) -> {
                System.out.println(entry2.getValue().getName()+": "+entry2.getValue().getDegrees()+", "+entry.getValue().getName()+": "+entry.getValue().getDegrees());
                // Planets on top of each other?
                return entry2;
            }).filter((entry2) -> ( Math.abs(entry2.getValue().getDegrees() - entry.getValue().getDegrees()) < Constants.POVERLAP &&
                    entry2.getValue().getOffset() == entry.getValue().getOffset() )).map((_item) -> {
                        entry.getValue().setOffset(entry.getValue().getOffset()+Constants.POFFSET);
                return _item;
            }).forEachOrdered((_item) -> {
                System.out.println("....> "+entry.getValue().getOffset());
            });
        });
        
        for( Map.Entry<String,Planet> entry: planets.entrySet() )
        {
            // Draw planet
            drawPlanet( g2, new File(entry.getValue().getFilename()), entry.getValue().getDegrees(), entry.getValue().getOffset() );
        }
    }
    
    /*
     * Get planet position
     */
    public void ephemerisCalc( Planet planet )
    {
        if( planet.getName() == "Sun" )
        {
            planet.setDegrees(15.d); return;
        }
        else
        if( planet.getName() == "Pluto")
        {
            planet.setDegrees(15.d); return;
        }
        else
        if( planet.getName() == "Neptune")
        {
            planet.setDegrees(240.d);  return;
        }

        planet.setDegrees(66.d); return;
    }
    
    /*
     * Draw planet
     */
    private void drawPlanet( SVGGraphics2D g2, File planet, double degrees, double offsetPct )
            throws IOException
    {
        // Calculate the planet radial position given an offset
        Point2D posn = calcPosn( degrees, offsetPct );
        
        // Planet
        BufferedImage bplanet = ImageIO.read(planet);
        Image sun = bplanet.getScaledInstance(40,40,Image.SCALE_SMOOTH);
        g2.drawImage( sun, (int)posn.getX(), (int)posn.getY(), this );
    }
    
    
    /*
     * Draw a zodiac border
     */
    private void drawZodiacBorder( Graphics2D g2, double degrees )
    {
        double radians = Math.toRadians(degrees);
        
        Path2D.Double border = new Path2D.Double();
        
        border.moveTo(originx+((majdo-thick)/2.)*Math.cos(radians), originy+((thick-mindo)/2.)*Math.sin(radians));
        border.lineTo(originx+(majdo/2.)*Math.cos(radians), originy-(mindo/2.)*Math.sin(radians));
        
        g2.setPaint(Color.BLACK);
        g2.draw(border);
    }
    
    /*
     * Draw all zodiac borders
     */
    private void drawAllZBorders( Graphics2D g2 )
    {
        // All zodiac sign borders
        for( int z=1; z<13; z++ )
        {
            drawZodiacBorder( g2, z*30.d );
        }
    }
    
    /*
     * Draw a zodiac sign
     */
    private void drawZodiacSign( SVGGraphics2D g2, double degrees )
            throws IOException
    {
        // Calculate the radial position given an offset
        Point2D posn = calcPosn( degrees, -0.12 );
        
        // A little ellipse
        //Ellipse2D planet1 = new Ellipse2D.Double(posn.getX(), posn.getY(), 10, 10);
        BufferedImage bmars = ImageIO.read(new File(Constants.RESOURCEPATH+"zodiac/virgo.png"));
        Image mars = bmars.getScaledInstance(Constants.SCALEDZ,Constants.SCALEDZ,Image.SCALE_SMOOTH);

        g2.drawImage( mars, (int)posn.getX(), (int)posn.getY(), this );
    }
    
    /*
     * Draw all zodiac signs
     */
    private void drawAllZSigns( SVGGraphics2D g2 )
            throws IOException
    {
        // All zodiac signs
        for( int z=1; z<13; z++ )
        {
            drawZodiacSign( g2, z*30.d-15.d );
        }
    }
    
    
    /*
     * Calculate the position given radial offset
     */
    private Point2D calcPosn( double degrees, double offsetPct )
    {
        // To radians
        double radians = Math.toRadians(degrees);
        
        // Division by 2 'cos radius is half the xxxdo.  But why the 20.??
        double posnx = originx-20.+majdo*(1+offsetPct)*Math.cos(radians)/2.;
        double posny = originy-20.-mindo*(1+offsetPct)*Math.sin(radians)/2.;
//        System.out.println("originy = " +originy+ ", vert = " +(mindo*(1+offsetPct)*Math.sin(radians)/2.)+ ", posny = " +posny);
//        System.out.println("originx = " +originx+ ", horz = " +(majdo*(1+offsetPct)*Math.cos(radians)/2.)+ ", posnx = " +posnx);

        return new Point2D( posnx, posny );
    }
    
    
    /*
     * Image update notification
     */
    @Override
    public boolean imageUpdate( Image img, int infoflags, int x, int y, int width, int height)
    {
        return true;
    }
    
    /*
     * Display the doc
     * Stream etc. empties the document (why??)
     */
    private static void displayDoc( final SVGDocument svgdoc  )
            throws FileNotFoundException, IOException
    {
        // Copy document (Yes, they get 'drained' on stream, display etc....)
        DOMImplementation domImpl =
            SVGDOMImplementation.getDOMImplementation();
        Document d = DOMUtilities.deepCloneDocument(svgdoc, domImpl);
        
        // Create an instance of the SVG Generator.
        SVGGraphics2D g2 = new SVGGraphics2D(d);
        
        // Write out doc
        PrintWriter writer = new PrintWriter(new File(Constants.RESOURCEPATH+"tests/test.doc"));
        DOMUtilities.writeDocument(svgdoc,writer);
        writer.flush();
        writer.close();

        // Get root
        g2.getRoot( d.getDocumentElement() );
        
        // Display
        JSVGCanvas canvas = new JSVGCanvas();
        JFrame f = new JFrame();
        f.getContentPane().add(canvas);
        canvas.setSVGDocument((SVGDocument)d);
        
        f.pack();
        f.setVisible(true);
    }

    /*
     * Mutators
     */
    public double getCx() { return cx; }
    public double getCy() { return cy; }
    public double getMajdo() { return majdo; }
    public double getMindo() { return mindo; }
    public double getThick() { return thick; }
    public int getVwidth() { return vwidth; }
    public int getVheight() { return vheight; }
    public double getOffx() { return offx; }
    public double getOffy() { return offy; }
    public double getOriginx() { return originx; }
    public double getOriginy() { return originy; }
    public double getTopleftx() { return topleftx; }
    public double getToplefty() { return toplefty; }
    
    
    //-----------------------------------
    
//    /* traverses tree starting with given node */
//  private static List<Node> traverse(Node n)
//  {
//    return traverse(Arrays.asList(n));
//  }
//
//  /* traverses tree starting with given nodes */
//  private static List<Node> traverse(List<Node> nodes)
//  {
//    List<Node> open = new LinkedList<Node>(nodes);
//    List<Node> visited = new LinkedList<Node>();
//
//    ListIterator<Node> it = open.listIterator();
//    while (it.hasNext() || it.hasPrevious())
//    {
//      Node unvisited;
//      if (it.hasNext())
//        unvisited = it.next();
//      else
//        unvisited = it.previous();
//
//      it.remove();
//
//      List<Node> children = getChildren(unvisited);
//      for (Node child : children)
//        it.add(child);
//
//      visited.add(unvisited);
//    }
//
//    return visited;
//  }
//
//  private static List<Node> getChildren(Node n)
//  {
//    List<Node> children = asList(n.getChildNodes());
//    Iterator<Node> it = children.iterator();
//    while (it.hasNext())
//      if (it.next().getNodeType() != Node.ELEMENT_NODE)
//        it.remove();
//    return children;
//  }
//
//  private static List<Node> asList(NodeList nodes)
//  {
//    List<Node> list = new ArrayList<Node>(nodes.getLength());
//    for (int i = 0, l = nodes.getLength(); i < l; i++)
//      list.add(nodes.item(i));
//    return list;
//  }
    
//    public static void recurse( Node node )
//    {
//        System.out.println( "==> " +node.getNodeName()+ " : " +node.getNodeType() );
//        
//        NodeList nl = node.getChildNodes();
//        for( int ix = 0; ix < nl.getLength(); ix++ )
//        {
//            Node currNode = nl.item( ix );
////            if( currNode.getNodeType() == Node.ELEMENT_NODE )
////            {
//                recurse( currNode );
////            }
//        }
//    }
    
    //-----------------------------------
    
    
    /*
     * M A I N
     * =======
     */
    public static void main(String[] args)
            throws Exception
    {
        double VIEWW = 1200.d, VIEWH = 1200.d, CX = 450.d, CY = 450.d, DO = 450.d, THICK = 120.d, CCIRCLE = 400.d, OFFX = 100.d, OFFY = 100.d;
        Ring1 ring = new Ring1( VIEWH, VIEWW, CX, CY, DO, DO, THICK, CCIRCLE, OFFX, OFFY );

        // Get a DOMImplementation.
        DOMImplementation domImpl =
            SVGDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        SVGDocument svgdoc = (SVGDocument)domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D g2 = new SVGGraphics2D(svgdoc);
        
        // Viewbox add... does not get added
//        Element root = g2.getRoot();
//        root.setAttributeNS(null, "viewBox", "0 0 600 600");
        
        // Make lines smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Setup the ring
        ring.drawHoroRing( g2, new Color(253, 237, 236), Color.WHITE, new Color(133, 193, 233) );
        // Draw zodiac arcs
        ring.drawAllZBorders(g2);
        // Draw zodiac signs
        ring.drawAllZSigns(g2);
        // Planets
        ring.drawPlanets(g2);
        
        //...Display (copy) doc
        displayDoc( svgdoc );
        
        // Does not work... it eats any added attributes like viewBox... sigh.  AND g2.getRoot() will cause this to fail!!
        g2.stream(Constants.RESOURCEPATH+"tests/ring-test.svg",true);

        //... For desktop
        
        // Recurse (but not the sub elements reqd... sigh)
        //recurse( root );
        
        
        //... For mobile
        
        // Create a JPEG transcoder
//        PNGTranscoder t = new PNGTranscoder();
//
//        // Create the transcoder input.
//        TranscoderInput input = new TranscoderInput(svgdoc);
//
//        // Create the transcoder output.
//        OutputStream ostream = new FileOutputStream(Constants.RESOURCEPATH+"tests/ring-test.png");
//        TranscoderOutput output = new TranscoderOutput(ostream);
//
//        // Save the image.
//        t.transcode(input, output);
//
//        // Flush and close the stream.
//        ostream.flush();
//        ostream.close();
        
        // Clean up
        g2.dispose();
    }
}

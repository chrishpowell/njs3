/*
 * Draw a ring
 */
package eu.discoveri.predikt.graphics;

import eu.discoveri.predikt.exception.WheelGeometryException;
import eu.discoveri.predikt.test.horochart.ZodiacHouse;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;
import eu.discoveri.prediktdemobe.Planet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Point2D;
import javax.imageio.ImageIO;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
//import org.jfree.graphics2d.svg.ViewBox;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Ring implements ImageObserver
{
    // Viewbox (NB: assuming majoraxis is x axis)
    private final int       vwidth,
                            vheight;                                // Graphics size (can be re-sized)
    // Ring
    private final double    thick;                                  // Thickness of ring
    private final double    rO,                                     // Outer ring radius
                            rI,                                     // Inner ring radius
                            logor;                                  // Logo circle radius
    private final Point2D   origin,                                 // [topleftx, toplefty]
                            ringO;                                  // Ring centre
    private double          wheelRot;                               // Rotation of zodiac wheel to put ASC at 9pm

    // Images
    private final Map<ZodiacHouse,ZImage> hImgMap   = new HashMap<>();
    private final Map<Integer,ZImage> cNumMap   = new HashMap<>();
    
    // --------- TEST: Wheel data ----------------------------------------------
    private static Map<Integer,Cpa> cpaMap3 = Cpa.getCpa(); // **** Test Cpa
    public Map<Integer,Cpa> getCpaMap() { return cpaMap3; }
    //--------------------------------------------------------------------------
    
    
    /**
     * Setup view.  Note circles are ellipses with equal major/minor axis.
     * 
     * @param viewboxHeight
     * @param viewboxWidth
     * @param origin [topleftX,topleftY] origin of viewbox
     * @param radius Radius of outer ring
     * @param thickness Thickness of ring (also determines inner radius)
     * @param logor radius of inside logo circle
     * @throws WheelGeometryException
     */
    public Ring( int viewboxHeight,
                 int viewboxWidth,
                 Point2D origin,
                 double radius,
                 double thickness,
                 double logor       )
            throws WheelGeometryException
    {
        // Origin
        double topleftX = origin.getX();
        double topleftY = origin.getY();
        
        // Check a bit of geometry
        if( radius < thickness )
            throw new WheelGeometryException("Radius must be greater than thickness: " +radius+" < " +thickness);
        if( (topleftX+radius*2.d) > viewboxWidth || (topleftY+radius*2.d) > viewboxHeight )
            throw new WheelGeometryException("Whole wheel will not be seen, wheel geometry exceeds viewbox size.");
        
        // Not checking for negative thickness etc. 'cos not seeing Ring drawn makes this obvious
        this.thick = thickness;
        // Outer ring radius
        this.rO = radius;

        // Where the view origin in relation to ring origin
        this.vheight = viewboxHeight;
        this.vwidth = viewboxWidth;

        // Set origin and wheel/ring centre
        this.origin = origin;
        this.ringO = new Point2D(topleftX+radius,topleftY+radius);
        
        // Set inner ring radius
        this.rI = rO - thick;
        
        // Logo inner circle
        this.logor = logor;
    }
    
    /**
     * Load up images etc.
     */
    private void init()
            throws IOException
    {
        System.out.println("*** Init...");
        System.out.println("Loading zodiac images...");
        Map<ZodiacHouse,Integer> zh = ZodiacHouse.getZHNameMap();

        // Load zodiac images
        for( ZodiacHouse house: zh.keySet() )
        {
            // Read image from file
            System.out.print(".");
            BufferedImage sign = ImageIO.read(new File(Constants.RESOURCEPATH+"zodiac/"+house.getName().toLowerCase()+".png"));

            // Store the image with some metadata
            Image image = sign.getScaledInstance((int)Constants.SCALEDZ,(int)Constants.SCALEDZ,Image.SCALE_SMOOTH);
            ZImage zimage = new ZImage( image, Constants.SCALEDZ, Constants.SCALEDZ, Color.BLACK );

            hImgMap.put(house, zimage);
        }
        System.out.println("\r\nLoaded zodiac images...");
        
        System.out.println("Loading circled numbers...");
        for( int ii = 1; ii <= 12; ii++ )
        {
            System.out.print(".");
            BufferedImage cusp = ImageIO.read(new File(Constants.RESOURCEPATH+"zodiac/c"+ii+".png"));

            // Store the image with some metadata
            Image image = cusp.getScaledInstance((int)Constants.SCALEDC,(int)Constants.SCALEDC,Image.SCALE_SMOOTH);
            ZImage zimage = new ZImage( image, Constants.SCALEDC, Constants.SCALEDC, Color.yellow );
            
            cNumMap.put(ii, zimage);
        }
        System.out.println("\r\nLoaded circled numbers...");
        System.out.println("***Init complete...");
    }

    /**
     * Draw an empty ring.
     * 
     * @param g2  Graphics 'container'
     * @param scolorOut Stroke and background colour outer
     * @param scolorIn Background colour inner
     */
    public void drawHoroRing( SVGGraphics2D g2, Color scolorOut, Color scolorIn )
            
    {
        // Origin
        double topleftX = origin.getX(), topleftY = origin.getY();
        
        // Docs, btw, are confusing!  The args are: [x: top-left, y: top-left] (framing rect); major-axis diameter, minor-axis diameter
        Ellipse2D outCircle = new Ellipse2D.Double(topleftX, topleftY, 2*rO, 2*rO);
        Ellipse2D inCircle = new Ellipse2D.Double(topleftX+thick, topleftY+thick, 2*rI, 2*rI);
        
        g2.setPaint(scolorOut);
        g2.draw(outCircle);
        g2.fill(outCircle);
        
        g2.setPaint(scolorIn);
        g2.draw(inCircle);
        g2.fill(inCircle);
    }

    /**
     * Draw centre circle with logo
     * @param g2
     * @param scolorCent
     * @throws IOException 
     */
    public void drawCentreRing( SVGGraphics2D g2, Color scolorCent )
            throws IOException
    {
        // Origin
        double topleftX = origin.getX(), topleftY = origin.getY();
        
        Ellipse2D cCircle = new Ellipse2D.Double(topleftX+rO-logor/2.d, topleftY+rO-logor/2.d, logor, logor);
        
        g2.setPaint(scolorCent);
        g2.draw(cCircle);
        g2.fill(cCircle);
        
        // Draw logo  @TODO: Do this better!
        BufferedImage blogo = ImageIO.read(new File(Constants.RESOURCEPATH+"images/deltat.png"));
        Image logo = blogo.getScaledInstance((int)Constants.SCALEDL,(int)Constants.SCALEDL,Image.SCALE_SMOOTH);
        g2.drawImage( logo, (int)(11.d+topleftX+rO-logor/2.d), (int)(11.d+topleftY+rO-logor/2.d), null );
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
                    entry2.getValue().getOffset() == entry.getValue().getOffset()                                    )).map((_item) -> {
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

    /**
     * Draw zodiac border.  See ZWheel for usage.
     * @param g2
     * @param theta Angular position 
     */
    private void drawZodiacBorder( Graphics2D g2, double theta )
    {
        // Calc end points of border
        double xyO[] = calcXY(rO,theta);
        double xyI[] = calcXY(rI,theta);
        
        // Draw a border
        g2.setPaint(Color.GRAY);
        g2.drawLine( (int)xyI[0],(int)xyI[1], (int)xyO[0],(int)xyO[1] );
    }
    
    /**
     * Draw all zodiac houses and borders, for classic 12 house wheels.
     * 
     * @param g2
     * @param radOff Radial shift within house for ASC
     * @param houseStart ASC house num
     */
    private void drawZWheel( SVGGraphics2D g2 )
            throws IOException
    {
        // Get the zodiac images
        Map<Integer,ZodiacHouse> zhMap = ZodiacHouse.getZHOrderMap();
        // Store current transform for resetting after image rotation
//        AffineTransform backup = g2.getTransform();
        // Offsets with zodiac borders, angle in radians
        double RDELTA = 0.85d, TDELTA = 0.26;
        
        // ASC house and angle in house
        Cpa asc = getCpaMap().get(0);
        double angleAsc = asc.getAngle();
        int houseAsc = asc.getNum();

        
        /*
         * Rotate all zodiac signs and borders. Do this in two sections (yes,
         * it could be one loop) to avoid rotating everything with AffineTransform
         */
        // Borders:
        for( int z=0; z<Constants.NUMHOUSESCLASSIC; z++ )
        {
            // Rotate borders to correct place
            double theta = wheelRotate(z) - angleAsc;
            // Draw border
            drawZodiacBorder( g2, theta );
        }
        
        // Signs:
        for( int z=0; z<Constants.NUMHOUSESCLASSIC; z++ )
        {
            // Houses starting with ASC (Mod loop, eg: 10,11,0,1,2...)
            int house = (z+houseAsc)%Constants.NUMHOUSESCLASSIC;
            
            // Angle: Rotate wheel + offset
            double theta = wheelRotate(house-houseAsc) - angleAsc;
            
            // Calculate the position given radial and degree offsets
            ZImage zimage = hImgMap.get(zhMap.get(house));                      // Get image
            double xy[] = calcXY( rO*RDELTA, theta-TDELTA, Constants.SCALEDZ/2.d, Constants.SCALEDZ/2.d );                    // Calc posn.
            
            // Draw the zodiac sign (rotated)        
//            AffineTransform at = AffineTransform.getRotateInstance(theta-TDELTA);
//            g2.setTransform(at);

            g2.drawImage( zimage.getImage(), (int)xy[0], (int)xy[1], null );
//            g2.setTransform(backup);
        }
//        g2.setTransform(backup);
    }
    
    /**
     * Rotate wheel clockwise.  30 degrees per segment.
     * 
     * @param segmentsDiff Numeric difference between houses
     * @return 
     */
    private double wheelRotate( int segmentsDiff )
    {
        // 30 degree rotation per segment
        // Math.PI to 'rotate' clockwise
        return Math.PI + (segmentsDiff+1) * Math.PI/6.d;
    }
    
    /**
     * Rotate over wheel.  30 degrees per segment.
     * 
     * @param segmentsDiff Numeric difference between houses
     * @return 
     */
    private double rotateOverWheel( int segmentsDiff )
    {
        // 30 degree rotation per segment
        // Math.PI to 'rotate' clockwise
        return Math.PI - (segmentsDiff-1) * Math.PI/6.d;
    }
    
    /*
     * Draw the cusp boundaries.  Note ASC-DSC is always horizontal line (for
     * Placidus etc.)
     */
    private void drawCuspBounds( SVGGraphics2D g2 )
    {
        // Ring centre
        double cx0 = getCx(), cy0 = getCy();
        // Default g2 color
        g2.setPaint(Color.GRAY);
        // Font
        Font font = g2.getFont();
        int fsiz = font.getSize();


        // ASC to DSC line (always horizontal, Placidus etc.)
        // --------------------------------------------------
        // Dashed line
        Graphics2D g2c = (Graphics2D)g2.create();
        Stroke dash = new BasicStroke(2,  BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2c.setStroke(dash);
        g2c.setPaint(Color.GRAY);
        g2c.drawLine( (int)(cx0+rO+9),(int)cy0, (int)(cx0-rO-9),(int)cy0 );
        
        //... Cusp notation
        // Get ASC
        Cpa asc = getCpaMap().get(0);

        double xy[] = calcXY(rO+28,Math.PI-0.025);
        g2.drawString("ASC", (float)xy[0], (float)xy[1]);
        
        // Angle: Degrees
        int[] dm = Util.dec2ddmm(Math.toDegrees(asc.getAngle()));
        xy = calcXY(rO+32.d,Math.PI+0.05);
        g2.drawString(Integer.toString(dm[0]), (float)xy[0], (float)xy[1]);
        // Angle: Minutes
        g2.setFont(font.deriveFont((float)(fsiz*0.8)));
        g2.drawString(Integer.toString(dm[1]), (float)(xy[0]+16.d), (float)(xy[1]-4.d));
        g2.setFont(font.deriveFont(fsiz));
        
        // Get DSC
        Cpa dsc = getCpaMap().get(6);
        
        xy = calcXY(rO+2,-0.05);
        g2.drawString("DSC", (float)xy[0], (float)xy[1]);
        
        // Angle: Degrees
        dm = Util.dec2ddmm(Math.toDegrees(dsc.getAngle()));
        xy = calcXY(rO,0.0375);
        g2.drawString(Integer.toString(dm[0]), (float)xy[0], (float)xy[1]);
        // Angle: Minutes  
        g2.setFont(font.deriveFont((float)(fsiz*0.8)));
        g2.drawString(Integer.toString(dm[1]), (float)(xy[0]+16.d), (float)(xy[1]-4.d));
        g2.setFont(font.deriveFont(fsiz));
        
        // Cusp nums.
        drawCuspNum( g2, 1, Math.PI );
        drawCuspNum( g2, 7, 0.d );


        // MC to IC
        // --------
        Cpa mc = getCpaMap().get(9);

        // 'Negative' difference between houses
        int negHouseRot =  Constants.NUMHOUSESCLASSIC - (mc.getNum()-asc.getNum());
        
        // Rotation (clock) (Pi minus, not PI plus):
        //  PI - (house difference * 30degs) -
        //           (ASC angle in house) - (30degs - (MC angle in house))
        double theta = rotateOverWheel(negHouseRot); 
        theta -= asc.getAngle() + (Math.PI/6.d - mc.getAngle());
        
        // Plot
        double xymc[] = calcXY(rO+9,theta);
        double xyic[] = calcXY(rO+9,Math.PI+theta);
        g2c.drawLine( (int)xymc[0],(int)xymc[1], (int)xyic[0],(int)xyic[1] );
        g2c.dispose();
        
        // MC to IC notation
        g2.drawString("MC", (float)xymc[0], (float)xymc[1]);
        g2.drawString("IC", (float)xyic[0]-9, (float)xyic[1]+9);
        
        // Cusp nums.
        drawCuspNum( g2, 10, theta );
        drawCuspNum( g2, 4, Math.PI+theta );
        
        // All other cusps
        // ---------------
        g2.setStroke(new BasicStroke());                        // Back to default
        // 8 remaining cusps
        for( int za = 1; za <= 4; za++ )
        {
            for( int zb = 0; zb <= 1; zb++ )
            {
                // Remaining cusps 2(1),3(2); 5(4),6(5); 8(7),9(8); 11(10),12(11)
                int z = 3*za-2+zb;

                // Cusp
                Cpa cz = getCpaMap().get(z);

                // 'Negative' difference between houses
                negHouseRot =  Constants.NUMHOUSESCLASSIC - (cz.getNum()-asc.getNum());

                // Rotation (clock) (NB: Pi minus, not PI plus):
                //  PI - (house difference * 30degs) -
                //           (ASC angle in house) - (30degs - (MC angle in house))
                theta = rotateOverWheel(negHouseRot); 
                theta -= asc.getAngle() + (Math.PI/6.d - cz.getAngle());

                // Plot
                double beg[]  = calcXY(rI,theta);
                double end[] = calcXY(logor/2.d,theta);

                g2.setPaint(Color.LIGHT_GRAY);
                g2.drawLine( (int)beg[0],(int)beg[1], (int)end[0],(int)end[1] );
                
                // Draw cusp nums
                drawCuspNum( g2, z+1, theta );
            }
        }
    }
    
    /**
     * Draw a cusp number.
     * 
     * @param g2
     * @param cuspNum 1 to 12
     * @param theta What angle
     */
    private void drawCuspNum( SVGGraphics2D g2, int cuspNum, double theta )
    {
        // Cusp num radius
        double cuspR = rI-10.d;
        
        ZImage zi = cNumMap.get(cuspNum);
        double xy[] = calcXY( cuspR,theta, zi.getHeight()/2.d,zi.getWidth()/2.d);
        g2.drawImage(zi.getImage(), (int)xy[0], (int)xy[1], null);
    }
    
    /**
     * Draw the aspects
     * @param g2 
     */
    private void drawAspects( SVGGraphics2D g2 ){}

    /**
     * Draw the whole wheel
     * @param g2
     * @throws Exception 
     */
    private void drawWheel( SVGGraphics2D g2 )
            throws IOException
    {
        // Setup the empty ring
        drawHoroRing(g2, new Color(247,247,217), new Color(225,244,249));
        
        // Draw ZWheel
        drawZWheel(g2);
        
        // Draw cusp boundaries and cusp indicators
        drawCuspBounds(g2);

//        // Planets
//        drawPlanets(g2);
//        // Draw aspects
//        drawAspects(g2);

        // Draw centre ring with logo
        drawCentreRing(g2, new Color(133, 193, 233));
    }

    /**
     * [x-shift,y-shift] from [r,theta,shift].  Origin is found in Point2D ringO.
     * Shift amounts (usually height/2,width/2) enable plotting to centre instead
     * of top left.
     * 
     * @param r
     * @param theta
     * @param shiftX Shift X (positive to left, negative to right)
     * @param shiftY Shift Y (positive up, negative down)
     * @return 
     */
    private double[] calcXY( double r, double theta, double shiftX, double shiftY )
    {
        double cx0 = getCx(), cy0 = getCy();
        double xy[] = new double[2];
        
        // Convert r,theta to [x-shift,y-shift]
        xy[0] = cx0+r*Math.cos(theta) - shiftX;
        xy[1] = cy0-r*Math.sin(theta) - shiftY;
        
        return xy;
    }

    /**
     * [x,y] from [r,theta].  Origin is found in Point2D ringO.
     * 
     * @param r
     * @param theta
     * @return 
     */
    private double[] calcXY( double r, double theta )
    {
        return calcXY( r, theta, 0.d, 0.d );
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
     * Mutators
     */
    public double getCx() { return ringO.getX(); }
    public double getCy() { return ringO.getY(); }
    public double getORadius() { return rO; }
    public double getIRadius() { return rI; }
    public double getThick() { return thick; }
    public int getVWidth() { return vwidth; }
    public int getVHeight() { return vheight; }
    public double getOriginX() { return origin.getX(); }
    public double getOriginY() { return origin.getY(); }


//------------------------------------------------------------------------------
//      T E S T S
//------------------------------------------------------------------------------
    /**
     * Coordinate checker
     * @param g2 
     */
    private void drawCoords( Graphics2D g2 )
    {
        // Coord size
        double WID = 8.d, HGT = 8.d;
        // Origin
        double x0 = getOriginX(), y0 = getOriginY();
        // Ring centre
        double cx0 = getCx(), cy0 = getCy();
        // Outer radius
        double r = getORadius();
        
        /*
         * Draw coords and viewbox
         */
        //... Topleft/View Origin
        g2.setPaint(Color.RED);
        g2.setBackground(Color.RED);
        Ellipse2D coord0 = new Ellipse2D.Double(x0, y0, WID, HGT);
        g2.draw(coord0);
        g2.fill(coord0);
        g2.drawString("Orig["+x0+","+y0+"]", (float)(x0-2), (float)(y0-2));
        
        //... BottomRight/View
        g2.setPaint(Color.RED);
        g2.setBackground(Color.RED);
        double brx = getVWidth(), bry = getVHeight();
        Ellipse2D coord8 = new Ellipse2D.Double(brx, bry, WID, HGT);
        g2.draw(coord8);
        g2.fill(coord8);
        g2.drawString("BR["+brx+","+bry+"]", (float)(brx+6), (float)(bry-2));
        
        //...Viewbox
        Path2D.Double vb = new Path2D.Double();
        g2.setStroke(new BasicStroke(0.5f));
        
        vb.moveTo(x0, y0);
        vb.lineTo(brx, y0);
        g2.draw(vb);
        vb.moveTo(brx,y0);
        vb.lineTo(brx,bry);
        g2.draw(vb);
        vb.moveTo(brx, bry);
        vb.lineTo(x0,bry);
        g2.draw(vb);
        vb.moveTo(x0, bry);
        vb.lineTo(x0,y0);
        g2.draw(vb);
        
        //...Ring origin
        g2.setPaint(Color.GREEN);
        g2.setBackground(Color.GREEN);
        Ellipse2D coord1 = new Ellipse2D.Double(cx0, cy0, WID, HGT);
        g2.draw(coord1);
        g2.fill(coord1);
        g2.drawString("R0["+cx0+","+cy0+"]", (float)(cx0-2), (float)(cy0-2));
        
        //...Top of ring
        g2.setPaint(Color.BLUE);
        g2.setBackground(Color.BLUE);
        Ellipse2D coord2 = new Ellipse2D.Double(cx0, cy0-r, WID, HGT);
        g2.draw(coord2);
        g2.fill(coord2);
        g2.drawString("RT", (float)(cx0-2), (float)(cy0-r-2));
        
        //...Right of ring
        g2.setPaint(Color.MAGENTA);
        g2.setBackground(Color.MAGENTA);
        Ellipse2D coord3 = new Ellipse2D.Double(cx0+r, cy0, WID, HGT);
        g2.draw(coord3);
        g2.fill(coord3);
        g2.drawString("RR", (float)(cx0-2+r), (float)(cy0-2));
        
        //...Bottom of ring
        g2.setPaint(Color.CYAN);
        g2.setBackground(Color.CYAN);
        Ellipse2D coord4 = new Ellipse2D.Double(cx0, cy0+r, WID, HGT);
        g2.draw(coord4);
        g2.fill(coord4);
        g2.drawString("RB", (float)(cx0-2), (float)(cy0+r-2));
        
        //...Left of ring
        g2.setPaint(Color.BLACK);
        g2.setBackground(Color.BLACK);
        Ellipse2D coord5 = new Ellipse2D.Double(cx0-r, cy0, WID, HGT);
        g2.draw(coord5);
        g2.fill(coord5);
        g2.drawString("RL", (float)(cx0-2-r), (float)(cy0-2));
        
        //...Thickness
        g2.setPaint(Color.GRAY);
        g2.setBackground(Color.GRAY);
        Ellipse2D coord6 = new Ellipse2D.Double(cx0-r+thick, cy0, WID, HGT);
        g2.draw(coord6);
        g2.fill(coord6);
        g2.drawString("Thk", (float)(cx0-2-r+thick), (float)(cy0-2));
        
        //...Thickness
        g2.setPaint(Color.GRAY);
        g2.setBackground(Color.GRAY);
        Ellipse2D coord7 = new Ellipse2D.Double(cx0+r-thick, cy0, WID, HGT);
        g2.draw(coord7);
        g2.fill(coord7);
        g2.drawString("Thk", (float)(cx0-2+r-thick), (float)(cy0-2));
        
        //...Logo edge
        g2.setPaint(Color.ORANGE);
        g2.setBackground(Color.ORANGE);
        Ellipse2D coord9 = new Ellipse2D.Double(cx0+logor, cy0, WID, HGT);
        g2.draw(coord9);
        g2.fill(coord9);
        g2.drawString("Logo", (float)(cx0-2+logor), (float)(cy0+18));
        
        //...Logo edge
        g2.setPaint(Color.ORANGE);
        g2.setBackground(Color.ORANGE);
        Ellipse2D coorda = new Ellipse2D.Double(cx0-logor, cy0, WID, HGT);
        g2.draw(coorda);
        g2.fill(coorda);
        g2.drawString("Logo", (float)(cx0-2-logor), (float)(cy0-2));
    }

    /**
     * Draw a circle, origin at Point2D ringO
     * @param g2 
     */
    private void drawCircleTest( SVGGraphics2D g2 )
    {
        // Outer radius
        double r = getORadius();
        // Circumference
        Path2D.Double circumf = new Path2D.Double();
        g2.setPaint(Color.GRAY);
        g2.setStroke(new BasicStroke(2));

        // Start here [x0+r,y] (3pm)
        double xy[] = new double[]{getCx()+r, getCy()};
        g2.drawString("S["+xy[0]+","+xy[1]+"]", (float)xy[0], (float)xy[1]+20);
        
        // Draw the circle
        for( int ii=1; ii<=24; ii++ )
        {
            // Start point
            circumf.moveTo(xy[0],xy[1]);
            
            // Draw to
            double theta = (double)ii*Math.PI/12.d;
            xy = calcXY(r,theta);
            circumf.lineTo(xy[0],xy[1]);
            g2.draw(circumf);
        }
    }
    
    /**
     * Tests that calcXY work correctly.
     * @param g2 
     */
    private void drawAnglesTest( SVGGraphics2D g2 )
    {
        double WID = 9.d, HGT = 9.d;
        double angle = Math.PI/8.d;
        
        double xy[] = new double[2];
        Ellipse2D ell;
        
        // Draw cross hairs
        g2.setPaint(Color.LIGHT_GRAY);
        for( int z=0; z<=7; z++ )
        {
            xy = calcXY(3.5d*logor,z*Math.PI/4.d);
            g2.drawLine((int)getCx(), (int)getCy(), (int)xy[0], (int)xy[1]);
        }
        
        // Draw bounding circle
        g2.drawOval((int)(getOriginX()+2.d*logor),(int)(getOriginY()+2.d*logor), (int)(6.d*logor), (int)(6.d*logor));
        

        /*
         * Loop round a circle, display circles and cuspNum images
         */
        // Simple ellipses (although offset, see below)
        g2.setPaint(Color.BLUE);
        for( int z=0; z<16; z++ )
        {
            xy = calcXY(2.d*logor,z*angle,WID/2.d,HGT/2.d);     // Plot to image centre
            ell = new Ellipse2D.Double(xy[0], xy[1], WID, HGT);
            g2.fill(ell);
            g2.draw(ell);
        }
        
        // Should be just below 9pm
        g2.setPaint(Color.RED);
        xy = calcXY(2.d*logor,Math.PI+0.075);
        ell = new Ellipse2D.Double(xy[0], xy[1], WID, HGT);
        g2.fill(ell);
        g2.draw(ell);

        
        // Draw cusp indicators
        // xy needs adjusting otherwise circles and cusps nums are plotted
        // offset as plotting is top left of image.
        double shift = Constants.SCALEDC/2.d;
        for( int z=1; z<=12; z++ )
        {
            ZImage c = cNumMap.get(z);
            
            xy = calcXY(3.d*logor,z*angle, shift, shift);
            g2.drawImage(c.getImage(), (int)xy[0], (int)xy[1], null);
        }
    }
    
    /**
     * Create test image (circled number)
     * @return 
     */
    private Image composeImage()
    {
        int width = 25, height = 25;
        
        BufferedImage bi = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        
        g2.setColor(Color.LIGHT_GRAY);
//        g2.fillOval(0,0, width, height);
        Ellipse2D ell = new Ellipse2D.Double(100, 100, width, height);
        g2.fill(ell);
        g2.draw(ell);
        g2.setColor(Color.GREEN);
        g2.drawString(Integer.toString(12), 5,17);
        
        return bi.getScaledInstance(50,50,Image.SCALE_SMOOTH);
    }
//------------------------------------------------------------------------------

    /*
     * M A I N
     * =======
     */
    public static void main(String[] args)
            throws IOException, WheelGeometryException
    {
        // Initialise ring
        int VIEWW = 800, VIEWH = 800;
        double RADIUS = 250.d, THICK = 75.d, LOGOR = 50.d, OFFX = 100.d, OFFY = 100.d;
        Ring ring = new Ring( VIEWH, VIEWW, new Point2D(OFFX,OFFY), RADIUS, THICK, LOGOR );
        
        // Get all images
        ring.init();
        
        // Set up graphics view
        SVGGraphics2D g2 = new SVGGraphics2D(ring.getVWidth(), ring.getVHeight());
        // Make lines smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Scale the output (depends on device etc.)
        AffineTransform at = new AffineTransform();
        at.scale(0.6, 0.6);
        g2.setTransform(at);
        
        // ------------- TEST -----------------
//        // Geom check
//        ring.drawCoords(g2);
//        ring.drawCircleTest(g2);
//        ring.drawAnglesTest(g2);
//        //--------------------------------------

//        ring.drawHoroRing(g2, new Color(247,247,217), new Color(225,244,249));
//        // Draw ZWheel
//        ring.drawZWheel(g2);
//        ring.drawCuspBounds(g2);
//        // Draw centre ring with logo
//        ring.drawCentreRing(g2, new Color(133, 193, 233));
        ring.drawWheel(g2);  // All of the above

        // Output
        SVGUtils.writeToSVG(new File(Constants.RESOURCEPATH+"tests/ring-test.svg"), g2.getSVGElement());
    }
}

//-----------------------------------Extend Image-------------------------------
class ZImage
{
    private final Image   image;
    private final double  height, width;
    private final Color   colour;

    /**
     * Constructor.
     * 
     * @param image
     * @param height
     * @param width
     * @param colour 
     */
    public ZImage(Image image, double height, double width, Color colour)
    {
        this.image = image;
        this.height = height;
        this.width = width;
        this.colour = colour;
    }

    public Image getImage() { return image; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
    public Color getColour() { return colour; }
}

//-----------------------Test CuspPlusAngle-------------------------------------
class Cpa
{
    private final double    angle;
    private final int       num;
    private final String    name;

    // Borth, Wales
    private static Map<Integer,Cpa> cpaMap0 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Dublin, Ireland
    private static Map<Integer,Cpa> cpaMap1 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Recife, Brazil
    private static Map<Integer,Cpa> cpaMap2 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Gaborone, Botswana
    private static Map<Integer,Cpa> cpaMap3 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.407215,2,"Gem"));
            put(1,new Cpa(0.442845,3,"Can"));
            put(2,new Cpa(0.00491497,5,"Vir"));
            put(3,new Cpa(0.07845,6,"Lib"));
            put(4,new Cpa(0.07615,7,"Sco"));
            put(5,new Cpa(0.52112779,7,"Sco"));
            put(6,new Cpa(0.407215,8,"Sag"));
            put(7,new Cpa(0.442845,9,"Cap"));
            put(8,new Cpa(0.0049149,11,"Pis"));
            put(9,new Cpa(0.07845,0,"Ari"));
            put(10,new Cpa(0.07615475,1,"Tau"));
            put(11,new Cpa(0.52112779,1,"Tau"));
        }};
    
    /**
     * Constructor.
     * 
     * @param angle
     * @param num
     * @param name 
     */
    public Cpa( double angle, int num, String name )
    {
        this.angle = angle;
        this.num = num;
        this.name = name;
    }
    
    public static Map<Integer,Cpa> getCpa() { return cpaMap3; }
    public double getAngle() { return angle; }
    public int getNum() { return num; }
    public String getName() { return name; }
}

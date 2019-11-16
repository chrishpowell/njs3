/*
 * Draw a ring
 */
package eu.discoveri.predikt.graphics;

import eu.discoveri.predikt.astronomy.library.Latitude;
import eu.discoveri.predikt.astronomy.library.Longitude;
import eu.discoveri.predikt.astronomy.library.NoInitException;
import eu.discoveri.predikt.astronomy.library.ObsInfo;
import eu.discoveri.predikt.astronomy.library.PlanetData;

import eu.discoveri.predikt.exception.GeonamesNoResultsException;
import eu.discoveri.predikt.exception.InvalidLocationException;
import eu.discoveri.predikt.exception.RealTimeConversionException;
import eu.discoveri.predikt.exception.WheelGeometryException;
import eu.discoveri.predikt.test.horochart.Aspect;

import eu.discoveri.predikt.test.horochart.ChartType;
import eu.discoveri.predikt.test.horochart.CuspPlusAngle;
import eu.discoveri.predikt.test.horochart.RotateDiff;
import eu.discoveri.predikt.test.horochart.HoroHouse;
import eu.discoveri.predikt.test.horochart.User;
import eu.discoveri.predikt.test.horochart.ZhAttribute;
import eu.discoveri.predikt.test.horochart.ZodiacHouse;

import eu.discoveri.predikt.utils.ZImage;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.ImagesUtil;
import eu.discoveri.predikt.utils.TimeScale;
import eu.discoveri.predikt.utils.Util;
import eu.discoveri.prediktdemobe.Planet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import java.text.DecimalFormat;
import javafx.geometry.Point2D;
import javax.imageio.ImageIO;

/*** Move to Batik? ***/
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;


/**
 * @TODO: Ditch ImageObserver??
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
    private final double    thick,                                  // Thickness of ring
                            arOrigin;                               // Aspect ring 'origin'
    private final double    rO,                                     // Outer ring radius
                            rI,                                     // Inner ring radius
                            rA,                                     // Aspect ring radius
                            rhvn,                                   // Heavens radius
                            logor;                                  // Logo circle radius
    private final Point2D   origin,                                 // [topleftx, toplefty]
                            ringO;                                  // Ring centre
    
    // Ascendant
    private CuspPlusAngle   asc;
    private double          angleAsc;                               // Rotation of zodiac wheel to put ASC at 9pm  @TODO: Remove??

    // Images  @TODO: Make all SVG
    private final Map<ZodiacHouse,ZImage>   zhImgMap = new HashMap<>();         // @TODO: Put in ZodiacHouse
    private final Map<Integer,ZImage>       cnImgMap = new HashMap<>();         // @TODO: Put in CircledNumbers (@TODO: Create)
    private final Map<Aspect,ZImage>        asImgMap = new HashMap<>();         // @TODO: Put in Aspect
    private Map<Planet,ZImage>              plDarkImgMap;
    private Map<Planet,ZImage>              plLiteImgMap;
    
    
    // Planets (initialises all planets)
    private final Map<Integer,Planet>       planets = Planet.getPlanetsByOrder();
    private final Map<String,Planet>        planetNames = Planet.getPlanetsByName();
    
    // Aspects by Planet
    Map<Planet,List<PlanetsAspect>>         planetAspectMap = new TreeMap<>();
    
    // Wheel is night at top (user view at birth)
    private boolean                         darkAtTop = true;


    // --------- TEST: Wheel data ----------------------------------------------
    LocalDateTime       ldt = LocalDateTime.MIN;
    private double      jd;
    private ObsInfo     oi;
    
    // House/Cusp system
    private HoroHouse   hh;
    
    // Output HTML
    private String      htmlHead = "<!DOCTYPE html>\n" +
"<!-- User zodiac -->\n" +
"<html>\n" +
"    <head>\n" +
"        <title>User data</title>\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"        <style> * { font-family: Arial, Helvetica; } </style>" +
"    </head>\n" +
"    <body>\n" +
"        <div style=\"width:98%; padding:10px; margin:20px 10px 30px 10px; font-size: 24px; background-color:#aed6f1; border-bottom: 3px solid black\">\n" +
"            &delta;iscoveri.world\n" +
"        </div>";
    
    private String wheel = "<div style=\"display: inline-block; margin: 0; padding: 0; vertical-align: top; border: 1px solid indigo;\">" +
            "<div style=\"text-align: center; vertical-align: top; background: indigo; color:white; font-size: 25px\">" +
            "<h2>Natal Chart Wheel</h2></div>" +
"            <object class=\"wheel\" type=\"image/svg+xml\" data=\"/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/ring-test.svg\">\n" +
"                Your browser does not support SVG\n" +
"            </object>\n" +
"            </div>";
    
    private String closetablediv = "</tbody></table></div>";
    private String htmlClose = "</div></body></html>";
    
    private StringBuilder usercusps = new StringBuilder("<div style=\"display: inline-block; vertical-align: top; margin-left: 10px;\">\n" +
"                <table style=\"width: 400px; border: 1px solid darkgray\">\n" +
"                    <th style=\"background-color: lightgray; padding: 0; margin: 0\">Houses</th>\n" +
"                    <th style=\"background-color: lightgray; padding: 0; margin: 0\">Wheel</th>\n" +
"                    <th style=\"background-color: lightgray; padding: 0; margin: 0\">Declination</th><tbody>");
    
    // style=\"display: inline-block; vertical-align: top; margin-right:10px;\"
    private StringBuilder userdata = new StringBuilder(
                    "<div><table style=\"width: 350px; border: 1px solid darkgray\">\n" +
                    "<th style=\"background-color: lightgoldenrodyellow; padding: 0; margin: 0\">User</th><tbody>");
    
    private StringBuilder aspectList = new StringBuilder(
                    "<table style=\"border: 1px solid darkgray\">"+
                    "<th colspan=\"2\" style=\"background-color: tomato; padding: 0; margin: 0\">Aspects</th>" +
                    "<th style=\"background-color:tomato; padding: 0 3px 0 3px; margin: 0\">Degrees</th>");
    
    private StringBuilder userplanets = new StringBuilder("<div style=\"margin-top:20px\">" +
"                <table style=\"width: 550px; border: 1px solid darkgray\">\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">Symb</th>\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">Planet</th>\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">House</th>\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">Longitude</th>\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">Latitude</th>\n" +
"                    <th style=\"background-color: lightsteelblue; padding: 0; margin: 0\">Declination</th><tbody>");
    
    // @TODO: Calculate colspan, don't do this in production
    private StringBuilder useraspects = new StringBuilder("<div style=\"margin-top:20px; margin-left:10px\">" +
"                <table style=\"border: 1px solid darkgray\">\n"+
                    "<th style=\"background-color: tomato; padding: 0; margin: 0\">Planet</th>" +
                    "<th colspan=\"7\" style=\"background-color: tomato; padding: 0; margin: 0;\">Aspects</th><tbody>");
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
        // Aspect ring 'origin'
        this.arOrigin = thick + Constants.ASPOUTER;
        // Heavens radius
        this.rhvn = radius + Constants.SKYRAD;

        // Where the view origin in relation to ring origin
        this.vheight = viewboxHeight;
        this.vwidth = viewboxWidth;

        // Set origin and wheel/ring centre
        this.origin = origin;
        this.ringO = new Point2D(topleftX+radius,topleftY+radius);
        
        // Set inner ring radius
        this.rI = rO - thick;
        // Set Aspect Ring radius
        this.rA = rI - Constants.ASPOUTER;
        
        // Logo inner circle
        this.logor = logor;
    }
    
    /**
     * Load images etc.
     * @throws IOException 
     */
    private void systemInit()
            throws IOException
    {
        String zodiacImagePath = Constants.RESOURCEPATH+"zodiac/";
        String aspectImagePath = Constants.RESOURCEPATH+"aspects/";
        
        System.out.println("discoveri/predikt (part) initialisation...");
        //----------------------------------------------------------------------
        // Should be in an external system init
        System.out.println("Loading zodiac images...");
        Map<ZodiacHouse,Integer> zh = ZodiacHouse.getZHNameMap();

        // Load zodiac images (@TODO: Move to Zodiac House)
        for( ZodiacHouse house: zh.keySet() )
        {
            // Read image from file
            System.out.print(".");
            BufferedImage sign = ImageIO.read(new File(zodiacImagePath+house.getName().toLowerCase()+".png"));

            // Store the image with some metadata
//            Image image = sign.getScaledInstance(Constants.SCALEDZ,Constants.SCALEDZ,Image.SCALE_SMOOTH);
            ZImage zimage = new ZImage( sign, Constants.SCALEDZ, Constants.SCALEDZ, Color.BLACK );
            System.out.println("..>" +house.getName());
            zimage.scalePreserveRatio(5,zimage);
            System.out.println("--ZH--> Wid/Hgt: "+zimage.getWidth()+"/"+zimage.getHeight());

            zhImgMap.put(house, zimage);
        }
        System.out.println("\r\nLoaded zodiac images...");
        
        // Circled cusp numbers (@TODO: Move to new class)
        System.out.println("Loading circled numbers...");
        for( int ii = 1; ii <= 12; ii++ )
        {
            System.out.print(".");
            BufferedImage cusp = ImageIO.read(new File(zodiacImagePath+"/c"+ii+".png"));

            // Store the image with some metadata
//            Image image = cusp.getScaledInstance(Constants.SCALEDC,Constants.SCALEDC,Image.SCALE_SMOOTH);
            ZImage zimage = new ZImage( cusp, Constants.SCALEDC, Constants.SCALEDC, Color.yellow );
            zimage.scalePreserveRatio(5,zimage);
            System.out.println("--CN--> ZHt/ZWd: " +zimage.getHeight()+"/"+zimage.getWidth()+ ", Img> Ht/Wid: " +zimage.getImage().getHeight()+"/"+zimage.getImage().getWidth());
            
            cnImgMap.put(ii, zimage);
        }
        System.out.println("\r\nLoaded circled numbers...");
        
        // Aspect images (@TODO: move to Aspect)
        System.out.println("\r\nLoading aspect images...");
        Map<String,Aspect> asp = Aspect.aspectsFactory();
        for( String aspectName: asp.keySet() )
        {
            System.out.print(".");
            BufferedImage aspect = ImageIO.read(new File(aspectImagePath+aspectName+".png"));
            
            // Store image with some metadata
//            Image image = aspect.getScaledInstance(Constants.SCALEDA,Constants.SCALEDA,Image.SCALE_SMOOTH);
            ZImage zimage = new ZImage( aspect, Constants.SCALEDA, Constants.SCALEDA, Color.BLACK );
            
            asImgMap.put(asp.get(aspectName), zimage);
        }
        System.out.println("\r\nLoaded aspect images...");
        
        // Planet images
        System.out.println("\r\nLoading planet images...");
        plDarkImgMap = Planet.getDarkImages();
        plLiteImgMap = Planet.getLightImages();
        System.out.println("\r\nLoaded planet images...");
        //----------------------------------------------------------------------
    }
    
    /**
     * Set up user
     */
    private HoroHouse init( User user )
            throws IOException, GeonamesNoResultsException, RealTimeConversionException
    {
        System.out.println("*** User init...");
        
        System.out.println("Init House/Cusps subsystem...");
        hh = new HoroHouse(user).init();                                        // Initialise with user etc.
        hh.setWheel(user.getCt());                                              // Set wheel type (default PLACIDUS)
        System.out.println("House/Cusp system initialised");
        
        System.out.println("Observation info set...");
        // User Observation info
        jd = TimeScale.julianDayTimeClassic(ldt);
        System.out.println("ldt: " +ldt.toString()+ ", jd: " +jd);
        oi = new ObsInfo(new Latitude(hh.getLatLon().getLatitude()), new Longitude(hh.getLatLon().getLongitude()));
        System.out.println("Observation info done...");
        
        System.out.println("Updating user location from place (LatLon)...");
        if( user.isFirsttime() )
            user.setPlace(hh.getLatLon());
        System.out.println("User location updated...");
        
        System.out.println("***User init complete...");
        
        return hh;
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
        Image logo = blogo.getScaledInstance(Constants.SCALEDL,Constants.SCALEDL,Image.SCALE_SMOOTH);
        g2.drawImage( logo, (int)(11.d+topleftX+rO-logor/2.d), (int)(11.d+topleftY+rO-logor/2.d), null );
    }

    /**
     * Draw the planets (and Sun and Moon etc.)
     * 
     * @param g2
     * @throws IOException
     * @throws RealTimeConversionException
     * @throws NoInitException  @TODO: GET RID OF THIS NONSENSE!
     * @throws InvalidLocationException
     */
    public void drawPlanets( SVGGraphics2D g2 )
            throws IOException, RealTimeConversionException, NoInitException, InvalidLocationException
    {
        // @TODO: Load planet data  ***** This all has to change *****
        for( Map.Entry<Integer,Planet> p: planets.entrySet() )
        {
            // Get planet posn. (Ecliptic longitude)
            PlanetData pde = new PlanetData(p.getKey(), jd, oi);
            Planet planet = p.getValue();
            //-----------
//            System.out.println(""+Planets.getPlanetmap().get(pde.planet()));
//            System.out.println("  RA: " +Util.dec2hhmmssRA(Math.toDegrees(pde.getRightAscension())));
//            System.out.println("  RA (degs): " +Math.toDegrees(pde.getRightAscension()));
//            System.out.println("  Decl: " +Util.dec2ddmmss(Math.toDegrees(pde.getDeclination()),Constants.LAT));
            //-----------
            planet.setRA(pde.getRightAscension());
            planet.setDeclination(pde.getDeclination());
            planet.setEclipticCoords(pde.getEclipticLat(), pde.getEclipticLon());
        }
        
        // See if any overlap, move one planet out if so.
        planets.entrySet().forEach((entry) -> {
            planets.entrySet().stream().filter((entry2) -> !( (entry2.getValue().getOrder() == entry.getValue().getOrder()))
                                                && entry.getValue().forDisplay() && entry2.getValue().forDisplay() ).map((entry2) -> {
                // Planets on top of each other?
                return entry2;
            }).filter((entry2) -> ( Math.abs(entry2.getValue().getRADegrees() - entry.getValue().getRADegrees()) < Constants.POVERLAP &&
                    entry2.getValue().getOffset() == entry.getValue().getOffset()  )).map((_item) -> {
                        entry.getValue().setOffset(entry.getValue().getOffset()+Constants.POFFSET);
                return _item;
            }).forEachOrdered((_item) -> {
                //System.out.println("....> "+entry.getValue().getName()+": "+entry.getValue().getOffset());
            });
        });
        
        for( Map.Entry<Integer,Planet> entry: planets.entrySet() )
        {
            // Draw planet
            drawPlanet( g2, entry.getValue() );
        }
    }
  

    /**
     * Draw a planet
     * @param g2
     * @param p
     * @throws IOException 
     */
    private void drawPlanet( SVGGraphics2D g2, Planet p )
            throws IOException
    {
        // Not to be displayed (such as NAP)
        if( !p.forDisplay() ) return;
        
        // PI/2 as zodiac planets measured from 12am not 3pm plus
        //    net ASC rotation (PI/6 - asc angle)
        // PI/2 + Pi/6 = 2PI/3
        double rotate = (2*Math.PI/3.d) - angleAsc;
        double planetPosnRads = p.getRA()+rotate;

        // Calculate the planet radial position given a radial offset (rO+x)
        double xy[] = calcXY( rO+p.getOffset(), planetPosnRads, Constants.SCALEDP/2.d, Constants.SCALEDP/2.d );
        System.out.println("  Planet posn: " +p.getName()+"> "+planetPosnRads);
        
        // Planet
        // @TODO: ************ For all planets
        // @TODO: Load all planet images at start
        ZImage body;
        if( darkAtTop )
            // Weird positioning!  [Wheel anti-clock: 0.0 to 2*Math.PI/3 AND then clock: 0 to -Math.PI/2]
            if( planetPosnRads > 0.d && planetPosnRads < Math.PI )
                body = plLiteImgMap.get(p);
            else
                body = plDarkImgMap.get(p);
        else
            if( planetPosnRads > 0.d && planetPosnRads < Math.PI )
                body = plDarkImgMap.get(p);
            else
                body = plLiteImgMap.get(p);

        // Draw the body
        g2.drawImage( body.getImage(), (int)xy[0], (int)xy[1], null );
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
        
        // ***** TEST ******
        // ASC house and angle in house
        asc = hh.getCPAMap().get(ZhAttribute.ASCN);
        
        // ASC house and angle in house
        angleAsc = asc.getAngle();
        int houseAsc = asc.getHouseNum();

        
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
            ZImage zimage = zhImgMap.get(zhMap.get(house));                      // Get image
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
        CuspPlusAngle asc = hh.getCPAMap().get(ZhAttribute.ASCN);

        double xy[] = calcXY(rO+28,Math.PI-0.025);                  // **** @TODO Tidy up!! [28, 0.025...]
        g2.drawString("ASC", (float)xy[0], (float)xy[1]);
        
        // Angle: Degrees
        int[] dm = Util.dec2ddmmRaw(Math.toDegrees(asc.getAngle()));
        xy = calcXY(rO+32.d,Math.PI+0.05);                          // **** @TODO: Tidy up !!!
        g2.drawString(Integer.toString(dm[0]), (float)xy[0], (float)xy[1]);
        // Angle: Minutes
        g2.setFont(font.deriveFont((float)(fsiz*0.8)));
        g2.drawString(Integer.toString(dm[1]), (float)(xy[0]+16.d), (float)(xy[1]-4.d));
        g2.setFont(font.deriveFont(fsiz));
        
        // Get DSC
        CuspPlusAngle dsc = hh.getCPAMap().get(ZhAttribute.DSCN);
        
        xy = calcXY(rO+2,-0.05);
        g2.drawString("DSC", (float)xy[0], (float)xy[1]);
        
        // Angle: Degrees
        dm = Util.dec2ddmmRaw(Math.toDegrees(dsc.getAngle()));
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
        CuspPlusAngle mc = hh.getCPAMap().get(ZhAttribute.MCN);

        // 'Negative' difference between houses
        int negHouseRot =  Constants.NUMHOUSESCLASSIC - (mc.getHouseNum()-asc.getHouseNum());
        
        // Rotation (clock) (Pi minus, not PI plus):
        //  PI - (house difference * 30degs) -
        //           (ASC angle in house) - (30degs - (MC angle in house))
        double theta = rotateOverWheel(negHouseRot); 
        theta -= asc.getAngle() + (Math.PI/6.d - mc.getAngle());
//        System.out.println("MC (degs)..> " +Math.toDegrees(mc.getAngle()));
        
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
                int z = 3*za-2+zb +1;  //******* +1

                // Cusp
                CuspPlusAngle cz = hh.getCPAMap().get(z);

                // 'Negative' difference between houses
                negHouseRot =  Constants.NUMHOUSESCLASSIC - (cz.getHouseNum()-asc.getHouseNum());

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
                drawCuspNum( g2, z, theta );
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
        double cuspR = rI-Constants.CNRADIUS;
        
        ZImage zi = cnImgMap.get(cuspNum);
        double xy[] = calcXY( cuspR,theta, zi.getImage().getHeight()/2.d,zi.getImage().getWidth()/2.d);
        g2.drawImage(zi.getImage(), (int)xy[0], (int)xy[1], null);
    }
    
        
    /**
     * Draw background (light/dark depending on time of birth)
     * @param g2 
     */
    private void drawHeavens( SVGGraphics2D g2 )
    {
        // Cartesian origin
        // @TODO: Fix all origins...
        double topleftX = getOriginX()-Constants.SKYRAD, topleftY = getOriginY()-Constants.SKYRAD;
        
        // Gradients and stroke 8,17,165
        GradientPaint d2l = new GradientPaint((float)(topleftX+rhvn),(float)(topleftY),new Color(11,0,112), (float)(topleftX+rhvn),(float)(topleftY+rhvn*1.66d),new Color(220,241,248));
        GradientPaint l2d = new GradientPaint((float)(topleftX+rhvn),(float)(topleftY),new Color(220,241,248), (float)(topleftX+rhvn),(float)(topleftY+rhvn*2.34d),new Color(11,0,112));
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        
        // Draw
        Ellipse2D ell = new Ellipse2D.Double(topleftX, topleftY, 2*rhvn, 2*rhvn);
        g2.draw(ell);
        
        // Night
        if( (ldt.getHour() > 18 && ldt.getHour() <= 24) || (ldt.getHour() >= 0 && ldt.getHour() < 6) )
        {
            darkAtTop = true;       // Let planets know night at top (user view at birth)
            g2.setPaint(d2l);
        }
        else
        {
            darkAtTop = false;      // Let planets know day time at top (user view at birth)
            g2.setPaint(l2d);
        }
        
        // Display
        g2.fill(ell);
    }
    
    
    /**
     * Aspect nodes touch this ring
     * @param g2 
     * @param arColor
     */
    public void drawAspectRing( SVGGraphics2D g2, Color arColor )
    {
        // Origin
        double topleftX = origin.getX(), topleftY = origin.getY();
        
        // Docs, btw, are confusing!  The args are: [x: top-left, y: top-left] (framing rect); major-axis diameter, minor-axis diameter
        Ellipse2D aspectCircle = new Ellipse2D.Double(topleftX+arOrigin, topleftY+arOrigin, 2*rA, 2*rA);
        
        g2.setPaint(arColor);
        g2.draw(aspectCircle);

        // **** TEST aspect @TODO Remove
        
    }
    
    /**
     * Calculate the planets' aspects.
     * @see planetAspectMap
     */
    private void calculateAspects()
    {
        // Aspect types
        Map<String,Aspect> asp = Aspect.aspectsFactory();
        // Planet aspects for this user
        Map<P1P2Key,PlanetsAspect> psa = PlanetsAspect.getPsaMap();
        
        /*
         * Check for (major) aspects
         */
        // Planets
        planets.entrySet().forEach((p1) -> {
            planets.entrySet().forEach((p2) -> {
                // Don't compare same planet against itself; 
                // AND ignore display==false either planet;
                if( !p1.equals(p2) &&
                    (p1.getValue().forDisplay() && p2.getValue().forDisplay()) )
                {
                    // Get planet names
                    Planet planet1 = p1.getValue();
                    Planet planet2 = p2.getValue();
                    
                    //... If a map entry, don't redo (eg: Sun:Moon/Moon:Sun);
                    if( !PlanetsAspect.checkFlippedKey(planet1,planet2) )
                    {
                        asp.entrySet().forEach((aspect) -> {
                            // Rotational difference between planets
                            double dd = Math.abs(p1.getValue().getRADegrees() - p2.getValue().getRADegrees());
                            // Num. degrees aspect range: 'orb'
                            RotateDiff orb = aspect.getValue().getDegreesDiff();
                            // Is the rot. difference inside the 'orb'
                            if( dd >= (double)(orb.getDegreesDiffExact() - orb.getDegreesDiffRange()) &&
                                dd <= (double)(orb.getDegreesDiffExact() + orb.getDegreesDiffRange())   )
                            {
                                psa.put(new P1P2Key(planet1,planet2),new PlanetsAspect(p1.getValue(),p2.getValue(),aspect.getValue(),orb.getDegreesDiffExact()-dd));
                            }
                        });
                    }
                }
            });
        });
        
        // Ascendant/Midheaven
        double mcDegs = Math.toDegrees(hh.getCPAMap().get(ZhAttribute.MCN).getAngle()); // Midheaven degrees
        
        planets.entrySet().forEach((p) -> {
            // Don't compare same planet against itself; 
            // AND ignore display==false either planet;
            if( p.getValue().forDisplay() )
            {
                // Get planet names
                Planet p1 = p.getValue();

                // Ascendant
                asp.entrySet().forEach((aspect) -> {
                    // Rotational difference between planets
                    double dd = Math.abs(p.getValue().getRADegrees() - Constants.NINETYDEGREES);

                    // Num. degrees aspect range: 'orb'
                    RotateDiff orb = aspect.getValue().getDegreesDiff();
                    
                    // Is the rot. difference inside the 'orb'
                    if( dd >= (double)(orb.getDegreesDiffExact() - orb.getDegreesDiffRange()) &&
                        dd <= (double)(orb.getDegreesDiffExact() + orb.getDegreesDiffRange())   )
                    {
                        psa.put(new P1P2Key(p1,planetNames.get(Planet.AC)),new PlanetsAspect(p.getValue(),planetNames.get(Planet.AC),aspect.getValue(),orb.getDegreesDiffExact()-dd));
                    }
                });
                
                // Midheaven
                asp.entrySet().forEach((aspect) -> {
                    // Rotational difference between planets
                    double dd = Math.abs(p.getValue().getRADegrees() - mcDegs);
                    // Num. degrees aspect range: 'orb'
                    RotateDiff orb = aspect.getValue().getDegreesDiff();
                    // Is the rot. difference inside the 'orb'
                    if( dd >= (double)(orb.getDegreesDiffExact() - orb.getDegreesDiffRange()) &&
                        dd <= (double)(orb.getDegreesDiffExact() + orb.getDegreesDiffRange())   )
                    {
                        psa.put(new P1P2Key(p1,planetNames.get(Planet.MC)),new PlanetsAspect(p.getValue(),planetNames.get(Planet.MC),aspect.getValue(),orb.getDegreesDiffExact()-dd));
                    }
                });
            }
        });
        
        /*
         * Dump map @TODO: Remove
         */
//        DecimalFormat dfs = new DecimalFormat("#.##");
//        psa.values().stream()
//            // Group and sort groups (TreeMap) by Order
//            .collect(Collectors.groupingBy(PlanetsAspect::getP1, TreeMap::new, Collectors.toList()))
//            // Dump data
//            .forEach((k,v)-> {
//                System.out.print(k.getName()+"("+k.getOrder()+"):");
//                v.forEach(a->System.out.print(" {"+a.getAspect().getName()+", "+a.getP2().getName()+ " ("+dfs.format(a.getDegsDiff())+")}"));
//                System.out.println("");
//            });
        planetAspectMap = psa.values().stream()
            // Group and sort groups (TreeMap) by Planet Order
            .collect(Collectors.groupingBy(PlanetsAspect::getP1, TreeMap::new, Collectors.toList()));
    }

    
    /**
     * Draw the aspects on Wheel.
     * 
     * @param g2 
     */
    private void drawAspects( SVGGraphics2D g2 )
    {
        // Calculate the aspect map
        calculateAspects();
        
        // ***** @TODO: Make rotate global
        // PI/2 as zodiac planets measured from 12am not 3pm plus
        //    net ASC rotation (PI/6 - asc angle)
        // PI/2 + Pi/6 = 2PI/3
        double rotate = (2*Math.PI/3.d) - angleAsc;
        // Used to route around both sides of chart wheel centre
        boolean add[] = {true};
            
        g2.setColor(Color.DARK_GRAY);
        planetAspectMap.forEach((k,v) -> {
            double p1RPosn = k.getRA() + rotate;
            // List of PlanetsAspect
            v.forEach(a -> {
                if( !a.getP2().getName().equals(Planet.AC) && !a.getP2().getName().equals(Planet.MC) )
                {
                   if( !a.getAspect().getName().equals(Constants.CONJUNCTION) )
                {
                    // New path
                    Path2D p2d = new Path2D.Double();
                    switch( a.getAspect().getName() )
                    {
                        default: g2.setColor(Color.RED);
                        break;
                        case "trine": g2.setColor(Constants.TRINEC);
                        break;
                        case "square": g2.setColor(Constants.SQUAREC);
                        break;
                        case "sextile": g2.setColor(Constants.SEXTILEC);
                        break;
                        case "opposition": g2.setColor(Constants.OPPOSITIONC);
                        break;
                        // Should not appear!
                        case "conjunction": g2.setColor(Color.WHITE);
                        break;
                    }

                    // Whither bound?
                    double p2RPosn = a.getP2().getRA() + rotate;

                    // Start point
                    double xy1[] = calcXY(rA,p1RPosn);
                    p2d.moveTo(xy1[0], xy1[1]);
                    // Intermediate point
                    double xyi[] = calcXY(rA*0.6d,(add[0]?(p1RPosn+p2RPosn)/2.d:(p1RPosn-p2RPosn)/2.d));
//                    double xyi[] = calcXY(rA*0.6,(p1RPosn+p2RPosn)/2.d);
                    add[0] = !add[0];
                    
                    // End point
                    double xy2[] = calcXY(rA,p2RPosn);

                    // Draw it
                    p2d.curveTo(xyi[0],xyi[1], xyi[0],xyi[1], xy2[0],xy2[1]);
                    //p2d.closePath(); -- Note this closes the path (into a loop), we don't want that
                    g2.draw(p2d);
                }}
            });
        });
    }

    
    /**
     * --------------------
     * Draw the whole wheel
     * --------------------
     * 
     * @param g2
     * @throws Exception 
     */
    private void drawWheel( SVGGraphics2D g2 )
            throws IOException, RealTimeConversionException, NoInitException, InvalidLocationException
    {
        // Draw heavens representation
        drawHeavens(g2);
        
        // Setup the empty ring
        drawHoroRing(g2, new Color(247,247,217), new Color(225,244,249));
        
        // Draw ZWheel
        drawZWheel(g2);
        
        // Draw cusp boundaries and cusp indicators
        drawCuspBounds(g2);

//        // Planets
        drawPlanets(g2);
//        // Draw aspects
        drawAspectRing(g2, Color.LIGHT_GRAY);
        drawAspects(g2);

        // Draw centre ring with logo
        drawCentreRing(g2, new Color(133, 193, 233));
    }

    /**
     * [x-shift,y-shift] from [r,theta,shift].  Origin is found in Point2D ringO.
     * Shift amounts (usually height/2,width/2) enable plotting to centre instead
     * of top left.
     * 
     * @param r radius
     * @param theta Starts at North (up)?
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
     * @param r radius
     * @param theta Starts at North (Up)?
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
     * Coordinate checker.
     * Draws small circles at specific coords.
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
            ZImage c = cnImgMap.get(z);
            
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
    
    /*
     * Pump out test HTML
     */
    private void outHTML()
            throws IOException
    {
        String HTMLFILE = "/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/eu/discoveri/predikt/html/userdata.html";
        String houseNum;
        
        // User data
        User u = hh.getUser();
        userdata.append("<tr><td>").append(u.getName()).append("</td></tr>");
        userdata.append("<tr><td>"+u.getBirthLDT().toLocalDate().toString()+" "+u.getBirthLDT().toLocalTime().toString()+"</td></tr>");
        userdata.append("<tr><td>"+u.getBirthPlace()+"</td></tr>");
        userdata.append("<tr><td>Sun sign: "+"Libra"+"</td></tr><tr><td>Ascendant: "+angleAsc+"</td></tr>");
        userdata.append("<tr><td>"+Util.dec2ddmmss(oi.getLatitudeDeg(),true)+" : "+Util.dec2ddmmss(oi.getLongitudeDeg(),false)+"</td></tr>");
        userdata.append(closetablediv);
        
        // Aspects
        Aspect.aspectsFactory().entrySet().forEach(k -> {
            // Get colour of this Aspect
            String aColor = "#"+Integer.toHexString(k.getValue().getAspectColor().getRGB()).substring(2);
            // To HTML
            aspectList.append("<tr><td><img src='"+k.getValue().getFileImage()+".png' width='18' height='18'></td><td style=\"padding-left:5px; color:"+aColor+"\">"+k.getValue().getName()+"</td><td style=\"text-align:right\">"+k.getValue().getDegreesDiff().getDegreesDiffExact()+"</td></tr>");
        });
        aspectList.append(closetablediv);
        
        // User cusps
        for( Map.Entry<Integer,CuspPlusAngle> entry: hh.getCPAMap().entrySet() )
        {
            CuspPlusAngle e = entry.getValue();
            switch( entry.getKey() )
            {
                default:
                    houseNum = ""+entry.getKey(); break;
                case ZhAttribute.ASCN:
                    houseNum = "Asc."; break;
                case ZhAttribute.ICN:
                    houseNum = "IC"; break;
                case ZhAttribute.DSCN:
                    houseNum = "Desc."; break;
                case ZhAttribute.MCN:
                    houseNum = "MC"; break;
            }
            usercusps.append("<tr><td>"+houseNum+"</td><td>"+Util.houseDec2ddmmss(e.getName(),Math.toDegrees(e.getAngle()))+"</td><td>"+Util.dec2ddmmss(Math.toDegrees(e.getDecl()),Constants.LAT)+"</td></tr>");
        }
        usercusps.append(closetablediv);
        
        // User planets
        for( Map.Entry<Integer,Planet> entry: planets.entrySet() )
        {
            Planet p = entry.getValue();
            if( !p.forDisplay() ) continue;  // Non-displayable
            
            userplanets.append( "<td style=\"font-size:25px\">"+p.getSymbol()+"</td><td>"+p.getName()+"</td><td>"+99+
                                "</td><td>"+Util.dec2ddmmss(p.getEclipticCoords().getLongitude(),Constants.LON)+"</td><td>"+Util.dec2ddmmss(p.getEclipticCoords().getLatitude(),Constants.LAT)+"</td><td>"+Util.dec2ddmmss(p.getDeclDegrees(),Constants.LAT)+"</td></tr>");
        }
        userplanets.append(closetablediv);
        
        // Planet aspects <Planet,Aspect>
        DecimalFormat dfs = new DecimalFormat("#.##");
        for( Map.Entry<Planet,List<PlanetsAspect>> entry: planetAspectMap.entrySet() )
        {
            Planet k = entry.getKey();
            List<PlanetsAspect> v = entry.getValue();

            // To HTML table
            useraspects.append("<tr><td width=\"75\"><img src='"+ImagesUtil.image2HTML((BufferedImage)plDarkImgMap.get(k).getImage())+"' height='25' width='20'></td>");
            v.forEach(a -> {
                // Get colour of this Aspect
                String aColor = "#"+Integer.toHexString(a.getAspect().getAspectColor().getRGB()).substring(2);
                String bgColor = "";
                if( a.getP2().getName().equalsIgnoreCase(Planet.AC) || a.getP2().getName().equalsIgnoreCase(Planet.MC) )
                {
                    aColor = "darkgray";
                    bgColor = "#f2f3f4";
                }
                String divBorder = "solid";
                if( a.getAspect().getName().equalsIgnoreCase(Constants.CONJUNCTION)) divBorder = "dotted";
                
                // Output Aspects table
                useraspects.append("<td width=\"44\" style=\"font-size:12px\"><div style=\"border: 1px "+divBorder+" "+aColor+"; background-color:"+bgColor+"; padding:2px; width:40px; height:33px\"><div><img src='"+a.getAspect().getFileImage()+".png' height='15' width='15'> "+a.getP2().getSymbol()+"</div><div style=\"text-align:center\">"+dfs.format(a.getDegsDiff())+"\u00b0</div></div></td>");
            });
        }
        useraspects.append("</tr>");
        useraspects.append(closetablediv);
        
        // Write out user stuff
        try( BufferedWriter writer = new BufferedWriter(new FileWriter(HTMLFILE)) )
        {
            writer.write(htmlHead);
            writer.write("<div id='A' style=\"float:left; margin-left:10px\">");
            writer.write("<div id='B'>");
            writer.write("<div id='X' style=\"float:left; margin-bottom:10px\">");
            writer.write(userdata.toString());
            writer.write("</div><div id='Y' style=\"float:left; margin-left:10px\">");
            writer.write(aspectList.toString());
            writer.write("</div>");
            writer.write(userplanets.toString());
            writer.write("</div>");
            writer.write("<div id='C' style=\"float:left; margin-left:10px\">");
            writer.write(wheel);
            writer.write("</div>");
            writer.write("<div id='D' style=\"display: inline-block; vertical-align: top; margin-right:10px;\">");
            writer.write(usercusps.toString());
            writer.write(useraspects.toString());
            writer.write("</div>");
            writer.write(htmlClose);
        }
    }
//------------------------------------------------------------------------------

    /*
     * M A I N
     * =======
     */
    public static void main(String[] args)
            throws IOException, WheelGeometryException, RealTimeConversionException, NoInitException, GeonamesNoResultsException, InvalidLocationException
    {
        // Initialise ring
        int VIEWW = 600, VIEWH = 600;
        double RADIUS = 250.d, THICK = 75.d, LOGOR = 50.d, OFFX = 100.d, OFFY = 100.d;
        Ring ring = new Ring( VIEWH, VIEWW, new Point2D(OFFX,OFFY), RADIUS, THICK, LOGOR );
        
        // Initialise House/Cusp system
        // **** Test data ****
        // User
        System.out.println("Initialise House/Cusp and Ring system...");
        String name = "Fred Bloggs";
        String placeName = "Gaborone, Botswana";
        ring.ldt = LocalDateTime.of(LocalDate.of(1966,9,30), LocalTime.of(0,0));
        User user = new User(name,placeName,ring.ldt,ChartType.PLACIDUS);

        // Init ring (incl. HoroHouse, location[LatLon] and ObsInfo) and get all images
        ring.systemInit();                                  // Should be in startup init
        ring.init(user);

        // Set up graphics view
        SVGGraphics2D g2 = new SVGGraphics2D(ring.getVWidth(), ring.getVHeight());
        // Make lines smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Scale the output (depends on device etc.)
        AffineTransform at = new AffineTransform();
        at.scale(0.8, 0.8);
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

        // All of sections of ring
        ring.drawWheel(g2);
        // Output the userdata HTML
        ring.outHTML();

        // Output
        SVGUtils.writeToSVG(new File(Constants.RESOURCEPATH+"tests/ring-test.svg"), g2.getSVGElement());

        // Close g2
        g2.dispose();
    }
}

//------------------------------------------------------------------------------
class PlanetsAspect// implements Comparable<Planet>
{
    private final Planet  p1, p2;
    private final Aspect  aspect;
    private final double  degsDiff;

    /**
     * Constructor.
     * 
     * @param p1
     * @param p2
     * @param aspect 
     */
    public PlanetsAspect(Planet p1, Planet p2, Aspect aspect, double degsDiff)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.aspect = aspect;
        this.degsDiff = degsDiff;
    }

    /*
     * Getters
     */
    public Planet getP1() { return p1; }
    public Planet getP2() { return p2; }
    public Aspect getAspect() { return aspect; }
    public double getDegsDiff() { return degsDiff; }
    
    
    // Map to this
    private final static Map<P1P2Key, PlanetsAspect> psaMap = new TreeMap<>();  // Order by P1 then P2
    public static Map<P1P2Key,PlanetsAspect> getPsaMap() { return psaMap; }
    public static void dumpPsaMap()
    {
        if( psaMap.isEmpty() ) return;
        psaMap.forEach((k,v) -> {
            System.out.println("psaMap..> " +k+":"+v);
        });
    }
    
    /*
     * Map psaMap to Map by order of P1(index) (from Planet)
     */
    public static List<PlanetsAspect> psaList()
    {
        List<PlanetsAspect> psaList = new ArrayList<>(psaMap.values());
        
        Collections.sort(psaList, new PsAByP1());
        return psaList;
    }
    
    /**
     * Check key - and flipped key.
     * @param p1
     * @param p2
     * @return 
     */
    public static boolean checkFlippedKey( Planet p1, Planet p2 )
    {
        return psaMap.containsKey(new P1P2Key(p1,p2)) || psaMap.containsKey(new P1P2Key(p2,p1) ); 
    }
    
    @Override
    public String toString()
    {
        DecimalFormat dfs = new DecimalFormat("#.##");
        return p1.getName()+":"+p2.getName()+" "+aspect.getName()+" "+dfs.format(degsDiff);
    }
}

//------------------------------------------------------------------------------
class PsAByP1 implements Comparator<PlanetsAspect>
{
    @Override
    public int compare(PlanetsAspect a, PlanetsAspect b)
    {
        return a.getP1().getOrder() - b.getP1().getOrder();
    }
}

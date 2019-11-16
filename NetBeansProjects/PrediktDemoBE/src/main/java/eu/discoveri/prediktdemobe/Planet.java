/*
 * Planet characteristics
 */
package eu.discoveri.prediktdemobe;

import eu.discoveri.predikt.exception.InvalidLocationException;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.ImagesUtil;
import eu.discoveri.predikt.utils.LatLon;
import eu.discoveri.predikt.utils.ZImage;
import java.awt.Color;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Planet implements Comparable<Planet>
{
    // For display
    private double              raDegrees = -180.d,
                                ra = 0.d,
                                latDegrees = 0.d,
                                lat = 0.d,
                                declination = 0.d,
                                declDegrees = 0.d,
                                offset = Constants.POFFSET;
    private LatLon              eclipticCoords;
    // Basic attributes
    private final int           order;
    private final String        name,
                                symbol,
                                filedark,
                                filelight;
    private final boolean       isBody,
                                forDisplay;
    // List of planets
    public static final String  SUN     = "Sun",
                                LUNA    = "Moon",
                                MERCURY = "Mercury",
                                VENUS   = "Venus",
                                EARTH   = "Earth",
                                MARS    = "Mars",
                                JUPITER = "Jupiter",
                                SATURN  = "Saturn",
                                URANUS  = "Uranus",
                                NEPTUNE = "Neptune",
                                PLUTO   = "Pluto",
                                PLANETOID = "Plantoid",
                                NAP     = "Not a planet",
                                AC      = "Ascendant",
                                MC      = "Midheaven";
    
    private final static String               svgImagePath = Constants.RESOURCEPATH+"planets/";
    private final static Map<String,Planet>   planetsByName = new HashMap<>();
    private final static Map<Integer,Planet>  planetsByOrder = new TreeMap<>();
    
    // Initialise all planets
    private final static List<Planet>         planetList = new ArrayList<Planet>() {{
        add(new Planet(0,SUN,"☉",svgImagePath+"sun-dark.png",svgImagePath+"sun-light.png"));
        add(new Planet(1,LUNA,"☾",svgImagePath+"moon-dark.png",svgImagePath+"moon-light.png"));
        add(new Planet(2,MERCURY,"☿",svgImagePath+"mercury-dark.png",svgImagePath+"mercury-light.png"));
        add(new Planet(3,VENUS,"♀",svgImagePath+"venus-dark.png",svgImagePath+"venus-light.png"));
        add(new Planet(4,MARS,"♂",svgImagePath+"mars-dark.png",svgImagePath+"mars-light.png"));
        add(new Planet(5,JUPITER,"♃",svgImagePath+"jupiter-dark.png",svgImagePath+"jupiter-light.png"));
        add(new Planet(6,SATURN,"♄",svgImagePath+"saturn-dark.png",svgImagePath+"saturn-light.png"));
        add(new Planet(7,URANUS,"⛢",svgImagePath+"uranus-dark.png",svgImagePath+"uranus-light.png"));
        add(new Planet(8,NEPTUNE,"♆",svgImagePath+"neptune-dark.png",svgImagePath+"neptune-light.png"));
        add(new Planet(9,PLUTO,"♇",svgImagePath+"pluto-dark.png",svgImagePath+"pluto-light.png"));
        add(new Planet(10,EARTH,"♁",svgImagePath+"null.png",svgImagePath+"null.png",false,true));
        add(new Planet(11,PLANETOID,"?",svgImagePath+"null.png",svgImagePath+"null.png",false,false));
        add(new Planet(-1,NAP,"?",svgImagePath+"null.png",svgImagePath+"null.png",false,false));
        add(new Planet(-2,AC,"AC",svgImagePath+"null.png",svgImagePath+"null.png",false,false));
        add(new Planet(-3,MC,"MC",svgImagePath+"null.png",svgImagePath+"null.png",false,false));
    }};
    
    
    // Populate maps (this order is important)
    static
    {
        planetsByName.put(SUN, planetList.get(0));
        planetsByName.put(LUNA, planetList.get(1));
        planetsByName.put(MERCURY, planetList.get(2));
        planetsByName.put(VENUS, planetList.get(3));
        planetsByName.put(MARS, planetList.get(4));
        planetsByName.put(JUPITER, planetList.get(5));
        planetsByName.put(SATURN, planetList.get(6));
        planetsByName.put(URANUS, planetList.get(7));
        planetsByName.put(NEPTUNE, planetList.get(8));
        planetsByName.put(PLUTO, planetList.get(9));
        planetsByName.put(EARTH, planetList.get(10));
        planetsByName.put(PLANETOID, planetList.get(11));
        planetsByName.put(NAP, planetList.get(12));
        planetsByName.put(AC, planetList.get(13));
        planetsByName.put(MC, planetList.get(14));
    };
    // TreeMap so ordered by key order (not entry order)
    static
    {
        planetsByOrder.put(0, planetList.get(0));
        planetsByOrder.put(1, planetList.get(1));
        planetsByOrder.put(2, planetList.get(2));
        planetsByOrder.put(3, planetList.get(3));
        planetsByOrder.put(4, planetList.get(4));
        planetsByOrder.put(5, planetList.get(5));
        planetsByOrder.put(6, planetList.get(6));
        planetsByOrder.put(7, planetList.get(7));
        planetsByOrder.put(8, planetList.get(8));
        planetsByOrder.put(9, planetList.get(9));
        planetsByOrder.put(10, planetList.get(10));
        planetsByOrder.put(11, planetList.get(11));
        planetsByOrder.put(-1, planetList.get(12));
        planetsByOrder.put(-2, planetList.get(13));
        planetsByOrder.put(-3, planetList.get(14));
    };

    /**
     * Constructor.
     * 
     * @param order
     * @param name
     * @param symbol
     * @param filedark
     * @param filelight
     * @param forDisplay
     * @param isBody
     */
    public Planet(int order, String name, String symbol, String filedark, String filelight, boolean forDisplay, boolean isBody)
    {
        this.order = order;
        this.name = name;
        this.symbol = symbol;
        this.filedark = filedark;
        this.filelight = filelight;
        this.forDisplay = forDisplay;
        this.isBody = isBody;
    }
    
    /**
     * Constructor.
     * 
     * @param order
     * @param name
     * @param symbol
     * @param filedark
     * @param filelight
     */
    public Planet(int order, String name, String symbol, String filedark, String filelight)
    {
        this(order,name,symbol,filedark,filelight,true,true);
    }
    
    /**
     * Map of planet (dark) images.
     * 
     * @return
     * @throws IOException 
     */
    public static Map<Planet,ZImage> getDarkImages()
            throws IOException
    {
        Map<Planet,ZImage> plImgMap = new HashMap<>();
        
        for( Planet planet: planetList )
        {
            System.out.print(".");
            BufferedImage bImage = ImageIO.read(new File(planet.getFileDark()));
            
            // Store image with some metadata
            ZImage zimage = new ZImage( bImage, Constants.SCALEDP, Constants.SCALEDP, Color.BLACK );
            plImgMap.put(planet, zimage);
        }
        
        return plImgMap;
    }
    
    /**
     * Map of planet (light) images.
     * 
     * @return
     * @throws IOException 
     */
    public static Map<Planet,ZImage> getLightImages()
            throws IOException
    {
        Map<Planet,ZImage> plImgMap = new HashMap<>();
        
        for( Planet planet: planetList )
        {
            System.out.print(".");
            BufferedImage bImage = ImageIO.read(new File(planet.getFileLight()));
            
            // Store image with some metadata
            ZImage zimage = new ZImage( bImage, Constants.SCALEDP, Constants.SCALEDP, Color.LIGHT_GRAY );
            plImgMap.put(planet, zimage);
        }
        
        return plImgMap;
    }

    /*
     * Mutators
     */
    public int getOrder() { return order; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getFileDark() { return filedark; }
    public String getFileLight() { return filelight; }

    /**
     * Planet posn. (RA) and offset
     * @return degrees the planet position
     */
    public double getRADegrees() { return raDegrees; }
    public double getRA() { return ra; }
    
    /**
     * Set Right Ascension (degrees)
     * @param degrees 
     */
    public void setRADegrees(double degrees)
    {
        this.raDegrees = degrees;
        this.ra = Math.toRadians(degrees);
    }
    /**
     * Set Right Ascension (rads)
     * @param radians 
     */
    public void setRA(double radians)
    {
        this.ra = radians;
        this.raDegrees = Math.toDegrees(radians);
    }
    
    public LatLon getEclipticCoords() { return eclipticCoords; }
    public void setEclipticCoords( double latitude, double longitude )
            throws InvalidLocationException
    {
        this.eclipticCoords = new LatLon( latitude, longitude );
    }


    /**
     * Declination.
     * @return 
     */
    public double getDeclDegrees() { return declDegrees; } 
    public double getDeclination() { return declination; }
    public void setDeclination(double radians)
    {
        this.declination = radians;
        this.declDegrees = Math.toDegrees(radians);
    }
    public void setDeclDegrees(double degrees)
    {
        this.declDegrees = degrees;
        this.declination = Math.toRadians(degrees);
    }


    public double getOffset() { return offset; }
    public void setOffset(double offset) { this.offset = offset; }
    
    /**
     * Display this body?
     * @return 
     */
    public boolean forDisplay() { return forDisplay; }
    
    /**
     * Include in Aspect?
     * @return 
     */
    public boolean isBody() { return isBody; }

    /**
     * Return the Planet (by name) map.
     * @return 
     */
    public static Map<String,Planet> getPlanetsByName() { return planetsByName; }
    
    /**
     * Return the Planet (by order, Sun=0) map.
     * @return 
     */
    public static Map<Integer,Planet> getPlanetsByOrder() { return planetsByOrder; }

    /**
     * Planet ordering.
     * 
     * @param p
     * @return 
     */
    @Override
    public int compareTo(Planet p)
    {
        /* 
         * compareTo() will return:
         *  < 0 if this(keyword) is "less" than object o,
         *  > 0 if this(keyword) is "greater" than object o and
         *  0 if they are equal.
         */
        return this.getOrder() - p.getOrder();
    }
}

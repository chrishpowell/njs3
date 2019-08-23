/*
 * Planet characteristics
 */
package eu.discoveri.prediktdemobe;

import eu.discoveri.predikt.utils.Constants;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Planet
{
    // For display
    private double          degrees = -180.d,
                            radians = 0.d,
                            offset = Constants.POFFSET;
    // Basic attributes
    private final int       order;
    private final String    name,
                            symbol,
                            filename;
    private final boolean   display;
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
                                NAP     = "Not a planet";
    
    private final static String               svgImagePath = Constants.RESOURCEPATH+"planets/";
    private final static Map<String,Planet>   planetsByName = new HashMap<>();
    private final static Map<Integer,Planet>   planetsByOrder = new HashMap<>();
    
    
    // Populate maps
    static
    {
        planetsByName.put(SUN, new Planet(0,SUN,"☉",svgImagePath+"sun.png"));
        planetsByName.put(MERCURY, new Planet(1,MERCURY,"☿",svgImagePath+"mercury.png"));
        planetsByName.put(VENUS, new Planet(2,VENUS,"♀",svgImagePath+"venus.png"));
        planetsByName.put(EARTH, new Planet(3,EARTH,"♁",svgImagePath+"null.png",false));
        planetsByName.put(MARS, new Planet(4,MARS,"♂",svgImagePath+"mars.png"));
        planetsByName.put(JUPITER, new Planet(5,JUPITER,"♃",svgImagePath+"jupiter.png"));
        planetsByName.put(SATURN, new Planet(6,SATURN,"♄",svgImagePath+"saturn.png"));
        planetsByName.put(URANUS, new Planet(7,URANUS,"⛢",svgImagePath+"uranus.png"));
        planetsByName.put(NEPTUNE, new Planet(8,NEPTUNE,"♆",svgImagePath+"neptune.png"));
        planetsByName.put(PLUTO, new Planet(9,PLUTO,"♇",svgImagePath+"pluto.png"));
        planetsByName.put(LUNA, new Planet(10,LUNA,"☾",svgImagePath+"moon.png"));
        planetsByName.put(PLANETOID, new Planet(11,PLANETOID,"*",svgImagePath+"null.png",false));
        planetsByName.put(NAP, new Planet(-1,NAP,"*",svgImagePath+"null.png",false));
    };
    static
    {
        planetsByOrder.put(0, new Planet(0,SUN,"☉",svgImagePath+"sun.png"));
        planetsByOrder.put(1, new Planet(1,MERCURY,"☿",svgImagePath+"mercury.png"));
        planetsByOrder.put(2, new Planet(2,VENUS,"♀",svgImagePath+"venus.png"));
        planetsByOrder.put(3, new Planet(3,EARTH,"♁",svgImagePath+"null.png",false));
        planetsByOrder.put(4, new Planet(4,MARS,"♂",svgImagePath+"mars.png"));
        planetsByOrder.put(5, new Planet(5,JUPITER,"♃",svgImagePath+"jupiter.png"));
        planetsByOrder.put(6, new Planet(6,SATURN,"♄",svgImagePath+"saturn.png"));
        planetsByOrder.put(7, new Planet(7,URANUS,"⛢",svgImagePath+"uranus.png"));
        planetsByOrder.put(8, new Planet(8,NEPTUNE,"♆",svgImagePath+"neptune.png"));
        planetsByOrder.put(9, new Planet(9,PLUTO,"♇",svgImagePath+"pluto.png"));
        planetsByOrder.put(10, new Planet(10,LUNA,"☾",svgImagePath+"moon.png"));
        planetsByOrder.put(11, new Planet(11,PLANETOID,"*",svgImagePath+"null.png",false));
        planetsByOrder.put(-1, new Planet(-1,NAP,"*",svgImagePath+"null.png",false));
    };

    /**
     * Constructor.
     * 
     * @param order
     * @param name
     * @param symbol
     * @param filename
     * @param display 
     */
    public Planet(int order, String name, String symbol, String filename, boolean display)
    {
        this.order = order;
        this.name = name;
        this.symbol = symbol;
        this.filename = filename;
        this.display = display;
    }
    
    /**
     * Constructor.
     * 
     * @param order
     * @param name
     * @param symbol
     * @param filename
     */
    public Planet(int order, String name, String symbol, String filename)
    {
        this(order,name,symbol,filename,true);
    }

    /*
     * Mutators
     */
    public int getOrder() { return order; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getFilename() { return filename; }

    /**
     * Planet posn. and offset
     * @return degrees the planet position
     */
    public double getDegrees() { return degrees; }
    public double getRadians() { return radians; }
    public void setDegrees(double degrees)
    {
        this.degrees = degrees;
        this.radians = Math.toRadians(degrees);
    }
    public void setRadians(double radians)
    {
        this.radians = radians;
        this.degrees = Math.toDegrees(radians);
    }

    public double getOffset() { return offset; }
    public void setOffset(double offset) { this.offset = offset; }
    
    /**
     * Display this body?
     * @return 
     */
    public boolean getDisplay() { return display; }

    /**
     * Return the Planet (by name) map.
     * @return 
     */
    public static Map<String,Planet> getPlanetsByName() { return planetsByName; }
    
    /**
     * Return the Planet (by order, Sun=1) map.
     * @return 
     */
    public static Map<Integer,Planet> getPlanetsByOrder() { return planetsByOrder; }
}

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
                            offset = Constants.POFFSET;
    // Basic attributes
    private final int       order;
    private final String    name,
                            symbol,
                            filename;
    // List of planets
    private final static String               svgImagePath = Constants.RESOURCEPATH+"planets/";
    private final static Map<String,Planet>   planets = new HashMap<>();
    // Populate
//    static
//    {
//        planets.put(Constants.SUN, new Planet(1,Constants.SUN,"☉",svgImagePath+"sun.png"));
//        planets.put("Mercury", new Planet(2,"Mercury","☿",svgImagePath+"sun.png"));
//        planets.put("Venus", new Planet(3,"Venus","♀︎",svgImagePath+"sun.png"));
//        planets.put(Constants.MARS, new Planet(4,Constants.MARS,"♂︎",svgImagePath+"mars.png"));
//        planets.put("Moon", new Planet(5,"Moon","☽",svgImagePath+"mars.png"));
//        planets.put("Jupiter", new Planet(6,"Jupiter","♃",svgImagePath+"mars.png"));
//        planets.put("Saturn", new Planet(7,"Saturn","♄",svgImagePath+"mars.png"));
//        planets.put("Uranus", new Planet(8,"Uranus","♅",svgImagePath+"mars.png"));
//        planets.put("Neptune", new Planet(9,"Neptune","♆",svgImagePath+"mars.png"));
//        planets.put("Pluto", new Planet(10,"Pluto","♇",svgImagePath+"mars.png"));
//    };
        static
    {
        planets.put(Constants.SUN, new Planet(1,Constants.SUN,"☉",svgImagePath+"sun.png"));
        planets.put(Constants.SUN, new Planet(2,Constants.SUN,"☉",svgImagePath+"sun.png"));
        planets.put(Constants.SUN, new Planet(3,Constants.SUN,"☉",svgImagePath+"sun.png"));
        planets.put(Constants.MARS, new Planet(4,Constants.MARS,"☿",svgImagePath+"mars.png"));
        planets.put(Constants.PLUTO, new Planet(5,Constants.PLUTO,"♇",svgImagePath+"pluto.png"));
        planets.put(Constants.NEPTUNE, new Planet(6,Constants.NEPTUNE,"♆",svgImagePath+"neptune.png"));
        planets.put(Constants.SUN, new Planet(7,Constants.SUN,"☉",svgImagePath+"sun.png"));
        planets.put(Constants.MARS, new Planet(8,Constants.MARS,"☿",svgImagePath+"mars.png"));
        planets.put(Constants.MARS, new Planet(9,Constants.MARS,"☿",svgImagePath+"mars.png"));
        planets.put(Constants.MARS, new Planet(10,Constants.MARS,"☿",svgImagePath+"mars.png"));
    };

    public Planet(int order, String name, String symbol, String filename)
    {
        this.order = order;
        this.name = name;
        this.symbol = symbol;
        this.filename = filename;
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
    public void setDegrees(double degrees) { this.degrees = degrees; }

    public double getOffset() { return offset; }
    public void setOffset(double offset) { this.offset = offset; }

    /**
     * Return the Planet map.
     * @return 
     */
    public static Map<String,Planet> getPlanets() { return planets; }
}

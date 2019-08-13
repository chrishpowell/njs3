/*
 * List of Western houses.  [Note: Offset by 1]
* @TODO Use enumMap??
 */
package eu.discoveri.predikt.test.horochart;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum ZodiacHouse
{
    ARIES("Aries","Ari",0.d,29.9999d),
    TAURUS("Taurus","Tau",30.d,59.9999d),
    GEMINI("Gemini","Gem",60.d,89.9999d),
    CANCER("Cancer","Can",90.d,119.9999d),
    LEO("Leo","Leo",120.d,149.9999d),
    VIRGO("Virgo","Vir",150.d,179.9999d),
    LIBRA("Libra","Lib",180.d,209.9999d),
    SCORPIO("Scorpio","Sco",210.d,239.9999d),
    SAGITTARIUS("Sagittarius","Sag",240.d,269.9999d),
    CAPRICORN("Capricorn","Cap",270.d,299.9999d),
    AQUARIUS("Aquarius","Aqu",300.d,329.9999d),
    PISCES("Pisces","Pis",330.d,359.9999d),
    // Special entries of Midheaven/MC and Ascendant
    MC("MC","MC",0.d,0.d),
    ASC("ASC","ASC",0.d,0.d);
    
    // Name
    private final String    name, shortName;
    // Start and end (degrees and radians)
    private final double    degStart, degEnd,
                            radStart, radEnd;
    
    private ZodiacHouse( String name, String shortname, double degStart, double degEnd )
    {
        this.name = name;
        this.shortName = shortname;
        this.degStart = degStart;
        this.degEnd = degEnd;
        this.radStart = Math.toRadians(degStart);
        this.radEnd = Math.toRadians(degEnd);
    }

    // Getters
    public String getName() { return name; }
    public String getShortname() { return shortName; }
    public double getDegStart() { return degStart; }
    public double getDegEnd() { return degEnd; }
    public double getRadStart() { return radStart; }
    public double getRadEnd() { return radEnd; }

    
    /*
     * Access maps
     */
    // Access by name
    private static final Map<ZodiacHouse,Integer> zhNameMap = new HashMap<ZodiacHouse,Integer>()
    {{
        put(ZodiacHouse.ARIES,0);
        put(ZodiacHouse.TAURUS,1);
        put(ZodiacHouse.GEMINI,2);
        put(ZodiacHouse.CANCER,3);
        put(ZodiacHouse.LEO,4);
        put(ZodiacHouse.VIRGO,5);
        put(ZodiacHouse.LIBRA,6);
        put(ZodiacHouse.SCORPIO,7);
        put(ZodiacHouse.SAGITTARIUS,8);
        put(ZodiacHouse.CAPRICORN,9);
        put(ZodiacHouse.AQUARIUS,10);
        put(ZodiacHouse.PISCES,11);
    }};
    
    // Access by order
    private static final Map<Integer,ZodiacHouse> zhOrderMap = new HashMap<Integer,ZodiacHouse>()
    {{
        put(0,ZodiacHouse.ARIES);
        put(1,ZodiacHouse.TAURUS);
        put(2,ZodiacHouse.GEMINI);
        put(3,ZodiacHouse.CANCER);
        put(4,ZodiacHouse.LEO);
        put(5,ZodiacHouse.VIRGO);
        put(6,ZodiacHouse.LIBRA);
        put(7,ZodiacHouse.SCORPIO);
        put(8,ZodiacHouse.SAGITTARIUS);
        put(9,ZodiacHouse.CAPRICORN);
        put(10,ZodiacHouse.AQUARIUS);
        put(11,ZodiacHouse.PISCES);
    }};

    
    /**
     * House to order num.
     * @return 
     */
    public static Map<ZodiacHouse,Integer> getZHNameMap()
        { return zhNameMap; }

    /**
     * Order to House name
     * @return 
     */
    public static Map<Integer,ZodiacHouse> getZHOrderMap()
        { return zhOrderMap; }
}

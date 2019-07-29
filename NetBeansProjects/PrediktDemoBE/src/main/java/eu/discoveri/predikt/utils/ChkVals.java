/*
 * Epoch (Constants.J2000.00) values and other periods for check
 */
package eu.discoveri.predikt.utils;

import eu.discoveri.predikt.exception.NoSuchPlanetTimeException;
import eu.discoveri.predikt.ephemeris.KeplerElement;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ChkVals
{
    // Empty string
    private final static String         EMPTY = "";
    // Values to check
    private final static Map<PlanetTime,ValTbl> pTable = new HashMap<>();
    // Get stored values
    public static Map<PlanetTime, ValTbl> getpTable() { return pTable; }

    
    /**
     * Load up some expected values for planets
     * 
     */
    public static void cvInit()
    {
        /*
         * Mars
         */
        // J2000: Longitude of Ascending Node (bigOmega)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));
        // J2000: Longitude of Perihelion (littleOmega)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.LITTLEOMEGA,Constants.J2000),
                    new ValTbl("NASA",336.04084,EMPTY,Double.MIN_VALUE));
        // J2000: Inclination to ecliptic (littleI)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.LITTLEI,Constants.J2000),
                    new ValTbl("Wikipedia",286.502,EMPTY,Double.MIN_VALUE));
        // J2000: Semi-major axis (littleA)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.LITTLEA,Constants.J2000),
                    new ValTbl("NASA",355.45332,EMPTY,Double.MIN_VALUE));
        // J2000: Eccentricity (litteE)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.LITTLEE,Constants.J2000),
                    new ValTbl("Wikipedia",358.617,"stargazing.net",336.0882));
        // J2000: Right Ascension (ra)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.RIGHTASC,Constants.J2000),
                    new ValTbl("SwissEph [22h:0m:34.71785884s]",Util.mod(Util.hrMinSec2DecDeg(22.d, 0.d, 34.71785884d),Constants.ANORBITDEG),
                            EMPTY,Double.MIN_VALUE));
        // J2000: Declination (decl)
        pTable.put( new PlanetTime(Constants.MARS,KeplerElement.DECL,Constants.J2000),
                    new ValTbl("SwissEph [8h:35m:4.47456882s]",Util.mod(Util.degMinSec2DecDeg(8.d, 35.d, 4.47456882d),Constants.ANORBITDEG),
                            EMPTY,Double.MIN_VALUE));


        /*
         * Venus
         */
        pTable.put( new PlanetTime(Constants.VENUS,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));

        /*
         * Earth/Sun
         */
        // J2000: Longitude of Ascending Node (bigOmega)
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));
        // Summer solstice
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.RIGHTASC,Constants.SSOLTICEN),
                    new ValTbl("stjarnhimlen.se",90.d,EMPTY,Double.MIN_VALUE));
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.DECL,Constants.SSOLTICEN),
                    new ValTbl("stjarnhimlen.se",23.4d,EMPTY,Double.MIN_VALUE));

        //... Summer solstice
        // Radius
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.HELIODIST,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",1.016246051d,EMPTY,Double.MIN_VALUE));
        // Ecliptic coords
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.XECL,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",0.001278088d,EMPTY,Double.MIN_VALUE));
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.YECL,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",1.016245248d,EMPTY,Double.MIN_VALUE));
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.ZECL,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",-0.000001855d,EMPTY,Double.MIN_VALUE));
        // Equatorial coords
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.XEQ,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",0.001278088d,EMPTY,Double.MIN_VALUE));
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.YEQ,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",0.932396984d,EMPTY,Double.MIN_VALUE));
        pTable.put( new PlanetTime(Constants.SUN,KeplerElement.ZEQ,Constants.SSOLTICEN),
                    new ValTbl("swisseph ~",0.404215619d,EMPTY,Double.MIN_VALUE));

        /*
         * Moon
         */
        pTable.put( new PlanetTime(Constants.MOON,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));

        /*
         * Jupiter
         */
        pTable.put( new PlanetTime(Constants.JUPITER,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));

        /*
         * Saturn
         */
        pTable.put( new PlanetTime(Constants.SATURN,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));

        /*
         * Neptune
         */
        pTable.put( new PlanetTime(Constants.NEPTUNE,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));

        /*
         * Pluto
         */
        pTable.put( new PlanetTime(Constants.PLUTO,KeplerElement.BIGOMEGA,Constants.J2000),
                    new ValTbl("NASA/Wikipedia",49.558,EMPTY,Double.MIN_VALUE));
    }
    
    /**
     * Output a line
     * @param planet
     * @param kl kepler element such as bigOmega, littleI etc.
     * @param ldt date time
     * @throws NoSuchPlanetTimeException
     * @return 
     */
    public static String toPrint( String planet, KeplerElement kl, LocalDateTime ldt )
            throws NoSuchPlanetTimeException
    {
        DecimalFormat df = new DecimalFormat("###.######");
        // The planet/time/check values map
        ValTbl vt;
        
        // Get the values
        PlanetTime pt = new PlanetTime(planet, kl, ldt);
        if( pTable.containsKey(pt) )
            { vt =  pTable.get(pt); }
        else
            throw new NoSuchPlanetTimeException(planet+":"+ldt.toString());
        
        // String them up!
        String src2 = !vt.getSource2().equals(EMPTY)?", "+vt.getSource2()+" "+df.format(vt.getValue2()):"";
        return pt.getKl().getDescription()+" ("+pt.getKl().getDimension()+")"+" ["+vt.getSource1()+ " "+df.format(vt.getValue1())+src2+"]: ";
    }
    
    /**
     * Dump the map
     */
    public static void dumpMap()
    {
        pTable.entrySet().forEach((entry) -> {
            System.out.println("[" +entry.getKey().getPlanet()+" (" +           // Planet
                    entry.getKey().getDt().toString()+ ") : " +                 // Date
                    entry.getKey().getKl().getDescription()+ "]");              // Check descrip.
        });
    }

    
    /**
     * M A I N
     * =======
     * Check output format ok
     * @throws Exception
     * @param args 
     */
    public static void main(String[] args)
            throws Exception
    {
        ChkVals.cvInit();
        
// Check equals
//        PlanetTime pt = new PlanetTime("Mars",KeplerElement.BIGOMEGA,Constants.J2000);
//        PlanetTime pt1 = new PlanetTime("Mars",KeplerElement.BIGOMEGA,Constants.J2000);
//        PlanetTime pt2 = new PlanetTime("Mars",KeplerElement.BIGOMEGA,LocalDateTime.parse("2001-01-01T00:00:00"));
//        
//        System.out.println("--------------------------");
//        System.out.println("pt1 == pt? " +pt.equals(pt1));
//        System.out.println("pt2 == pt? " +pt.equals(pt2));
//        System.out.println("--------------------------");
        
        //ChkVals.dumpMap();
        // Check equals() functions correctly
        System.out.println(ChkVals.toPrint(Constants.MARS, KeplerElement.LITTLEI, Constants.J2000));
        System.out.println(ChkVals.toPrint("Mars", KeplerElement.LITTLEI, LocalDateTime.parse("2000-01-01T00:00:00")));
    }
}


//---------------------------------------
class PlanetTime
{
    private final KeplerElement kl;
    private final String        planet;
    private final LocalDateTime dt;

    public PlanetTime(String planet, KeplerElement kl, LocalDateTime dt)
    {
        this.planet = planet;
        this.kl = kl;
        this.dt = dt;
    }

    public String getPlanet() { return planet; }
    public KeplerElement getKl() { return kl; }
    public LocalDateTime getDt() { return dt; }
    
    /**
     * Gotta override hashcode and equals for key checks
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(planet,dt.toString());
    }

    @Override
    public boolean equals( Object obj )
    {
        // Quick object checks
        if( this == obj ) { return true; }
        if( obj == null ) { return false; }
        if( getClass() != obj.getClass() ) { return false; }
        
        // Now check input object values against this
        final PlanetTime other = (PlanetTime) obj;
        if( !this.getPlanet().equals(other.getPlanet()) ) { return false; }
        if( !this.getKl().getName().equals(other.getKl().getName()) ) { return false; }
        return this.getDt().equals(other.getDt());
    }
}


//-------------------------------------
class ValTbl
{
    private final String        source1;
    private final double        value1;
    private final String        source2;
    private final double        value2;

    /**
     * Constructor.
     * 
     * @param check  What is being checked eg: Longitude of AN
     * @param dimension Eg: deg or AU
     * @param source1 Eg: NASA
     * @param value1 The value of the check
     * @param source2
     * @param value2 
     */
    public ValTbl(  String source1,
                    double value1,
                    String source2,
                    double value2   )
    {
        this.source1 = source1;
        this.value1 = value1;
        this.source2 = source2;
        this.value2 = value2;
    }

    /*
     * Getters
     */
    public String getSource1() { return source1; }
    public double getValue1() { return value1; }
    public String getSource2() { return source2; }
    public double getValue2() { return value2; }
}

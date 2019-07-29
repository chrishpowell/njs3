/*
 * Various utils
 */
package eu.discoveri.predikt.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Util
{
    /**
     * Calculate mod (incl. for negatives).
     * 
     * @param in
     * @param mod
     * @return 
     */
    public static double mod( double in, double mod )
    {
        double rem = in%mod;
        
        if( rem < 0.d )
            return mod + rem;
        else
            return rem;
    }
    
    /**
     * Convert Hours decimal to Degrees decimal.
     * 
     * @param hrs
     * @return 
     */
    public static double hrs2DegDec( double hrs )
    {
        return hrs * Constants.DEGREESPERHR;
    }
    
    /**
     * Convert Degrees decimal to Hours decimal.
     * 
     * @param degs
     * @return 
     */
    public static double degs2HrDec( double degs )
    {
        return degs / Constants.DEGREESPERHR;
    }
    
    /**
     * Convert hour triple to decimal.
     * Nota bene: if entry is, eg: -5h:11m:30s, the entry needs to be -5, -11, -30
     * 
     * @param hrdeg
     * @param min
     * @param sec 
     * @return decimal (degrees)
     */
    public static double hrDegMinSec2Dec( double hrdeg, double min, double sec )
    {
        return hrdeg + min/60.d + sec/3600.d;
    }
    
    /**
     * Degree triple to decimal degrees.
     * (A wrapper for hrDegMinSec2Dec)
     * 
     * @param deg
     * @param min
     * @param sec
     * @return 
     */
    public static double degMinSec2DecDeg( double deg, double min, double sec )
    {
        return hrDegMinSec2Dec( deg, min, sec );
    }
    
    /**
     * Degree triple to radians.
     * 
     * @param deg
     * @param min
     * @param sec
     * @return deg:min:sec to decimal radians
     */
    public static double degMinSec2DecRad( double deg, double min, double sec )
    {
        return Math.toRadians(hrDegMinSec2Dec(deg,min,sec));
    }
    
    /**
     * Hour triple to decimal degrees.
     * 
     * @param hr
     * @param min
     * @param sec
     * @return 
     */
    public static double hrMinSec2DecDeg( double hr, double min, double sec )
    {
        return hrDegMinSec2Dec(hr,min,sec)*Constants.DEGREESPERHR;
    }
    
    /**
     * Hour triple to radians.
     * 
     * @param hr
     * @param min
     * @param sec
     * @return hr:min:sec to decimal radians
     */
    public static double hrMinSec2DecRad( double hr, double min, double sec )
    {
        return Math.toRadians(hrDegMinSec2Dec(hr,min,sec)*Constants.DEGREESPERHR);
    }
    
    /**
     * Calculate obliquity.
     * 
     * @param date
     * @return 
     */
    public static double calcObliquityDegs( LocalDate date )
    {
        return Constants.ETILT1JAN1900 - Constants.ETILTMOVE * TimeScale.daysSince1900(date);
    }
    

    /*
     *  =========
     *  T E S T S
     *  =========
     */
    public static void testMod()
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        System.out.println("Mod function 10. mod 5. should be zero: "+Util.mod(10.d,5.d));
        System.out.println("Mod function 7. mod 5. should be 2.: "+Util.mod(7.d,5.d));
        System.out.println("Mod function 12.3 mod 5. should be 2.3: "+df.format(Util.mod(12.3d,5.d)));
        System.out.println("Mod function -12.3 mod 5. should be 2.7: "+df.format(Util.mod(-12.3d,5.d)));
        System.out.println("Mod function -3135.9347 mod 360. should be 104.0652: "+df.format(Util.mod(-3135.9347d,360.d)));
        System.out.println("Mod function -54.7324 mod 2*pi should be 1.81866: " +df.format(Util.mod(-54.7324d,6.283185d)));
        System.out.println("Mod function -45 mod 360 should be 315: " +Util.mod(-45.d,Constants.ANORBITDEG));
        System.out.println("");
    }
    
    public static void testHrMinSec()
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        System.out.println("1h:35m:23.99718867s in rads [hrMinSec2DecRad] (approx. 0.4): " +
                df.format(Util.hrMinSec2DecRad(1.d, 35.d, 23.99718867d)));
        System.out.println("8d:35m:4.47456882s in rads [degMinSec2DecRad] (approx. 0.15): " +
                df.format(Util.degMinSec2DecRad(8.d, 35.d, 4.47456882d)));
        System.out.println("");
        System.out.println("3h:0m:0s in degrees [hrMinSec2DecDeg] (45): " +Util.hrMinSec2DecDeg(3.d, 0.d, 0.d));
        System.out.println("-2h:59m:59.9999s in degrees [hrMinSec2DecDeg] (-45): " +
                df.format(Util.hrMinSec2DecDeg(-2.d, -59.d, -59.9999d)));
                System.out.println("-2h:59m:59.999s in degrees mod [hrMinSec2DecDeg] (315): " +
                df.format(Util.mod(Util.hrMinSec2DecDeg(-2.d, -59.d, -59.999d),Constants.ANORBITDEG)));
        System.out.println("6h:0m:0s in degrees [hrMinSec2DecDeg] (90): " +Util.hrMinSec2DecDeg(6.d, 0.d, 0.d));
        System.out.println("21h:0m:0s in degrees [hrMinSec2DecDeg] (315): " +Util.hrMinSec2DecDeg(21.d, 0.d, 0.d));
        System.out.println("");
    }
    
    public static void testDegRad()
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        System.out.println("ra/decl in (mod) degs: [SwissEph: 22h:0m:34.71785884s/-13d:19m:21.26730596s] is: " +
                df.format(Util.mod(Util.hrMinSec2DecDeg(22.d, 0.d, 34.71785884d),Constants.ANORBITDEG)) +"/"+
                df.format(Util.mod(Util.degMinSec2DecDeg(-13.d, 19.d, 21.26730596d),Constants.ANORBITDEG)));
        System.out.println("");
        System.out.println("Degrees to radians 330.144658 deg to mod radians should be 5.76: " +
                df.format(Util.mod(Math.toRadians(330.144658d),Constants.ANORBITRADS)));
        System.out.println("Degrees to radians -3135.9347 deg to radians should be -54.7324 (not mod) : " +df.format(Math.toRadians(-3135.9347d)));
        System.out.println("Degrees to radians 104.0652 deg to radians should be 1.8265: " +
                df.format(Util.mod(Math.toRadians(104.652d),Constants.ANORBITRADS)));
        System.out.println("Degrees to radians 26.8388 deg to radians should be 0.4684: " +
                df.format(Util.mod(Math.toRadians(26.8388d),Constants.ANORBITRADS)));
        System.out.println("");
    }
    
    public static void testObliq()
    {
        System.out.println("Obliq (1.1.1900): " +calcObliquityDegs(LocalDate.of(1900,1,1)));
        System.out.println("Obliq (1.1.2000): " +calcObliquityDegs(LocalDate.of(2000,1,1)));
    }
    

    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
//        testMod();
//        
//        testHrMinSec();
//        
//        testDegRad();

//        testObliq();
    }
}

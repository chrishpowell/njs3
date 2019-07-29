/*
 * Jupiter.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.TimeScale;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;

import java.text.DecimalFormat;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Jupiter extends Orbits
{
        // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 100.47390909d,                      // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = 0.20469106d,
                                LITTLEI0 = 1.30439695d,                         // Little i, inclination (deg)
                                LITTLEIDOT = -0.00183714d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 14.72847983d,                    // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.21252668d/Constants.JULDAYSPERC,
                                LITTLEA0 = 5.20288700d,                         // Little a, semi-major axis (AU)
                                LITTLEADOT = -0.00011607d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.04838624d,                         // Little e, Eccentricity
                                LITTLEEDOT = -0.00013253d/Constants.JULDAYSPERC,
                                BIGL0 = 3034.74612775d,                         // Big L, mean longitude
                                BIGLDOT = 0.0830853001d/Constants.JULDAYSPERC,
                                LITTLEN = 0.0831d;                              // Daily motion

    
    /**
     * Set Jupiter position for given day.
     * 
     * @param julDay 
     */
    public Jupiter( double julDay )
    {
        super( BIGOMEGA0, BIGOMEGAMOVE,
               LITTLEI0, LITTLEIDOT,
               LITTLEOMEGA0, LITTLEOMEGADOT,
               LITTLEA0, LITTLEADOT,
               LITTLEE0, LITTLEEDOT,
               BIGL0, BIGLDOT,
               LITTLEN,
               julDay );
    }
    
    /**
     * Dump Jupiter details
     */
    @Override
    public void dumpPlanet()
    {
        DecimalFormat df = new DecimalFormat("###.######");

        // Coords and ra/decl
        //System.out.println("Ecliptic coords. xe/ye/ze: " +earth.getXe()+"/"+earth.getYe()+"/"+earth.getZe());
        //System.out.println("Equatorial coords, xeq/yeq/zeq: " +earth.getXeq()+"/"+earth.getYeq()+"/"+earth.getZeq());
        System.out.println("ra/decl: " +df.format(this.getRightAsc())+"/"+df.format(this.getDecl()));

        // Julian day
        System.out.println("Mod. Julian Day: " +this.getJulDay());  //+ Constants.MJD1JAN2000ZERO
    }
    
    private static void J2000Check()
    {
        DecimalFormat df = new DecimalFormat("###.######");
        System.out.println("JUPITER:  J2000.00 calculation check");
        
        // J2000.00
        Jupiter jupiter = new Jupiter(TimeScale.modJulDayTime(2000, 1, 1, 0.d));
        
        // Right, get the ra and decl
        System.out.println("*** Expected ra/decl: [SwissEph: 1h:35m:23.99718867s/8h:35m:4.47456882s]: " +
                df.format(Util.hrMinSec2DecRad(1.d, 35.d, 23.99718867d)) +"/"+
                df.format(Util.degMinSec2DecRad(8.d, 35.d, 4.47456882d)));
        jupiter.raDecl();
        System.out.println("*** Calculated ra/decl: " +df.format(jupiter.getRightAsc())+"/"+df.format(jupiter.getDecl()));
        
        System.out.println("\r\nDistance checks, two values should be same: [" +df.format(jupiter.getHelioDist())+ " : " +df.format(jupiter.getDistChk()) +"]");
        System.out.println("Longitude of ascending node (deg) [NASA: 100.47390909, Wikipedia: 100.464]: " +df.format(Math.toDegrees(jupiter.getLonANRad())));
        System.out.println("Longitude of perihelion (deg) [NASA: 336.04084]: " +df.format(Math.toDegrees(jupiter.getLonPRad())));
        System.out.println("Argument of perihelion (deg) [Wikipedia: 273.867]: " +df.format(Math.toDegrees(jupiter.getArgPRad())));
        System.out.println("Mean longitude (deg) [NASA: 34.39644051]: " +df.format(Math.toDegrees(jupiter.getMeanLongRad())));
        System.out.println("Mean anomaly (deg) [quae.nl: 20.0202, Wikipedia: 20.02]: " +df.format(Math.toDegrees(jupiter.getMeanAnomRad())));
        System.out.println("");
    }
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        // Epoch test
        J2000Check();
        
        //------------------------------------------------------
        // Other times
    }
}

/*
 * Mars.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.TimeScale;
import eu.discoveri.predikt.ephemeris.KeplerElement;
import eu.discoveri.predikt.utils.ChkVals;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;

import java.text.DecimalFormat;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Mars extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 49.55953891d,                       // Big omega, longitude of ascending node (deg)
                                BIGOMEGAMOVE = -0.29257343d,
                                LITTLEI0 = 1.84969142d,                         // Little i, inclination to ecliptic (deg)
                                LITTLEIDOT = -0.00813131d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 336.04084d,                      // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.44441088d/Constants.JULDAYSPERC,
                                LITTLEA0 = 1.52371034d,                         // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = 0.00001847d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.09339410d,                         // Little e, Eccentricity
                                LITTLEEDOT = 0.00007882d/Constants.JULDAYSPERC,
                                BIGL0 = -4.55343205d,                           // Big L, mean longitude
                                BIGLDOT = 19140.30268499d/Constants.JULDAYSPERC,
                                LITTLEN = 0.5242d;                              // Daily motion
    
    /**
     * Set Mars position for given day.
     * 
     * @param julDay 
     */
    public Mars( double julDay )
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
     * Dump Mars details
     * https://web.archive.org/web/20140323165619/http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
     */
    @Override
    public void dumpPlanet()
    {
        DecimalFormat df = new DecimalFormat("##.######");

        // Coords and ra/decl
        //System.out.println("Ecliptic coords. xe/ye/ze: " +earth.getXe()+"/"+earth.getYe()+"/"+earth.getZe());
        //System.out.println("Equatorial coords, xeq/yeq/zeq: " +earth.getXeq()+"/"+earth.getYeq()+"/"+earth.getZeq());
        System.out.println("ra/decl (rads): " +df.format(this.getRightAsc())+"/"+df.format(this.getDecl()));

        // Julian day
        System.out.println("Mod. Julian Day: " +this.getJulDay());  //+ Constants.MJD1JAN2000ZERO
    }
    
    private static void J2000Check()
    {
        DecimalFormat df = new DecimalFormat("###.######");
        System.out.println("MARS:  J2000.00 calculation check");
        
        // J2000.00
        Mars mars = new Mars(TimeScale.modJulDayTime(2000, 1, 1, 0.d));
        
        // Right, get the ra and decl
        System.out.println("*** Expected ra/decl: [SwissEph: 22h:0m:34.71785884s/-13d:19m:21.26730596s]: " +
                df.format(Util.hrMinSec2DecRad(22.d, 0.d, 34.71785884d)) +"/"+
                df.format(Util.degMinSec2DecRad(-13.d, 19.d, 21.26730596d)));
        mars.raDecl();
        System.out.println("*** Calculated ra/decl: " +df.format(mars.getRightAsc())+"/"+df.format(mars.getDecl()));
        
        System.out.println("\r\nDistance checks, two values should be same: [" +df.format(mars.getHelioDist())+ " : " +df.format(mars.getDistChk()) +"]");
        System.out.println("Longitude of ascending node (deg) [NASA/Wikipedia: 49.558]: " +df.format(Math.toDegrees(mars.getLonANRad())));
        System.out.println("Longitude of perihelion (deg) [NASA: 336.04084]: " +df.format(Math.toDegrees(mars.getLonPRad())));
        System.out.println("Argument of perihelion (deg) [Wikipedia: 286.502]: " +df.format(Math.toDegrees(mars.getArgPRad())));
        System.out.println("Mean longitude (deg) [NASA: 355.45332]: " +df.format(Math.toDegrees(mars.getMeanLongRad())));
        System.out.println("Mean anomaly (deg) [Wikipedia: 358.617, stargazing.net: 336.0882]: " +df.format(Math.toDegrees(mars.getMeanAnomRad())));
        System.out.println("");
    }
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        // Initialise the checks
        ChkVals.cvInit();
        
        // Init Mars
        Mars mars = new Mars(TimeScale.modJulDayTime(2000, 1, 1, 0.d));
        System.out.println(ChkVals.toPrint(Constants.MARS, KeplerElement.LITTLEI, Constants.J2000) +Math.toDegrees(mars.getInclinRad()));
        
        // Epoch test
        J2000Check();
        
        //------------------------------------------------------
        // Other times
    }
}

/*
 * Mercury.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.TimeScale;
import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Mercury extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 48.330537095d,                      // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = -0.12534081d,
                                LITTLEI0 = 7.0050141996d,                       // Little i, inclination (deg)
                                LITTLEIDOT = -0.00594749d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 77.45779628d,                    // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.16047689d/Constants.JULDAYSPERC,
                                LITTLEA0 = 0.38709822527d,                      // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = 0.00000037d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.2056302512d,                       // Little e, Eccentricity
                                LITTLEEDOT = 0.00001906d/Constants.JULDAYSPERC,
                                BIGL0 = 252.25032350d,                          // Big L, mean longitude
                                BIGLDOT = 149472.67411175d/Constants.JULDAYSPERC,
                                LITTLEN = 1.3833d;                              // Daily motion

    
    /**
     * Set Mercury position for given day.
     * 
     * @param julDay 
     */
    public Mercury( double julDay )
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
     * Dump Mercury details
     */
    @Override
    public void dumpPlanet()
    {
        DecimalFormat df = new DecimalFormat("##.######");

        // Coords and ra/decl
        //System.out.println("Ecliptic coords. xe/ye/ze: " +earth.getXe()+"/"+earth.getYe()+"/"+earth.getZe());
        //System.out.println("Equatorial coords, xeq/yeq/zeq: " +earth.getXeq()+"/"+earth.getYeq()+"/"+earth.getZeq());
        System.out.println("Check against NASA...");
        System.out.println("");
        System.out.println("ra/decl: " +df.format(this.getRightAsc())+"/"+df.format(this.getDecl()));

        // Julian day
        System.out.println("Mod. Julian Day: " +this.getJulDay());  //+ Constants.MJD1JAN2000ZERO
    }
    
    public static void main(String[] args)
    {
        DecimalFormat df = new DecimalFormat("###.######");
        
        //------------------------------------------------------
        System.out.println("J2000.00 calculation check");
        Mercury mercury = new Mercury(TimeScale.modJulDayTime(2000, 1, 1, 0.d));
        
        // Right, calc the ra and decl
        mercury.raDecl();
        mercury.dumpPlanet();
        System.out.println("Longitude of ascending node (deg) [NASA: 48.33167]: " +df.format(Math.toDegrees(mercury.getLonANRad())));
        System.out.println("Longitude of perihelion (deg) [NASA: 77.45645]: " +df.format(Math.toDegrees(mercury.getLonPRad())));
        System.out.println("Argument of perifocus (deg) [Wikipedia: 29.12428]: " +df.format(Math.toDegrees(mercury.getArgPRad())));
        System.out.println("Mean longitude (deg) [Wikipedia: 252.25032350]: " +df.format(Math.toDegrees(mercury.getMeanLongRad())));
        System.out.println("Mean anomaly (deg) [Wikipedia: 174.796]: " +df.format(Math.toDegrees(mercury.getMeanAnomRad())));
        System.out.println("");
    }
}

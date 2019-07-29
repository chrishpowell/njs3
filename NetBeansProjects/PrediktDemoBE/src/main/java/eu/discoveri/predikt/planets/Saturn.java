/*
 * Saturn.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Saturn extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 113.66242448d,                      // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = -0.28867794d,
                                LITTLEI0 = 2.48599187d,                         // Little i, inclination (deg)
                                LITTLEIDOT = 0.00193609d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 92.59887831d,                    // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = -0.41897216d/Constants.JULDAYSPERC,
                                LITTLEA0 = 9.53667594d,                         // Little a, semi-major axis (AU)
                                LITTLEADOT = -0.00125060d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.05386179d,                         // Little e, Eccentricity
                                LITTLEEDOT = -0.00050991d/Constants.JULDAYSPERC,
                                BIGL0 = 49.95424423d,                           // Big L, mean longitude
                                BIGLDOT = 1222.49362201d/Constants.JULDAYSPERC,
                                LITTLEN = 0.0336d;                              // Daily motion
    
    /**
     * Set Saturn position for given day.
     * 
     * @param julDay 
     */
    public Saturn( float julDay )
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
     * Dump Pluto details
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
        
    }
}

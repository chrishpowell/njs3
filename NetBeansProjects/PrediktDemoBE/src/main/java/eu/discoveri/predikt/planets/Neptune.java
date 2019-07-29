/*
 * Neptune.
 */

package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Neptune extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 131.78422574d,                      // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = -0.00508664d,
                                LITTLEI0 = 1.77004347d,                         // Little i, inclination (deg)
                                LITTLEIDOT = 0.00035372d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 44.96476227d,                    // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = -0.32241464d/Constants.JULDAYSPERC,
                                LITTLEA0 = 30.06992276d,                        // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = 0.00026291d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.00859048d,                         // Little e, Eccentricity
                                LITTLEEDOT = 0.00005105d/Constants.JULDAYSPERC,
                                BIGL0 = -55.12002969d,                          // Big L, mean longitude
                                BIGLDOT = 218.45945325d/Constants.JULDAYSPERC,
                                LITTLEN = 0.006668d;                            // Daily motion
    
    /**
     * Set Neptune position for given day.
     * 
     * @param julDay 
     */
    public Neptune( float julDay )
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
     * Dump Neptune details
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

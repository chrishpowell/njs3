/*
 * Venus.
 */

package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Venus extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 76.67984255d,                       // Big omega, longitude of ascending node (deg)
                                BIGOMEGAMOVE = -0.27769418d,
                                LITTLEI0 = 3.39467605d,                         // Little i, inclination to ecliptic (deg)
                                LITTLEIDOT = -0.00078890d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 131.60246718d,                      // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.00268329d/Constants.JULDAYSPERC,
                                LITTLEA0 = 0.72333566d,                         // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = 0.00000390d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.00677672d,                         // Little e, Eccentricity
                                LITTLEEDOT = -0.00004107d/Constants.JULDAYSPERC,
                                BIGL0 = 181.97909950d,                           // Big L, mean longitude
                                BIGLDOT = 58517.81538729d/Constants.JULDAYSPERC,
                                LITTLEN = 1.2084673d;                              // Daily motion

    
    /**
     * Set Venus position for given day.
     * 
     * @param julDay 
     */
    public Venus( float julDay )
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
     * Dump Earth details
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


    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("###.######");
    }
}

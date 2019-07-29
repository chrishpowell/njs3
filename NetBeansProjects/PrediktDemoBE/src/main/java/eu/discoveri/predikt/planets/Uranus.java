/*
 * Uranus.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Uranus extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 74.01692503d,                                // Big omega, longitude of ascending node (deg)
                                BIGOMEGAMOVE = 0.04240589d,
                                LITTLEI0 = 0.77263783d,                        // Little i, inclination (deg)
                                LITTLEIDOT = -0.00242939d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 170.95427630d,                   // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.40805281d/Constants.JULDAYSPERC,
                                LITTLEA0 = 19.18916464d,                         // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = -0.00196176d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.04725744d,                         // Little e, Eccentricity
                                LITTLEEDOT = -0.00004397d/Constants.JULDAYSPERC,
                                BIGL0 = 313.23810451d,                          // Big L, mean longitude
                                BIGLDOT = 428.48202785d/Constants.JULDAYSPERC,
                                LITTLEN = 0.026666d;                            // Daily motion
    
    /**
     * Set Uranus position for given day.
     * 
     * @param julDay 
     */
    public Uranus( double julDay )
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
     * Dump Uranus details
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

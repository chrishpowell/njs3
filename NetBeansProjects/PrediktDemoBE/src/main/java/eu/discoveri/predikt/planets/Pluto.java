/*
 * Pluto.  Will require different method of calc.?
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import java.text.DecimalFormat;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Pluto extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 110.30393684d,                      // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = -0.01183482d,
                                LITTLEI0 = 17.14001206d,                       // Little i, inclination (deg)
                                LITTLEIDOT = 0.00004818d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 224.06891629d,                    // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = -0.04062942d/Constants.JULDAYSPERC,
                                LITTLEA0 = 39.48211675d,                      // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = -0.00031596d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.24882730d,                       // Little e, Eccentricity
                                LITTLEEDOT = 0.00005170d/Constants.JULDAYSPERC,
                                BIGL0 = 238.92903833d,                          // Big L, mean longitude
                                BIGLDOT = 145.20780515d/Constants.JULDAYSPERC,
                                LITTLEN = 0.0041666d;                              // Daily motion
    
    /**
     * Set Pluto position for given day.
     * 
     * @param julDay 
     */
    public Pluto( float julDay )
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

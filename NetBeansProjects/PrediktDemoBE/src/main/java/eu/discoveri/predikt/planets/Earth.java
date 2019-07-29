/*
 * Earth.
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
public class Earth extends Orbits
{
    // Initialisation, see: https://ssd.jpl.nasa.gov/txt/p_elem_t1.txt, https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
    private final static double BIGOMEGA0 = 0.d,                                // Big omega, longitude of ascending node (deg)
                                BIGOMEGAMOVE = 0.d,
                                LITTLEI0 = -0.00001531d,                        // Little i, inclination (deg)
                                LITTLEIDOT = -0.01294668d/Constants.JULDAYSPERC,
                                LITTLEOMEGA0 = 102.93768193d,                   // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.32327364d/Constants.JULDAYSPERC,
                                LITTLEA0 = 1.00000011d,                         // Little a, semi-major axis (AU) [1.00000261d elsewhere]
                                LITTLEADOT = 0.00000562d/Constants.JULDAYSPERC,
                                LITTLEE0 = 0.01671123d,                         // Little e, Eccentricity
                                LITTLEEDOT = -0.00004392d/Constants.JULDAYSPERC,
                                BIGL0 = 100.46457166d,                          // Big L, mean longitude
                                BIGLDOT = 35999.37244981d/Constants.JULDAYSPERC,
                                LITTLEN = 0.985555d;                            // Daily motion


    /**
     * Set Earth position for given day.
     * 
     * @param julDay 
     */
    public Earth( double julDay )
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
        
        //------------------------------------------------------
        System.out.println("EARTH/SUN: J2000.00 calculation check");
        
        // J2000.00
        Earth earth = new Earth(TimeScale.modJulDayTime(2000, 1, 1, 0.d));

        // Right, what's the ra and decl supposed to be?
        System.out.println( "*** Expected ra/decl for 1 Jan 2000 should be:\r\n (Heavens above)(18h:42m:57.s/-23h:4m:19.s) [" +
            df.format(Util.hrMinSec2DecRad(18.d, 42.d, 57.d)) +"/"+
            df.format(Util.degMinSec2DecRad(-23.d, 4.d, 19.d)) +"]"  );
        // Now calc the ra and decl
        earth.raDecl();
        System.out.println("*** Calculated ra/decl>> " +df.format(earth.getRightAsc())+"/"+df.format(earth.getDecl()));
        
        System.out.println("");
        earth.dumpPlanet();
        System.out.println("Distance checks, two values should be same: [" +df.format(earth.getHelioDist())+ " : " +df.format(earth.getDistChk()) +"]");
        System.out.println("Longitude of ascending node (deg) [NASA/Wikipedia: -11.26064 or zero?]: " +Constants.ELONGANODEDEG); //+df.format(Math.toDegrees(earth.getLonANRad())));
        System.out.println("Longitude of perihelion (deg) [NASA: 102.94719]: " +df.format(Math.toDegrees(earth.getLonPRad())));
        System.out.println("Argument of perihelion (deg) [Wikipedia: 114.20783]: " +df.format(Math.toDegrees(earth.getArgPRad())-Constants.ELONGANODEDEG));
        System.out.println("Mean longitude (deg) [NASA: 100.46435]: " +df.format(Math.toDegrees(earth.getMeanLongRad())));
        System.out.println("Mean anomaly (deg) [Wikipedia: 358.617, quae.nl: 357.5291]: " +df.format(Math.toDegrees(earth.getMeanAnomRad())));
        System.out.println("");
        
        
        //------------------------------------------------------
        // Summer solstice (2000-06-21 03:00:00UT)
        ChkVals.cvInit();
        
        // Set up earth
        earth = new Earth(TimeScale.modJulDayTime(2000, 6, 21, 0.08334d*24.d));
        earth.raDecl();
        
        System.out.println("\r\nSummer solstice\r\n===============");
        System.out.println("(2000-06-21 02:00:00UT)");
        // RA, Decl
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.RIGHTASC,Constants.SSOLTICEN) +
                df.format(Math.toDegrees(earth.getRightAsc())));
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.DECL,Constants.SSOLTICEN) +
                df.format(Math.toDegrees(earth.getDecl())));
        // r
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.HELIODIST,Constants.SSOLTICEN) +
                df.format(earth.getHelioDist()));
        // Ecliptic coords
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.XECL,Constants.SSOLTICEN) +
                df.format(earth.getXe()));
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.YECL,Constants.SSOLTICEN) +
                df.format(earth.getYe()));
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.ZECL,Constants.SSOLTICEN) +
                df.format(earth.getZe()));
        
        
        // Set up another earth date, recalc all julDay stuff
        earth.setJulDay(TimeScale.modJulDayTime(2019, 6, 21, 0.9375d*24.d));
        // Change of date, re-calc positions
        earth.raDecl();
        
        System.out.println("(2019-06-21 22:30:00UT)");
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.RIGHTASC,Constants.SSOLTICEN) +
                df.format(Math.toDegrees(earth.getRightAsc())));
        System.out.println(ChkVals.toPrint(Constants.SUN, KeplerElement.DECL,Constants.SSOLTICEN) +
                df.format(Math.toDegrees(earth.getDecl())));
        
        
        //------------------------------------------------------
        //... 7 Mar 2015
        System.out.println("\r\nFor 7Mar2015");
        System.out.println("=============");
        earth = new Earth(TimeScale.modJulDayTime(2015, 3, 7, 0.d));
        
        // Right, calc the ra and decl
        earth.raDecl();
        
        // ra and decl
        System.out.println( "ra/decl for 7 Mar 2015 should be:\r\n (SkyLive)(23h:8m:0.s/-5h:34m:6.s) [" +
                df.format(Util.hrMinSec2DecRad(23.d, 8.d, 0.d)) +"/"+
                df.format(Util.degMinSec2DecRad(-5.d, 34.d, 6.d)) +
                            "]\r\n (Heavens above)(23h:8m:1s/-5h:34m:8s) [" +
                df.format(Util.hrMinSec2DecRad(23.d, 8.d, 1.d)) +"/"+
                df.format(Util.degMinSec2DecRad(-5.d, 34.d, 8.d)) +
                            "]\r\n (AstroPixels)(23h:08m:46.96s/-5h:29m:14.4s) [" +
                df.format(Util.hrMinSec2DecRad(23.d, 8.d, 46.96d)) +"/"+
                df.format(Util.degMinSec2DecRad(-5.d, 29.d, 14.4d)) +
                            "]\r\n (SwissEph [swetest]) (23h:8m:46.78458002s/-5h:29m:15.52560251s) [" +
                df.format(Util.hrMinSec2DecRad(23.d, 8.d, 46.78458002d)) +"/"+
                df.format(Util.degMinSec2DecRad(-5.d, 29.d, 15.52560251d)) +"]"   );
                
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();


        //------------------------------------------------------
        //... 19 Apr 1990
        System.out.println("\r\nFor 19Apr1990");
        System.out.println("=============");
        earth = new Earth(TimeScale.modJulDayTime(1990, 4, 19, 0.d));
        
        // Right, calc the ra and decl
        earth.raDecl();

        // ra and decl
        System.out.println( "ra/decl for 19 Apr 1990 should be:\r\n (SkyLive)(1h:45m:26.7s/10h:53m:48.8s) [" +
                df.format(Util.hrMinSec2DecRad(1.d, 45.d, 26.7d)) +"/"+
                df.format(Util.degMinSec2DecRad(10.d, 53.d, 48.8d)) +
                            "]\r\n (Heavens above)(1h:47m:8s/11h:3m:17s) [" +
                df.format(Util.hrMinSec2DecRad(1.d, 47.d, 8.d)) +"/"+
                df.format(Util.degMinSec2DecRad(11.d, 3.d, 17.d)) +"]"     );
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();


        //------------------------------------------------------
        //... 19 Apr 1990
        System.out.println("\r\nFor 7Aug1997 11.00UT");
        System.out.println("====================");
        earth = new Earth(TimeScale.modJulDayTime(1997, 8, 7, 11.d));
        
        // Right, calc the ra and decl
        earth.raDecl();

        // ra and decl
        System.out.println( "ra/decl for 7 Aug 1997 11:00UT should be:\r\n (SkyLive)(9h:7m:23.8s/16h:30m:48.4s) [" +
                df.format(Util.hrMinSec2DecRad(9.d, 7.d, 23.8d)) +"/"+
                df.format(Util.degMinSec2DecRad(16.d, 30.d, 48.4d)) +
                            "]\r\n (Heavens above)(9h:9m:55s/16h:19m:55s) [" +
                df.format(Util.hrMinSec2DecRad(9.d, 9.d, 55.d)) +"/"+
                df.format(Util.degMinSec2DecRad(16.d, 19.d, 55.d)) +"]"     );
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();


        //------------------------------------------------------
        //... 1 Jan 2019
        System.out.println("\r\nFor 1Jan2019");
        System.out.println("================");
        earth = new Earth(TimeScale.modJulDayTime(2019, 1, 1, 0.d));
        
        // Right, calc the ra and decl
        earth.raDecl();
        
        // ra and decl
        System.out.println( "ra/decl for 1 Jan 2019 should be:\r\n (SkyLive)(18h:46m:5.6s/-23h:0m:48.8s) [" +
                df.format(Util.hrMinSec2DecRad(18.d, 46.d, 5.6d)) +"/"+
                df.format(Util.degMinSec2DecRad(-23.d, 0.d, 48.8d)) +
                            "]\r\n (Heavens above)(18h:43m:31s/-23h:3m:35s) [" +
                df.format(Util.hrMinSec2DecRad(18.d, 43.d, 31.d)) +"/"+
                df.format(Util.degMinSec2DecRad(-23.d, 3.d, 35.d)) +
                            "]\r\n (AstroPixels)(18h:44m:37.55s/-23h:3m:20.1s) [" +
                df.format(Util.hrMinSec2DecRad(18.d, 44.d, 37.55d)) +"/"+
                df.format(Util.degMinSec2DecRad(-23.d, 3.d, 20.1d)) +"]"             );
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();
        

        //------------------------------------------------------
        //... 1 Jan 2015 11:05UT
        System.out.println("\r\nFor 1Jan2015 11:05UT");
        System.out.println("=======================");
        earth = new Earth(TimeScale.modJulDayTime(2015, 1, 1, 11.0334d));
        
        // Right, calc the ra and decl
        earth.raDecl();
        
        // ra and decl
        System.out.println( "ra/decl for 1 Jan 2015 11:05UT should be:\r\n (SkyLive)(18h:45m:50.4s/-23h:1m:9.1s) [" +
                df.format(Util.hrMinSec2DecRad(18.d, 45.d, 50.4d)) +"/"+
                df.format(Util.degMinSec2DecRad(-23.d, 1.d, 9.1d)) +
                            "]\r\n (Heavens above)(18h:45m:39s/-23h:1m:23s) [" +
                df.format(Util.hrMinSec2DecRad(18.d, 45.d, 39.d)) +"/"+
                df.format(Util.degMinSec2DecRad(-23.d, 1.d, 23.d)) +"]"            );
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();
        

        //------------------------------------------------------
        //... 9 Oct 2017 00:00UT
        System.out.println("\r\nFor 9Oct2017");
        System.out.println("================");
        earth = new Earth(TimeScale.modJulDayTime(2017, 10, 9, 0.d));
        
        // Right, calc the ra and decl
        earth.raDecl();
        
        // ra and decl
        System.out.println( "ra/decl for 9 Oct 2017 00:00UT should be:\r\n (SkyLive)(12h:57m:29.8s/-6h:8m:37s) [" +
                df.format(Util.hrMinSec2DecRad(12.d, 57.d, 29.8d)) +"/"+
                df.format(Util.degMinSec2DecRad(-6.d, 8.d, 37.d)) +
                            "]\r\n (Heavens above)(12h:57m:40s/-6h:9m:33s) [" +
                df.format(Util.hrMinSec2DecRad(12.d, 57.d, 40.d)) +"/"+
                df.format(Util.degMinSec2DecRad(-6.d, 9.d, 33.d)) +
                            "]\r\n (AstroPixels)(12h:58m:33.01s/-6h:15m:4.5s) [" +
                df.format(Util.hrMinSec2DecRad(12.d, 58.d, 33.01d)) +"/"+
                df.format(Util.degMinSec2DecRad(-6.d, 15.d, 4.5d)) +"]"           );
        
        // Dump Earth data
        System.out.println("...but is:");
        earth.dumpPlanet();
    }
}

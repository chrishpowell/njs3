/*
 * Static constants
 */
package eu.discoveri.predikt.utils;

import java.time.LocalDateTime;
import java.awt.Color;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constants
{
    //... Graphics
    public static int           XVIEWBOX = 600;
    public static int           YVIEWBOX = 600;
    
    //... Resource path
    public static String        RESOURCEPATH = "/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/";
    
    // Counts
    public static int           NUMHOUSESCLASSIC = 12;
    public static int           NUMMAJORBODIES = 10;                            // See SSBody enum
    
    //... Planet degree 'overlap' and offset (to avoid overlap)
    public static double        POVERLAP = 3.d;
    public static double        POFFSET = 30.d;
    // Plaent plot size
    public static int           SCALEDP = 28;
    // Zodiac image scaled size
    public static int           SCALEDZ = 35;
    // Wheel logo scaled size
    public static int           SCALEDL = 30;
    // Circled numbers scaled size
    public static int           SCALEDC = 20;
    // Aspect images scaled size
    public static int           SCALEDA = 20;

    // Heavens extra radius
    public static double        SKYRAD = 80.d;
    // Circled number (cusp num.) radius
    public static double        CNRADIUS = 2.5d;
    // Aspect ring
    public static double        ASPOUTER = CNRADIUS*5.d;
    
    // Aspect colours
    public static Color         SQUAREC = Color.green.darker();
    public static Color         TRINEC = Color.blue;
    public static Color         SEXTILEC = Color.MAGENTA;
    public static Color         OPPOSITIONC = Color.red;
    public static Color         CONJUNCTIONC = Color.red;                // Should not be displayed
    
    //... Timescale constants
    public static int           TSDAYSINYR = 367;
    public static int           TSOFFSET = 730515;
    public static double        JDOFFSETCLASSIC = 0.5;
    public static double        MJD1JAN2000ZERO = 51544.d;
    public static double        JD1JAN2000ZERO = 2451545.d;
    public static double        JULDAYSPERC = 36525.d;
    public static LocalDateTime J2000 = LocalDateTime.parse("2000-01-01T00:00:00");
    public static LocalDateTime SSOLTICEN = LocalDateTime.parse("2000-06-21T03:00:00");
    public static double        RADS2HRS = 12.d / Math.PI;          // radians -> hours
    
    /**
     * The number of standard hours in one sidereal day.
     * Approximately 24.93.
     * @internal
     */
    public static final double SIDEREAL_DAY = 23.93446960027;
    
    /**
     * The number of sidereal hours in one mean solar day.
     * Approximately 24.07.
     * @internal
     */
    public static final double SOLAR_DAY =  24.065709816;
    
    /**
     * The average number of solar days from one new moon to the next.  This is the time
     * it takes for the moon to return the same ecliptic longitude as the sun.
     * It is longer than the sidereal month because the sun's longitude increases
     * during the year due to the revolution of the earth around the sun.
     * Approximately 29.53.
     *
     * @see #SIDEREAL_MONTH
     * @internal
     */
    public static final double SYNODIC_MONTH = 29.530588853;
    
    /**
     * The average number of days it takes
     * for the moon to return to the same ecliptic longitude relative to the
     * stellar background.  This is referred to as the sidereal month.
     * It is shorter than the synodic month due to
     * the revolution of the earth around the sun.
     * Approximately 27.32.
     *
     * @see #SYNODIC_MONTH
     * @internal
     */
    public static final double SIDEREAL_MONTH = 27.32166;
    
    /**
     * The average number number of days between successive vernal equinoxes.
     * Due to the precession of the earth's
     * axis, this is not precisely the same as the sidereal year.
     * Approximately 365.24
     *
     * @see #SIDEREAL_YEAR
     * @internal
     */
    public static final double TROPICAL_YEAR = 365.242191;
    
    /**
     * The average number of days it takes
     * for the sun to return to the same position against the fixed stellar
     * background.  This is the duration of one orbit of the earth about the sun
     * as it would appear to an outside observer.
     * Due to the precession of the earth's
     * axis, this is not precisely the same as the tropical year.
     * Approximately 365.25.
     *
     * @see #TROPICAL_YEAR
     * @internal
     */
    public static final double  SIDEREAL_YEAR = 365.25636;
    
    // For Util method
    public static boolean       LAT = true;                                         // Yes, it's a latitude
    public static boolean       LON = false;                                        // No, it's a longitude
    
    // Light
    public static final double  LIGHTSPEED = 299792.458;                            // km/s
    public static final double  LIGHTDISTPERDAY = LIGHTSPEED*86400;                 // How far in a day
    
    //... Orbital constants
    public static final double  CCIRCLE = 360.d;                                    // 360 degrees
    public static final double  ANORBITDEG = CCIRCLE;
    public static final double  TWOPI = Math.PI+Math.PI;                            // 2 pi
    public static final double  ANORBITRADS = TWOPI;                                // 2pi rads
    public static final double  DEGREESPERHR = 15.d;                                // Degrees per hour
    public static final double  PIDIV6 = Math.PI/6.d;                               // (30 degrees)
    public static final double  THIRTYDEGS = 30.d;                                  // 30 degrees
    public static final double  NINETYDEGREES = 90.d;                               // Right Angle, Ascendant etc.
    public static final double  EVENHOUSEANGLE = PIDIV6;                            // 12 houses even angles (=360/12)
    
    public static final double  AUKM = 149597870.7d;                                // AU fixed by IAU 2012
    public static final double  ETILT1JAN1900 = 23.452294444d;                      // Earth axis tilt
    public static final double  ETILT1JAN2000 = 23.4392911d;                        // Earth axis tilt
    public static final double  COSETILT = Math.cos(Math.toRadians(ETILT1JAN2000)); // Cosine of tilt
    public static final double  SINETILT = Math.sin(Math.toRadians(ETILT1JAN2000)); // Sine of tilt 
    public static final double  ETILTMOVE = 3.563e-7d;                              // Earth axis move per day
    public static final double  ELONGANODEDEG =  -11.26064;                         // Earth long. of asc. node (or zero!)
    
    public static final double  CUSPCONVERGE = 0.00005d;                            // Convergence in calc. of cusps
    public static final double  NORTHERNLATS = 1.160644d;                           // Arctic/Antarctic circle (rads)
    public static final double  NORTHERNLIMT = 1.1606d;                             // Arctic circle limit
    
    //... Ephemeris
    public static String        JPLEPHFILE = "";                                    // ASCII file from JPL
    
    //...Aspects (Note: Range is completely arbitrary)
    public static final String  CONJUNCTION = "conjunction";
    public static final int     CONJUNCTIONDIFF = 0;
    public static final int     CONJUNCTIONRANGE = 8;
    public static final String  SEMISEXTILE = "semisextile";
    public static final int     SEMISEXTILEDIFF = 30;
    public static final int     SEMISEXTILERANGE = 2;
    public static final String  SEMIQUINTILE = "semiquintile";
    public static final int     SEMIQUINTILEDIFF = 36;
    public static final int     SEMIQUINTILERANGE = 2;
    public static final String  SEMISQUARE = "semisquare";
    public static final int     SEMISQUAREDIFF = 45;
    public static final int     SEMISQUARERANGE = 3;
    public static final String  SEXTILE = "sextile";
    public static final int     SEXTILEDIFF = 60;
    public static final int     SEXTILERANGE = 3;
    public static final String  SQUARE = "square";
    public static final int     SQUAREDIFF = 90;
    public static final int     SQUARERANGE = 4;
    public static final String  TRINE = "trine";
    public static final int     TRINEDIFF = 120;
    public static final int     TRINERANGE = 6;
    public static final String  OPPOSITION = "opposition";
    public static final int     OPPOSITIONDIFF = 180;
    public static final int     OPPOSITIONRANGE = 8;
}

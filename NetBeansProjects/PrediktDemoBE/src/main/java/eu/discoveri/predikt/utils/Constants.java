/*
 * Static constants
 */
package eu.discoveri.predikt.utils;

import java.time.LocalDateTime;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constants
{
    //... Graphics
    public static int           XVIEWBOX = 400;
    public static int           YVIEWBOX = 400;
    
    //... Resource path
    public static String        RESOURCEPATH = "/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/";
    
    //... Planets
    public static String        SUN = "Sun";
    public static String        MERCURY = "Mercury";
    public static String        VENUS = "Venus";
    public static String        MARS = "Mars";
    public static String        JUPITER = "Jupiter";
    public static String        SATURN = "Saturn";
    public static String        URANUS = "Uranus";
    public static String        NEPTUNE = "Neptune";
    public static String        PLUTO = "Pluto";
    public static String        MOON = "Moon";
    
    //... Planet degree 'overlap' and offset (to avoid overlap)
    public static double        POVERLAP = 3.d;
    public static double        POFFSET = 0.15d;
    // Zodiac image size
    public static int           SCALEDZ = 35;
    
    //... Timescale constants
    public static int           TSDAYSINYR = 367;
    public static int           TSOFFSET = 730515;
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
    public static final double SIDEREAL_YEAR = 365.25636;
    
    //... Orbital constants
    public static final double  ANORBITDEG = 360.d;                                 // 360 degrees
    public static final double  ANORBITRADS = Math.PI*2.d;                          // 2pi rads
    public static final double  DEGREESPERHR = 15.d;                                // Degrees per hour
    public static final double  EVENHOUSEANGLE = Math.PI/6.d;                       // 12 houses even angles (=360/12)
    
    public static double        AUKM = 149597870.7d;                                // AU fixed by IAU 2012
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
}

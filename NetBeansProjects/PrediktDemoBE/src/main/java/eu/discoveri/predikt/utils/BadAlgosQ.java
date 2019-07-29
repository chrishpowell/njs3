/*
 * TStore these seemingly bad routines.
 */

package eu.discoveri.predikt.utils;

import static eu.discoveri.predikt.utils.TimeScale.modJulDayTime;
import static eu.discoveri.predikt.utils.TimeScale.realTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BadAlgosQ
{
    public static void siderealAlgos()
    {
        // aa.usno.navy.mil method, Circular 179 (Can't get either to work)
//        double T =  Util.mod(mjdt/36525.d,1.d);     // JD centuries
//        double du = (int)mjdt;                      // Whole JD days
//        double frac = mjdt - (double)(du);          // Frac part JD
//        double theta1 = 0.7790572732640 + 0.00273781191135448 * du + frac;
//        
//        // Seconds (time)
//        double gmstsec = 86400.d * theta1 +(0.014506 + 4612.156534*T + 1.3915817*T*T - 0.00000044*T*T*T)/15.d;
//        // Degrees in hours dec. (15/86400) 
//        double lmstsec = (gmstsec + 240.d * longitude);     // seconds (time)
//        double lmstdeg = lmstsec * 0.004166667d;            // degrees(hrs): 15/3600
//        
//        System.out.println("-->> (usno) Sidereal: " +Util.mod(lmstdeg, 24.d));
//
// quae.nl (accurate?) calc (for some places, eg: Utrecht) but significantly out for others!
//        double theta1 = 99.967794687d +
//                        mjdt * 360.98564736628603d +
//                        mjdt * mjdt * 2.907879e-13d -
//                        mjdt * mjdt * mjdt * 5.302e-22d +
//                        longitude;
//
    }
    
    /*
     * Don't do this!
     */
    public static double getDeltaT( LocalDateTime ldtUTC )
    {   
// radixpro.com method... Produces wildly wrong results.
        // deltaT
        double yr = ldtUTC.getYear();
        double hr = ldtUTC.getHour();
        double mn = ldtUTC.getMinute();
        double sc = ldtUTC.getSecond();

        double t = 0.d;
        double deltaT = 0.d;
        
        if( yr > 1900. && yr <= 1920. )
        {
            t = yr - 1900.d;
            deltaT = -2.79 + (1.494119 * t) - (0.0598939 * t*t) + (0.0061966 * t*t*t) - (0.000197 * t*t*t*t);
        }
            else
        if( yr > 1920. && yr <= 1941. )
        {
            t = yr - 1920.d;
            deltaT = 21.2 + (0.84493 * t) - (0.076100 * t*t) + (0.0020936 * t*t*t);
        }
            else
        if( yr > 1941. && yr <= 1961. )
        {
            t = yr - 1950.d;
            deltaT = 29.07 + (0.407 * t) - (t*t/233.) + (t*t*t/2547.);
        }
            else
        if( yr > 1961. && yr <= 1986. )
        {
            t = yr - 1975.d;
            deltaT =  45.45 + (1.067 * t) - (t*t/260.) - (t*t*t/718.);
        }
            else
        if( yr > 1986. && yr <= 2005. )
        {
            t = yr - 1975.d;
            deltaT = 63.86 +  (0.3345 * t) - (0.060374 * t*t) + (0.0017275 * t*t*t) + (0.000651814 * t*t*t*t) + 0.00002373599;
        }
            else
        if( yr > 2005. && yr <= 2050. )
        {
            t = yr - 2000.d;
            deltaT = 62.92 + (0.32217 * t) + (0.005589 * t*t);
        }
        
        return deltaT/3600. + hr + mn/60. + sc/3600.;
    }
    
    public static void deltatTests()
    {
        System.out.println("\r\nExpect: 21:18:39.5/21.31097");
        LocalDateTime ldtUTC = LocalDateTime.of(LocalDate.of(2016,11,2), LocalTime.of(21,17,30));
        double dt = getDeltaT( ldtUTC );
        System.out.println("---> deltat: " +dt);
        
        System.out.println("modJulDay: " +modJulDayTime(2106,11,2,21.31097));

        // A lot oif this is no good!
        double mjdt = modJulDayTime(ldtUTC);
        double ft = mjdt/36525.d;
        System.out.println("ft: " +ft);
        // Following is only a small difference to above (4th dec. place) but
        // results in a large diff. in end result
        ft = 0.168364134155d;

        double st0 = 100.46061837 + 36000.770053608*ft + 0.000387933*ft*ft - ft*ft*ft/38710000.d;
        double decHrs = Util.mod(st0, 360.d)/15.d;
        System.out.println("--> mjdt/ft/st0/decHrs: " +mjdt+"/"+ft+"/"+st0+"/"+decHrs);
        System.out.println("decHrs: " +decHrs);
        
        double st0corr = realTime(LocalTime.of(21,17,30)) * 1.00273790935;
        System.out.println("st0corr: " +st0corr);
        double actualUT = Util.mod(decHrs + st0corr,24.d);
        System.out.println("actualUT: " +actualUT);
        
        System.out.println("Sidereal time: " +(actualUT+6.9/15.));
    }
}

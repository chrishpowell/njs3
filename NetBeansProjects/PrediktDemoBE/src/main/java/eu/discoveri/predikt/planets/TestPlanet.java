/*
 * A test planet.
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;
import java.text.DecimalFormat;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TestPlanet
{
    /*
     * bigM, mean anomaly in degrees
     * e, eccentricity, a number from 0 to 1
     */
    private static double solveKepler( double bigM, double e )
    {
        double TOL = 1.e-6d;                            // limit/tolerance
        double deltaE = 1.d;                            // Initial value
        double deltaM;
        
        // e...
        double estar = 57.29578d * e;
        
        // mod so that -180 <= M <= 180.
        double M = Math.toDegrees(bigM) - 180.d;
        
        // Initial eccnAnom, E0 (deg)
        double En = M + estar * Math.sin(Math.toRadians(M));
        
        // Iterate until limit reached
        while( Math.abs(deltaE) > TOL )
        {
            deltaM = M - (En - estar * Math.sin(Math.toRadians(En)));
            deltaE = deltaM / (1 - e * Math.cos(Math.toRadians(En)));
            En = En + deltaE;
        }
        
        return En;
    }
        
    public static void main(String[] args)
    { // Mars
        DecimalFormat df = new DecimalFormat("###.######");
        
        // (Degrees)
        double julDay = 0.d;
        double bigOmegaInit = 49.55953891d;
        double bigOmegaM = 2.11081E-5;
        double littleiInit = 1.8497d;
        double littleiMove = 1.78E-8d;
        double argPinit = 286.5016d;
        double argPmove = 2.92961E-5d;
        double eccnInit = 0.093405d;
        double eccnMove = 2.516E-9d;
        double bigMinit = 18.6021d;
        double bigMmove = 0.5240207766d;
        
        // Long of asc node
        double N =  bigOmegaInit + bigOmegaM * julDay;
        
        // Inclination
        double i = littleiInit - littleiMove * julDay;
        
        // Arg. of perihelion
        double w = argPinit + argPmove * julDay;
        
        // AU
        double a = 1.523688d;
        
        // Eccentricity
        double e = eccnInit + eccnMove * julDay;
        
        // Mean anomaly
        double M =  bigMinit + bigMmove * julDay;
        
        // Eccentric anomaly
//        double Mrad = Math.toRadians(M);
//        double eA = M + e * (180.d/Math.PI) * Math.sin(Mrad) * (1.d + e * Math.sin(Mrad));
        double mRad = Math.toRadians(M);
        double eArad = solveKepler( mRad, e );
        
        // True anomaly and distance
        double xv = a * (Math.cos(eArad) - e);                                  // Not an angle
        double yv = a * (Math.sqrt(1.d - e*e) * Math.sin(e));                   // Not an angle
       
        double r = Math.sqrt(xv*xv + yv*yv); System.out.println(">>> r: " +r);
        double vRad = Math.atan2(yv, xv);
        
        // Ecliptic coords: Heliocentric for planets, geocentric for the Moon
        double Nrad = Math.toRadians(N);
        double wRad = Math.toRadians(w);
        double iRad = Math.toRadians(i);
        
        double xhRad = r * (Math.cos(Nrad) * Math.cos(vRad+wRad) - Math.sin(Nrad) * Math.sin(vRad+wRad) * Math.cos(iRad));
        double yhRad = r * (Math.sin(Nrad) * Math.cos(vRad+wRad) + Math.cos(Nrad) * Math.sin(vRad+wRad) * Math.cos(iRad));
        double zhRad = r * (Math.sin(vRad+wRad) * Math.sin(iRad));
        
//        System.out.println(">>> Coords check: r: " +r+ ", coords sqrt: " +Math.sqrt(xhRad*xhRad+yhRad*yhRad+zhRad*zhRad));
        
//        double lonecl = Math.atan2(yhRad,xhRad);
//        double latecl = Math.atan2(xhRad*xhRad, yhRad*yhRad);

        // Obliquity (determines view from Earth) [Don't need to mod as always less than 2*pi]
        double obliqJ2000 = Math.toRadians(Constants.ETILT1JAN2000 + (Constants.ETILTMOVE * julDay));
        
        double xeqRad = xhRad;
        double yeqRad = yhRad * Math.cos(obliqJ2000) - zhRad * Math.sin(obliqJ2000);
        double zeqRad = yhRad * Math.sin(obliqJ2000) + zhRad * Math.cos(obliqJ2000);

        double ra = Util.mod(Math.atan2(yeqRad,xeqRad),Constants.ANORBITRADS);
        double decl = Math.atan2(zeqRad, Math.sqrt(xeqRad*xeqRad+yeqRad*yeqRad));
        
        System.out.println("ra/decl (SwissEph)[22h:0m:34.71785884s/-13d:19m:21.26730596s]: " +
            df.format(Util.hrMinSec2DecRad(22.d, 0.d, 34.71785884d)) +"/"+
            df.format(Util.degMinSec2DecRad(-13.d, 19.d, 21.26730596d)) );
        System.out.println("ra/decl: " +df.format(ra)+ "/" +df.format(decl));
    }
}

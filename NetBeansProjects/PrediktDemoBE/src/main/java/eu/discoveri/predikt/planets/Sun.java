/*
 * Sun position
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.TimeScale;
import java.text.DecimalFormat;
import eu.discoveri.predikt.utils.Constants;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Sun extends Orbits
{
    private double          xv, yv,                             // Rect. coords
                            v, r,                               // Dist. and true anomaly
                            xs, ys, zs,                         // ecliptic rectangular geocentric coords
                            xe, ye, ze,                         // equatorial rectangular geocentric coords
                            rightAsc, decl;
                            
    private final static double BIGOMEGA0 = 0.d,                // Big omega, longitude of ascending node(deg)
                                BIGOMEGAMOVE = 0.d,
                                LITTLEI0 = -0.00001531d,         // Little i, inclination (deg)
                                LITTLEIDOT = -0.01294668d/36525.d,
                                LITTLEOMEGA0 = 102.93768193d,   // Little omega, lon of perihelion (deg)
                                LITTLEOMEGADOT = 0.32327364d/36525.d,
                                LITTLEA0 = 1.00000261d,         // Little a, semi-major axis (AU)
                                LITTLEADOT = 0.00000562d/36525.d,
                                LITTLEE0 = 0.01671123d,         // Little e, Eccentricity
                                LITTLEEDOT = -0.00004392d/36525.d,
                                BIGL0 = 100.46457166d,          // Big L, mean longitude
                                BIGLDOT = 35999.37244981d/36525.d;
    
    /**
     * Set Sun position for given day.
     * 
     * @param julDay 
     */
    public Sun( double julDay )
    {
        super( BIGOMEGA0, BIGOMEGAMOVE,
               LITTLEI0, LITTLEIDOT,
               LITTLEOMEGA0, LITTLEOMEGADOT,
               LITTLEA0, LITTLEADOT,
               LITTLEE0, LITTLEEDOT,
               BIGL0, BIGLDOT,
               julDay );
    }
    
    /**
     * Eccentric anomaly (viewed from Earth)
     * @return 
     */
    public double eccentricAnomRad()
    {
        double m = Orbits.mod(this.getMeanAnomRad(),Constants.ANORBITRADS);
        double e = Orbits.mod(this.getEccnRad(),Constants.ANORBITRADS);
        
        return Orbits.mod(m + e * Math.sin(m) * (1.d + e * Math.cos(m)),Constants.ANORBITRADS);
    }
    
    /**
     * Sun's position (r,v)
     */
    public void posnSun()
    {
        double e = Orbits.mod(this.getEccnRad(),Constants.ANORBITRADS);
        double eA = Orbits.mod(this.eccentricAnomRad(),Constants.ANORBITRADS);
        
        // Rectangular coords
        xv = Math.cos(eA) - e;
        yv = Math.sqrt(1.f - e*e) * Math.sin(eA);
        
        v = Orbits.mod(Math.atan2(yv, xv),Constants.ANORBITRADS);
        r = Math.sqrt(xv*xv + yv*yv);
    }
    
    // Equatorial coords
    public void geocentricPosn()
    {
        // Calculate v,r
        this.posnSun();
        
        // Sun lon position
        double lonSun = Orbits.mod(v + this.getArgPRad(),Constants.ANORBITRADS);
        
        xs = r * Math.cos(lonSun);
        ys = r * Math.sin(lonSun);
        zs = 0.f;
    }
    
    public void equatorialPosn()
    {
        //Get the oblique ecliptic
        double ecl = this.obliqueEclipticRad( getJulDay() );
        
        // Calc geocentric, set xs,ys,zs
        this.geocentricPosn();
        
        xe = xs;
        ye = ys * Math.cos(ecl) - zs * Math.sin(ecl);
        ze = ys * Math.sin(ecl) + zs * Math.cos(ecl);
    }
    
    public void radeclPosn()
    {
        // Get the equatorial position: set xe,ye,ze
        this.equatorialPosn();
        
        rightAsc = Orbits.mod(Math.atan2(ye,xe),Constants.ANORBITRADS);
        decl = Math.atan2(ze, Math.sqrt(xe*xe+ye*ye));
    }
    
    /**
     * Dump Sun details
     * @param sun 
     */
    public static void dumpSun( Sun sun )
    {
        DecimalFormat df = new DecimalFormat("##.######");
        
        // Julian day
        System.out.println("Julian Day: " +sun.getJulDay());
        
        // Right, calc the ra and decl
        sun.radeclPosn();
        
        // Show some intermediate calcs
        System.out.println("Oblique ecliptic: " +sun.obliqueEclipticRad(sun.getJulDay()));
        System.out.println("LonPRad: " +sun.getLonPRad());
        System.out.println("InclinRad: " +sun.getInclinRad());
        System.out.println("ArgPRad: " +sun.getArgPRad());
        System.out.println("SemiMaj: " +sun.getSemiMaj());
        
        // Anomalies
        System.out.println("Mean anom: " +sun.getMeanAnomRad());
        System.out.println("Ecc anom and mean anom: " +sun.eccentricAnomRad()+", "+sun.getEccnRad());
        
        // Coords
        System.out.println("Rect. coords xv,yv: " +sun.getXv()+","+sun.getYv());
        System.out.println("Dist. and true anomaly v,r: " +sun.getV()+", "+sun.getR());
        System.out.println("Geocentric posn, xs/ys/zs: " +sun.getXs()+"/"+sun.getYs()+"/"+sun.getZs());
        System.out.println("Equatorial posn, xe/ye/ze: " +sun.getXe()+"/"+sun.getYe()+"/"+sun.getZe());
        
        // ra and decl
        System.out.println("ra/decl: " +df.format(sun.getRightAsc())+"/"+df.format(sun.getDecl()));
    }

    /*
     * Getters
     */
    public double getXv() { return xv; }
    public double getYv() { return yv; }
    public double getV() { return v; }
    public double getR() { return r; }
    public double getXs() { return xs; }
    public double getYs() { return ys; }
    public double getZs() { return zs; }

    
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        DecimalFormat df = new DecimalFormat("##.######");
        
        //... 7 Mar 2015
        System.out.println("\r\nFor 7Mar2015");
        System.out.println("=============");
        Sun sun = new Sun(TimeScale.modJulDayTime(2015, 3, 07, 0.d));
        // ra and decl
        System.out.println("[ra/decl for 7 Mar 2015 should be (23h:11m:33s/-5:11:38)] " +
                df.format(Orbits.hrMinSec2DecRad(23.d, 11.d, 33.d))+"/"+
                df.format(Orbits.degMinSec2DecRad(-5.d, -11.d, -38.d)) );
        // Dump Sun data
        Sun.dumpSun(sun);

        //... 19 Apr 1990
        System.out.println("\r\nFor 19Apr1990");
        System.out.println("=============");
        sun = new Sun(TimeScale.modJulDayTime(1990, 4, 19, 23.5999d));
        // ra and decl
        System.out.println("[ra/decl for 19 Apr 1990 should be (1h:46m:36.0s/11d:0m:22s)] " +
                df.format(Orbits.hrMinSec2DecRad(1.d, 46.d, 36.d))+"/"+
                df.format(Orbits.degMinSec2DecRad(11.d, 0.d, 22.d)) );
        // Dump Sun data
        Sun.dumpSun(sun);
        
        //... 19 Apr 1990
        System.out.println("\r\nFor 7Aug1997 11.00UT");
        System.out.println("====================");
        sun = new Sun(TimeScale.modJulDayTime(1997, 8, 7, 11.d));
        // ra and decl
        System.out.println("[ra/decl for 7 Aug 1997 11:00 UT should be (9h:9m:45.347/16d:20m:30.89s)] " +
                df.format(Orbits.hrMinSec2DecRad(9.d, 9.d, 45.347d))+"/"+
                df.format(Orbits.degMinSec2DecRad(16.d, 20.d, 30.89d)) );
        // Dump Sun data
        Sun.dumpSun(sun);
        
        //... 1 Jan 2019
        System.out.println("\r\nFor 1Jan2019");
        System.out.println("================");
        sun = new Sun(TimeScale.modJulDayTime(2019, 1, 1, 0.d));
        // ra and decl
        System.out.println("[ra/decl for 1 Jan 2019 should be (SE)(10h:15m:24s/0d:0m:0s)] " +
                df.format(Orbits.hrMinSec2DecRad(10.d, 15.d, 24.d))+"/"+
                df.format(Orbits.degMinSec2DecRad(0.d, 0.d, 0.d)) );
        // Dump Sun data
        Sun.dumpSun(sun);
    }
}

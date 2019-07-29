/*
 * Orbital elements
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Orbits1
{
    private double  lonAN,                                          // longitude of ascending node
                    inclin,                                         // inclination to ecliptic
                    argP,                                           // Argument of perihelion
                    semiMaj,                                        // semi major axis, mean dist. from Sun (AU)
                    eccn,                                           // eccentricity, 0=circle, (0..1)=ellipse, 1=parabola (big E)
                    meanAnom,                                       // mean anomaly (0 at perhelion, increases uniformly with time by 360 deg per orbit)
                    trueAnom,                                       // true anomaly (angle between position and perihelion, viewed from Sun) accel/decel
                    eccnAnom;                                       // eccentric anomaly
    private double  julDay;

    // Calculated values
    private final double    lonP,                                       // longitude of perihelion
                            meanLon,                                    // mean longitude
                            pDist,                                      // perihelion distance
                            aDist,                                      // aphelion distance
                            orbPeriod,                                  // orb period (years, if AU)
                            timePeri;                                   // time of perihelion

    /**
     * Constructor for this orbit (this day).
     * 
     * @param lonANinit
     * @param lonANmove
     * @param inclinInit
     * @param inclinMove
     * @param argPinit
     * @param argPmove
     * @param semiMajInit
     * @param semiMajMove
     * @param eccnInit
     * @param eccnMove
     * @param meanAnomInit
     * @param meanAnomMove
     * @param julDay 
     */
    public Orbits1(  double lonANinit, double lonANmove,
                    double inclinInit, double inclinMove,
                    double argPinit, double argPmove,
                    double semiMajInit, double semiMajMove,
                    double eccnInit, double eccnMove,
                    double meanAnomInit, double meanAnomMove,
                    double julDay )
    {
        // Convert all to radians
        double bigOmegaInit = Math.toRadians(lonANinit);
        double bigOmegaMove = Math.toRadians(lonANmove);
        double iiInit = Math.toRadians(inclinInit);
        double iiMove = Math.toRadians(inclinMove);
        double litOmegaInit = Math.toRadians(argPinit);
        double litOmegaMove = Math.toRadians(argPmove);
        double smInit = Math.toRadians(semiMajInit);
        double smMove = Math.toRadians(semiMajMove);
        double bigEint = Math.toRadians(eccnInit);
        double bigEmove = Math.toRadians(eccnMove);
        double bigMinit = Math.toRadians(meanAnomInit);
        double bigMmove = Math.toRadians(meanAnomMove);
        
        
        // Now calc some variables (in rads mod PI*2)
        this.lonAN = mod(bigOmegaInit + (bigOmegaMove * julDay),Constants.ANORBITRADS);
        this.inclin = mod(iiInit + (iiMove * julDay),Constants.ANORBITRADS);
        this.argP = mod(litOmegaInit + (litOmegaMove * julDay),Constants.ANORBITRADS);
        this.semiMaj = smInit + (smMove * julDay);
        this.eccn = mod(bigEint + (bigEmove * julDay),Constants.ANORBITRADS);
        this.meanAnom = mod(bigMinit + (bigMmove * julDay),Constants.ANORBITRADS);
        this.julDay = julDay;
        
        this.lonP = mod(lonAN + argP,Constants.ANORBITRADS);                    // longitude of perihelion
        this.meanLon = mod(meanAnom + lonP,Constants.ANORBITRADS);              // mean longitude
        this.pDist = semiMaj * (1.f - eccn);                                    // perihelion distance
        this.aDist = semiMaj * (1.f + eccn);                                    // aphelion distance
        this.orbPeriod = (float)Math.pow(semiMaj,1.5);                          // orb period (years, if AU)
        this.timePeri = meanAnom/Constants.ANORBITRADS/orbPeriod;               // time of perihelion
    }
    

    
    /**
     * Calculate mod (incl. for negatives).
     * 
     * @param in
     * @param mod
     * @return 
     */
    public static double mod( double in, double mod )
    {
        double rem = in%mod;
        
        if( rem < 0.f )
            return mod + rem;
        else
            return rem;
    }
    
    /**
     * 'Tilt' of Earth axis (around 23.4 degrees)
     * 
     * @param jDay
     * @return 
     */
    public double obliqueEclipticRad( double jDay )
    {
       return Math.toRadians(Constants.ETILT1JAN2000 - jDay * Constants.ETILTMOVE);
    }
    
    /**
     * Convert ra, decl etc. to decimal.
     * Nota bene: if entry is, eg: -5:11:30, the entry needs to be -5, -11, -30
     * 
     * @param hrdeg
     * @param min
     * @param sec 
     * @return decimal degrees
     */
    public static double hrdegMinSec2DecDeg( double hrdeg, double min, double sec )
    {
        return hrdeg + min/60.d + sec/3600.d;
    }
    
    /**
     * 
     * @param deg
     * @param min
     * @param sec
     * @return deg:min:sec to decimal radians
     */
    public static double degMinSec2DecRad( double deg, double min, double sec )
    {
        return Math.toRadians(hrdegMinSec2DecDeg(deg,min,sec));
    }
    
    /**
     * 
     * @param hr
     * @param min
     * @param sec
     * @return hr:min:sec to decimal radians
     */
    public static double hrMinSec2DecRad( double hr, double min, double sec )
    {
        return Math.toRadians(hrdegMinSec2DecDeg(hr,min,sec)*Constants.DEGREESPERHR);
    }
    


    //... Get data
    public double getLonPRad() { return lonP; }
    public double getInclinRad() { return inclin; }
    public double getArgPRad() { return argP; }
    public double getSemiMaj() { return semiMaj; }
    public double getEccnRad() { return eccn; }
    public double getMeanAnomRad() { return meanAnom; }
    public double getEccnAnomRad() { return eccnAnom; }
    public double getMeanLonRad() { return meanLon; }
    
    // Julian day alters all
    public double getJulDay() { return julDay; }
    public void setJulDay( double julDay ) { this.julDay = julDay; }
    
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        System.out.println("Mod function 10. mod 5. should be zero: "+mod(10.f,5.f));
        System.out.println("Mod function 7. mod 5. should be 2.: "+mod(7.f,5.f));
        System.out.println("Mod function 12.3 mod 5. should be 2.3: "+mod(12.3f,5.f));
        System.out.println("Mod function -12.3 mod 5. should be 2.7: "+mod(-12.3f,5.f));
        System.out.println("Mod function -3135.9347 mod 360. should be 104.0652: "+mod(-3135.9347f,360.f));
        System.out.println("Mod function -54.7324 mod 2*pi should be 1.81866: " +mod(-54.7324f,6.283185f));
        System.out.println("");
        System.out.println("Degrees to radians -3135.9347 deg to radians should be -54.7324: " +Math.toRadians(-3135.9347f));
        System.out.println("Degrees to radians 104.0652 deg to radians should be 1.8265: " +Math.toRadians(104.652f));
        System.out.println("Degrees to radians 26.8388 deg to radians should be 0.4684: " +Math.toRadians(26.8388f));
    }
}

/*
 * Orbital elements
 */
package eu.discoveri.predikt.planets;

import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;
import java.text.DecimalFormat;
import javafx.geometry.Point3D;


/**
 * For details, see: https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf
 * See also: https://arxiv.org/pdf/1609.00915.pdf
 * 
 * Generally RA and Declination (raDecl method) only is reqd.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public abstract class Orbits implements Planet
{
    // Input values
    private double          bigOmegaInit,
                            bigOmegaMove,
                            iiInit,
                            iiMove,
                            litOmegaInit,
                            litOmegaMove,
                            litAInit,
                            litAMove,
                            bigEint,
                            bigEmove,
                            bigLinit,
                            bigLmove,
                            littleN;
    private double          julDay;                                         // Mod. Julian day J2000.00
    
    // Calculated input values (modified by movement/drift per day)
    private double          lonAN,                                          // longitude of ascending node
                            inclin,                                         // inclination to ecliptic
                            lonP,                                           // Longitude of perihelion
                            semiMaj,                                        // semi major axis, mean dist. from Sun (AU)
                            eccn,                                           // eccentricity, 0=circle, (0..1)=ellipse, 1=parabola (big E)
                            meanLong;                                       // mean longitude

    // Calculated values
    private double          argP = 6.5d,                                    // argument of perihelion (> 6.28 radians is an error)
                            bigM,                                           // mean anomaly (0 at perhelion, increases uniformly with time by 360 deg per orbit)
                            trueAnom,                                       // true anomaly (angle between position and perihelion, viewed from Sun) accel/decel
                            eccnAnom,                                       // eccentric anomaly (degrees)
                            pDist = 0.0d,                                   // perihelion distance
                            aDist = 0.0d,                                   // aphelion distance
                            orbPeriod = 0.0d,                               // orb period (years, if AU)
                            timePeri = 0.0d,                                // time of perihelion
                            obliqJ2000;                                     // Axis obliquity
    
    // Coordinates (Note: Spherical coords are X(=RA), Y(=Decl), Z(=r)). 
    private double          xprime, yprime, zprime;                         // Heliocentric
    private double          xe, ye, ze;                                     // Ecliptic
    private double          xeq, yeq, zeq;                                  // Equatorial
    private double          r, vRad;                                        // Dist. and true anomaly
    private double          rightAsc=0.d, decl=0.d;                         // Right ascension and declination
    private Point3D         rectCoords;                                     // Rectangular coords
    private Point3D         sphCoords;                                      // Spherical coords



    /**
     * Constructor for this orbit (this day).
     * 
     * Initial values at J2000.00 plus change per day
     * 
     * @param lonANinit     big omega, longitude of ascending node
     * @param lonANmove
     * @param inclinInit    inclination
     * @param inclinMove
     * @param lonPinit      little omega, long. of perifocus
     * @param lonPmove
     * @param semiMajInit   little a, semi-major axis
     * @param semiMajMove
     * @param eccnInit      big E, eccentricity
     * @param eccnMove
     * @param bigLInit      big L, mean longitude
     * @param bigLMove
     * @param littleNinit   Daily motion (deg)
     * @param julDay        modified Julian day (midnight not midday)
     */
    public Orbits(  double lonANinit, double lonANmove,
                    double inclinInit, double inclinMove,
                    double lonPinit, double lonPmove,
                    double semiMajInit, double semiMajMove,
                    double eccnInit, double eccnMove,
                    double bigLInit, double bigLMove,
                    double littleNinit,
                    double julDay )
    {
        // Convert all to radians
        bigOmegaInit = Math.toRadians(lonANinit);
        bigOmegaMove = Math.toRadians(lonANmove);
        iiInit = Math.toRadians(inclinInit);
        iiMove = Math.toRadians(inclinMove);
        litOmegaInit = Math.toRadians(lonPinit);
        litOmegaMove = Math.toRadians(lonPmove);
        litAInit = semiMajInit;
        litAMove = semiMajMove;
        bigEint = eccnInit;
        bigEmove = eccnMove;
        bigLinit = Math.toRadians(bigLInit);
        bigLmove = Math.toRadians(bigLMove);
        this.littleN = Math.toRadians(littleNinit);
        
        // Julian day
        this.julDay = julDay;
        
        // Calc all julDay moves
        calcJulDayMoves();
    }

    /**
     * Calculate julDay (osculating) movements.
     */
    public final void calcJulDayMoves()
    {
        // Now calc the osculating elements (in rads mod PI*2)
        this.lonAN = Util.mod(bigOmegaInit + (bigOmegaMove * julDay),Constants.ANORBITRADS);
        this.inclin = Util.mod(iiInit + (iiMove * julDay),Constants.ANORBITRADS);
        this.lonP = Util.mod(litOmegaInit + (litOmegaMove * julDay),Constants.ANORBITRADS);
        this.semiMaj = litAInit + (litAMove * julDay);
        this.eccn = bigEint + (bigEmove * julDay);
        this.meanLong = Util.mod(bigLinit + (bigLmove * julDay),Constants.ANORBITRADS);
        
        // Obliquity (determines view from Earth) [Don't need to mod as always less than 2*pi]
        this.obliqJ2000 = Math.toRadians(Constants.ETILT1JAN2000 + (Constants.ETILTMOVE * julDay));
    }
    
    /**
     * Calculate eccentric anomaly (in degrees).
     * (using argument of perifocus and mean anomaly)
     * 
     * @return 
     */
    public double eccentricAnomaly()
    {
        // Argument of perifocus (perihelion): lon of perifocus - lon of ascending node
        this.argP = Util.mod(lonP - lonAN,Constants.ANORBITRADS);
        // Mean anomaly (bigM).  [Last three terms of equation ignored being for Jupiter etc. to 3000AD)
        this.bigM = Util.mod(meanLong - argP,Constants.ANORBITRADS);            // Good to 2050 [ + littleN * julDay]
        
        // Solve Kepler for eccentric anomaly (returns degrees)
        this.eccnAnom = solveKepler( this.bigM, this.eccn );                    // Eccentric anomaly
        
        return this.eccnAnom;
    }

    /**
     * Calculate heliocentric coords.
     * 
     * Outlook from centre of Sun.
     * Coords are in orbital plane with xprime aligned from focus to perihelion.
     * (from eccentric anomaly)
     */
    public void heliocentricCoords()
    {
        // Eccentric anomaly (degrees back to radians)
        double eArad = Math.toRadians(eccentricAnomaly());
        
        // Heliocentric coords
        xprime = this.semiMaj * (Math.cos(eArad) - this.eccn);
        yprime = this.semiMaj * Math.sqrt(1.d - this.eccn*this.eccn) * Math.sin(eArad);
        zprime = 0.d;
        
        this.r = Math.sqrt(xprime*xprime + yprime*yprime);
        this.vRad = Math.atan2(yprime, xprime);
    }
    
    /**
     * Calculate ecliptic coords.
     * Coords are in J2000 ecliptic plane (plane of Earth's orbit) with x-axis
     * aligned towards equinox.  The principal coordinate system for ancient
     * astronomy and is still useful for computing the apparent motions of the
     * Sun, Moon, and planets
     * (derived from heliocentric coords)
     */
    public void eclipticCoords()
    {
        // Ecliptic coords setup (rotation via litte omega, big omega and inclination)
        double sinwsinO = Math.sin(this.lonP)*Math.sin(this.lonAN);
        double sinwcosO = Math.sin(this.lonP)*Math.cos(this.lonAN);
        double coswcosO = Math.cos(this.lonP)*Math.cos(this.lonAN);
        double coswsinO = Math.cos(this.lonP)*Math.sin(this.lonAN);
        double cosI = Math.cos(this.inclin);
        double sinI = Math.sin(this.inclin);
        
        // Get heliocentric coords
        heliocentricCoords();
        
        // Calc. equatorial coords
        xe = (coswcosO - sinwsinO*cosI) * xprime + (-sinwcosO - coswsinO*cosI) * yprime;
        ye = (coswsinO + sinwcosO*cosI) * xprime + (-sinwsinO + coswcosO*cosI) * yprime;
        ze = Math.sin(this.lonP) * sinI * xprime + Math.cos(this.lonP) * sinI * yprime;
    }
    
    /**
     * Calculate equatorial coords.
     * http://astronomy.swin.edu.au/cosmos/E/Equatorial+Coordinate+System
     * Projection of earth lat/lon onto celestial sphere.  (Lat.:Decl, Long:RA).
     * Modern star maps invariably use this coord system.
     * (derived from obliquity and ecliptic coords)
     */
    public void equatorialCoords()
    {
        // Get ecliptic coords
        eclipticCoords();
        
        // Calc equatorial coords
        xeq = xe;
        yeq = ye * Math.cos(this.obliqJ2000) - ze * Math.sin(this.obliqJ2000);
        zeq = ye * Math.sin(this.obliqJ2000) + ze * Math.cos(this.obliqJ2000);
    }
    
    /**
     * Calculate Right Ascension and Declination (rads)
     * (from equatorial coords)
     */
    public void raDecl()
    {
        // Get the equatorial coords
        equatorialCoords();
        
        // Calc. ra/decl
        rightAsc = Util.mod(Math.atan2(yeq,xeq),Constants.ANORBITRADS);
        decl = Math.atan2(zeq, Math.sqrt(xeq*xeq+yeq*yeq));
    }
    
    /**
     * Calculate rectangular coords from RA, Decl and r. raDecl() should have been
     * called beforehand for meaningful values.
     * 
     * @return x,y,z coords
     */
    public Point3D rectCoords()
    {
        double cosRA = Math.cos(this.rightAsc);
        double cosDecl = Math.cos(this.decl);

        double x = this.r * cosRA * cosDecl;
        double y = this.r * Math.sin(this.rightAsc) * cosDecl;
        double z = this.r * Math.sin(this.decl);
        
        this.rectCoords = new Point3D(x,y,z);
        return this.rectCoords;
    }
    
    /**
     * Set spherical coords from RA, Decl and r.  raDecl() should have been
     * called beforehand for meaningful values.
     * 
     * Note:
     *   X (getx()) eqv. to RA
     *   Y (getY()) eqv. to Decl
     *   Z (getZ()) eqv. to r
     * 
     * @return 
     */
    public Point3D sphCoords()
    {
        this.sphCoords = new Point3D( this.rightAsc, this.decl, this.r );
        return this.sphCoords;
    }
    
    /**
     * Calculate Perihelion dist., Aphelion dist., Orbital period, Perihelion time
     */
    public void miscCalcs()
    {
        this.pDist = semiMaj * (1.f - eccn);                                    // perihelion distance
        this.aDist = semiMaj * (1.f + eccn);                                    // aphelion distance
        this.orbPeriod = (float)Math.pow(semiMaj,1.5);                          // orb period (years, if AU)
        this.timePeri = meanLong/Constants.ANORBITRADS/orbPeriod;               // time of perihelion
    }

    /*
     * Solve Kepler (using Newton) to get eccentric anomaly. bigM is mean
     * anomaly (rads), e eccentricity (rads).
     *
     * For details, see: https://ssd.jpl.nasa.gov/txt/aprx_pos_planets.pdf and
     * https://arxiv.org/pdf/1609.00915.pdf (Kepler equation)
     */
    private double solveKepler( double bigM, double e )
    {
        double TOL = 1.e-6d;                            // limit/tolerance
        double deltaE = 1.d;                            // Initial value
        double deltaM;
        
        // estar has to be in degrees
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


    //... Get data
    public double getLonPRad() { return lonP; }
    public double getLonANRad() { return lonAN; }
    public double getInclinRad() { return inclin; }
    public double getArgPRad() { return argP; }
    public double getSemiMaj() { return semiMaj; }
    public double getEccnRad() { return eccn; }
    public double getMeanLongRad() { return meanLong; }
    public double getMeanAnomRad() { return bigM; }
    public double getEccnAnomRad() { return eccnAnom; }
    public double getOrbPeriod() { return orbPeriod; }
    public double getTimePeri() { return timePeri; }
    public double getObliqJ2000() { return obliqJ2000; }
    public double getXprime() { return xprime; }
    public double getYprime() { return yprime; }
    public double getZprime() { return zprime; }
    public double getXe() { return xe; }                                        // Ecliptic
    public double getYe() { return ye; }
    public double getZe() { return ze; }
    public double getXeq() { return xeq; }                                      // Equatorial
    public double getYeq() { return yeq; }
    public double getZeq() { return zeq; }
    public double getHelioDist() { return r; }
    public double getDistChk() { return Math.sqrt(xe*xe+ye*ye+ze*ze); }
    public double getTrueAnomRad() { return vRad; }
    public double getRightAsc() { return rightAsc; }
    public double getDecl() { return decl; }
    
    /**
     * Get rectangular coords.  raDecl() and rectCoords() should have been
     * called beforehand for meaningful values.
     * @return 
     */
    public Point3D getRectCoords() { return rectCoords; }
    /**
     * Get spherical coords, being r, RA and Decl. raDecl() should have been
     * called beforehand for meaningful values.
     * @return 
     */
    public Point3D getSphCoords() { return sphCoords; }

    /**
     * Get current (mod.) Julian day
     * @return 
     */
    public double getJulDay() { return julDay; }
    /**
     * Set (mod.) Julian day.  Also re-calcs osculating moves.
     * @param julDay 
     */
    public void setJulDay( double julDay )
    {
        // Set the day
        this.julDay = julDay;
        // Re-calc osculating movements
        calcJulDayMoves();
    }
}

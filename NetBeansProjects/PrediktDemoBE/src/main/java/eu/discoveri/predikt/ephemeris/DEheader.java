/*
 * Header.
 */
package eu.discoveri.predikt.ephemeris;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public abstract class DEheader
{
    /**
     * Numbers per interval.<br>
     * Each interval contains an interval number, length, start and end
     * jultimes, and Chebyshev coefficients. We keep only the coefficients
     * NCOEFF-2. NCOEFF is from file header.xxx.
     */
    // protected int numbers_per_interval = 1016;
    protected int numbersperinterval;

    /* GROUP 1010 from file header.xxx */
    /**
     * Three-digit number of planetary ephemeris version.
     */
    protected int denomber;

    /* GROUP 1030 from file header.xxx */
    /**
     * Start epoch for all ephemeris in Julian Days.
     */
    protected double startepoch;
    /**
     * End epoch for all ephemeris in Julian Days.
     */
    protected double finalepoch;
    /**
     * Ephemerides files are broken into intervals of length
     * "interval duration", in [days].
     */
    protected int intervalduration;

    /* GROUP 1040 - 1041 from file header.xxx */
    /**
     * Speed of light in [km/sec].
     */
    protected double clight;
    /**
     * Length of an A.U., in [km].
     */
    protected double au;
    /**
     * Earth - Moon mass ratio.
     */
    protected double emrat;
    /**
     * Mass parameter for Mercury GM in [AU^3/day^2].
     */
    protected double gm1;
    /**
     * Mass parameter for Venus GM in [AU^3/day^2].
     */
    protected double gm2;
    /**
     * Mass parameter for Earth-Moon barycenter GM in [AU^3/day^2].
     */
    protected double gmb;
    /**
     * Mass parameter for Mars GM in [AU^3/day^2].
     */
    protected double gm4;
    /**
     * Mass parameter for Jupiter GM in [AU^3/day^2].
     */
    protected double gm5;
    /**
     * Mass parameter for Saturn GM in [AU^3/day^2].
     */
    protected double gm6;
    /**
     * Mass parameter for Uranus GM in [AU^3/day^2].
     */
    protected double gm7;
    /**
     * Mass parameter for Neptune GM in [AU^3/day^2].
     */
    protected double gm8;
    /**
     * Mass parameter for Pluto GM in [AU^3/day^2].
     */
    protected double gm9;
    /**
     * Mass parameter for Sun GM in [AU^3/day^2].
     */
    protected double gms;

    /* GROUP 1050 from file header.xxx */
    /**
     * For each planet (and the Moon makes 10, and the Sun makes 11),Earth
     * nutations and Moon librations, each interval contains several complete
     * sets of coefficients, each covering a fraction of the interval duration.
     */
    protected int[] numberofcoefsets = { 0, 4, 2, 2, 1, 1, 1, 1, 1, 1, 8, 2, 4, 4 };
    /**
     * Each planet (and the Moon makes 10, and the Sun makes 11), Earth
     * nutations and Moon librations, has a different number of Chebyshev
     * coefficients used to calculate each component of position and velocity.
     */
    protected int[] numberofcoefs = { 0, 14, 10, 13, 11, 8, 7, 6, 6, 6, 13, 11, 10, 10 };
    /**
     * Initialize array for number of Chebyshev polynomials.
     */
    protected int[] numberofpoly = { 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3 };
    /**
     * Define array for Chebyshev coefficients in one file. Real size will be
     * compute in subclasses.
     */
    protected double[] ephemeriscoefficients;
    /**
     * Define array for start and final dates of file in Julian Days.
     */
    protected double[] ephemerisdates = new double[3];
    /**
     * Path to ASCII ephemerides files.
     */
    protected String patheph = "";

    /**
     * @return string with planetary ephemeris version.
     */
    @Override
    public String toString()
    {
	return "JPL Planetary Ephemeris DE" + denomber + "/LE" + denomber;
    }
    
        /**
     * Prototype for the methods to read files with Chebyshev coefficients.
     * Method is override in the subclasses for the specific ephemeris.
     * 
     * @param jultime
     *            Julian date for calculation.
     * @return true if the operations of reading is success.
     */
    protected abstract boolean readEphCoeff(double jultime);

    /**
     * Get path to the ASCII ephemerides files.
     * 
     * @return String with current path to the ASCII ephemerides files.
     */
    public String getPathEph() { return patheph; }

    /**
     * Set path to the ASCII ephemerides files.
     * 
     * @param patheph
     *            String with path to the ASCII ephemerides files.
     */
    public void setPathEph(String patheph) { this.patheph = patheph; }

    /**
     * @return Start epoch for all ephemeris in Julian Days.
     */
    public double getStartEpoch() { return startepoch; }

    /**
     * @return End epoch for all ephemeris in Julian Days.
     */
    public double getFinalEpoch() { return finalepoch; }

    /**
     * @return Speed of light in [km/sec].
     */
    public double getCLIGHT(){ return clight; }

    /**
     * @return Length of an A.U., in [km].
     */
    public double getAU() { return au; }

    /**
     * @return Earth - Moon mass ratio.
     */
    public double getEMRAT() { return emrat; }

    /**
     * @return Mass parameter for Mercury GM in [AU^3/day^2].
     */
    public double getGM1() { return gm1; }

    /**
     * @return Mass parameter for Venus GM in [AU^3/day^2].
     */
    public double getGM2() { return gm2; }

    /**
     * @return Mass parameter for Earth-Moon barycenter GM in [AU^3/day^2].
     */
    public double getGMB() { return gmb; }

    /**
     * @return Number of coefficients for Mars.
     */
    public double getGM4() { return gm4; }

    /**
     * @return Mass parameter for Jupiter GM in [AU^3/day^2].
     */
    public double getGM5() { return gm5; }

    /**
     * @return Mass parameter for Saturn GM in [AU^3/day^2].
     */
    public double getGM6() { return gm6; }

    /**
     * @return Mass parameter for Uranus GM in [AU^3/day^2].
     */
    public double getGM7() { return gm7; }

    /**
     * @return Mass parameter for Neptune GM in [AU^3/day^2].
     */
    public double getGM8() { return gm8; }

    /**
     * @return Mass parameter for Pluto GM in [AU^3/day^2].
     */
    public double getGM9() { return gm9; }

    /**
     * @return Mass parameter for Sun GM in [AU^3/day^2].
     */
    public double getGMS() { return gms; }
}

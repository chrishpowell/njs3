/*
 * Read JPL ephemeris files, D438t.
 */

package eu.discoveri.predikt.ephemeris;

import eu.discoveri.predikt.utils.Constants;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class JPLEphemeris
{
    private static String line;
    private static String nl = System.getProperty("line.separator");
    private static long dt = 0;
    private static 	int ntarg;
    private static 	int nctr;
    private static 	int ncoord;
    private static 	int nerr = 0;
    private static 	int ntrue = 0;
    private static 	int nnan = 0;
    private static 	double et;
    private static 	double xi;
    private static 	double del;
    private static 	String ts = "";
    private static 	String cs = "";
    private static 	String filename = "";

    /* In this variable will be return position and velocity. */
    private static double[] rv = new double[7];
    /* Choice of JPL ephemeris version. */
    private static int nde = 438;
    /* Path to ASCII ephemeris files. */
    private static String eph_path = "";

    
    public static void readEphemerisFile()
    {
        // Preamble
        System.out.printf(nl + "%s%3d%s%3d " + nl, "DE-0", nde, "LE-0", nde);
	System.out.printf("%s" + nl, " -- jed --   t#   c#   x#   --- jpl value ---"
		+ "   --- user value --   -- difference --");
        
        // Read the file
        try
        {
	    FileReader file = new FileReader(Constants.RESOURCEPATH+"ephemeris/testpo.438t");
	    BufferedReader buff = new BufferedReader(file);

	    do {
		line = buff.readLine();
	    } while (!line.contains("EOT"));

	    line = buff.readLine();
	    long t1 = System.currentTimeMillis();
            
	    while (line != null)
            {
		/* Julian Ephemeris Date. */
		et = Double.parseDouble(line.substring(16, 25));
                
		/*
		 * target number (1-Mercury, ...,3-Earth, ,,,9-Pluto, 10-Moon,
		 * 11-Sun, 12-Solar System Barycenter, 13-Earth-Moon Barycenter
		 * 14-Nutations, 15-Librations).  Nope, wrong!!
		 */
		ts = line.substring(26, 28);
		ntarg = Integer.parseInt(ts.trim());
		/* center number (same codes as target number) */
		cs = line.substring(29, 31);
		nctr = Integer.parseInt(cs.trim());
		/* coordinate number (1-x, 2-y, ... 6-zdot) */
		ncoord = Integer.parseInt(line.substring(33, 34));
		/* coordinate [au, au/day] */
		xi = Double.parseDouble(line.substring(36, 64));

		/*
		 * Calulated position and velocity of the point 'ntarg' with
		 * respect to 'nctr'.
		 */
		rv = j_Pleph(new DE438(), et, ntarg, nctr);

		del = StrictMath.abs(rv[ncoord] - xi);
		if ((ntarg == 15) && (ncoord == 3)) {
		    del = del / (0.23 * (et - 2451545.0));
		}
		if ((ntarg == 15) && (ncoord == 6)) {
		    del = del * 0.01 / (1.0 + (et - 2451545.0) / 36525.0);
		}

		if (del >= 1.0e-13) {
		    System.out.printf("%s", "*****  WARNING : next difference >= 1.0E-13  *****");
		    nerr += 1;
		} else {
		    if (Double.isNaN(del)) {
			nnan += 1;
		    } else {
			ntrue += 1;
		    }
		}
		System.out.printf("%10.1f%5d%5d%5d%20.13f%20.13f%13.5e" + nl, et, ntarg, nctr, ncoord, xi,
			rv[ncoord], del);
		line = buff.readLine();
	    }
	    long t2 = System.currentTimeMillis();
	    dt = t2 - t1;
	    buff.close();
	} catch (IOException e) {
	    System.out.printf("%s%s", "Error = ", e.toString());
	}
	System.out.printf("ok: %d, NaN: %d, err: %d; time: %d msec" + nl, ntrue, nnan, nerr, dt);
    }

    /**
     * Method gives the position and velocity of the point 'ntarg' with respect
     * to 'ncent'. This method is Java version of the procedure 'PLEPH', which
     * not repeats fortran source exactly, but return a similar result.
     * 
     * @param de
     *            Class containing the parameters of the JPL ephemeris and
     *            coefficients of the Chebyshev polynomials.
     * @param et
     *            julian ephemeris date at which interpolation is wanted.
     * @param ntarg
     *            integer number of target point: 1 = MERCURY, 2 = VENUS, 3 =
     *            EARTH, 4 = MARS, 5 = JUPITER, 6 = SATURN , 7 = URANUS, 8 =
     *            NEPTUNE, 9 = PLUTO, 10 = MOON, 11 = SUN, 12 = SOLAR-SYSTEM
     *            BARYCENTER, 13 = EARTH-MOON BARYCENTER, 14 = NUTATIONS
     *            (LONGITUDE AND OBLIQ), 15 = LIBRATIONS, IF ON EPH FILE.
     * @param ncent
     *            integer number of center point, same as 'ntarg'.
     * @return array with positions in [AU] and velocity in [AU/day]. In the
     *         case of nutations the first four number will be set to nutations
     *         and rate in [rad] and [rad/day] respectively. If 'ntarg=15' in
     *         [rad] and [rad/day].
     */
    public static double[] j_Pleph( DEheader de, double et, int ntarg, int ncent )
    {
	double[] t_rv = new double[7];
	double[] c_rv = new double[7];
	double[] tmc_rv = new double[7];
	DEephem deephem = new DEephem();

	/*
	 * Calculate the solar system barycentric position and velocity of the
	 * body 'ntarg'
	 */
	switch (ntarg)
        {
	case 3:
	    double[] e_rv = deephem.getPlanetPosvel(de, et, 3);
	    double[] m_rv = deephem.getPlanetPosvel(de, et, 10);
            
	    for (int j = 1; j < 7; j++)
            {
		t_rv[j] = e_rv[j] - m_rv[j] / (1.0 + de.getEMRAT());
	    }
	    break;
	case 10:
	    e_rv = deephem.getPlanetPosvel(de, et, 3);
	    m_rv = deephem.getPlanetPosvel(de, et, 10);
            
	    for (int j = 1; j < 7; j++)
            {
		t_rv[j] = e_rv[j] + m_rv[j] - m_rv[j] / (1.0 + de.getEMRAT());
	    }
	    break;
	case 12:
	    for (int j = 1; j < 7; j++)
            {
		t_rv[j] = 0.0;
	    }
	    break;
	case 13:
	    t_rv = deephem.getPlanetPosvel(de, et, 3);
	    break;
	case 14:
	    t_rv = deephem.getPlanetPosvel(de, et, 12);
	    break;
	case 15:
	    t_rv = deephem.getPlanetPosvel(de, et, 13);
	    break;
	default:
	    t_rv = deephem.getPlanetPosvel(de, et, ntarg);
	    break;
	}
        
	/*
	 * Calculate the solar system barycentric position and velocity of the
	 * body 'ncent'
	 */
	switch (ncent)
        {
	case 3:
	    double[] e_rv = deephem.getPlanetPosvel(de, et, 3);
	    double[] m_rv = deephem.getPlanetPosvel(de, et, 10);
            
	    for (int j = 1; j < 7; j++)
            {
		c_rv[j] = e_rv[j] - m_rv[j] / (1.0 + de.getEMRAT());
	    }
	    break;
	case 10:
	    e_rv = deephem.getPlanetPosvel(de, et, 3);
	    m_rv = deephem.getPlanetPosvel(de, et, 10);
            
	    for (int j = 1; j < 7; j++)
            {
		c_rv[j] = e_rv[j] + m_rv[j] - m_rv[j] / (1.0 + de.getEMRAT());
	    }
	    break;
	case 13:
	    c_rv = deephem.getPlanetPosvel(de, et, 3);
	    break;
	case 0:
	case 12:
	case 14:
	case 15:
	    for (int j = 1; j < 7; j++)
            {
		c_rv[j] = 0.0;
	    }
	    break;
	default:
	    c_rv = deephem.getPlanetPosvel(de, et, ncent);
	    break;
	}

	/* Calculate the relative position and velocity */
	for (int j = 1; j < 7; j++)
        {
	    tmc_rv[j] = t_rv[j] - c_rv[j];
	}
        
	return tmc_rv;
    }
    
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        readEphemerisFile();
    }
}

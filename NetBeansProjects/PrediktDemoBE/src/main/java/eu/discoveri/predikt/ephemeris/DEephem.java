/*
 * Calculate body attributes.
 */
package eu.discoveri.predikt.ephemeris;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class DEephem
{
/**
     * Method calculate the position and velocity of body 'i' (nutations and
     * librations also) in epoch 'jultime' for a specific JPL ephemeris.<br>
     * The general idea is as follows: First read the coefficients corresponding
     * to 'jultime', and then calculate the positions and velocities of the
     * planet.
     * 
     * @param de
     *            class containing constants and the coefficients of Chebyshev
     *            polynomials for a specific JPL ephemeris.
     * @param jultime
     *            Julian ephemeris epoch at which interpolation is wonted.
     * @param i
     *            designate of the astronomical bodies: 1 Mercury, 2 Venus, 3
     *            Earth-Moon barycenter, 4 Mars, 5 Jupiter, 6 Saturn, 7 Uranus,
     *            8 Neptune, 9 Pluto, 10 Geocentric Moon, 11 Sun, 12 nutations
     *            in longitude and obliquity (if on file), 13 lunar librations
     *            (if on file).
     * @return array with solar system barycentric positions and velocity for
     *         planets and Sun or geocentric position and velocity for Moon, for
     *         all in units [AU] and [AU/day]. The order of components is: X, Y,
     *         Z, dX, dY, dZ. If i=12 first four components return Earth
     *         nutation in longitude and obliquity and their rates in units
     *         [rad] and [rad/day]. If i=13 array contains lunar librations
     *         angles phi, thita and psi and their rates in units [rad] and
     *         [rad/day].
     */
    public double[] getPlanetPosvel( DEheader de, double jultime, int i )
    {
	int interval = 0;
	int numberstoskip = 0;
	int pointer = 0;
	int j = 0;
	int k = 0;
	int subinterval = 0;
	double intervalstarttime = 0;
	double subintervalduration = 0;
	double chebyshevtime = 0;

	double[] ephemerisrv = new double[7];

	double[] positionpoly = new double[20];
	double[][] coef = new double[4][20];
	double[] velocitypoly = new double[20];

	if ((de.readEphCoeff(jultime)) && (i <= 13) && (i > 0)) {

	    interval = (int) (Math.floor((jultime - de.ephemerisdates[1]) / de.intervalduration) + 1);
	    intervalstarttime = (interval - 1) * de.intervalduration + de.ephemerisdates[1];
	    if (de.numberofcoefsets[i] != 0) {
		subintervalduration = (double) de.intervalduration / de.numberofcoefsets[i];
	    }
	    subinterval = (int) (Math.floor((jultime - intervalstarttime) / subintervalduration) + 1);
	    numberstoskip = (interval - 1) * de.numbersperinterval;

	    /*
	     * Starting at the beginning of the coefficient array, skip the
	     * first "numbers_to_skip" coefficients. This puts the pointer on
	     * the first piece of data in the correct interval.
	     */
	    pointer = numberstoskip + 1;

	    /* Skip the coefficients for the first (i-1) planets */
	    for (j = 1; j <= (i - 1); j++) {
		pointer = pointer + de.numberofpoly[j] * de.numberofcoefsets[j] * de.numberofcoefs[j];
	    }

	    /*
	     * Skip the next (subinterval -
	     * 1)*number_of_poly(i)*number_of_coefs(i) coefficients
	     */
	    pointer = pointer + (subinterval - 1) * de.numberofpoly[i] * de.numberofcoefs[i];

	    for (j = 1; j <= de.numberofpoly[i]; j++) {
		for (k = 1; k <= de.numberofcoefs[i]; k++) {
		    /*
		     * Read the pointer'th coefficient as the array entry
		     * coef[j][k]
		     */
		    coef[j][k] = de.ephemeriscoefficients[pointer];
		    pointer = pointer + 1;
		}
	    }

	    /*
	     * Calculate the chebyshev time within the subinterval, between -1
	     * and +1.
	     */
	    chebyshevtime = 2 * (jultime - ((subinterval - 1) * subintervalduration + intervalstarttime))
		    / subintervalduration - 1;

	    /* Calculate the Chebyshev position polynomials. */
	    positionpoly[1] = 1;
	    positionpoly[2] = chebyshevtime;
	    for (j = 3; j <= de.numberofcoefs[i]; j++) {
		positionpoly[j] = 2 * chebyshevtime * positionpoly[j - 1] - positionpoly[j - 2];
	    }

	    /* Calculate the position of the i'th planet at jultime. */
	    for (j = 1; j <= de.numberofpoly[i]; j++) {
		ephemerisrv[j] = 0;
		for (k = 1; k <= de.numberofcoefs[i]; k++) {
		    ephemerisrv[j] = ephemerisrv[j] + coef[j][k] * positionpoly[k];
		}

		/* Convert from km to A.U. */
		if (i < 12) {
		    ephemerisrv[j] = ephemerisrv[j] / de.getAU();
		}
	    }

	    /* Calculate the Chebyshev velocity polynomials. */
	    velocitypoly[1] = 0;
	    velocitypoly[2] = 1;
	    velocitypoly[3] = 4 * chebyshevtime;
	    for (j = 4; j <= de.numberofcoefs[i]; j++) {
		velocitypoly[j] = 2 * chebyshevtime * velocitypoly[j - 1] + 2 * positionpoly[j - 1]
			- velocitypoly[j - 2];
	    }

	    /* Calculate the velocity of the i'th planet. */
	    for (j = de.numberofpoly[i] + 1; j <= 2 * de.numberofpoly[i]; j++) {
		ephemerisrv[j] = 0;
		for (k = 1; k <= de.numberofcoefs[i]; k++) {
		    ephemerisrv[j] = ephemerisrv[j] + coef[j - de.numberofpoly[i]][k] * velocitypoly[k];
		}
		/*
		 * The next line accounts for differentiation of the iterative
		 * formula with respect to chebyshev time. Essentially, if dx/dt
		 * = (dx/dct) times (dct/dt), the next line includes the factor
		 * (dct/dt) so that the units are km/day.
		 */
		ephemerisrv[j] = ephemerisrv[j] * (2.0 * de.numberofcoefsets[i] / de.intervalduration);

		/* Convert from km to A.U. */
		if (i < 12) {
		    ephemerisrv[j] = ephemerisrv[j] / de.getAU();
		}
	    }
	} else {
	    for (j = 1; j <= 6; j++) {
		ephemerisrv[j] = Double.NaN;
	    }
	}

	return ephemerisrv;
    }
}

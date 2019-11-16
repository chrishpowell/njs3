/*
 * Aspects: Degrees difference
 */

package eu.discoveri.predikt.test.horochart;

/**
 * Aspect: actual degrees and +/- range.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class RotateDiff
{
    private final int       degreesDiffExact,
                            degreesDiffRange;
    private final double    radsDiffExact,
                            radsDiffRange;

    public RotateDiff(int degreesDiffExact, int degreesDiffRange)
    {
        this.degreesDiffExact = degreesDiffExact;
        this.radsDiffExact = Math.toRadians(degreesDiffExact);
        this.degreesDiffRange = degreesDiffRange;
        this.radsDiffRange = Math.toRadians(degreesDiffRange);
    }

    public int getDegreesDiffExact() { return degreesDiffExact; }
    public double getRadsDiffExact() { return radsDiffExact; }
    public int getDegreesDiffRange() { return degreesDiffRange; }
    public double getRadsDiffRange() { return radsDiffRange; }
}

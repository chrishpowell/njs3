/** ***************************************************************************\
 * VisLimitAngularBrightnessData
 * \**************************************************************************** */
package eu.discoveri.predikt.astronomy.library;

/**
 * A support class for VisLimit.
 * <P>
 * Holds values which vary across the sky
 */
public class VisLimitAngularBrightnessData
{
        /**
     * The zenith angle
     */
    public double zenithAngle;

    /**
     * The lunar angular distance
     */
    public double distMoon;

    /**
     * The solar angular distance
     */
    public double distSun;
    
    
    public VisLimitAngularBrightnessData()
    {
        zenithAngle = 0D;
        distMoon = 0D;
        distSun = 0D;
    }

    public VisLimitAngularBrightnessData(double za, double dm, double ds)
    {
        zenithAngle = za;
        distMoon = dm;
        distSun = ds;
    }
};

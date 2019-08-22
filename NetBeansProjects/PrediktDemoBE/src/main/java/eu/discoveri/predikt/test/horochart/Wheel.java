/*
 * Chart wheel, Placidus, Hoch etc.
 */

package eu.discoveri.predikt.test.horochart;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public abstract class Wheel implements HouseWheel
{
    private final ChartType wheel;
    
    public Wheel( ChartType wheel )
    {
        this.wheel = wheel;
    }
    
    
    public ChartType getWheel() { return wheel; }
}

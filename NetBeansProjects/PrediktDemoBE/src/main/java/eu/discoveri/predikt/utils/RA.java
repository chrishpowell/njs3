/*
 */
package eu.discoveri.predikt.utils;

import java.text.DecimalFormat;

/**
 * Right Ascension: Hour(int), min(int), secs(double).  But can be used for an
 * Hour/Min/Sec struct.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class RA
{
    private final int     hour, min;
    private final double  secs;
    
    /**
     * Constructor.
     * 
     * @param hour
     * @param min
     * @param secs 
     */
    public RA( int hour, int min, double secs )
    {
        this.hour = hour;
        this.min = min;
        this.secs = secs;
    }

    /**
     * Get the hour
     * @return 
     */
    public int getHour() { return hour; }
    /**
     * Get the min
     * @return 
     */
    public int getMin() { return min; }
    /**
     * Get the secs
     * @return 
     */
    public double getSecs() { return secs; }
    
    /**
     * Output to String
     * @return 
     */
    @Override
    public String toString()
    {
        DecimalFormat dfs = new DecimalFormat("##.###");
        return ""+hour+" "+min+" "+dfs.format(secs);
    }
}

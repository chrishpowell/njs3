/*
 * Latitude, Longitude.
 * Based on javafx.geometry.Point2D.
 */
package eu.discoveri.predikt.utils;

import javafx.geometry.Point2D;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LatLon extends Point2D
{
    /**
     * Constructor.
     * 
     * @param latitude
     * @param longitude 
     */
    public LatLon( double latitude, double longitude )
    {
        super(latitude,longitude);
    }
    
    /**
     * Get the latitude.
     * @return 
     */
    public double getLatitude()
    {
        return getX();
    }
    
    /**
     * Get the longitude.
     * @return 
     */
    public double getLongitude()
    {
        return getY();
    }
    
    /**
     * Return [lat,lon] string.
     * @return 
     */
    @Override
    public String toString()
    {
        return "["+getX()+","+getY()+"]";
    }
}

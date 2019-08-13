/*
 * Latitude, Longitude.
 * Based on javafx.geometry.Point2D.
 */
package eu.discoveri.predikt.utils;

import java.text.DecimalFormat;
import javafx.geometry.Point2D;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LatLon extends Point2D
{
    // Set default type (degrees or radians)
    private boolean   inrads = false;
    
    /**
     * Constructor.
     * 
     * @param latitude in degrees/radians (usually from geonames etc.)
     * @param longitude in degrees/radians
     * @param inRadians default is true
     */
    public LatLon( double latitude, double longitude, boolean inRadians )
    {
        super(latitude,longitude);
        this.inrads = inRadians;
    }
    
    /**
     * Constructor.
     * 
     * @param latitude in degrees (usually from geonames etc.)
     * @param longitude in degrees
     */
    public LatLon( double latitude, double longitude ){ this(latitude,longitude,false); }
    
    /**
     * Get the latitude.
     * @return 
     */
    public double getLatitude(){ return getX(); }
    
    /**
     * Get the longitude.
     * @return 
     */
    public double getLongitude(){ return getY(); }
    
    /**
     * Is this in radians or degrees?
     * @return true if in radians else false
     */
    public boolean getIfInRadians(){ return inrads; }
    
    /**
     * Lat/Lon to decimal N/S, E/W.
     * 
     * @return 
     */
    public String fmtLatLonDec()
    {
        DecimalFormat df = new DecimalFormat("###.####");
        String lat, lon;
        
        if( inrads )
        {
            double x = Math.toDegrees(getX());
            double y = Math.toDegrees(getY());
            
            lat = getX() < 0.d ? df.format(-x)+"S" : df.format(x)+"N";
            lon = getY() < 0.d ? df.format(-y)+"W" : df.format(y)+"E";
        }
        else
        {
            lat = getX() < 0.d ? df.format(-getX())+"S" : df.format(getX())+"N";
            lon = getY() < 0.d ? df.format(-getY())+"W" : df.format(getY())+"E";
        }
        
        return lat+", "+lon;
    }
    
    /**
     * Lat/Lon in DegX(N/S, E/W) mm' ss.ss"
     * 
     * @return 
     */
    public String fmtLatLonDMS()
    {
        DecimalFormat df = new DecimalFormat("##.##");
        String lathemi = null, lonhemi = null;
        double lat = getX(), lon = getY();
        
        // Convert radians to degrees
        if( inrads )
        {
            lat = Math.toDegrees(lat);
            lon = Math.toDegrees(lon);
        }

        // Determine hemisphere
        if( getX() < 0.d )
        {
            lathemi = "S";
            lat = -lat;
        }
        else
        {
            lathemi = "N";
        }
        
        if( getY() < 0.d )
        {
            lonhemi = "W";
            lon = -lon;
        }
        else
        {
            lonhemi = "E";
        }
        
        int lathrs = (int)(lat - Util.mod(lat,1.0));
        double latmns = (lat - lathrs)*60.d;
        double latsec = (latmns - (int)latmns)*60.d;

        int lonhrs = (int)(lon - Util.mod(lon,1.0));
        double lonmns = (lon - lonhrs)*60.d;
        double lonsec = (lonmns - (int)lonmns)*60.d;
        
        return lathrs+"\u00B0"+ lathemi+" "+(int)latmns+"' "+df.format(latsec)+"\", "+lonhrs+"\u00B0"+lonhemi+" "+(int)lonmns+"' "+df.format(lonsec)+"\"";
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
    
    
    /**
     * M A I N (test)
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        LatLon ll1 = new LatLon(23.671235,7.000199, false);
        LatLon ll2 = new LatLon(0.2750111,0.345678, false);
        LatLon ll3 = new LatLon(-1.000999,90023456, false);
        LatLon ll4 = new LatLon(2.22222,-48.675432, false);
        LatLon ll5 = new LatLon(-0.23456789,-0.789543, false);
        LatLon ll6 = new LatLon(0.413141d, 0.12217652084, true);
        
        System.out.println("ll1 dmsN/S,E/W: " +ll1.fmtLatLonDMS());
        System.out.println("ll6 dmsN/S,E/W: " +ll6.fmtLatLonDMS());
        System.out.println("ll2 dms: " +ll2.fmtLatLonDMS());
        System.out.println("ll3 dms: " +ll3.fmtLatLonDMS());
        System.out.println("ll4 dms: " +ll4.fmtLatLonDMS());
        System.out.println("ll5 dms: " +ll5.fmtLatLonDMS());
    }
}

/*
 * Houses and ascendants from sidereal time
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.exception.GeonamesNoResultsException;
import eu.discoveri.predikt.utils.*;
import java.io.IOException;
import java.net.MalformedURLException;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * Placidus system.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class HoroHouse
{
    // Statics
    private static double           sinTilt, cosTilt;                           // Earth tilt
    // Class attributes
    private double                  tilt, tiltRads,
                                    latRads, lngRads,
                                    declMC, declMCRads,
                                    declAsc, declAscRads,
                                    ascMod, ascRads,                            // Ascendant
                                    lst,                                        // Local Sideral Time
                                    ramcRads, mcRads;
    private final LocalDateTime     ldt;                                        // Birth date
    private LatLon                  place;
    private final String            placeName;

    // Cusps
    private Map<Integer,CuspPlusAngle>    cpaMap;


    /**
     * Constructor.
     * 
     * @param placename
     * @param localdatetime
     * @param cpamap Map for cusps.
     */
    public HoroHouse( String placename, LocalDateTime localdatetime, Map<Integer,CuspPlusAngle> cpamap )
    {
        this.placeName = placename;
        this.ldt = localdatetime;
        this.cpaMap = cpamap;
    }

    /**
     * Initialise system with user details, calculate obliquity, the lat/lon from
     * the place name, and the LST in hours decimal.
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws GeonamesNoResultsException 
     */
    public HoroHouse init()
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        // Get obliquity 
        calcObliquity( ldt );
        
        // Get LatLon from place (degrees)
        place = getLatLonFromPlace( placeName );

        // Convert place to radians
        latRads = Math.toRadians(place.getLatitude());
        lngRads = Math.toRadians(place.getLongitude());
        
        // LST from geonames
        calcLstGN();

        return this;
    }
    
    /*
     * Local Sidereal Time (LST in hrs) from geonames
     */
    public void calcLstGN()
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        lst = TimeScale.getLSTHrs(place, ldt);
    }
    
    /*
     * Local Sidereal Time (LST in hrs) from arbitrary lat/lon
     */
    public void calcLST( LatLon anyPlace )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        lst = TimeScale.getLocalSiderealHrsDec(anyPlace, ldt);
    }

    /**
     * Get latitude and longitude from geonames/place (degrees).
     * 
     * @param placeName
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws GeonamesNoResultsException 
     */
    public static LatLon getLatLonFromPlace( String placeName )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        LatLon p = null;

        // Place
        try
        {
            p = TimeScale.getGeonameLatLon(placeName);
        }
        catch( Exception ex )
        {
            System.out.println("PROBLEM! Geonames fail");
            System.out.println("Possibly misspelled name? "+placeName+". Otherwise, please enter Latitude and Longitude coordinates manually.");
            throw new GeonamesNoResultsException();
        }
        
        return p;
    }
    
    /**
     * Set an arbitrary place.
     * @param newPlace 
     */
    public void setPlace( LatLon newPlace )
    {
        this.place = newPlace;
        
        if( place.getIfInRadians() )
        {
            latRads = place.getLatitude();
            lngRads = place.getLongitude();
        }
        else
        {
            latRads = Math.toRadians(place.getLatitude());
            lngRads = Math.toRadians(place.getLongitude());
        }
    }

    /**
     * Calc earth tilt in radians and sin/cos of the tilt angle.
     * 
     * @param localdatetime 
     */
    public void calcObliquity( LocalDateTime localdatetime )
    {
        // Obliquity
        tilt = Util.calcObliquityDegs(localdatetime.toLocalDate());
        tiltRads = Math.toRadians(tilt);
        
        // sin & cos of tilt
        sinTilt = Math.sin(tiltRads);
        cosTilt = Math.cos(tiltRads);
    }
    
    /**
     * Calculate RAMC (thetaL) in radians.
     */
    public void calcRAMC()
    {
        // 1. Sidereal time to degrees then radians
        double stRads = Math.toRadians(lst * 15.d);

        // In range 0 to 360
        ramcRads = Util.mod(stRads,2.d*Math.PI);
    }
    
    /**
     * 2. Longitude of MC.
     *    Long of MC = arctan (tan RAMC/cos e) in radians.  https://en.wikipedia.org/wiki/Midheaven
     *   [Rule: if RAMC in range 0 to 90d, use the MC figure; if RAMC 90 to 270
     *    add 180 to the MC; if RAMC 270 to 360 add 360 (MC will be negative)
     *    (eg Virgo to Pisces)]  NB: RAMC always positive 0-360, tan(ramc) will
     *    switch between positive and negative, cosTilt is positive.  In *this*
     *    algo, MC does not go negative hence quadrant adjustment.  @TODO: Some
     *    scenarios may allow MC negative - this algo will then be adjusted
     */
    public void calcMC()
    {
        // Midheaven = arctan(tan(thetaL)/cos(eps))
        mcRads = Math.atan(Math.tan(ramcRads)/cosTilt);
        
        // Adjust quadrant
        if( ramcRads >= Math.PI/2.d && ramcRads < 3.d*Math.PI/2.d )
            { mcRads += Math.PI; }
        else
        if( ramcRads >= 3.d*Math.PI/2.d )
            { mcRads += 2.d * Math.PI; }
    }
    
    /**
     * The ascendant is the sign on the Eastern horizon when the birth occurred.
     * The degree (and ultimately, the house/sign) that is ascending on the
     * Eastern horizon at a given time, usually the birth date/time.  It is the
     * geocentric Ecliptic or Celestial Longitude (lambda) as outlined in:
     * https://en.wikipedia.org/wiki/Ecliptic_coordinate_system#Spherical_coordinates.
     *
     * Be aware that with latitudes north/south of Arctic/Antarctic circles,
     * there are discontinuities, hence applied limit.
     * 
     * For algo and quadrants, see: https://en.wikipedia.org/wiki/Ascendant
     * 
     * Note: This is the 'raw' value.  Ascendant is calculated by House.  See
     * CuspPlusAngle.evenAngleHouse().
     * 
     */
    public void calcAsc()
    {
        // @TODO: Check latitude outliers (probably better ways of doing this...
        //  switch to Porphyry houses (each quadrant is divided into three equal
        //  parts) and return an error?
        //----------------------------------------------------------------------
        if( latRads >= Constants.NORTHERNLATS )                                 // Arctic circle
            { latRads = Constants.NORTHERNLIMT; }
        else
        if( latRads <= -Constants.NORTHERNLATS )                                // Antarctic circle
            { latRads = -Constants.NORTHERNLIMT; }
        //----------------------------------------------------------------------
        
        // Algo 1
//        double lstRad = Math.toRadians(Util.hrs2DegDec(lst));
//        double term1 = Math.tan(latrads) * sinTilt;
//        double term2 = Math.sin(lstRad) * cosTilt;
//        double ascRads = Math.atan2( -Math.cos(lstRad),(term1 + term2) );
        // Algo 2 (equivalent)
        double lstRad = Math.toRadians(Util.hrs2DegDec(lst));
        double term1 = Math.tan(latRads) * sinTilt;
        double term2 = Math.sin(ramcRads) * cosTilt;
        
        // Do NOT use atan2 here as atan(-cos(RAMC),...) returns a positive
        // value - which is not wanted.
        ascRads = Math.atan( -Math.cos(ramcRads)/(term1 + term2) );

        // Adjust quadrant
        if( (term1+term2) < 0.d )
            { ascRads += Math.PI; }
        else
            { ascRads += 2.d * Math.PI; }
        
        if( ascRads < Math.PI )
            { ascRads += Math.PI; }
        else
            { ascRads -= Math.PI; }  
    }
    
    /**
     * 3A. Find the declination of the MC
     */
    public void calcMCDecl()
    {
        // Set Decl of MC
        declMCRads = declination(mcRads);
        declMC = declMCRads;
    }

    /**
     * 3B. Find the declination of the ASC
     */
    public void calcASCDecl()
    {
        // Set Dcl of Asc
        declAscRads = declination(ascRads);
        declAsc = declAscRads;
    }
    
    /**
     * Declination.
     * 
     * @param lon longitude (rads)
     * @return declination (rads)
     */
    public static double declination( double lon )
    {
        // arcsin(sin(zodiacLong)*sin(Obliq))
        return Math.asin(Math.sin(lon) * sinTilt);
    }

    
    /**
     * 6. Convert final RA to longitude (rads):
     *    2nd/3rd cusp longitude = arctan (tan RA/cos e).
     * 
     * @param ra
     * @return 
     */
    public static double ra2Longitude( double ra )
    {
        double longitude = Math.atan(Math.tan(ra)/cosTilt);
//        System.out.println("..> Initial longitude (rads/degs): " +longitude +"/"+ Math.toDegrees(longitude));
//        System.out.println("..> RA, tan RA: " +ra+ ", " +Math.tan(ra));
        
        // Make positive
        if( longitude < 0.d )
            { longitude += Math.PI; }

        /*
         * Input ra is very nearly in the right place (compare ra and initial
         * longitude). But atan(tan(ra)) can 'move' longitude into wrong
         * quadrant by 180.  Move it back.
         */
        if( ra > Math.PI && ra <= 2*Math.PI )
            { longitude += Math.PI; }
        
//        System.out.println("ret long (rads/degs): " +longitude+"/"+Math.toDegrees(longitude));
        return longitude;
    }

    /**
     * Create the house wheel.
     * @param ct 
     */
    public void setWheel( ChartType ct )
    {
        // 10th house cusp (MC)
        // 1st. Convert Local Sidereal Time (LST) to Right Ascension of the MC (RAMC)
        calcRAMC();                                                 // ramc used a lot...
        // Midheaven
        calcMC();
        // Ascendant
        calcAsc();
        
        // 3A. To find the declination of the MC and Asc:
        //    Declination = arcsin [sin(zodiacal longitude) x sin e]
        calcMCDecl();
        calcASCDecl();
        
        switch( ct )
        {
            default:
            case PLACIDUS:
                PlacidusWheel pw = new PlacidusWheel( tiltRads, latRads );
                pw.determineCusps(cpaMap, ramcRads, mcRads, ascRads, declMC, declAsc);
        }
    }
    
    /*
     * Getters
     */
    public double getLST() { return lst; }
    public double getRAMC() { return ramcRads; }
    public LocalDateTime getLDT() { return ldt; }
    public LatLon getLatLon() { return place; }
    public double getAsc() { return ascRads; }
    public double getMC() { return mcRads; }
    public double getAscMod() { return ascMod; }
    public double getDeclMC() { return declMC; }
    public double getDeclMCRads() { return declMCRads; }
    public double getDeclAsc() { return declAsc; }
    public double getDeclAscRads() { return declAscRads; }
    public Map<Integer,CuspPlusAngle> getCpaMap() {  return cpaMap; }

 
    /*
     * Quick checks
     * ------------
     */
    /**
     * Check LST/MClong range.
     * LST[0-12h): MC[0Aries,0Libra), LST[12-24h): MC[0Libra,0Aries)
     * 
     * @param lst
     * @param hpa
     * @return checked OK/NotOK
     */
    public boolean quikLonMCChk( double lst, CuspPlusAngle hpa )
    {
        // What's the house number
        int houseNum = ZodiacHouse.getZHNameMap().get(hpa.getHouse());
        
        // LST[0-12h): MC[0Aries,0Libra), LST[12-24h): MC[0Libra,0Aries)  
        if( lst >= 0.d && lst < 12.d )
            { return houseNum >= 0 && houseNum <= 5; }
        else
            { return houseNum >= 6 && houseNum <= 11; }
    }

    /**
     * Table for ASC/MC cross check.
     * 
     * @param zhASC
     * @param zhMC
     * @return checks OK/NotOK
     */
    public boolean ascMCHouseChk( ZodiacHouse zhASC, ZodiacHouse zhMC )
    {
        switch( zhASC )
        {
            default:
            case ARIES:
                return  zhMC == ZodiacHouse.CAPRICORN ||
                        zhMC == ZodiacHouse.AQUARIUS;
            case TAURUS:
                return  zhMC == ZodiacHouse.CAPRICORN ||
                        zhMC == ZodiacHouse.AQUARIUS ||
                        zhMC == ZodiacHouse.PISCES;
            case GEMINI:
                return  zhMC == ZodiacHouse.PISCES ||
                        zhMC == ZodiacHouse.AQUARIUS ||
                        zhMC == ZodiacHouse.CAPRICORN ||
                        zhMC == ZodiacHouse.ARIES;
            case CANCER:
                return  zhMC == ZodiacHouse.PISCES ||
                        zhMC == ZodiacHouse.GEMINI ||
                        zhMC == ZodiacHouse.TAURUS ||
                        zhMC == ZodiacHouse.ARIES;
            case LEO:
                return  zhMC == ZodiacHouse.PISCES ||
                        zhMC == ZodiacHouse.AQUARIUS ||
                        zhMC == ZodiacHouse.TAURUS ||
                        zhMC == ZodiacHouse.ARIES;
            case VIRGO:
                return  zhMC == ZodiacHouse.TAURUS ||
                        zhMC == ZodiacHouse.GEMINI;
            case LIBRA:
                return  zhMC == ZodiacHouse.CANCER ||
                        zhMC == ZodiacHouse.LEO;
            case SCORPIO:
                return  zhMC == ZodiacHouse.LIBRA ||
                        zhMC == ZodiacHouse.LEO ||
                        zhMC == ZodiacHouse.VIRGO ||
                        zhMC == ZodiacHouse.CANCER;
            case SAGITTARIUS:
                return  zhMC == ZodiacHouse.LIBRA ||
                        zhMC == ZodiacHouse.SCORPIO ||
                        zhMC == ZodiacHouse.VIRGO ||
                        zhMC == ZodiacHouse.LEO;
            case CAPRICORN:
                return  zhMC == ZodiacHouse.LIBRA ||
                        zhMC == ZodiacHouse.SCORPIO ||
                        zhMC == ZodiacHouse.VIRGO ||
                        zhMC == ZodiacHouse.SAGITTARIUS;
            case AQUARIUS:
                return  zhMC == ZodiacHouse.LIBRA ||
                        zhMC == ZodiacHouse.SCORPIO ||
                        zhMC == ZodiacHouse.SAGITTARIUS;
            case PISCES:
                return  zhMC == ZodiacHouse.SCORPIO ||
                        zhMC == ZodiacHouse.SAGITTARIUS;
        }
    }
}


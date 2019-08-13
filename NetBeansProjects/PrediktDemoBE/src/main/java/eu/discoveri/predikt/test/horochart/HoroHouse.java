/*
 * Houses and ascendants from sidereal time
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.exception.GeonamesNoResultsException;
import eu.discoveri.predikt.utils.*;
import java.io.IOException;
import java.net.MalformedURLException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Placidus system.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class HoroHouse
{
    // Class attributes
    private double                  tilt, tiltRads,
                                    sinTilt, cosTilt,                           // Earth tilt
                                    latRads, lngRads,
                                    declMC, declMCRads,
                                    declAsc, declAscRads,
                                    ascMod, ascRads,                            // Ascendant
                                    lst,                                        // Local Sideral Time
                                    ramcRads, mcRads;
    private static double           cusp2Rads,
                                    cusp3Rads,
                                    cusp11Rads,
                                    cusp12Rads;
    private final LocalDateTime     ldt;                                        // Birth date
    private LatLon                  place;
    private final String            placeName;

    
    /*
     * Create the cusps
     */
    private final Map<Integer,CuspPlusAngle> cpaMap = new HashMap<Integer,CuspPlusAngle>()
    {{
        put(1,new CuspPlusAngle().setAttribute(ZhAttribute.ASC));
        put(2,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(3,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(4,new CuspPlusAngle().setAttribute(ZhAttribute.IC));
        put(5,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(6,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(7,new CuspPlusAngle().setAttribute(ZhAttribute.DSC));
        put(8,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(9,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(10,new CuspPlusAngle().setAttribute(ZhAttribute.MC));
        put(11,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
        put(12,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
    }};


    /**
     * Constructor.
     * 
     * @param placename
     * @param localdatetime
     */
    public HoroHouse( String placename, LocalDateTime localdatetime )
    {
        this.placeName = placename;
        this.ldt = localdatetime;
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
        mcRads = Math.atan2(Math.tan(ramcRads),cosTilt);
        
        // Adjust quadrant
        if( ramcRads >= Math.PI/2.d && ramcRads < 3.d*Math.PI/2.d )
            { mcRads += Math.PI; }
        else
        if( ramcRads >= 3.d*Math.PI/2.d )
            { mcRads += 2.d * Math.PI; }
    }
    
    /**
     * (The ascendant is the sign on the Eastern horizon when the birth occurred)
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
    public double declination( double lon )
    {
        // arcsin(sin(zodiacLong)*sin(Obliq))
        return Math.asin(Math.sin(lon) * sinTilt);
    }
    
    /**
     * 4. 11th/12th house cusp
     * See: http://vytautus.com/files/File/pamoka.pdf and Hugh Rice "American Astrology tables ofHouses"
     * 
     * @param ramcrads
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp1112RA( double ramcrads, double f, double offset )
    {
        double ra1 = ramcrads+offset;
        double ra0 = Double.MIN_VALUE;
        double tiltxLat = Math.tan(tiltRads)*Math.tan(latRads);
        
        while(true)
        {
            ra0 = ramcrads + Math.acos(-Math.sin(ra1)*tiltxLat) / f;
            if( Math.abs(ra0-ra1) < Constants.CUSPCONVERGE ) break;
            ra1 = ra0;
        }
            
        // 'Final' RA (unlikely ever to exceed 180.?)
        if( ra0 > Math.PI/2.d )
            return ra0 - Math.PI;
        else
            return ra0;
    }
    
    /**
     * 5. 2nd/3rd house cusp
     * See: http://vytautus.com/files/File/pamoka.pdf and Hugh Rice "American Astrology tables ofHouses"
     * 
     * @param ramcrads
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp23RA( double ramcrads, double f, double offset )
    {
        double ra1 = ramcrads+offset;
        double ra0 = Double.MIN_VALUE;
        double tiltxLat = Math.tan(tiltRads)*Math.tan(latRads);
        
        while(true)
        {
            ra0 = ramcrads + Math.PI - (Math.acos(Math.sin(ra1)*tiltxLat)) / f;
            if( Math.abs(ra0-ra1) < Constants.CUSPCONVERGE ) break;
            ra1 = ra0;
        }

        // 'Final' RA (unlikely ever to exceed 180.?)
        if( ra0 > Math.PI/2.d )
            return ra0 - Math.PI;
        else
            return ra0;
    }
    
    /**
     * 6. Convert final RA to longitude (rads):
     *    2nd/3rd cusp longitude = arctan (tan RA/cos e).
     * 
     * @param ra
     * @return 
     */
    public double ra2Longitude( double ra )
    {
        double longitude = Math.atan(Math.tan(ra)/cosTilt);
        // Is positive value always correct?
        if( longitude < 0.d )
            return longitude + Math.PI;
        else
            return longitude;
    }


    /**
     * Determine all cusps
     * -------------------
     */
    public void allCusps()
    {
        // Populate cusps
        // --------------

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

        // 2nd. MC = arctan (tan RAMC/cos e)
        // Set cusp, we don't use ra2longitude() here
        CuspPlusAngle cpa10 = cpaMap.get(ZhAttribute.MCN).evenAngleHouse(mcRads);
        cpa10.setDeclRads(declMC);

        // 1st house cusp (ASC)
        // 3. Asc = arccot {-[(tan L x sin e) + (sin RAMC x cos e)]/cos RAMC}
        // Set cusp, we don't use ra2longitude() here
        CuspPlusAngle cpa1 = cpaMap.get(ZhAttribute.ASCN).evenAngleHouse(ascRads);
        cpa1.setDeclRads(declAsc);
        
        // 11th house cusp
        //   RA1 = RAMC+30 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/3
        //   :
        //   Etc until delta RA tends to zero
        cusp11Rads = houseCusp1112RA( ramcRads, 3.d, Math.PI/6.d );
        // Get this (empty-ish) cusp
        CuspPlusAngle cpa11 = cpaMap.get(11);
        // Enter up angle in House
        cpa11.evenAngleHouse(ra2Longitude(cusp11Rads));
        // Set declination
        cpa11.setDeclRads(declination(cpa11.getAngle()+cpa11.getHouse().getRadStart()));
        
        // 12th house cusp
        //   RA1 = RAMC+60 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp12Rads = houseCusp1112RA( ramcRads, 1.5d, Math.PI/3.d );
        // Get this (empty-ish) cusp
        CuspPlusAngle cpa12 = cpaMap.get(12);
        // Enter up angle in House
        cpa12.evenAngleHouse(ra2Longitude(cusp12Rads));
        // Set declination
        cpa12.setDeclRads(declination(cpa12.getAngle()+cpa12.getHouse().getRadStart()));
        
        // 2nd house cusp
        //   RA1 = RAMC+120 degrees
        //   RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp2Rads = houseCusp23RA( ramcRads, 1.5d, 2*Math.PI/3.d );

        // Get this (empty-ish) cusp
        CuspPlusAngle cpa2 = cpaMap.get(2);
        // Enter up angle in House
        cpa2.evenAngleHouse(ra2Longitude(cusp2Rads));
        // Set declination
        cpa2.setDeclRads(declination(cpa2.getAngle()+cpa2.getHouse().getRadStart()));
                
        // 3rd house cusp
        //    RA1 = RAMC+150 degrees
        //    RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/3
        //    :
        //    Etc until delta RA tends to zero
        cusp3Rads = houseCusp23RA( ramcRads, 3.d, 5*Math.PI/6.d );

        // Get this (empty-ish) cusp
        CuspPlusAngle cpa3 = cpaMap.get(3);
        // Enter up angle in House
        cpa3.evenAngleHouse(ra2Longitude(cusp3Rads));
        // Set declination
        cpa3.setDeclRads(declination(cpa3.getAngle()+cpa3.getHouse().getRadStart()));
        
        
        // 12. Alternate cusps are simply the opposite signs
        // -------------------------------------------------
        // 3 -> 9
        cpaMap.get(9).setOppositeCusp(cpa3);
        
        // 2 -> 8
        cpaMap.get(8).setOppositeCusp(cpa2);
        
        // 1 -> 7
        cpaMap.get(ZhAttribute.DSCN).setOppositeCusp(cpa1);
        
        // 10 -> 4
        cpaMap.get(ZhAttribute.ICN).setOppositeCusp(cpa10);
        
        // 11 -> 5
        cpaMap.get(5).setOppositeCusp(cpa11);
        
        // 12 -> 6
        cpaMap.get(6).setOppositeCusp(cpa12);
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


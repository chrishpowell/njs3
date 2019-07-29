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
import javafx.geometry.Point2D;


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
                                    ramc, ramcRads,
                                    declMC, declMCRads,
                                    declAsc, declAscRads,
                                    sinTilt, cosTilt,
                                    ra0, raRads,
                                    asc,                                    // Ascendant
                                    ascMod, ascRads,
                                    lst, lstRads,                           // Local Sideral Time
                                    mc, mcRads;
    private  LocalDateTime          ldt;                                    // Birth date
    // Following can be stored against user on db
    private static double           lng, lngRads,
                                    lat, latRads,
                                    cusp2, cusp2Rads,
                                    lon2, lon2Rads,
                                    cusp3, cusp3Rads,
                                    lon3, lon3Rads,
                                    cusp11, cusp11Rads,
                                    lon11, lon11Rads,
                                    cusp12, cusp12Rads,
                                    lon12, lon12Rads;
    
    // Cusp map
    private static final Map<Integer,HousePlusAngle> cuspMap = new HashMap<>();
    
    /**
     * Constructor.
     * 
     * @param localdatetime
     */
    public HoroHouse( LocalDateTime localdatetime )
    {
        this.ldt = localdatetime;
    }


    /**
     * Get latitude and longitude from geonames/place.
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
        LatLon place = null;

        // Place
        try
        {
            place = TimeScale.getGeonameLatLon(placeName);
        }
        catch( Exception ex )
        {
            System.out.println("PROBLEM! Geonames server fail: " +ex.getLocalizedMessage());
            System.out.println("Please enter Latitude and Longitude coordinates manually. Default is [0,0].");
            place = (LatLon)Point2D.ZERO;
        }
        
        return place;
    }
    
    /*
     * Obliquity
     */
    private void calcObliquity( LocalDateTime localdatetime )
    {
        // Obliquity
        tilt = Util.calcObliquityDegs(localdatetime.toLocalDate());
        tiltRads = Math.toRadians(tilt);
        
        // sin & cos of tilt
        sinTilt = Math.sin(tiltRads);
        cosTilt = Math.cos(tiltRads);
    }
    
    /**
     * 2. MC = arctan (tan RAMC/cos e) in radians
     *   [Rule: if RAMC in range 0 to 90d, use the MC figure; if RAMC 90 to 270
     *    add 180 to the MC; if RAMC 270 to 360 add 180 and reverse the sign
     *    (eg Virgo to Pisces)]
     * @return 
     */
    public double calcMC()
    {
        double r = Math.tan(ramcRads)/cosTilt;
        
        double mcr = Math.atan(r);
        if( ramcRads >= 0.d && ramcRads <= Math.PI/2.d )
            { return mcr; }
        else
        if( ramcRads > Math.PI/2. && ramcRads <= 3.d*Math.PI/2.d )
            { return mcr + Math.PI; }
        else
            { return -mcr - Math.PI; }
    }
    
    /**
     * (The ascendant is the sign on the Eastern horizon when the birth occurred)
     * 3. Asc = arccot {-[(tan L x sin e) + (sin RAMC x cos e)]/cos RAMC}
     * [Rule: if Asc 0 to 90 degrees (Aries to Gemini quadrant), accept it ; if Asc -90 to 0
     *  (Cancer to Virgo), add 180 degrees; if Asc 0 to 90 (Libra to Sagittarius), add 180d ;
     *  if Asc -90 to 0 (Capricorn to Pisces), add 180d and reverse the sign]
     *
     * Be aware that with latitudes north/south of Arctic/Antarctic circles,
     * there are discontinuities, hence applied limit.
     * https://en.wikipedia.org/wiki/Ascendant
     * @return 
     */
    public double calcAsc()
    {
        // @TODO: Check latitude outliers (probably better ways of doing this!)
        if( latRads >= Constants.NORTHERNLATS )                                 // Arctic circle
            { latRads = Constants.NORTHERNLIMT; }
        else
        if( latRads <= -Constants.NORTHERNLATS )                                // Antarctic circle
            { latRads = -Constants.NORTHERNLIMT; }
        
        double term1 = Math.tan(latRads) * sinTilt;
        double term2 = Math.sin(ramcRads) * cosTilt;
        double lstRad = Math.toRadians(Util.hrs2DegDec(lst));
        
        ascRads = Math.atan2(-Math.cos(lstRad),(term1+term2));

        if( ascRads < Math.PI )
            { ascRads += Math.PI; }
        else
            { ascRads -= Math.PI; }

        return ascRads;
    }
    
    /**
     * 3A. Find the declination of the MC and Asc
     */
    public void calcDeclMCAsc()
    {
        // Set Decl of MC
        declMCRads = Math.asin(Math.sin(mcRads) * sinTilt);
        declMC = Math.toDegrees(declMCRads);
        
        // Set Dcl of Asc
        declAscRads = Math.asin(Math.sin(ascRads) * sinTilt);
        declAsc = Math.toDegrees(declAscRads);
    }
    
    /**
     * 4. 11th/12th house cusp
     * See: http://vytautus.com/files/File/pamoka.pdf and Hugh Rice "American Astrology tables ofHouses"
     * 
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp1112RA( double f, double offset )
    {
        double ra1 = ramcRads+offset;
        ra0 = Double.MIN_VALUE;
        
        while(true)
        {
            ra0 = ramcRads + Math.acos(-Math.sin(ra1)*Math.tan(tiltRads)*Math.tan(latRads)) / f;
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
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp23RA( double f, double offset )
    {
        double ra1 = ramcRads+offset;
        ra0 = Double.MIN_VALUE;
        
        while(true)
        {
            ra0 = ramcRads + Math.PI - (Math.acos(Math.sin(ra1)*Math.tan(tiltRads)*Math.tan(latRads))) / f;
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
     * 6. Convert final RA to longitude: 2nd/3rd cusp longitude = arctan (tan RA/cos e).
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
     * Determine northern hemisphere cusps
     */
    public void northHemiCusps( double lst )
    {
        // 10th house cusp (MC)
        // --------------------
        // 1. Convert Local Sidereal Time (LST) to Right Ascension of the MC (RAMC)
        ramc = lst * 15.d;
        ramcRads = Math.toRadians(ramc);
        
        // 2. MC = arctan (tan RAMC/cos e)
        mcRads = calcMC();
        // Determine house
        HousePlusAngle cusp10hpa = new HousePlusAngle().evenAngleHouse(mcRads);
        // Store House + angle for MC
        cuspMap.put(10, cusp10hpa);
        
        // 1st house cusp (ASC)
        // --------------------
        // 3. Asc = arccot {-[(tan L x sin e) + (sin RAMC x cos e)]/cos RAMC}
        ascRads = calcAsc();
        // Determine house
        HousePlusAngle cusp1hpa = new HousePlusAngle().evenAngleHouse(ascRads);
        cuspMap.put(1, cusp1hpa);
        
        // 3A. To find the declination of the MC and Asc:
        //    Declination = arcsin [sin(zodiacal longitude) x sin e]
        calcDeclMCAsc();
        cusp1hpa.setDeclRads(declAsc);
        
        // 11th house cusp
        // ---------------
        //   RA1 = RAMC+30 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/3
        //   :
        //   Etc until delta RA tends to zero
        cusp11Rads = houseCusp1112RA( 3.d, Math.PI/6.d );
        lon11Rads = ra2Longitude(cusp11Rads);
        // Determine house
        HousePlusAngle cusp11hpa = new HousePlusAngle().evenAngleHouse(lon11Rads);
        cuspMap.put(11, cusp11hpa);
        
        // 12th house cusp
        // ---------------
        //   RA1 = RAMC+60 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp12Rads = houseCusp1112RA( 1.5d, Math.PI/3.d );
        lon12Rads = ra2Longitude(cusp12Rads);
        // Determine house
        HousePlusAngle cusp12hpa = new HousePlusAngle().evenAngleHouse(lon12Rads);
        cuspMap.put(12, cusp12hpa);
        
        // 2nd house cusp
        // --------------
        //   RA1 = RAMC+120 degrees
        //   RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp2Rads = houseCusp23RA( 1.5d, 2*Math.PI/3.d );
        lon2Rads = ra2Longitude(cusp2Rads);
        // Determine house
        HousePlusAngle cusp2hpa = new HousePlusAngle().evenAngleHouse(lon2Rads);
        cuspMap.put(2, cusp2hpa);
                
        // 3rd house cusp
        // --------------
        //    RA1 = RAMC+150 degrees
        //    RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/3
        //    :
        //    Etc until delta RA tends to zero
        cusp3Rads = houseCusp23RA( 3.d, 5*Math.PI/6.d );
        lon3Rads = ra2Longitude(cusp3Rads);
        // Determine house
        HousePlusAngle cusp3hpa = new HousePlusAngle().evenAngleHouse(lon3Rads);
        cuspMap.put(3, cusp3hpa);
        
        // 12. Alternate cusps are simply the opposite signs
        // -------------------------------------------------
        // 3 -> 9
        HousePlusAngle cusp9hpa = new HousePlusAngle().setOppositeCusp(cusp3hpa);
        cuspMap.put(9, cusp9hpa);
        
        // 2 -> 8
        HousePlusAngle cusp8hpa = new HousePlusAngle().setOppositeCusp(cusp2hpa);
        cuspMap.put(8, cusp8hpa);
        
        // 1 -> 7
        HousePlusAngle cusp7hpa = new HousePlusAngle().setOppositeCusp(cusp1hpa);
        cuspMap.put(7, cusp7hpa);
        
        // 10 -> 4
        HousePlusAngle cusp4hpa = new HousePlusAngle().setOppositeCusp(cusp10hpa);
        cuspMap.put(4, cusp4hpa);
        
        // 11 -> 5
        HousePlusAngle cusp5hpa = new HousePlusAngle().setOppositeCusp(cusp11hpa);
        cuspMap.put(5, cusp5hpa);
        
        // 12 -> 6
        HousePlusAngle cusp6hpa = new HousePlusAngle().setOppositeCusp(cusp12hpa);
        cuspMap.put(6, cusp6hpa);
    }

    /*
     * Getters
     */
    public double getAsc() { return asc; }
    public double getAscMod() { return ascMod; }
    public double getDeclMC() { return declMC; }
    public double getDeclMCRads() { return declMCRads; }
    public double getDeclAsc() { return declAsc; }
    public double getDeclAscRads() { return declAscRads; }
    /*
     * Get cusp map
     */
    public Map<Integer,HousePlusAngle> getCuspMap()
    {
        return cuspMap;
    }
    
    
    /* -----------------------------------------------------
     *          T E S T
     * -----------------------------------------------------
     */
    // 0. Initialisation
    public double init( String placeName, LocalDateTime ldt )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        // Get obliquity 
        calcObliquity( ldt );
        
        // Get LatLon from place
        LatLon place = getLatLonFromPlace( placeName );
        // Convert to radians
        System.out.println("Place: " +place);
        latRads = Math.toRadians(place.getLatitude());
        lngRads = Math.toRadians(place.getLongitude());
        
        // Local Sidereal Time (LST in hrs)
        return TimeScale.getLSTHrs(place, ldt);
    }
    
    // Simple test
    private static void test1()
            throws Exception
    {
        //... *** Southern hemisphere Test for Stanley, Falkland Islands 13Jan1972 6:30 (Ken Ward, trans4mind 1:06:36/1.11)
        //        === Wrong! He assumed DST when it wasn't operating in the Falklands in 1972
        //            [https://www.timeanddate.com/time/change/falkland/stanley?year=1972] ===
        //-----------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 2:06:36/2.11");
        TimeScale.siderealTest( "Stanley, Falkland Islands", LocalDateTime.of(LocalDate.of(1972,1,13), LocalTime.of(6,30,0)) );
        
        // Test for 3 Oct 1990 00:00 Berlin, Germany (astrologerdsbaquila.com, 0h:39m:6.419s/0.65178306)
        //----------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 0h:39m:6.419s/0.65178306");
        TimeScale.siderealTest( "Berlin, Germany", LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.of(0,0,0)) );
    }



    /*
     * Southern hemisphere
     * -------------------
     */
    public void southHemiTest()
            throws Exception
    {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1966,9,30), LocalTime.of(0,0,0));
                
        // Initialise (LST in hrs)
        lst = init("Gaborone, Botswana", ldt);
        System.out.println("lst (expect 0.27468672 hrs): " +lst);
        
        // 10th house cusp (MC)
        // --------------------
        // 1. Convert Local Sidereal Time (LST) to Right Ascension of the MC (RAMC)
        ramc = lst *15.d;
        ramcRads = Math.toRadians(ramc);
        System.out.println("ramc (expect 4.12030083 degs): " +ramc);

        // 2. MC = arctan (tan RAMC/cos e)
        mcRads = calcMC();
        //mc = Math.toDegrees(mcRads);
        // Determine house
        HousePlusAngle scusp10hpa = new HousePlusAngle().evenAngleHouse(mcRads);
        System.out.println("mc{10} (expect 4.48963599): " +Math.toDegrees(scusp10hpa.getAngle()));
        System.out.println("MC{10} in house (expect Aries): " +scusp10hpa.getHouse());
        cuspMap.put(10, scusp10hpa);
        
        // 1st house cusp
        // --------------
        // 3. Asc = arccot {-[(tan L x sin e) + (sin RAMC x cos e)]/cos RAMC}
        ascRads = calcAsc();
        //asc = Math.toDegrees(ascRads);
        // Determine house
        HousePlusAngle scusp1hpa = new HousePlusAngle().evenAngleHouse(ascRads);
        System.out.println("asc{1} (expect 23.32483017): " +Math.toDegrees(scusp1hpa.getAngle()));
        System.out.println("Asc{1} in house (expect Gemini): " +scusp1hpa.getHouse());
        
        // Find the declination of the MC and Asc:
        //    Declination = arcsin [sin(zodiacal longitude) x sin e]
        calcDeclMCAsc();
        System.out.println("decl of MC (expect 1.78478171): " +declMC);
        scusp10hpa.setDeclRads(declMC);
        System.out.println("decl of Asc (expect 23.27715826): " +declAsc);
        scusp1hpa.setDeclRads(declAsc);
        // Store House + angle for ASC
        cuspMap.put(1, scusp1hpa);
        
        // 11th house cusp
        // ---------------
        //   RA1 = RAMC+30 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/3
        //   :
        //   Etc until delta RA tends to zero
        cusp11Rads = houseCusp1112RA( 3.d, Math.PI/6.d );
        System.out.println("cusp11 (astrologerdsbaquila.com, expect 32.09620266): " +Math.toDegrees(cusp11Rads));
        lon11Rads = ra2Longitude(cusp11Rads);
        // Determine house
        HousePlusAngle scusp11hpa = new HousePlusAngle().evenAngleHouse(lon11Rads);
        System.out.println("cusp11 (expect 4.358433889): " +Math.toDegrees(scusp11hpa.getAngle()));
        System.out.println("cusp11 in house (expect Taurus): " +scusp11hpa.getHouse());
        cuspMap.put(11, scusp11hpa);
        
        // 12th house cusp
        // ---------------
        //   RA1 = RAMC+60 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp12Rads = houseCusp1112RA( 1.5d, Math.PI/3.d );
        System.out.println("cusp12 (astrologerdsbaquila.com, expect 57.66430036): " +Math.toDegrees(cusp12Rads));
        // Determine house
        HousePlusAngle scusp12hpa = new HousePlusAngle().evenAngleHouse(ra2Longitude(cusp12Rads));
        System.out.println("cusp12 (expect 29.852783333): " +Math.toDegrees(scusp12hpa.getAngle()));
        System.out.println("cusp12 in house (expect Taurus): " +scusp12hpa.getHouse());
        cuspMap.put(12, scusp12hpa);
        
        // 2nd house cusp
        // --------------
        //   RA1 = RAMC+120 degrees
        //   RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        cusp2Rads = houseCusp23RA( 1.5d, 2*Math.PI/3.d );
        System.out.println("cusp2 (astrologerdsbaquila.com, expect 117.32878912 [or -62.67121088?]): " +Math.toDegrees(cusp2Rads));
        lon2Rads = ra2Longitude(cusp2Rads);
        // Determine house
        HousePlusAngle scusp2hpa = new HousePlusAngle().evenAngleHouse(lon2Rads);
        System.out.println("cusp2 (expect 25.366075): " +Math.toDegrees(scusp2hpa.getAngle()));
        System.out.println("cusp2 in house (expect Cancer): " +scusp2hpa.getHouse());
        cuspMap.put(2, scusp2hpa);
                
        // 3rd house cusp
        // --------------
        //    RA1 = RAMC+150 degrees
        //    RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/3
        //    :
        //    Etc until delta RA tends to zero
        cusp3Rads = houseCusp23RA( 3.d, 5*Math.PI/6.d );
        System.out.println("cusp3 (astrologerdsbaquila.com, expect 152.35346570): " +Math.toDegrees(cusp3Rads));
        lon3Rads = ra2Longitude(cusp3Rads);
        // Determine house
        HousePlusAngle scusp3hpa = new HousePlusAngle().evenAngleHouse(lon3Rads);
        System.out.println("cusp3 (expect 0.275315): " +Math.toDegrees(scusp3hpa.getAngle()));
        System.out.println("cusp3 in house (expect Virgo): " +scusp3hpa.getHouse());
        cuspMap.put(3, scusp3hpa);
        
        // 12. Alternate cusps are simply the opposite signs
        // -------------------------------------------------
        // 3 -> 9
        HousePlusAngle scusp9hpa = new HousePlusAngle().setOppositeCusp(scusp3hpa);
        System.out.println("cusp9 (expect 0.275315): " +Math.toDegrees(scusp9hpa.getAngle()));
        System.out.println("cusp9 in house (expect Pisces): " +scusp9hpa.getHouse());
        cuspMap.put(9, scusp9hpa);

        // 2 -> 8
        HousePlusAngle scusp8hpa = new HousePlusAngle().setOppositeCusp(scusp2hpa);
        System.out.println("cusp8 in house (expect Capricorn): " +scusp8hpa.getHouse());
        System.out.println("cusp8 (expect 25.366075): " +Math.toDegrees(scusp8hpa.getAngle()));
        cuspMap.put(8, scusp8hpa);

        // 1 -> 7
        HousePlusAngle scusp7hpa = new HousePlusAngle().setOppositeCusp(scusp1hpa);
        System.out.println("cusp7 in house (expect Sagittarius): " +scusp7hpa.getHouse());
        System.out.println("cusp7 (expect 23.324830278): " +Math.toDegrees(scusp7hpa.getAngle()));
        cuspMap.put(7, scusp7hpa);

        // 10 -> 4
        HousePlusAngle scusp4hpa = new HousePlusAngle().setOppositeCusp(scusp10hpa);
        System.out.println("cusp4 in house (expect Libra): " +scusp4hpa.getHouse());
        System.out.println("cusp4 (expect 4.489636111): " +Math.toDegrees(scusp4hpa.getAngle()));
        cuspMap.put(4, scusp4hpa);

        // 11 -> 5
        HousePlusAngle scusp5hpa = new HousePlusAngle().setOppositeCusp(scusp11hpa);
        System.out.println("cusp5 in house (expect Scorpio): " +scusp5hpa.getHouse());
        System.out.println("cusp5 (expect 4.358433889): " +Math.toDegrees(scusp5hpa.getAngle()));
        cuspMap.put(5, scusp5hpa);

        // 12 -> 6
        HousePlusAngle scusp6hpa = new HousePlusAngle().setOppositeCusp(scusp12hpa);
        System.out.println("cusp6 in house (expect Scorpio): " +scusp6hpa.getHouse());
        System.out.println("cusp6 (expect 29.852783333): " +Math.toDegrees(scusp6hpa.getAngle()));
        cuspMap.put(6, scusp6hpa);
    }
    
    public static void main(String[] args)
            throws Exception
    {
       /*
        * Southern hemisphere
        * -------------------
        */
        //... Birth date
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1966,9,3), LocalTime.of(0,0));
        
        //... Class
        HoroHouse hhs = new HoroHouse( ldt );
        hhs.southHemiTest();
        
        System.out.println("");
        Map<ZodiacHouse,Integer> nord = ZodiacHouse.getZHNameMap();
        hhs.getCuspMap().forEach((k,v)->{
            System.out.println("Key, value: " +k+", house: "+v.getHouse().getName()+", angle: "+Math.toDegrees(v.getAngle()));
        });
        
       /*
        * Northern hemisphere
        * -------------------
        */
        //... Birth date
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.of(0,0));
//        
//        //... Setup (note LST in degrees)
//        HoroHouse hhn = new HoroHouse( ldt );
//        double lst = hhn.init("Berlin, Germany", ldt);
//        System.out.println("lst (expect 0.65178306 hrs): " +lst);
//
//        hhn.northHemiCusps(lst);
//        Map<ZodiacHouse,Integer> nord = ZodiacHouse.getZHNameMap();
//        hhn.getCuspMap().forEach((k,v)->{
//            System.out.println("Key, value: " +k+", house: "+v.getHouse().getName()+", angle: "+Math.toDegrees(v.getAngle()));
//        });
    }
}


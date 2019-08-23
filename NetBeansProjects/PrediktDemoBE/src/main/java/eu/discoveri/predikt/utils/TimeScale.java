/*
 * TimeScale: Various time utilities
 */
package eu.discoveri.predikt.utils;

import eu.discoveri.predikt.exception.RealTimeConversionException;
import eu.discoveri.predikt.exception.GeonamesNoResultsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.time.temporal.JulianFields;

import org.json.JSONObject;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TimeScale
{
    // Date for 1st Jan 1900
    private static final LocalDate firstJan1900 = LocalDate.of(1900,1,1);
    
    // Format double string
    static DecimalFormat df = new DecimalFormat("#######.#####");
    
    /**
     * Julian DayTime since 31Dec1999 23:59.99  (not 12:00 as with 'classic' MJD).
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param univTime Use 3 dec places (or more) for decent accuracy
     * @return 
     */
    public static double dayModTimeScale( int year, int month, int dayOfMonth, double univTime )
    {
        return LocalDate.of(year, month, dayOfMonth).getLong(JulianFields.MODIFIED_JULIAN_DAY) - Constants.MJD1JAN2000ZERO + univTime/24.d;
    }
    
    // Time since 31Dec1999 23:59.99 (not 12:00 as with 'classic' JD)
    /**
     * Julian DayTime since 31Dec1999 23:59.99  (not 12:00 as with 'classic' MJD).
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param univTime Use 3 dec places (or more) for decent accuracy
     * @return 
     */
    public static double julDayTime( int year, int month, int dayOfMonth, double univTime )
            throws DateTimeException
    {
        return LocalDate.of(year, month, dayOfMonth).getLong(JulianFields.JULIAN_DAY) - Constants.JD1JAN2000ZERO + univTime/24.d;
    }
    
    /**
     * Julian century from J2000.00.
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param univTime
     * @return
     * @throws DateTimeException 
     */
    public static double julCentury( int year, int month, int dayOfMonth, double univTime )
            throws DateTimeException
    {
        return julDayTime(year,month,dayOfMonth,univTime)/Constants.JULDAYSPERC;
    }
    
    /**
     * Calculate days since 1.1.1900.
     * 
     * @param recent
     * @return 
     */
    public static long daysSince1900( LocalDate recent )
    {
        return DAYS.between(firstJan1900,recent) + 1;
    }
    
    /**
     * Calculate centuries since 1.1.1900.
     * 
     * @param recent
     * @return 
     */
    public static double centsSince1900( LocalDate recent )
    {
        System.out.println("daysSince: " +daysSince1900(recent));
        return daysSince1900(recent) / Constants.JULDAYSPERC;
    }
    
    /**
     * Modified Julian Daytime.
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param univTime
     * @return
     * @throws DateTimeException 
     */
    public static double modJulDayTime( int year, int month, int dayOfMonth, double univTime )
            throws DateTimeException
    {
        return LocalDate.of(year, month, dayOfMonth).getLong(JulianFields.MODIFIED_JULIAN_DAY) - Constants.MJD1JAN2000ZERO + univTime/24.d;
    }
    
    /**
     * Modified Julian Daytime.
     * 
     * @param ldt
     * @return
     * @throws DateTimeException 
     */
    public static double modJulDayTime( LocalDateTime ldt )
            throws DateTimeException
    {
        return ldt.getLong(JulianFields.MODIFIED_JULIAN_DAY) - Constants.MJD1JAN2000ZERO + realTime(ldt.toLocalTime())/24.d;
    }
    
    /**
     * Modified Julian Day (MJD)
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     * @throws DateTimeException 
     */
    public static long modJulianDay( int year, int month, int dayOfMonth )
            throws DateTimeException
    {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        return date.getLong(JulianFields.MODIFIED_JULIAN_DAY);
    }
    
    /**
     * Julian Day (date).  Note midnight to midnight (not class noon)
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     * @throws DateTimeException 
     */
    public static long julianDay( int year, int month, int dayOfMonth )
            throws DateTimeException
    {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        return date.getLong(JulianFields.JULIAN_DAY);
    }
    
    /**
     * Julian Day (datetime) as nnnnn.mmm.  Note midnight to midnight (not classic noon).
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param min
     * @param sec
     * @return
     * @throws DateTimeException 
     * @throws RealTimeConversionException
     */
    public static double julianDayTime( int year, int month, int dayOfMonth, int hour, int min, int sec )
            throws DateTimeException, RealTimeConversionException
    {
        return LocalDate.of(year, month, dayOfMonth).getLong(JulianFields.JULIAN_DAY) + realTime(hour,min,sec,0)/24.d;
    }
    
    /**
     * Julian Day Classic (datetime) as nnnnn.mmm.  Note noon to noon (classic).
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param min
     * @param sec
     * @return
     * @throws DateTimeException 
     * @throws RealTimeConversionException
     */
    public static double julianDayTimeClassic( int year, int month, int dayOfMonth, int hour, int min, int sec )
            throws DateTimeException, RealTimeConversionException
    {
        return LocalDate.of(year, month, dayOfMonth).getLong(JulianFields.JULIAN_DAY) + realTime(hour,min,sec,0)/24.d - Constants.JDOFFSETCLASSIC;
    }
    
    /**
     * Julian Day Classic (datetime) as nnnnn.mmm.  Note noon to noon (classic).
     * 
     * @param ldt
     * @return
     * @throws DateTimeException
     * @throws RealTimeConversionException 
     */
    public static double julianDayTimeClassic( LocalDateTime ldt )
            throws DateTimeException, RealTimeConversionException
    {
        LocalTime lt = ldt.toLocalTime();
        return ldt.getLong(JulianFields.JULIAN_DAY) + realTime(lt.getHour(),lt.getMinute(),lt.getSecond(),0)/24.d - Constants.JDOFFSETCLASSIC;
    }
    
    /**
     * Convert hr/min/sec/10ths-sec to decimal time.
     * 
     * @param hour
     * @param min
     * @param sec
     * @param centiSec (max. 36000)
     * @return
     * @throws RealTimeConversionException 
     */
    public static double realTime( int hour, int min, int sec, int centiSec )
            throws RealTimeConversionException
    {
        if( (hour < 0 || hour > 60) ||
            (min < 0 || min > 60)   ||
            (sec < 0 || sec > 60)   ||
            (centiSec < 0 || centiSec > 36000) )
           throw new RealTimeConversionException("Value out of range (usually 0 to 60)");

        return (double)hour + (double)min/60.d + (double)sec/3600.d + (double)centiSec/36000.d;
    }
    
    /**
     * Convert hours decimal to hh:mm:ss.ss
     * 
     * @param realt
     * @return 
     */
    public static String time2Hhmmss( double realt )
    {
        DecimalFormat dfs = new DecimalFormat("##.#");
        
        // Convert to hh:mm:ss.ss
        int hrs = (int)(realt - Util.mod(realt,1.0));
        double mns = (realt - hrs)*60.d;
        double sec = (mns - (int)mns)*60.d;
        
        return String.format("%02d",hrs)+"h:"+String.format("%02d",(int)mns)+"m:"+dfs.format(sec)+"s";
    }
    
    /**
     * Convert hr/min/sec/10ths-sec to decimal time.
     * 
     * @param lt
     * @return 
     */
    public static double realTime( LocalTime lt )
    {
        return (double)lt.getHour() + (double)lt.getMinute()/60.d + (double)lt.getSecond()/3600.d + (double)lt.getNano()/999999999.d;
    }

    
    /*
     * Timezone stuff
     * ==============
     */
    /**
     * Datetime in given zone converted to UTC.
     * 
     * @param timeInZone
     * @param place
     * @throws Exception
     * @return 
     */
    public static LocalDateTime zone2UTC(LocalDateTime timeInZone, LatLon place)
            throws Exception
    {
        ZonedDateTime timeZoned = ZonedDateTime.of(timeInZone, ZoneId.of(getTimezone(place)));
        return timeZoned.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Datetime in UTC converted to time in given zone.
     * 
     * @param timeInUTC
     * @param place Note N is positive, S is negative/Note E is negative, W is positive.
     * @throws Exception
     * @return 
     */
    public static LocalDateTime utc2Zone(LocalDateTime timeInUTC, LatLon place)
            throws Exception
    {
        ZonedDateTime timeZoned = ZonedDateTime.of(timeInUTC,ZoneId.of("UTC"));
        return timeZoned.withZoneSameInstant(ZoneId.of(getTimezone(place))).toLocalDateTime();
    }
    
    /**
     * Datetime in given zone converted to UTC.
     * 
     * @param timeInZone
     * @param zoneId eg: "Europe/Paris", "Etc/GMT+11".  Returns GMT zone if not
     * recognised.
     * @return 
     */
    public static LocalDateTime zone2UTC(LocalDateTime timeInZone, String zoneId)
    {
        ZonedDateTime timeZoned = ZonedDateTime.of(timeInZone, ZoneId.of(zoneId));
        return timeZoned.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Datetime in UTC converted to time in given zone.
     * 
     * @param timeInUTC
     * @param zoneId eg: "Europe/Paris", "Etc/GMT+11".  Returns GMT zone if not
     * recognised.
     * @return 
     */
    public static LocalDateTime utc2Zone(LocalDateTime timeInUTC, String zoneId)
    {
        ZonedDateTime timeZoned = ZonedDateTime.of(timeInUTC,ZoneId.of("UTC"));
        return timeZoned.withZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
    }
    
    /*
     * Get info from geonames.org (<2000 requests daily, may need to go premium)
     * @param url Eg: "http://api.geonames.org/search?q=London,Ontario&maxRows=1&username=demo"
     * @param placeLookup Is this a place query?  Wouldn't be necessary if geonames sent back consistent JSON (ie: resultsCount)
     */
    private static JSONObject getGeoname( URL url, boolean placeLookup )
            throws IOException, GeonamesNoResultsException
    {
        String output, concat = "";
        
        // Connect
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if( conn.getResponseCode() != 200 )
        {
            throw new RuntimeException("Failed : HTTP error code : " +conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while( (output = br.readLine()) != null )
            { concat += output; }
        
        // Disconnect
        conn.disconnect();
        
        JSONObject jo = new JSONObject(concat);
        if( placeLookup && jo.getInt("totalResultsCount") == 0 )
            { throw new GeonamesNoResultsException(url.toString()); }
        
        return jo;
    }
    
    /*
     * Get info from geonames.org (<2000 requests daily, may need to go premium)
     * @param url Eg: "http://api.geonames.org/search?q=London,Ontario&maxRows=1&username=demo"
     */
    private static JSONObject getGeonamePlace( URL url )
            throws IOException, GeonamesNoResultsException
    {
        // This gets a placename and a results count (true)
        return getGeoname( url, true );
    }
    
    /*
     * Get info from geonames.org (<2000 requests daily, may need to go premium)
     * @param url Eg: "http://api.geonames.org/search?q=London,Ontario&maxRows=1&username=demo"
     */
    private static JSONObject getGeonameLatLon( URL url )
            throws IOException, GeonamesNoResultsException
    {
        // This gets the timezone from the Lon/Lat, hence no results count (false)
        return getGeoname( url, false );
    }

    /**
     * Get position from place (degrees).
     * 
     * @param place
     * @return [lat,lon]
     * @throws Exception
     */
    public static LatLon getGeonameLatLon( String place )
            throws Exception
    {
        JSONObject jo;
        String BASESTR = "http://api.geonames.org/search?q=";
        String INTERSTR = "&name_equals=";
        String POSTSTR = "&maxRows=9&type=json&username=discoveri";
        
        // Parse input string
        place = place.trim();                                                   // Get rid of some spaces
        String[] subps = place.split(",");                                      // First is city/town etc., last is country
        
        // Encode the query (and yep NOT the BASESTR or POSTSTR)
        String qquery = URLEncoder.encode(place, StandardCharsets.UTF_8.toString());
        String nquery = URLEncoder.encode(subps[0], StandardCharsets.UTF_8.toString());
        
        // Get Lat/Lon of place from geonames
        URL url = new URL(BASESTR+qquery+INTERSTR+nquery+POSTSTR);
        jo = getGeonamePlace(url).getJSONArray("geonames").getJSONObject(0);

        return new LatLon( Double.parseDouble(jo.getString("lat")), Double.parseDouble(jo.getString("lng")) );
    }
    
    /**
     * ??
     * Get LST as hh:mm:ss.ss
     * 
     * @param place
     * @param ldt
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws GeonamesNoResultsException 
     */
    public static String getLSTHrMinSec( LatLon place, LocalDateTime ldt )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        return time2Hhmmss( getLSTHrs(place,ldt) );
    }
    
    /**
     * ??
     * Get Local Sidereal Time (LST) in Hrs decimal
     * 
     * @param place
     * @param ldt
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws GeonamesNoResultsException 
     */
    public static double getLSTHrs( LatLon place, LocalDateTime ldt )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        // Now get timezone from geonames
        URL url = new URL( "http://api.geonames.org/timezoneJSON?lat="+
                            place.getLatitude()+"&lng="+place.getLongitude()+
                            "&username=discoveri" );
        JSONObject joll = getGeonameLatLon(url);

        // Convert date to UTC, adjusting for timezone, DST etc.
        LocalDateTime uldt = zone2UTC(ldt,joll.getString("timezoneId"));
        
        // Ok, get sidereal time.
        return getLocalSiderealHrsDec(place,uldt);
    }
    
    /**
     * ??
     * Get Local Sidereal Time (LST) in Deg:Min:Sec
     * 
     * @param place
     * @param ldt
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws GeonamesNoResultsException 
     */
    public static double getLSTDeg( LatLon place, LocalDateTime ldt )
            throws MalformedURLException, IOException, GeonamesNoResultsException
    {
        // Now get timezone from geonames
        URL url = new URL(" http://api.geonames.org/timezoneJSON?lat="+place.getLatitude()+"&lng="+place.getLongitude()+"&username=discoveri");
        JSONObject joll = getGeonameLatLon(url);

        // Convert date to UTC, adjusting for timezone, DST etc.
        LocalDateTime uldt = zone2UTC(ldt,joll.getString("timezoneId"));
        
        // Ok, get sidereal time.
        return getLocalSiderealDegDec(place,uldt);
    }
    
    /*
     * Get timezone from lat/long
     */
    private static String getTimezone( LatLon place )
            throws Exception
    {
        JSONObject joll = getGeonameLatLon( new URL("http://api.geonames.org/timezoneJSON?lat="+df.format(place.getLatitude())+"&lng="+df.format(place.getLongitude())+"&username=discoveri") );
        return joll.getString("timezoneId");
    }
    
    /**
     * Returns the current Greenwich sidereal time, measured in degrees decimal.
     * (Note: Convert from zoned datetime to localdatetime using zone2UTC()).
     * 
     * @param ldtUTC The local datetime in UTC
     * @param place 
     * @return degrees decimal
     */
    public static double getLocalSiderealDegDec( LatLon place, LocalDateTime ldtUTC )
    {
        double mjdt = modJulDayTime(ldtUTC)-0.5;
        // Simple calculation for sidereal (degrees)
        double du = (double)ldtUTC.getHour()+(double)ldtUTC.getMinute()/60.d;
        double theta = 100.46 + 0.985647 * mjdt + place.getLongitude() + du * 15.d;
        
        // Southern latitude correction  **** Is this correct?
        if( place.getLatitude() < 0.d ) theta += 180.d;

        return Util.mod( theta, 360.d );
    }
    
    /**
     * Returns the current Greenwich sidereal time, measured in hours decimal.
     * Note: Convert from zoned datetime to localdatetime using zone2UTC().
     * 
     * @param ldtUTC The local datetime in UTC
     * @param place 
     * @return hours
     */
    public static double getLocalSiderealHrsDec( LatLon place, LocalDateTime ldtUTC )
    {
        double sid = Util.mod(getLocalSiderealDegDec(place,ldtUTC) / 15.d, 24.d);
        if( place.getLatitude() < 0.d ) sid += 12.d;
        
        return Util.mod(sid,24.d);
    }
    
    /**
     * Returns the current Greenwich sidereal time, as hh:mm:ss.ss.
     * Note: Convert from zoned datetime to localdatetime using zone2UTC().
     * 
     * @param ldtUTC Local datetime in UTC
     * @param place
     * @return 
     */
    public static String getLocalSiderealHrMinSec( LatLon place, LocalDateTime ldtUTC )
    {
        return time2Hhmmss( getLocalSiderealHrsDec(place, ldtUTC) );
    }
    
    
    
    /*  ------------------------------------------------------------------------
     *  =========
     *  T E S T S
     *  =========
     */
    public static void julDayTests()
            throws Exception
    {
        System.out.println("[Note 'classic' JD goes to midday.  Hence 0.5 added for classic calcs.]");
        System.out.println("realTime for 13:18:59.9: " +realTime(13,18,59,9));
        System.out.println("realTime for 13:18:59.9: " +realTime(LocalTime.of(21,17,30)));
        System.out.println("");
        
        System.out.println("MJDT for 1 Jan 2000 should be 51544: " + modJulianDay(2000,1,1));
        System.out.println("JDT for 1 Jan 2000 should be (classic): 2451544 (+0.5): " + julianDay(2000,1,1));
        System.out.println("JDT less J2000 for 1 Jan 2016 (5844): " +(julianDay(2016,1,1)-2451545));
        System.out.println("");
        
        System.out.println("julDayTime for 31 Dec 1999 23.9999 UT should be zero: " + df.format(julDayTime(1999, 12, 31, 23.9999d)) );
        System.out.println("modJulDayTime for 31 Dec 1999 23.9999 UT should be zero: " + df.format(modJulDayTime(1999, 12, 31, 23.9999d)) );
        System.out.println("julDayTime for 1 Jan 2000 0.0 UT should be zero: " + df.format(julDayTime(2000, 1, 1, 0.d)) );
        System.out.println("modJulDayTime for 1 Jan 2000 0.0 UT should be zero: " + df.format(modJulDayTime(2000, 1, 1, 0.d)) );
        System.out.println("");
        
        System.out.println("modJulDayTime for 2 Nov 2016 21:17:30: " +df.format(modJulDayTime(2006,12,1, 0.d)));
        System.out.println("modJulDayTime for 1 Dec 2006 23:00: " +df.format(modJulDayTime(2006,12,1, 0.d)));
        System.out.println("modJulDayTime for 7 Aug 1997 at 11:00am (stargazing.net [classic]) should be -877.04167(+0.5): " +df.format(modJulDayTime(1997, 8, 7, 11.d)));
        System.out.println("modJulDayTime for 6 Jul 2019 20:32 (Wikipedia) should be "+(2458671-2451545)+ "+(0.85 odd) : " +df.format(modJulDayTime(2019, 7, 6, 20.53334d)) );
        System.out.println("modJulDayTime for 14 Jan 2007 13:18:59.9 (Wikipedia) should be " +df.format(2454115.05486-2451544.5)+ ": " +df.format(modJulDayTime(2007,1,14,realTime(13,18,59,9))));
        System.out.println("modJulDayTime for 19 Apr 1990 23:59.9 UT should be -3543.0: " + df.format(modJulDayTime(1990, 4, 19, 23.999d)) );
        
        // Deliberate error
        // ----------------
        System.out.println("\n\r*** Deliberate error...");
        System.out.println("ERR: Wrong date 32 12 1999: " +modJulDayTime(1999, 12, 32, 23.99d));
    }
    
    /**
     * Test getting sidereal time from pace and local time.
     * 
     * @param placename
     * @param ldt 
     * @throws Exception
     */
    public static void siderealTest( String placename, LocalDateTime ldt )
            throws Exception
    {
        JSONObject jo;
        String BASESTR = "http://api.geonames.org/search?q=";
        String INTERSTR = "&name_equals=";
        String POSTSTR = "&maxRows=9&type=json&username=discoveri";
        
        // Parse input string
        placename = placename.trim();                                           // Get rid of some spaces                        
        
        // Encode the query (and yep NOT the BASESTR or POSTSTR)
        // First is city/town etc., last is country
        String qquery = URLEncoder.encode(placename, StandardCharsets.UTF_8.toString());
        String nquery = URLEncoder.encode(placename.split(",")[0], StandardCharsets.UTF_8.toString());
        
        // Get Lat/Lon of place from geonames
        URL url = new URL(BASESTR+qquery+INTERSTR+nquery+POSTSTR);
        jo = getGeonamePlace(url).getJSONArray("geonames").getJSONObject(0);
        LatLon latlng = new LatLon( Double.parseDouble(jo.getString("lat")),
                                    Double.parseDouble(jo.getString("lng")) );

        // Now get timezone from geonames
        System.out.println( placename+" lat/lon: " +latlng );
        url = new URL( "http://api.geonames.org/timezoneJSON?lat="+
                        latlng.getLatitude()+"&lng="+latlng.getLongitude()+
                        "&username=discoveri");
        JSONObject joll = getGeonameLatLon(url);

        // Convert date to UTC, adjusting for timezone, DST etc.
        LocalDateTime uldt = zone2UTC(ldt,joll.getString("timezoneId"));
        System.out.println( placename+" "+ldt.getHour()+":"+ldt.getMinute()+":"+ldt.getSecond()+" in UTC: " +uldt );
        
        // Ok, get sidereal time.
        double stime = getLocalSiderealHrsDec(latlng,uldt);
        System.out.println( placename+">> LST: " +
                df.format(stime)+"/"+time2Hhmmss(stime) );
    }
    
    /*
     * Run some timezone tests.
     *
     * LSTs all good!
     */
    private static void timezoneTests()
            throws Exception
    {
        LocalDateTime ldt;
        
        //... Random timezone
        ldt = zone2UTC(LocalDateTime.of(LocalDate.of(2006,12,1), LocalTime.of(23,0)),"Europe/Paris");
        System.out.println("CET to UTC 1 Dec 2006 23:00: " +ldt.toString());

        System.out.println("Julian Day number (quae.nl)[2526.91667]: " +df.format(modJulDayTime(ldt)));
        //... Sidereal time
        double lst = getLocalSiderealDegDec(new LatLon(0.d,-5.d),ldt);
        System.out.println("Random>> Sidereal time 1 Dec 2006 23:00, lon 5degW (quae.nl)[45.61655deg/3.0411h]: " +df.format(lst)+"deg/"+df.format(lst/15.d)+"h");
        System.out.println("");
        
        //... Test for Ahmedabad, Gujerat, 12Oct1975 14:20 (Note, timezone IST is ambiguous) (aliceportman.com 15:03:08)
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 15:03:08/15.05223");
        siderealTest( "Ahmedabad, Gujerat, India", LocalDateTime.of(LocalDate.of(1975,10,12), LocalTime.of(14,20,0)) );
        
        //... Test for Utrecht, 1Dec2006 23:00 (quae.nl 3.0411h)
        //------------------------------------------------------
        System.out.println("\r\nExpect: 3.0411");
        siderealTest( "Utrecht, Netherlands", LocalDateTime.of(LocalDate.of(2006,12,1), LocalTime.of(23,0,0)) );
        
        //... Test for West Bromwich, Staffs, England, 7Mar1953 20:00
        //-----------------------------------------------------------
        System.out.println("");
        siderealTest( "West Bromwich, England", LocalDateTime.of(LocalDate.of(1953,3,7), LocalTime.of(20,40,0)) );
        
        //... Test for Cuddapah, India, 25Aug1952 12:45 (Karanam Ramakumar PDF 10:44:7)
        //-----------------------------------------------------------------------------
        System.out.println("\r\nExpect: 10:44:07/10.7353");
        siderealTest( "Cuddapah, India", LocalDateTime.of(LocalDate.of(1952,8,25), LocalTime.of(12,45,0)) );
                
        //... Test for Idaho City, USA, 18Jul1963 14:18 (Ken Ward, trans4mind 09:18:34)
        //-----------------------------------------------------------------------------
        System.out.println("\r\nExpect: 09:18:34/9.30944");
        siderealTest( "Idaho City, USA", LocalDateTime.of(LocalDate.of(1963,7,18), LocalTime.of(14,18,0)) );
                
        //... *** Note date change! Test for Los Angeles, USA, 3Feb1985 20:42 (Ken Ward, trans4mind 4Feb 05:46:11/5.7697)
        //---------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect date change and: 05:46:11/5.7697");
        siderealTest( "Los Angeles, USA", LocalDateTime.of(LocalDate.of(1985,2,3), LocalTime.of(20,42,0)) );

        //... *** Note date change!  Test for Jerusalem, Israel 16May1959 1:05 (Ken Ward, trans4mind 16:57:41/16.9614)
        //------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect date change and: 16:57:41/16.9614");
        siderealTest( "Jerusalem, Israel", LocalDateTime.of(LocalDate.of(1959,5,16), LocalTime.of(1,05,0)) );

        //... *** Southern hemisphere Test for Gisborne, New Zealand 6Mar1944 14:00 (Ken Ward, trans4mind 12:46:28/12.7744)
        //-----------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 12:46:28/12.7744");
        siderealTest( "Gisborne, New Zealand", LocalDateTime.of(LocalDate.of(1944,3,6), LocalTime.of(14,0,0)) );

        //... *** Southern hemisphere Test for Stanley, Falkland Islands 13Jan1972 6:30 (Ken Ward, trans4mind 1:06:36/1.11)
        //        === Wrong! He assumed DST when it wasn't operating in the Falklands in 1972
        //            [https://www.timeanddate.com/time/change/falkland/stanley?year=1972] ===
        //-----------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 1:06:36/1.11");
        siderealTest( "Stanley, Falkland Islands", LocalDateTime.of(LocalDate.of(1972,1,13), LocalTime.of(6,30,0)) );

        //... *** Southern hemisphere Test for Stanley, Falkland Islands 13Jan1972 6:30 (Ken Ward, trans4mind 1:06:36/1.11)
        //        === Wrong! He assumed DST when it wasn't operating in the Falklands in 1972
        //            [https://www.timeanddate.com/time/change/falkland/stanley?year=1972] ===
        //-----------------------------------------------------------------------------------------------------------------
        System.out.println("\r\nExpect: 14:06:40");
        siderealTest( "Stanley, Falkland Islands", LocalDateTime.of(LocalDate.of(1972,1,13), LocalTime.of(6,30,0)) );
        
        //... Test for Idaho City, USA, 18Jul1963 14:18 (Ken Ward, trans4mind 09:18:34)
        //-----------------------------------------------------------------------------
        System.out.println("\r\nExpect: 09:18:34/9.30944");
        siderealTest( "Idaho City, USA", LocalDateTime.of(LocalDate.of(1963,7,18), LocalTime.of(14,18,0)) );
        
        // Test for 3 Oct 1990 00:00 Berlin, Germany (astrologerdsbaquila.com, 0h:39m:6.419s/0.65178306)
        //----------------------------------------------------------------------------------------------
//        System.out.println("\r\nExpect: 0h:39m:6.419s/0.65178306");
//        TimeScale.siderealTest( "Berlin, Germany", LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.of(0,0,0)) );
        
        //        // Test for 3 Oct 1990 00:00 Berlin, Germany (astrologerdsbaquila.com, 0h:39m:6.419s/0.65178306)
//        // ---------------------------------------------------------------------------------------------
//        System.out.println("\r\nExpect: 0h:39m:6.419s/0.65178306");
//        siderealTest( "Berlin, Germany", LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.of(0,0,0)) );

        // Test for 3 Oct 1990 00:00 Berlin, Germany (astrologerdsbaquila.com, 0h:39m:6.419s/0.65178306)
        // ---------------------------------------------------------------------------------------------
//        System.out.println("\r\nExpect: 19h:03m:08s");
//        siderealTest( "Rome, Italy", LocalDateTime.of(LocalDate.of(1982,1,15), LocalTime.of(11,35,0)) );
//        System.out.println("lst: " +time2Hhmmss(19.05459));

        //... Test for Enschede, Netherlands, 2Nov2016 21:17:30 (radixpro.com)
        // neoprogrammics.com: 23.5871639662
        //--------------------------------------------------------------------
//        System.out.println("\r\nExpect (radixpro.com): 0:35:23.6/0.5899018653 (NB: neoprogrammics.com: 23.5871639662)");
//        siderealTest( "Enschede, Netherlands", LocalDateTime.of(LocalDate.of(2016,11,2), LocalTime.of(21,17,30)) );
        
        //... Test for Gaborone, Botswana, 30Sep1966 0:0:0
        // Astrodienst
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 0:16:30");
        siderealTest( "Gaborone, Botswana", LocalDateTime.of(LocalDate.of(1966,9,30), LocalTime.MIDNIGHT) );
        
        //... Test for Manaus, Brazil, 8Jul1961 20:09
        // Astrology Math Made Easy
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 15:15:42");
        siderealTest( "Manaus,Brazil", LocalDateTime.of(LocalDate.of(1961,7,8), LocalTime.of(20,9)) );
        
        //... Test for Karos, South Africa 15May1982 02:22
        // Astrology Math Made Easy
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 17:17:58");
        siderealTest( "Karos, South Africa", LocalDateTime.of(LocalDate.of(1982,5,15), LocalTime.of(2,22)) );

        //... Test for Chicago, Illinois, 22Jun1954 00:44
        // Astrology Math Made Easy
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 21:38:31 (neoprogrammics)");
        siderealTest( "Chicago, USA", LocalDateTime.of(LocalDate.of(1954,6,22), LocalTime.of(4,29)) );

        //... Test for Castellon de la Plana, 1Jan2000 12:00
        // Astrology Math Made Easy
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 21:38:31 (neoprogrammics)");
        siderealTest( "Castellon de la Plana, Spain", LocalDateTime.of(LocalDate.of(2000,1,1), LocalTime.of(12,00)) );
    }

    
    /**
     * M A I N
     * =======
     * @param args 
     * @throws Exception
     */
    public static void main(String[] args)
            throws Exception
    {
        //... Test for Castellon de la Plana, 1Jan2000 12:00
        // Astrology Math Made Easy
        //--------------------------------------------------------------------
        System.out.println("\r\nExpect 17:41:32");
        siderealTest( "Castellon de la Plana, Spain", LocalDateTime.of(LocalDate.of(2000,1,1), LocalTime.of(12,00)) );
        // DeltaT and LST testing
//        deltatTests();
        
        // TimeZone checks
//        System.out.println("\r\nTimezone tests...");
//        timezoneTests();
    }
}

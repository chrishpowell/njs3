/*
 * Test HoroHouse stuff
 */

package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.LatLon;
import eu.discoveri.predikt.utils.TimeScale;
import eu.discoveri.predikt.utils.Util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class HoroHouseTest
{
    private static void mcAscTesting()
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("####.######");
        
        /*
         * Note: Everything is in radians here
         * -----------------------------------
         */
        // Data map
        MCTestTable mctt = new MCTestTable();
        
        // Loop over data map
        mctt.getMctMap().forEach((k,v)->
        {
            // Latitude, longitude
            double lat, lon;
            // UTC
            LocalDateTime ldt = v.getUtcDT();

            //... Setup (note LST in degrees).  Order is important...
            HoroHouse hh = new HoroHouse( new User("A",k,ldt, ChartType.PLACIDUS) );
            
            // 1. Calc obliquity
            hh.calcObliquity(ldt);

            // 2. Get latitude in rads
            LatLon place = v.getPlace();
            if( place.getIfInRadians() )
            {
                lat = place.getLatitude();
                lon = place.getLongitude();
            }
            else
            {
                lat = Math.toRadians(place.getLatitude());
                lon = Math.toRadians(place.getLongitude());
            }
            
            // 3. Set place
            hh.setPlace( place );

            // 4. Local Sidereal Time (LST in hrs)
            try
            {
                hh.calcLST(place);
            }
            catch( Exception ex )
            {
                System.out.println("Problem getting LST: " +ex.getLocalizedMessage());
                System.exit(-1);
            }

            // 5. Calc RAMC
            hh.calcRAMC();
            
            // 6. Calc MC and ASC
            hh.calcMC();
            hh.calcAsc();


            /*
             * Ok, output details
             */
            System.out.println("\r\nName: " +k);
            System.out.println("  Datetime: " +v.getLdt().toString()+"/"+ldt.toString());
            System.out.println("  Lat/Lon: " +df.format(lat)+ "/" + df.format(lon));
            System.out.println("  expected lst: " +v.getExplst()+ ", actual: "+ df.format(hh.getLST()));
            System.out.println("  expected ramc: " +v.getExpramc()+ ", actual: " +df.format(hh.getRAMC()));
            
            // MC: Determine house with net angle.
            CuspPlusAngle mc = hh.getCpaMap().get(ZhAttribute.MCN).evenAngleHouse(hh.getMC());
            System.out.println("  expected mc: " +v.getExpmc()+ ", actual: " +mc.houseWithDegrees());

            // ASC: Determine house with net angle. Note: LST is in hours decimal
            CuspPlusAngle asc = hh.getCpaMap().get(ZhAttribute.ASCN).evenAngleHouse(hh.getAsc());
            // Now for Ascendant
            System.out.println("  expected asc: " +v.getExpAsc()+ ", actual: " +asc.houseWithDegrees() );
        });
    }
    
    private static void hhTesting()
            throws Exception
    {
        String          placeName = null;
        LocalDateTime   ldt = null;
        HoroHouse       hh = null;
        
//... Gaborone, Botswana
// Should be
// 1,  23 Gem 17' 10" (ASC)  23°16'31" N
// 2,  25 Can 20' 33"        21° 4'29" N
// 3,   0 Vir 16' 24"        11°22'43" N
// 4,   4 Lib 29' 46" (IC)    1°47'14" S
// 5,   4 Sco 21' 21"        12°58'32" S
// 6,  29 Sco 50'  0"        20° 7'13" S
// 7,  23 Sag 17' 10" (DSC)  23°16'31" S
// 8,  25 Cap 20' 33"        21° 4'29" S
// 9,   0 Pis 16' 24"        11°22'43" S
// 10,  4 Ari 29' 46" (MC)    1°47'14" N
// 11,  4 Tau 21' 21"        12°58'32" N
// 12, 29 Tau 50'  0"        20° 7'13" N
        placeName = "Gaborone, Botswana";
        System.out.println("Place: " +placeName);
        ldt = LocalDateTime.of(LocalDate.of(1966,9,30), LocalTime.of(4, 30));//LocalTime.MIDNIGHT);
        User user = new User("FredB",placeName,ldt,ChartType.PLACIDUS);
        System.out.println( "Date, time of birth: " +
                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
                ldt.toLocalDate().toString()+ " " +
                ldt.toLocalTime().toString()         );
        
        //... Setup & calc LST
        hh = new HoroHouse(user).init();
        System.out.println("LST (expect 00:16:30): " + TimeScale.time2Hhmmss(hh.getLST()));
        System.out.println("LatLon: " +hh.getLatLon().fmtLatLonDMS());
        
        // Get the cusps
        hh.setWheel(ChartType.PLACIDUS);
        
        // Rough check for longitudes and houses
        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
        System.out.println("");

        hh.getCpaMap().forEach((k,v)->{
            System.out.println("\r\nAngle: " +v.getAngle());
            System.out.println("Housenum: " +v.getHouseNum());
            System.out.println("Housename: " +v.getShortName());
            System.out.println("Key: " +k);
        });

        // Dump cusps
        hh.getCpaMap().forEach((k,v)->{
            double deg = Math.toDegrees(v.getAngle());
            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
        });
//------------------------------------------------------------------------------

//... Rome, Italy.  Seems not to work.
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1982,1,15), LocalTime.of(11,35));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Rome, Italy", ldt);
//        System.out.println("lst (expect 19h:03m:08s): " +hh.lst);
//------------------------------------------------------------------------------

//... Toronto, Ontario
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2000,1,1), LocalTime.of(0,0));
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Toronto, Ontario, ", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------
        
//... Cornwall, England (Harry Potter) Cardinham, Cornwall (4.66667 W, 50.5 N)
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1980,7,31), LocalTime.of(16,39));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Cardinham, Cornwall, England", ldt);
//        System.out.println("lst (expect 11.966388889): " +hh.lst);
//------------------------------------------------------------------------------
        
//... Brandenburg Gate, Berlin, Germany (52N30’58.6", 13E22’39.8")  Slightly different Lat/Lon but works....
// Should be
// 1,   4 Leo 20' 50" (ASC)  19°10'30" N
// 2,  20 Leo 35' 55"        14°37'36" N
// 3,  11 Vir 29' 42"         7°15'14" N
// 4,  10 Lib 36' 25" (IC)    4°11'58" S
// 5,  20 Sco 27' 51"        17°52' 2" S
// 6,   2 Cap 25' 36"        23°25'11" S
// 7,   4 Aqu 20' 50" (DSC)  19°10'30" S
// 8,  20 Aqu 35' 55"        14°37'36" S
// 9,  11 Pis 29' 42"         7°15'14" S
// 10, 10 Ari 36' 25" (MC)    4°11'58" N
// 11, 20 Tau 27' 51"        17°52' 2" N
// 12,  2 Can 25' 36"        23°25'11" N
//        placeName = "Berlin,Germany";
//        System.out.println("Place: " +placeName);
//        ldt = LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.MIDNIGHT);
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup & calc LST
//        hh = new HoroHouse(placeName, ldt).init();
//        System.out.println("LST (expect 0:39:00): " + TimeScale.time2Hhmmss(hh.getLST()));
//        System.out.println("LatLon (52n29,13e21): " +hh.getLatLon().fmtLatLonDMS());
//        System.out.println("");
//
//        // Get the cusps
//        hh.setWheel(ChartType.PLACIDUS);
//        
//        // Rough check for longitudes and houses
//        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
//        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
//        System.out.println("");
//
//        // Dump cusps
//        hh.getCpaMap().forEach((k,v)->{
//            double deg = Math.toDegrees(v.getAngle());
//            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
//        });
//------------------------------------------------------------------------------
        
//... Borth, Wales (52°N 29' 19.93", 4°W 3' 1.4")
// Should be
// 1,  21 Can 46' 45" (ASC)  21°40'49" N
// 2,   7 Leo  9' 22"        18°29' 7" N
// 3,  25 Leo 52' 41"        12°53'41" N
// 4,  21 Vir 40' 12" (IC)    3°18'15" N
// 5,  29 Lib 25' 41"        11°16'18" S
// 6,  15 Sag 32' 42"        22°39'29" S
// 7,  21 Cap 46' 45" (DSC)  19°10'30" S
// 8,   7 Aqu  9' 22"        18°29' 7" S
// 9,  25 Aqu 52' 41"        12°53'41" S
// 10, 21 Pis 40' 12" (MC)    3°18'15" S
// 11, 29 Ari 25' 41"        11°16'18" N
// 12, 15 Gem 32' 42"        22°39'29" N
//        placeName = "Borth,Wales";
//        System.out.println("Place: " +placeName);
//        ldt = LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.MIDNIGHT);
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup & calc LST
//        hh = new HoroHouse(placeName, ldt, cpaMap).init();
//        System.out.println("LST (expect 23:29:24): " + TimeScale.time2Hhmmss(hh.getLST()));
//        System.out.println("LatLon (52n29,4w03): " +hh.getLatLon().fmtLatLonDMS());
//        System.out.println("");
//
//        // Get the cusps
//        hh.setWheel(ChartType.PLACIDUS);
//        
//        // Rough check for longitudes and houses
//        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
//        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
//        System.out.println("");
//
//        // Dump cusps
////        hh.getCpaMap().forEach((k,v)->{
////            double deg = Math.toDegrees(v.getAngle());
////            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
////        });
//        hh.getCpaMap().forEach((k,v)->{
//            System.out.println("\r\nAngle: " +v.getAngle());
//            System.out.println("Housenum: " +v.getHouseNum());
//            System.out.println("Housename: " +v.getShortName());
//            System.out.println("Key: " +k);
//        });
//        double adRot = 2.d*Math.PI-hh.getCpaMap().get(1).getAngle();
//        System.out.println("AscDsc rotate (rads/degs): " +adRot+"/"+Math.toDegrees(adRot));
//------------------------------------------------------------------------------

//... Riga, Latvia
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(0,3));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Riga, Latvia", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//... Riga, Latvia
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(23,57));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Riga, Latvia", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//... Dublin, Ireland
// Should be
// 1,  18 Cap 59' 25" (ASC)
// 2,  18 Pis 39' 41"
// 3,   1 Tau 14' 9"
// 4,  25 Tau 56' 1"  (IC)
// 5,  13 Gem 44' 42"
// 6,  29 Gem 52' 20"
// 7,  18 Can 59' 25" (DSC)
// 8,  18 Vir 39' 41"
// 9,   1 Sco 14' 9"
// 10, 25 Sco 56' 1" (MC)
// 11, 13 Sag 44' 42"
// 12, 29 Sag 52' 20"
//        placeName = "Dublin,Ireland";
//        System.out.println("Place: " +placeName);
//        ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(0,3));
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup & calc LST
//        hh = new HoroHouse(placeName, ldt).init();
//        System.out.println("LST (expect 15:34:26): " + TimeScale.time2Hhmmss(hh.getLST()));
//        System.out.println("LatLon (53n20,6w15): " +hh.getLatLon().fmtLatLonDMS()+"/"+ hh.getLatLon().toString());
//        System.out.println("");
//
//        // Get the cusps
//        hh.setWheel(ChartType.PLACIDUS);
//        
//        // Rough check for longitudes and houses
//        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
//        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
//        System.out.println("");
//
//        // Dump cusps
//        hh.getCpaMap().forEach((k,v)->{
//            double deg = Math.toDegrees(v.getAngle());
//            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
//        });
//------------------------------------------------------------------------------
//... Enschede, Holland
// Should be
// 1,  22 Can 30' 58" (ASC) 21°33'16" N
// 2,   8 Leo  2' 54"       18°15'  5" N
// 3,  27 Leo  0' 38"       12°30'22" N
// 4,  23 Vir  7'  2" (IC)   2°43'54" N
// 5,   1 Sco  2' 28"       11°50'  2" S
// 6,  16 Sag 40' 57"       22°46'  7" S
// 7,  22 Cap 30' 58" (DSC) 21°33'16" S
// 8,   8 Aqu  2' 54"       18°15'  5" S
// 9,  27 Aqu  0' 38"       12°30'22" S
// 10, 23 Pis  7'  2"(MC)    2°43'54" S
// 11,  1 Tau  2' 28"       11°50'  2" N
// 12, 16 Gem 40' 57"       22°46'  7" N
//        placeName = "Enschede,Holland";
//        System.out.println("Place: " +placeName);
//        ldt = LocalDateTime.of(LocalDate.of(2016,11,2), LocalTime.of(21,17,30));
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup & calc LST
//        hh = new HoroHouse(placeName, ldt).init();
//        System.out.println("LST (expect 23:34:43): " + TimeScale.time2Hhmmss(hh.getLST()));
//        System.out.println("LatLon (53n13,6e54): " +hh.getLatLon().fmtLatLonDMS() +"/"+ hh.getLatLon().toString());
//        System.out.println("");
//
//        // Get the cusps
//        hh.setWheel(ChartType.PLACIDUS);
//        
//        // Rough check for longitudes and houses
//        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
//        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
//        System.out.println("");
//
//        // Dump cusps
//        hh.getCpaMap().forEach((k,v)->{
//            double deg = Math.toDegrees(v.getAngle());
//            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
//        });
//------------------------------------------------------------------------------
//... Recife, Brazil
// Should be
// 1,  27 Leo 27' 12" (ASC) 12°21'22" N
// 2,   1 Lib 37'  9"        0°38'38" S
// 3,   4 Sco 18' 20"       12°57'17" S
// 4,   3 Sag 26' 53" (IC)  20°50'34" S
// 5,   0 Cap 13' 54"       23°26'16" S
// 6,  27 Cap 19' 16"       20°41'41" S
// 7,  27 Aqu 27' 12" (DSC) 12°21'22" S
// 8,   1 Ari 37'  9"        0°38'38" N
// 9,   4 Tau 18' 20"       12°57'17" N
// 10,  3 Gem 26' 53"(MC)   20°50'34" N
// 11,  0 Can 13' 54"       23°26'16" N
// 12, 27 Can 19' 16"       20°41'41" N
//        placeName = "Recife,Brazil";
//        System.out.println("Place: " +placeName);
//        ldt = LocalDateTime.of(LocalDate.of(2000,5,5), LocalTime.of(12,30));
//        System.out.println( "Date, time of birth: " +
//                Util.caps(ldt.getDayOfWeek().toString())+ ", " +
//                ldt.toLocalDate().toString()+ " " +
//                ldt.toLocalTime().toString()         );
//        
//        //... Setup & calc LST
//        hh = new HoroHouse(placeName, ldt).init();
//        System.out.println("LST (expect 4:05:42): " + TimeScale.time2Hhmmss(hh.getLST()));
//        System.out.println("LatLon (8s03,34w52): " +hh.getLatLon().fmtLatLonDMS() +"/"+ hh.getLatLon().toString());
//        System.out.println("");
//
//        // Get the cusps
//        hh.setWheel(ChartType.PLACIDUS);
//        
//        // Rough check for longitudes and houses
//        System.out.println("\r\nMC longitude check OK: " +hh.quikLonMCChk(hh.getLST(), hh.getCpaMap().get(10)));
//        System.out.println("ASC/MC cross check: " +hh.ascMCHouseChk( hh.getCpaMap().get(1).getHouse(), hh.getCpaMap().get(10).getHouse() ) );
//        System.out.println("");
//
//        // Dump cusps
//        hh.getCpaMap().forEach((k,v)->{
//            double deg = Math.toDegrees(v.getAngle());
//            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
//        });
//------------------------------------------------------------------------------
//... Dublin, Ireland
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(23,57));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Dublin, Ireland", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//... Newcastle, Australia
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(0,3));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Newcastle, Australia", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//... Newcastle, Australia [Fails on House 7, null pointer]
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(23,57));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Newcastle, Australia", ldt);
//        System.out.println("lst (expect 17:02:57/17.0491667): " +hh.lst);
//------------------------------------------------------------------------------

//... Havana, Cuba [MC longitude check OK: false]
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(0,3));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Havana, Cuba", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//... Havana, Cuba [MC longitude check OK: false]
//        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(23,57));
//        
//        //... Setup
//        HoroHouse hh = new HoroHouse( ldt );
//        hh.lst = hh.init("Havana, Cuba", ldt);
//        System.out.println("lst (expect ?): " +hh.lst);
//------------------------------------------------------------------------------

//        hh.northHemiCusps(hh.lst);
//        System.out.println("ramc (rads): " +ramc);
//        System.out.println("MC longitude check OK: " +quikLonMCChk(lst, cusp10hpa));
//        hh.getCuspMap().forEach((k,v)->{
//            double deg = Math.toDegrees(v.getAngle());
//            System.out.println("[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg));
//        });
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
        //mcAscTesting();
        hhTesting();
    }
}


//------------------------------------------------------------------------------
//  Table for MC and ASC
class MCTestTable
{
    private LatLon              place = null;                         // Can be degrees or radians
    private LocalDateTime       ldt = null;                           // Local datetime
    private String              tz = null;                            // Timezone (eg: Europe/London)
    
    private LocalDateTime       utcDT;                                // UTC time

    // Expected values
    private double              explst,
                                expramc;
    private HouseAngle          expmc = null,
                                expasc = null;

    private static Map<String,MCTestTable>   mctMap = null;
    static
    {
        try
        {
            mctMap = new HashMap<>();
            mctMap.put( "Rosscarbery-20000101-12:00",
                        new MCTestTable( new LatLon(51.4934d,-9.167325d, false),
                        LocalDateTime.of(LocalDate.of(2000,1,1),LocalTime.NOON),"Europe/London",
                        18.096111d, 4.74d,
                            new HouseAngle(ZodiacHouse.CAPRICORN,0.02303835d), new HouseAngle(ZodiacHouse.ARIES,0.060403d)  ));

            mctMap.put( "Gaborone-19660930-00:00",
                        new MCTestTable( new LatLon(-24.6282d,25.9231d, false),
                        LocalDateTime.of(LocalDate.of(1966,9,30),LocalTime.MIDNIGHT),"Africa/Gaborone",
                        0.275d, 0.071995d,
                            new HouseAngle(ZodiacHouse.ARIES,0.0784719d), new HouseAngle(ZodiacHouse.GEMINI,0.406419d)  ));

            mctMap.put( "Recife-20000101-12:30",
                        new MCTestTable( new LatLon(-8.053889,-34.8811d, false),
                        LocalDateTime.of(LocalDate.of(2000,1,1),LocalTime.of(12,30)),"America/Recife",
                        18.87861d, 4.94d,
                            new HouseAngle(ZodiacHouse.CAPRICORN,0.211616d), new HouseAngle(ZodiacHouse.ARIES,0.2356194d)  ));

            mctMap.put( "Recife-20000322-12:30",
                        new MCTestTable( new LatLon(-8.0578,-34.8829d, false),
                        LocalDateTime.of(LocalDate.of(2000,3,22),LocalTime.of(12,30)),"America/Recife",
                        1.203889d, 0.315177d,
                            new HouseAngle(ZodiacHouse.ARIES,0.34142d), new HouseAngle(ZodiacHouse.CANCER,0.235469d)  ));

            mctMap.put( "Recife-20000621-12:30",
                        new MCTestTable( new LatLon(-8.0578,-34.8829d, false),
                        LocalDateTime.of(LocalDate.of(2000,6,21),LocalTime.of(12,30)),"America/Recife",
                        7.18334d, 1.8806d,
                            new HouseAngle(ZodiacHouse.CANCER,0.28567d), new HouseAngle(ZodiacHouse.LIBRA,0.356949d)  ));

            mctMap.put( "Recife-20000923-12:30",
                        new MCTestTable( new LatLon(-8.0578,-34.8829d, false),
                        LocalDateTime.of(LocalDate.of(2000,9,23),LocalTime.of(12,30)),"America/Recife",
                        13.36d, 3.4976398d,
                            new HouseAngle(ZodiacHouse.LIBRA,0.3851263d), new HouseAngle(ZodiacHouse.CAPRICORN,0.38161d)  ));

            mctMap.put( "Recife-20001222-12:30",
                        new MCTestTable( new LatLon(-8.0578,-34.8829d, false),
                        LocalDateTime.of(LocalDate.of(2000,12,22),LocalTime.of(12,30)),"America/Recife",
                        19.27389d, 5.04589d,
                            new HouseAngle(ZodiacHouse.CAPRICORN,0.307779d), new HouseAngle(ZodiacHouse.ARIES,0.34077d)  ));

            mctMap.put( "ChadwellStMary-20001222-12:30",
                        new MCTestTable( new LatLon(51.48334d,0.3667d, false),
                        LocalDateTime.of(LocalDate.of(2000,1,1),LocalTime.of(12,30)),"Europe/London",
                        19.223d, 5.03257d,
                            new HouseAngle(ZodiacHouse.CAPRICORN,0.29531d), new HouseAngle(ZodiacHouse.TAURUS,0.17975d)  ));

            mctMap.put( "Santiago,Chile-20000322-00:30",
                        new MCTestTable( new LatLon(-33.4489d,-70.6693d, false),
                        LocalDateTime.of(LocalDate.of(2000,3,22),LocalTime.of(0,30)),"America/Santiago",
                        11.788d, 3.08609d,
                            new HouseAngle(ZodiacHouse.VIRGO,0.46311d), new HouseAngle(ZodiacHouse.CAPRICORN,0.2091d)  ));

            mctMap.put( "Greenwich-20000101-12:00",
                        new MCTestTable( new LatLon(51.5d,0d, false),
                        LocalDateTime.of(LocalDate.of(2000,1,1),LocalTime.of(12,0)),"Europe/London",
                        18.6972d, 4.89492d,
                            new HouseAngle(ZodiacHouse.CAPRICORN,0.16775d), new HouseAngle(ZodiacHouse.ARIES,0.42362d)  ));
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Setup MC test map
     */
    public MCTestTable(){}
    
    /**
     * Constructor.
     * 
     * @param latlon
     * @param localdt
     * @param timezone 
     */
    public MCTestTable( LatLon latlon, LocalDateTime localdt, String timezone, double expLST, double expRAMC, HouseAngle expMC, HouseAngle expASC )
    {
        this.place = latlon;
        this.ldt = localdt;
        this.tz = timezone;
        this.explst = expLST;
        this.expramc = expRAMC;
        this.expmc = expMC;
        this.expasc = expASC;
        this.utcDT = TimeScale.zone2UTC(this.ldt,this.tz);
    }
    

    // Get map
    public Map<String, MCTestTable> getMctMap() { return mctMap; }
    public LatLon getPlace() { return place; }
    public LocalDateTime getLdt() { return ldt; }
    public String getTz() { return tz; }
    public LocalDateTime getUtcDT() { return utcDT; }
    public double getExplst() { return explst; }
    public double getExpramc() { return expramc; }
    public HouseAngle getExpmc() { return expmc; }
    public HouseAngle getExpAsc() { return expasc; }
}


//-------------------------------------------------------------
class HouseAngle
{
    private final double      angle;
    private final ZodiacHouse zh;

    /**
     * Constructor.
     * @param zh
     * @param angle (in radians, converted to degrees)
     */
    public HouseAngle( ZodiacHouse zh, double angle )
    {
        this.angle = Math.toDegrees(angle);
        this.zh = zh;
    }

    public double getAngle() { return angle; }
    public ZodiacHouse getZh() { return zh; }
    
    @Override
    public String toString()
    {
        return Util.houseDec2ddmmss(zh.getShortname(),angle);
    }
}

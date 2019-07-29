/*
 * Test HoroHouse stuff
 */

package eu.discoveri.predikt.test.horochart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class HoroHouseTest
{
    public static void main(String[] args)
            throws Exception
    {
       /*
        * Northern hemisphere
        * -------------------
        */
        //... Birth date
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,10,3), LocalTime.of(0,0));
        
        //... Setup (note LST in degrees)
        HoroHouse hh = new HoroHouse( ldt );
        double lst = hh.init("Berlin, Germany", ldt);
        System.out.println("lst (expect 0.65178306 hrs): " +lst);

        hh.northHemiCusps();
    }
}

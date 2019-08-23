/** ***************************************************************************\
 * AstroTest
 *
 * A Generic Test Harness
 *
 * \**************************************************************************** */
package eu.discoveri.predikt.astronomy.library;

import eu.discoveri.predikt.utils.TimeScale;
import java.time.LocalDateTime;


public class AstroTest
{
    public static void main(String args[])
            throws Exception 
    {
         Punit();
//        LunarRS();
    }

    public static void LunarRS()
    {
        ObsInfo oi = new ObsInfo(new Latitude(40.0), new Longitude(-75.8));
        System.out.println(LunarCalc.summary(oi));
        System.out.println(LunarCalc.summaryPHL());
    }

    public static void Punit()
            throws Exception
    {
//        double jd = AstroDate.jd(new AstroDate(30,9,1966,12,30,0),false);
        LocalDateTime ldt = LocalDateTime.of(1966,9,30, 0,0,0);
        double jd = TimeScale.julianDayTimeClassic(ldt);
        
        ObsInfo oi = new ObsInfo(new Latitude(-24.75), new Longitude(-25.9167));

        // Sun
        try
        {
            PlanetData pde = new PlanetData(Planets.SUN, jd, oi);
            System.out.println("Sun Lon = " + Math.toDegrees(pde.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Sun: " + e);
        }

        // Mars
        try
        {
            PlanetData pdm = new PlanetData(Planets.MARS, jd, oi);
            System.out.println("Mars Lon = " + Math.toDegrees(pdm.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Mars: " + e);
        }
        
        // Jupiter
        try
        {
            PlanetData pdj = new PlanetData(Planets.JUPITER, jd, oi);
            System.out.println("Jup Lon = " + Math.toDegrees(pdj.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Jup: " + e);
        }
        
        // Mercury
        try
        {
            PlanetData pdmy = new PlanetData(Planets.MERCURY, jd, oi);
            System.out.println("Merc Lon = " + Math.toDegrees(pdmy.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Mercury: " + e);
        }
        
        // Moon
        try
        {
            PlanetData pdmn = new PlanetData(Planets.LUNA, jd, oi);
            System.out.println("Moon Lon = " + Math.toDegrees(pdmn.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Moon: " + e);
        }

        // Neptune
        try
        {
            PlanetData pdmn = new PlanetData(Planets.NEPTUNE, jd, oi);
            System.out.println("Nepune Lon = " + Math.toDegrees(pdmn.getEclipticLon()));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Moon: " + e);
        }
    }
}

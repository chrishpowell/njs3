/** ***************************************************************************\
 * AstroTest
 *
 * A Generic Test Harness
 *
 * \**************************************************************************** */
package eu.discoveri.predikt.astronomy.library;

import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;


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
    
    private static void outPlanet( PlanetData pde )
            throws Exception
    {
        System.out.println(Planets.getPlanetmap().get(pde.planet())+": AltAzLon: " +Util.mod(Math.toDegrees(pde.getAltAzLon()),30.d) +
        ", RADeg: " +Math.toDegrees(pde.getRightAscension()) + 
        ", RAhms: " +Util.dec2hhmmssRA(Math.toDegrees(pde.getRightAscension())) + 
        ", Decldms: " +Util.dec2ddmmss(Math.toDegrees(pde.getDeclination()),Constants.LAT) +
        ", HAm: " +Math.toDegrees(pde.hourAngle()) +
        ", PLonm: " +Math.toDegrees(pde.getPolarLon()) );
    }

    public static void Punit()
            throws Exception
    {
        double jd = AstroDate.jd(new AstroDate(30,9,1966,22,30,0),false);
        System.out.println("jd: " +jd);
//        LocalDateTime ldt = LocalDateTime.of(1966,9,30, 4,30,0);
//        double jd = TimeScale.julianDayTimeClassic(ldt);
        
        ObsInfo oi = new ObsInfo(new Latitude(-24.75), new Longitude(25.9167));

        // All bodies
        try
        {
            outPlanet(new PlanetData(Planets.SUN, jd, oi));
            outPlanet(new PlanetData(Planets.LUNA, jd, oi));
            outPlanet(new PlanetData(Planets.MERCURY, jd, oi));
            outPlanet(new PlanetData(Planets.VENUS, jd, oi));
            outPlanet(new PlanetData(Planets.MARS, jd, oi));
            outPlanet(new PlanetData(Planets.JUPITER, jd, oi));
            outPlanet(new PlanetData(Planets.SATURN, jd, oi));
            outPlanet(new PlanetData(Planets.URANUS, jd, oi));
            outPlanet(new PlanetData(Planets.NEPTUNE, jd, oi));
            outPlanet(new PlanetData(Planets.PLUTO, jd, oi));
        }
        catch( NoInitException e )
        {
            System.out.println("Error calculating Body: " + e);
        }
    }
}

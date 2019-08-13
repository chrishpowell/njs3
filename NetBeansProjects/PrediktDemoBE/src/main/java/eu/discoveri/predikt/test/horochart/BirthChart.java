/*
 * Birth chart
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.LatLon;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BirthChart
{
    private final String                      shortName;
    // Local Sidereal Time (of birth)
    private final double                      lst = Double.MIN_VALUE;
    // Only Placidus at time of development...
    private final ChartType                   defChartType = ChartType.PLACIDUS;
    private final LocalDateTime               birthDate;
    private final LatLon                      birthPlace;
    private final Map<Integer,CuspPlusAngle> hpa = new HashMap<>();
    
    public BirthChart( String shortname, LocalDateTime ldt, LatLon birthplace )
    {
        this.shortName = shortname;
        this.birthDate = ldt;
        this.birthPlace = birthplace;
    }
    
    private static LatLon getLatLon( String place )
    {
        
    }
    
    private void houseChartBuild()
    {
        
    }
}

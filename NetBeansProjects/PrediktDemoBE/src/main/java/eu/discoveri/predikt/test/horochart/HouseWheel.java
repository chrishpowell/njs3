/*
 * House Wheel, marker only?
 */
package eu.discoveri.predikt.test.horochart;

import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 */
public interface HouseWheel
{
    // Calculate the cusps
    public void determineCusps( Map<Integer,CuspPlusAngle> cpaMap,
                                double ramcRads, double mcRads, double ascRads, double declMC, double declAsc);
}

/*
 * Zodiac house plus angle.
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class HousePlusAngle
{
    private double      angleNetRads,
                        declRads;
    private ZodiacHouse zh;
    private String      attribute;                  // MC or ASC usually
    
    /**
     * Constructor.
     */
    public HousePlusAngle(){}

    /**
     * Even House angles (eg: Placidus).
     * 
     * @param inAngle in radians
     * @return 
     */
    public HousePlusAngle evenAngleHouse( double inAngle )
    {
        // Net angle
        angleNetRads = Util.mod(inAngle, Constants.EVENHOUSEANGLE);
        // House
        int numHouse = (int)((inAngle - angleNetRads)/Constants.EVENHOUSEANGLE);
        zh = ZodiacHouse.getZHOrderMap().get(numHouse);
        
        return this;
    }

    // Getters
    /**
     * Get angle in this house.
     * 
     * @return 
     */
    public double getAngle() { return angleNetRads; }
    /**
     * This old house...
     * 
     * @return 
     */
    public ZodiacHouse getHouse() { return zh; }

    /**
     * Set an attribute (usually MC or ASC).
     * @param attribute 
     */
    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }
    
    /**
     * Set decl
     * @param declRads 
     */
    public void setDeclRads(double declRads)
    {
        this.declRads = declRads;
    }

    /**
     * Set opposite cusps
     * 
     * @param hpa
     * @return 
     */
    public HousePlusAngle setOppositeCusp( HousePlusAngle hpa )
    {
        Map<ZodiacHouse,Integer> name = ZodiacHouse.getZHNameMap();
        
        // Expect only 0,1,2 and 9,10,11
        int order = name.get(ZodiacHouse.valueOf(hpa.getHouse().getName().toUpperCase()));
        order = order > 6 ? order - 6 : order + 6;
        
        Map<Integer,ZodiacHouse> nord = ZodiacHouse.getZHOrderMap();
        this.zh = nord.get(order);
        this.angleNetRads = hpa.getAngle();
        
        return this;
    }
}

/*
 * Zodiac house plus angle.
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.Util;
import java.text.DecimalFormat;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class CuspPlusAngle
{
    private double                      angleNetRads,
                                        declRads = Double.MIN_VALUE;
    private int                         houseNum = 0;
    private ZodiacHouse                 zh;
    private ZhAttribute                 attribute;                              // MC, IC, ASC, DSC
    
    private Map<ZodiacHouse,Integer>    name = ZodiacHouse.getZHNameMap();      // Name to ordering
    private Map<Integer,ZodiacHouse>    nord = ZodiacHouse.getZHOrderMap();     // Ordering to name

    
    /**
     * Constructor.
     */
    public CuspPlusAngle(){}

    /**
     * Set House and Angle (excluding MC). Even House angles (eg: Placidus).
     * 
     * @param inAngle in radians
     * @return 
     */
    public CuspPlusAngle evenAngleHouse( double inAngle )
    {
        // Net angle
        angleNetRads = Util.mod(inAngle, Constants.EVENHOUSEANGLE);
        
        // Set House
        houseNum = (int)((inAngle - angleNetRads)/Constants.EVENHOUSEANGLE);
        zh = ZodiacHouse.getZHOrderMap().get(houseNum);
        
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
     * Get declination
     * @return 
     */
    public double getDecl() { return declRads; }
    
    /**
     * This old house...
     * 
     * @return 
     */
    public ZodiacHouse getHouse() { return zh; }
    
    /**
     * Get house name.
     * 
     * @return 
     */
    public String getName() { return zh.getName(); }
    
    /**
     * Get short house name
     * @return 
     */
    public String getShortName() { return zh.getShortname(); }
    
    /**
     * Get house number (0-11)
     * 
     * @return 
     */
    public int getHouseNum() { return houseNum; }
    
    /**
     * Get the attribute for this cusp
     * 
     * @return 
     */
    public ZhAttribute getAttribute() { return attribute; }
    
    /**
     * Setthe attribute for this cusp
     * @param zha 
     * @return 
     */
    public CuspPlusAngle setAttribute( ZhAttribute zha )
    {
        this.attribute = zha;
        return this;
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
    public CuspPlusAngle setOppositeCusp( CuspPlusAngle hpa )
    {
        // Expect only 0,1,2 and 9,10,11 (ie 1st 6 houses of 0-11)
        int order = hpa.getHouseNum();
        order = order >= 6 ? order - 6 : order + 6;
        
        // Set opposite
        zh = nord.get(order);
        houseNum = order;
        angleNetRads = hpa.getAngle();
        declRads = -hpa.getDecl();
        
        return this;
    }
    
    /**
     * Output NN Hse MM Ss.ss for the net angle (rads).
     * 
     * @return 
     */
    public String houseWithDegrees()
    {
        return Util.houseDec2ddmmss(getShortName(),Math.toDegrees(angleNetRads));
    }

    /**
     * Stringify.
     * @return 
     */
    @Override
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("#.####");
        return zh.getName()+", ra(rads): "+df.format(angleNetRads)+", decl(rads):"+df.format(declRads);
    }
}

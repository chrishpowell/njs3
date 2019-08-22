/*
 * Placidus
 */

package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.Constants;
import java.util.Map;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PlacidusWheel extends Wheel 
{
    
    private final double    tiltRads,                   // Earth tilt
                            latRads;                    // User latitude
    
    public PlacidusWheel( double tiltRads, double latRads )
    {
        super(ChartType.PLACIDUS);
        this.latRads = latRads;
        this.tiltRads = tiltRads;
    }
    
    
    
    /**
     * 11th/12th house cusp
     * See: http://vytautus.com/files/File/pamoka.pdf and Hugh Rice "American Astrology tables ofHouses"
     * 
     * @param ramcrads
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp1112RA( double ramcrads, double f, double offset )
    {
        double ra1 = ramcrads+offset;
        double ra0 = Double.MIN_VALUE;
        double tiltxLat = Math.tan(tiltRads)*Math.tan(latRads);
        
        // Converge to longitude
        while(true)
        {
            ra0 = ramcrads + Math.acos(-Math.sin(ra1)*tiltxLat) / f;
            if( Math.abs(ra0-ra1) < Constants.CUSPCONVERGE ) break;
            ra1 = ra0;
        }

        return ra0;
    }
    
    /**
     * 2nd/3rd house cusp
     * See: http://vytautus.com/files/File/pamoka.pdf and Hugh Rice "American Astrology tables ofHouses"
     * 
     * @param ramcrads
     * @param f
     * @param offset
     * @return 
     */
    public double houseCusp23RA( double ramcrads, double f, double offset )
    {
        double ra1 = ramcrads+offset;
        double ra0 = Double.MIN_VALUE;
        double tiltxLat = Math.tan(tiltRads)*Math.tan(latRads);
        
        // Converge to longitude
        while(true)
        {
            ra0 = ramcrads + Math.PI - (Math.acos(Math.sin(ra1)*tiltxLat)) / f;
            if( Math.abs(ra0-ra1) < Constants.CUSPCONVERGE ) break;
            ra1 = ra0;
        }

        return ra0;
    }
    

    /**
     * Determine all cusps
     * -------------------
     * 
     * @param cpaMap Map initialised to an integer and (empty) class CuspPlusAngle
     * (house and net angle) defining the set of cusps with house/angle indexed by 
     * an integer, normally 1 to 12.  It is usual to initialise the Map with an
     * attribute such as ZhAttribute.ASC [new CuspPlusAngle().setAttribute(...)],
     * otherwise the attribute may be set to ZhAttribute.UNDEF.
     * @param ramcRads Right Ascension of Midheaven (MC)
     * @param mcRads Longitude of Midheaven (MC)
     * @param ascRads The longitude (and ultimately, the house/sign) that is
     * ascending on the Eastern horizon at a given time
     * @param declMC The declination of Midheaven
     * @param declAsc The declination of the Ascendant
     */
    @Override
    public void determineCusps( Map<Integer,CuspPlusAngle> cpaMap,
                                double ramcRads, double mcRads, double ascRads, double declMC, double declAsc )
    {
        // Populate cusps
        // --------------

        // 2nd. MC = arctan (tan RAMC/cos e)
        // Set cusp, we don't use ra2longitude() here
        CuspPlusAngle cpa10 = cpaMap.get(ZhAttribute.MCN).evenAngleHouse(mcRads);
        cpa10.setDeclRads(declMC);

        // 1st house cusp (ASC)
        // 3. Asc = arccot {-[(tan L x sin e) + (sin RAMC x cos e)]/cos RAMC}
        // Set cusp, we don't use ra2longitude() here
        CuspPlusAngle cpa1 = cpaMap.get(ZhAttribute.ASCN).evenAngleHouse(ascRads);
        cpa1.setDeclRads(declAsc);
        
        // 11th house cusp
        //   RA1 = RAMC+30 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/3
        //   :
        //   Etc until delta RA tends to zero
        double cusp11Rads = houseCusp1112RA( ramcRads, 3.d, Math.PI/6.d );
        
        // Get this (empty-ish) cusp
        CuspPlusAngle cpa11 = cpaMap.get(11);
        // Enter up angle in House
        cpa11.evenAngleHouse(HoroHouse.ra2Longitude(cusp11Rads));
        // Set declination
        cpa11.setDeclRads(HoroHouse.declination(cpa11.getAngle()+cpa11.getHouse().getRadStart()));
        
        // 12th house cusp
        //   RA1 = RAMC+60 degrees
        //   RA2 = RAMC+{arcos[-(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        double cusp12Rads = houseCusp1112RA( ramcRads, 1.5d, Math.PI/3.d );
        
        // Get this (empty-ish) cusp
        CuspPlusAngle cpa12 = cpaMap.get(12);
        // Enter up angle in House
        cpa12.evenAngleHouse(HoroHouse.ra2Longitude(cusp12Rads));
        // Set declination
        cpa12.setDeclRads(HoroHouse.declination(cpa12.getAngle()+cpa12.getHouse().getRadStart()));
        
        // 2nd house cusp
        //   RA1 = RAMC+120 degrees
        //   RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/1.5
        //   :
        //   Etc until delta RA tends to zero
        double cusp2Rads = houseCusp23RA( ramcRads, 1.5d, 2*Math.PI/3.d );

        // Get this (empty-ish) cusp
        CuspPlusAngle cpa2 = cpaMap.get(2);
        // Enter up angle in House
        cpa2.evenAngleHouse(HoroHouse.ra2Longitude(cusp2Rads));
        // Set declination
        cpa2.setDeclRads(HoroHouse.declination(cpa2.getAngle()+cpa2.getHouse().getRadStart()));
                
        // 3rd house cusp
        //    RA1 = RAMC+150 degrees
        //    RA2 = RAMC + 180 – {arcos[(sin RA1) x (tan e) x (tan L)]}/3
        //    :
        //    Etc until delta RA tends to zero
        double cusp3Rads = houseCusp23RA( ramcRads, 3.d, 5*Math.PI/6.d );

        // Get this (empty-ish) cusp
        CuspPlusAngle cpa3 = cpaMap.get(3);
        // Enter up angle in House
        cpa3.evenAngleHouse(HoroHouse.ra2Longitude(cusp3Rads));
        // Set declination
        cpa3.setDeclRads(HoroHouse.declination(cpa3.getAngle()+cpa3.getHouse().getRadStart()));
        
        
        // 12. Alternate cusps are simply the opposite signs
        // -------------------------------------------------
        // 3 -> 9
        cpaMap.get(9).setOppositeCusp(cpa3);
        
        // 2 -> 8
        cpaMap.get(8).setOppositeCusp(cpa2);
        
        // 1 -> 7
        cpaMap.get(ZhAttribute.DSCN).setOppositeCusp(cpa1);
        
        // 10 -> 4
        cpaMap.get(ZhAttribute.ICN).setOppositeCusp(cpa10);
        
        // 11 -> 5
        cpaMap.get(5).setOppositeCusp(cpa11);
        
        // 12 -> 6
        cpaMap.get(6).setOppositeCusp(cpa12);
    }
}

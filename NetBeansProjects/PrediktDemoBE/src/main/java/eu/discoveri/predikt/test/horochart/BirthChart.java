/*
 * Birth chart
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.LatLon;
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
public class BirthChart
{
    private final String                    shortName,
                                            birthPlaceName;
    // Local Sidereal Time (of birth)
    private final double                    lst = Double.MIN_VALUE;
    // Only Placidus at time of development...
    private ChartType                       ct = ChartType.PLACIDUS;
    private final LocalDateTime             birthDate;
    private LatLon                          birthPlace;
    
    // The horoscope houses and cusps
    private HoroHouse                       hh;
    
    // Cusps map
    private Map<Integer,CuspPlusAngle>      cpaMap = null;

    /**
     * Constructor.
     * 
     * @param shortname
     * @param ldt
     * @param birthPlacename eg: "Sydney,Australia"
     * @param ct eg: ChartType.PLACIDUS
     */
    public BirthChart( String shortname, LocalDateTime ldt, String birthPlacename, ChartType ct )
    {
        this.shortName = shortname;
        this.birthDate = ldt;
        this.birthPlaceName = birthPlacename;
        this.ct = ct;
    }
 
    /**
     * Uses default wheel of PLACIDUS.
     * 
     * @param shortname
     * @param ldt
     * @param birthPlacename eg: "Sydney,Australia"
     */
    public BirthChart( String shortname, LocalDateTime ldt, String birthPlacename )
    {
        this(shortname,ldt,birthPlacename, ChartType.PLACIDUS);
    }
    
    /*
     * Create the cusps.  This can be different for different wheels...
     */
    public BirthChart init()
    {
        switch( ct )
        {
            default:
            case PLACIDUS:
            {
                cpaMap = new HashMap<Integer,CuspPlusAngle>()
                {{
                    put(1,new CuspPlusAngle().setAttribute(ZhAttribute.ASC));
                    put(2,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(3,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(4,new CuspPlusAngle().setAttribute(ZhAttribute.IC));
                    put(5,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(6,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(7,new CuspPlusAngle().setAttribute(ZhAttribute.DSC));
                    put(8,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(9,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(10,new CuspPlusAngle().setAttribute(ZhAttribute.MC));
                    put(11,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                    put(12,new CuspPlusAngle().setAttribute(ZhAttribute.UNDEF));
                }};
            }
        }
        
        return this;
    }
    
    public void createWheel()
            throws Exception
    {
        hh = new HoroHouse(birthPlaceName, birthDate, cpaMap).init();
        hh.setWheel(ct);
    }
    
    /*
     * Mutators
     * --------
     */
    /**
     * Set LatLon of birth.
     * @param place
     * @return 
     */
    public LatLon setPlace( LatLon place )
    {
        this.birthPlace = place;
        return birthPlace;
    }
    
    /**
     * Get the wheel for this chart.
     * @return 
     */
    public HoroHouse getWheel() { return hh; }

    public String getShortName() {
        return shortName;
    }

    public String getBirthPlaceName() {
        return birthPlaceName;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public LatLon getBirthPlace() {
        return birthPlace;
    }
    
    
    
    
    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("####.######");
        
        // Create user
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(1990,6,6), LocalTime.of(23,57));
        BirthChart bc = new BirthChart("Chris", ldt, "Cardiff, Wales").init();
        
        // Create the horoscope wheel for this user
        bc.createWheel();
        HoroHouse hh = bc.getWheel();
        
        // Set birthplace
        bc.setPlace( hh.getLatLon() );
        
        // Personal data
        System.out.println("\r\nName: " +bc.getShortName());
        System.out.println("  Datetime: " +bc.getBirthDate().toString());
        System.out.println("  Birth place: " +bc.getBirthPlaceName());
        System.out.println("  Lat/Lon: " +bc.getBirthPlace().fmtLatLonDMS());
        System.out.println("");
            
        // Dump wheel data
        hh.getCpaMap().forEach((k,v)->{
            double deg = Math.toDegrees(v.getAngle());
            System.out.println( "[" +k+"] "+Util.houseDec2ddmmss(v.getHouse().getShortname(),deg)+"\t"+Util.dec2ddmmss(Math.toDegrees(v.getDecl()),true) );
        });
    }
}

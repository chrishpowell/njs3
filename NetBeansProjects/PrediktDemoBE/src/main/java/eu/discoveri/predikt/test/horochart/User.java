/*
 * User data.  This will be a database class eventually.
 */

package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.LatLon;
import java.time.LocalDateTime;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class User
{
    private String          name,
                            birthPlace;
    private LatLon          place;
    private LocalDateTime   birthLDT;
    // Preferences
    private ChartType       ct;
    
    // ***** Remove when database based ******
    private boolean         firsttime = true;

    /**
     * Constructor.
     * 
     * @param name
     * @param birthPlace
     * @param birthLDT 
     */
    public User(String name, String birthPlace, LocalDateTime birthLDT, ChartType ct)
    {
        this.name = name;
        this.birthPlace = birthPlace;
        this.birthLDT = birthLDT;
        this.ct = ct;
    }

    /*
     * Mutators
     */
    public String getName() { return name; }
    public String getBirthPlace() { return birthPlace; }
    public LocalDateTime getBirthLDT() { return birthLDT; }
    
    public LatLon getPlace() { return place; }
    public void setPlace( LatLon place ) { this.place = place; }

    public ChartType getCt() { return ct; }
    public void setCt(ChartType ct) { this.ct = ct; }
    
    // ***********  @TODO: Remove
    public boolean isFirsttime() { return firsttime; }
    public void setFirsttime(boolean firsttime) { this.firsttime = firsttime; }
}

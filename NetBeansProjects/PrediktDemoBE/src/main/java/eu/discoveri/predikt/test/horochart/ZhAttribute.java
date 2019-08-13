/*
 * Attributes for Zodiac House
 */
package eu.discoveri.predikt.test.horochart;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum ZhAttribute
{
    ASC("ASC","Ascendant","Rising Sign: the cusp of the first house. A point on the ecliptic that rises on the eastern horizon at a particular moment. It is the representation of how you appear to other people."),
    MC("MC","Medium Coeli","Midheaven: the cusp of the tenth house of the natal chart and is the Zodiac Sign that was at the southern highest point above the horizon at the moment of birth. It relates to a career or “life path” and suggests social standing and reputation."),
    IC("IC","Imuni Coeli",""),
    DSC("DSC","Descendant",""),
    UNDEF("UNDEF","Undefined","No attribute to associate");
    
    // Cusp numbers
    public static final int MCN = 10,
                            ASCN = 1,
                            ICN = 4,
                            DSCN = 7;
    
    private final String    shortName, name, descrip;
    
    private ZhAttribute( String shortname, String name, String description )
    {
        this.shortName = shortname;
        this.name = name;
        this.descrip = description;
    }

    public String getShortName() { return shortName; }
    public String getName() { return name; }
    public String getDescription() { return descrip; }
}

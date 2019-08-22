/*
 * Types of chart
 */
package eu.discoveri.predikt.test.horochart;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum ChartType
{
    PLACIDUS("Placidus", ChartCategory.TIMEORIENTED, "The Placidus house system divides the phases of planetary and star movement above and below the horizon into equal-sized parts"),
    KOCH("Koch", ChartCategory.TIMEORIENTED, "The Koch house system (also called 'House system of the birth place') is defined by horizon lines at different times on the day of birth.S"),
    PORPHYRY("Porphyry", ChartCategory.EQUALHOUSE, "The Porphyry house system creates the intermediate houses by dividing each of the four quadrants into three equal-sized segments. "),
    MAGINI("Magini", ChartCategory.UNKNOWN, ""),
    CAMPANUS("Campanus", ChartCategory.SPACEORIENTED, "The Campanus house system divides the celestial space above and below the horizon like an orange into twelve equal segments."),
    REGIOMONTANUS("Regiomontanus", ChartCategory.SPACEORIENTED, "The Regiomontanus house system divides the celestial equator into twelve equal segments."),
    MERIDIAN("Meridian", ChartCategory.UNKNOWN, "Similar to the Campanus system, the Meridian system resembles a well-proportioned 'orange' but with the difference that the axis of this 'orange' is not placed on the horizon but runs through the celestial poles"),
    KRUSINSKI("Krusinski", ChartCategory.UNKNOWN, "In this house system, first a vertical great circle is drawn through Ascendant, Zenith, Descendant and Nadir. This is then divided into 12 equal-sized segments, and Meridian circles are drawn through the resulting segment points. The house cusps result from the intersections of the Meridian circles with the ecliptic. "),
    ALCABITIUS("Alcabitius", ChartCategory.UNKNOWN, "The Alcabitius house system divides the diurnal arc of the Ascendant into six equal-sized segments.");
    
    private final String        name,
                                descrip;
    private final ChartCategory category;
    
    private ChartType( String name, ChartCategory category, String description )
    {
        this.name = name;
        this.category = category;
        this.descrip = description;
    }
    
    public String getName() { return name; }
    public String getDescription() { return descrip; }
    public ChartCategory getCategory() { return category; }
}

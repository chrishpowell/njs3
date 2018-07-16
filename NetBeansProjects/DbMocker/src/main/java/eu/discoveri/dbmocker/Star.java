/*
 */

package eu.discoveri.dbmocker;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Star
{
    private final int       id;
    private final String    name,
                            altName;
    private final int       constlID;
    private final int[]     link2Stars;
    private final float     mag;
    private final String    colour;
    private final boolean   display;

    public Star(int id, String name, String altName, int constlID, int[] link2Stars, float mag, String colour, boolean display)
    {
        this.id = id;
        this.name = name;
        this.altName = altName;
        this.constlID = constlID;
        this.link2Stars = link2Stars;
        this.mag = mag;
        this.colour = colour;
        this.display = display;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAltName() { return altName; }
    public int getConstlID() { return constlID; }
    public int[] getLink2Stars() { return link2Stars; }
    public float getMag() { return mag; }
    public String getColour() { return colour; }
    public boolean isDisplay() { return display; }
}

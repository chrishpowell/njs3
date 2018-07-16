/*
 */

package eu.discoveri.dbmocker;

import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constellation
{
    private final int           id;
    private final String        name;
    private final List<Star>    stars;

    public Constellation(int id, String name, List<Star> stars)
    {
        this.id = id;
        this.name = name;
        this.stars = stars;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Star> getStars() { return stars; }
}

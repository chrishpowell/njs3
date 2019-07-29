/*
 * Moon position.
 * Note, this is apogee and perigee as the Moon orbits the Earth
 */

package eu.discoveri.predikt.planets;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Moon extends Orbits
{
    private static float    LONANINIT,
                            LONANMOVE,
                            INCLININIT,
                            INCLINMOVE,
                            ARGPINIT,
                            ARGPMOVE,
                            SEMIMAJINIT,
                            SEMIMAJMOVE,
                            ECCENINIT,
                            ECCENMOVE,
                            MEANANOMINIT,
                            MEANANOMMOVE;
    static {
        float   LONANINIT = 125.1228f,          // Big omega
                LONANMOVE = -0.0529538083f,
                INCLININIT = 5.1454f,
                INCLINMOVE = 0.f,
                ARGPINIT = 318.0634f,
                ARGPMOVE = 0.1643573223f,
                SEMIMAJINIT = 5.20256f,     // Earth radii
                SEMIMAJMOVE = 0.f,          // Earth radii
                ECCENINIT = 0.054900f,
                ECCENMOVE = 0.f,
                MEANANOMINIT = 115.3654f,
                MEANANOMMOVE = 13.0649929509f;
    }
    
    /**
     * Set Moon position for given day.
     * 
     * @param julDay 
     */
    public Moon( float julDay )
    {
        super( LONANINIT, LONANMOVE,
               INCLININIT, INCLINMOVE,
               ARGPINIT, ARGPMOVE,
               SEMIMAJINIT, SEMIMAJMOVE,
               ECCENINIT, ECCENMOVE,
               MEANANOMINIT, MEANANOMMOVE,
               julDay );
    }
}

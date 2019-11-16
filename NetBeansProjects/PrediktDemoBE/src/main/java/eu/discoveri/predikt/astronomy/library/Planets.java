/** ***************************************************************************\
 * Planets
 * \**************************************************************************** */
package eu.discoveri.predikt.astronomy.library;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A common place to store planetary constants.
 */
public final class Planets
{
    /**
     * NAP = Not A Planet.
     */
    public static final int NAP = -1;
    public static final int SUN = 0, MERCURY = 1, VENUS = 2, EARTH = 3, MARS = 4,
            JUPITER = 5, SATURN = 6, URANUS = 7, NEPTUNE = 8, PLUTO = 9;
    /**
     * Earth's moon - not a planet, but it is a heavenly body.
     */
    public static final int LUNA = 10;
    public static final int PLANETOID = 11;
    
//  Avoid this initialization technique as it creates an anonymous extra class at every usage,
//  and also holds hidden references to the enclosing object and might cause memory leak issues.
//
//    private Map<Integer,String> planetMap = new HashMap<Integer,String>() {{
//        put(NAP,"NAP");
//        put(SUN,"Sun");
//        put(MERCURY,"Mercury");
//        put(VENUS,"Venus");
//        put(EARTH,"Earth");
//        put(MARS,"Mars");
//        put(JUPITER,"Jupiter");
//        put(SATURN,"Saturn");
//        put(URANUS,"Uranus");
//        put(NEPTUNE,"Neptune");
//        put(PLUTO,"Pluto");
//        put(LUNA,"Moon");
//        put(PLANETOID,"Planetoid?");
//    }};
    
    private static Map<Integer,String> planetMap = Stream.of(new Object[][] {
        {NAP,"NAP"},
        {SUN,"Sun"},
        {MERCURY,"Mercury"},
        {VENUS,"Venus"},
        {EARTH,"Earth"},
        {MARS,"Mars"},
        {JUPITER,"Jupiter"},
        {SATURN,"Saturn"},
        {URANUS,"Uranus"},
        {NEPTUNE,"Neptune"},
        {PLUTO,"Pluto"},
        {LUNA,"Moon"},
        {PLANETOID,"Planetoid?"},
    }).collect(Collectors.toMap(data -> (Integer)data[0], data -> (String)data[1]));
    
    public static Map<Integer,String> getPlanetmap() { return planetMap; }
    
    
    /*==========================================================================
     *          M A I N
     *=========================================================================*/
    public static void main(String[] args)
    {
        Planets ps = new Planets();
        System.out.println("Get Mercury via ord: " +Planets.getPlanetmap().get(Planets.MERCURY));
    }
}


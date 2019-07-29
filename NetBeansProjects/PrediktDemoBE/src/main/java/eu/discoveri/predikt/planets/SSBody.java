/*
 * Solar system body codes
 */
package eu.discoveri.predikt.planets;

/**
 * Note: TT-TDB (time in SI secs at geocenter) Note obsolete ET, ephemeris time 
 * almost same as TT.  Order depends on what documentation you read...
 * 
 * @author Chris Powell, Discoveri OU
 */
public enum SSBody
{
// 1-Mercury, Venus, Earth-Moon barycenter, Mars, Jupiter, Saturn, Uranus, 
//   Neptune, Pluto, Moon (geocentric), 11-Sun,
//   12-Earth Nutations in longitude and obliquity (IAU 1980 model)
//   13-Lunar mantle libration, 14-Lunar mantle angular velocity, 15-TT-TDB
// OR?
// 1-Mercury, ...,3-Earth, ,,,9-Pluto, 10-Moon, 11-Sun,
//    12-Solar System Barycenter, 13-Earth-Moon Barycenter, 14-Nutations,
//    15-Librations
//
// [Assume first]
//  
    MERCURY(1,"Mercury"),
    VENUS(2,"Venus"),
    EARTHMOON(3,"Earth-Moon barycentre"),
    MARS(4,"Mars"),
    JUPITER(5,"Jupiter"),
    SATURN(6,"Saturn"),
    URANUS(7,"Uranus"),
    NEPTUNE(8,"Neptune"),
    PLUTO(9,"Pluto"),
    GEOCMOON(10,"Geocentric Moon"),
    SUN(11,"Sun"),
    NUTS(12,"Earth nutations in longitude and obliquity"),
    LLIBRAS(13,"Lunar mantle librations"),
    LMAV(14,"Lunar mantle angular velocity"),
    TTTDB(15,"TT: Terrrestrial Time, TDB: Barycentric Dynamical Time");
    
    private final int       code;
    private final String    name;
    
    /**
     * Solar system bodies, codes and names.
     * 
     * @param code
     * @param name 
     */
    private SSBody ( int code, String name )
    {
        this.code = code;
        this.name = name;
    }
}

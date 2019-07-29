/*
 * Kepler elements such as Longitude of ascending Node, Mean Anomaly etc.
 */
package eu.discoveri.predikt.ephemeris;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum KeplerElement
{
    BIGOMEGA("bigOmega","N","Longitude of Ascending Node","deg"),
    LITTLEOMEGA("littleOmega","w","Argument of Perihelion (Perifocus)","deg"),
    LITTLEI("littleI","i","Inclination to ecliptic","deg"),
    LITTLEA("littleA","a","Semi-major axis","AU"),
    LITTLEE("littleE","e","Eccentricity (0..1)","scalar"),
    BIGM("bigM","M","Mean anomaly","deg"),
    BIGL("bigL","L","Mean longitude","deg"),
    LITTLEV("littleV","v","True anomaly","deg"),
    BIGE("bigE","E","Eccentric anomaly","deg"),
    OMEGABAR("omegaBar","w1","Longitude of Perihelion","deg"),
    LITTLEN("littleN","n","Daily motion","deg/day"),
    RIGHTASC("ra","ra","rightAscension","deg"),
    DECL("decl","decl","declination","deg"),
    PDIST("littleQ","q","Perihelion distance","AU"),
    ADIST("bigQ","Q","Aphelion distance","AU"),
    ORBP("bigP","P","Orbital period","days"),
    HELIODIST("littleR","r","Dist. from Sun","AU"),
    XECL("xe","xe","Ecliptic coords, x","scalar"),
    YECL("ye","ye","Ecliptic coords, y","scalar"),
    ZECL("ze","ze","Ecliptic coords, z","scalar"),
    XEQ("xq","xq","Equatorial coords, x","scalar"),
    YEQ("yq","yq","Equatorial coords, y","scalar"),
    ZEQ("zq","zq","Equatorial coords, z","scalar");
    

    private final String    name,
                            symbol,
                            description,
                            dimension;

    /**
     * Constructor.
     * 
     * @param name
     * @param symbol
     * @param description 
     */
    private KeplerElement( String name, String symbol, String description, String dimension )
    {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.dimension = dimension;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getDescription() { return description; }
    public String getDimension() { return dimension; }
}

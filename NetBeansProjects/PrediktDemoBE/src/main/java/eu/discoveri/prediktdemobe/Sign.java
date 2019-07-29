/*
 * Zodiac signs
 */

package eu.discoveri.prediktdemobe;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Sign
{
    private int             order;
    private String          name,
                            symbol,
                            element;
    private Path            imagePath;
    private static String   svgImagePath = "./resources/svg/zodiac/";
    // *** Convert to Map
    private static Sign    signs[] = new Sign[] { new Sign( 1, "Aries", "♈", Paths.get(svgImagePath+"aries.svg"), "fire"),
                                            new Sign( 2, "Taurus", "♉", Paths.get(svgImagePath+"taurus.svg"),"earth" ),
                                            new Sign( 3, "Gemini", "♊", Paths.get(svgImagePath+"gemini.svg"),"wind" ),
                                            new Sign( 4, "Cancer", "♋", Paths.get(svgImagePath+"cancer.svg"),"water" ),
                                            new Sign( 5, "Leo", "♌", Paths.get(svgImagePath+"leo.svg"),"fire" ),
                                            new Sign( 6, "Virgo", "♍", Paths.get(svgImagePath+"virgo.svg"), "earth" ),
                                            new Sign( 7, "Libra", "♎", Paths.get(svgImagePath+"libra.svg"), "wind" ),
                                            new Sign( 8, "Scorpio", "♏", Paths.get(svgImagePath+"scorpio.svg"), "water" ),
                                            new Sign( 9, "Sagittarius", "♐", Paths.get(svgImagePath+"sagittarius.svg"), "fire" ),
                                            new Sign( 10, "Capricorn", "♑", Paths.get(svgImagePath+"capricorn.svg"), "earth" ),
                                            new Sign( 11, "Aquarius", "♒", Paths.get(svgImagePath+"aquarius.svg"), "wind" ),
                                            new Sign( 12, "Pisces", "♓", Paths.get(svgImagePath+"pisces.svg"), "water" )
                                           };

    public Sign( int order, String name, String symbol, Path imagePath, String element )
    {
        this.order = order;
        this.name = name;
        this.symbol = symbol;
        this.element = element;
        this.imagePath = imagePath;
    }

    public int getOrder() { return order; }
    public String getName() { return name; }
    
    public static Sign[] getSigns() { return signs; }
}

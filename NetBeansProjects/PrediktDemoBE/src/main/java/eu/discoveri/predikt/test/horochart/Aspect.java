/*
 * Aspects: The rotation difference bewtween planets interpreted
 */
package eu.discoveri.predikt.test.horochart;

import eu.discoveri.predikt.utils.Constants;
import java.awt.Color;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Aspect
{
    // Attributres
    private final String        name;
    private final MajorMinor    majmin;
    private final RotateDiff    degreesDiff;
    private final String        fileImage;
    private final Color         aspectColor;
    // Image path
    private final static String aspectImagePath = Constants.RESOURCEPATH+"aspects/";
    
    // Map of Aspects
    private static final Map<String,Aspect>  aspectMap = Stream.of(new Object[][] {
        {Constants.CONJUNCTION,
            new Aspect(Constants.CONJUNCTION,MajorMinor.MAJOR,new RotateDiff(Constants.CONJUNCTIONDIFF,Constants.CONJUNCTIONRANGE),
                    Constants.CONJUNCTIONC, aspectImagePath+Constants.CONJUNCTION)},
        {Constants.OPPOSITION,
            new Aspect(Constants.OPPOSITION,MajorMinor.MAJOR,new RotateDiff(Constants.OPPOSITIONDIFF,Constants.OPPOSITIONRANGE),
                    Constants.OPPOSITIONC, aspectImagePath+Constants.OPPOSITION)},
        {Constants.SQUARE,
            new Aspect(Constants.SQUARE,MajorMinor.MAJOR,new RotateDiff(Constants.SQUAREDIFF,Constants.SQUARERANGE),
                    Constants.SQUAREC, aspectImagePath+Constants.SQUARE)},
        {Constants.SEXTILE,
            new Aspect(Constants.SEXTILE,MajorMinor.MAJOR,new RotateDiff(Constants.SEXTILEDIFF,Constants.SQUARERANGE),
                    Constants.SEXTILEC, aspectImagePath+Constants.SEXTILE)},
        {Constants.TRINE,
            new Aspect(Constants.TRINE,MajorMinor.MAJOR,new RotateDiff(Constants.TRINEDIFF,Constants.TRINERANGE),
                    Constants.TRINEC, aspectImagePath+Constants.TRINE)},
    }).collect(Collectors.toMap(data -> (String)data[0], data -> (Aspect)data[1]));


    /**
     * Constructor.
     * 
     * @param name
     * @param majmin
     * @param degreesDiff
     * @param aspectColor
     * @param fileImage
     * [@param aspectImage] 
     */
    public Aspect(String name, MajorMinor majmin, RotateDiff degreesDiff, Color aspectColor, String fileImage)
    {
        this.name = name;
        this.majmin = majmin;
        this.degreesDiff = degreesDiff;
        this.aspectColor = aspectColor;
        this.fileImage = fileImage;
    }

    public String getName() { return name; }
    public MajorMinor getMajmin() { return majmin; }
    public RotateDiff getDegreesDiff() { return degreesDiff; }
    public String getFileImage() { return fileImage; }
    public Color getAspectColor() { return aspectColor; }
    
    
    /**
     * Dump.
     * @return String representation of Aspect
     */
    @Override
    public String toString()
    {
        return "["+name+": "+majmin.name()+
                "/(degs) "+degreesDiff.getDegreesDiffExact()+
                "+/-"+degreesDiff.getDegreesDiffRange()+"]";
    }
    /**
     * Generate all Aspects into map.
     * 
     * @return 
     */
    public static Map<String,Aspect> aspectsFactory() { return aspectMap; }
}

//------------------------------------------------------------------------------
/*
 * Major or minor type
 */
enum MajorMinor
{
    MAJOR, MINOR;
}

/*
 * Earth, Wind, Water, Fire
 */
package eu.discoveri.prediktdemobe;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Element
{
    private final String  name;
    private final String  fillColour;
    
    // Element list
    private Map<String,Element> elements = Stream.of(new Object[][] {
        {"Fire", new Element("Fire","rgba(206, 0, 0, 1.0)")},
        {"Wind", new Element("Fire","rgba(255, 224, 0, 1.0)")},
        {"Earth", new Element("Fire","rgba(93, 211, 158, 1.0)")},
        {"Water", new Element("Fire","rgba(58, 162, 255, 1.0)")}
    }).collect(Collectors.toMap(data->(String)data[0],data->(Element)data[1]));
    
    // Constructor
    public Element( String name, String fillColour )
    {
        this.name = name;
        this.fillColour = fillColour;
    }

    // Mutators
    public String getName() { return name; }
    public String getFillColour() { return fillColour; }
    
    // Get all elements
    public Map<String,Element> getelements()
    {
        return elements;
    }
    
    // Get an element
    public Element getElement( String name )
    {
        return elements.get(name);
    }
}

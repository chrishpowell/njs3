/*
 */
package eu.discoveri.codesnippets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Apple
{
    Color   color;
    int     weight;                                     // Grams
    static List<Apple> appleList = new ArrayList<>();
    
    public Apple(Color color, int weight)
    {
        this.color = color;
        this.weight = weight;
    }

    public Color getColor() { return color; }
    public int getWeight() { return weight; }
    public List<Apple> getAppleList() { return appleList; }
    
    @Override
    public String toString()
    {
        return "(r="+color.getRed()+",g="+color.getGreen()+",b="+color.getBlue()+"):"+weight;
    }
}

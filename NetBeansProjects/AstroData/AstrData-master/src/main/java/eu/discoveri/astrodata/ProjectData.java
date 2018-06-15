/*
 */
package eu.discoveri.astrodata;

import java.awt.Color;


/**
 *
 * @author chris
 */
public class ProjectData
{
    private double  x, y, radius;
    private String  name;
    private Color   colour;
    private boolean display;

    public ProjectData(double x, double y, double radius, Color colour, String name, boolean display)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.colour = colour;
        this.name = name;
        this.display = display;
    }

    public double getX() { return x; }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() { return y; }
    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() { return radius; }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
    
    @Override
    public String toString()
    {
        return "[" +x+ "," +y+ "," +radius+ "," +"\"#" +colour.getRGB()+ "\"," +"\"" +name+ "\"," +display +"],";
    }
}

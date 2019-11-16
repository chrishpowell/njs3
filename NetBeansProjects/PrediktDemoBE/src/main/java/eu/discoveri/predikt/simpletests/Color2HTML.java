/*
 */
package eu.discoveri.predikt.simpletests;

import java.awt.Color;


/**
 * Convert Color to HTML RGB.
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Color2HTML
{
    public static void main(String[] args)
    {
        Color c = Color.ORANGE;
        int iR = c.getRed();
        int iG = c.getGreen();
        int iB = c.getBlue();
        int sRGB = c.getRGB();
        System.out.println("sRGB: #"+Integer.toHexString(sRGB).substring(2));
    }
}

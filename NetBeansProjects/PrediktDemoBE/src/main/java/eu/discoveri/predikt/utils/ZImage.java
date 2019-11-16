/*
 * Set image size, colour
 */
package eu.discoveri.predikt.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;


/**
 * Set image size, colour
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ZImage
{
    private       BufferedImage image;
    private final int           origHeight, origWidth;
    private final Color         colour;

    /**
     * Constructor.
     * 
     * @param image
     * @param height
     * @param width
     * @param colour 
     */
    public ZImage(BufferedImage image, int height, int width, Color colour)
    {
        this.image = image;
        origHeight = height;
        origWidth = width;
        this.colour = colour;
    }
    
    /**
     * Replacement owing to: The Perils of Image.getScaledInstance() Blog
     * https://community.oracle.com/docs/DOC-983611
     * 
     * @param newWidth
     * @param newHeight
     * @param imageType
     * @return 
     */
    public ZImage setScaledImage( int newWidth, int newHeight, int imageType )
    {
        BufferedImage resized = new BufferedImage( newWidth, newHeight, imageType );
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(this.image, 0, 0, newWidth, newHeight, 0, 0, resized.getWidth(),resized.getHeight(), null);
        g.dispose();
        this.image = resized;
        System.out.println("ZI SSI, Ht/Wid: " +resized.getHeight()+"/"+resized.getWidth());
        
        return this;
    }
    
    /**
     * Scale an image preserving aspect ratio.
     * 
     * @param scale
     * @param img
     * @return 
     */
    public ZImage scalePreserveRatio( int scale, ZImage img )
    {
        // Preserved scaling
        double scaled;
        // Get raw width/height
        double imgWidth = img.getWidth(), imgHeight = img.getHeight();
        
        System.out.println("::> Scale, Ht/Wid (type): " +scale+", "+imgHeight+"/"+imgWidth+" ("+img.getImage().getType()+")");
        
        // Scale body/planet for wheel display
        if( scale > imgWidth && scale > imgHeight )
            return img;
        else
        if( scale/imgWidth < scale/imgHeight )
        {
            scaled = scale/imgWidth;
        }
        else
        if( scale/imgWidth > scale/imgHeight )
        {
            scaled = scale/imgHeight;
        }
        else
            scaled = scale/imgWidth;

        System.out.println("::> Wid/Hgt: " +this.getWidth()+"/"+this.getHeight());
        // Deprecated version: img.getScaledInstance((int)(scaled*imgWidth),(int)(scaled*imgHeight),img.getImage().getType());

        return img.setScaledImage((int)(scaled*imgWidth), (int)(scaled*imgHeight), BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    /**
     * Scale an image preserving aspect ratio.
     * Scaled by avg. width+height
     * 
     * @param img
     * @return 
     */
    public ZImage scalePreserveRatio( ZImage img )
    {
        return scalePreserveRatio((origHeight+origWidth)/2,img);
    }

    public BufferedImage getImage() { return image; }
    public int getHeight() { return origHeight; }
    public int getWidth() { return origWidth; }
    public Color getColour() { return colour; }
}

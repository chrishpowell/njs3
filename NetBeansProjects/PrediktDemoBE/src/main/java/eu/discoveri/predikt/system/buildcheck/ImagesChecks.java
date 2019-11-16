/*
 */
package eu.discoveri.predikt.system.buildcheck;

import eu.discoveri.predikt.test.horochart.ZodiacHouse;
import eu.discoveri.predikt.utils.Constants;
import eu.discoveri.predikt.utils.ImagesUtil;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import javax.imageio.ImageIO;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ImagesChecks
{
    static String zodiacImagePath = Constants.RESOURCEPATH+"zodiac/";
    static String aspectImagePath = Constants.RESOURCEPATH+"aspects/";

    
    /**-------------------------------------------------------------------------
     * Set all House images to given type.
     * 
     * @param imageType
     * @throws IOException 
     */
    public static void setHouseImagesType( int imageType )
            throws IOException
    {
        System.out.println("Loading zodiac images...");
        Map<ZodiacHouse,Integer> zh = ZodiacHouse.getZHNameMap();

        // Load zodiac images
        for( ZodiacHouse house: zh.keySet() )
        {
            // Read image from file
            System.out.print(".");
            File imgFile = new File(zodiacImagePath+house.getName().toLowerCase()+".png");
            BufferedImage sign = ImageIO.read(imgFile);

            // Change image type and write file
            ImageIO.write(  ImagesUtil.changeImageType(sign,imageType),
                            "png", imgFile  );
        }
        
        System.out.println("\r\nLoaded zodiac images...");
    }


    /**
     * Set all House images to 4BYTE_ABGR (6).
     * See: https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_4BYTE_ABGR
     * 
     * @throws IOException 
     */
    public static void setHouseImagesType4ByteABGR()
            throws IOException
    {
        setHouseImagesType( BufferedImage.TYPE_4BYTE_ABGR );
    }
    
    /**
     * Write image with adjusted size for product.
     * 
     * @param scale
     * @throws IOException
     */
    public static void setHouseImagesSize( int scale )
            throws IOException
    {
        System.out.println("Sizing: Loading zodiac/house images...");
        Map<ZodiacHouse,Integer> zh = ZodiacHouse.getZHNameMap();

        // Load zodiac images
        for( ZodiacHouse house: zh.keySet() )
        {
            // Read image from file
            System.out.print(".");
            String imgFileName = zodiacImagePath+house.getName().toLowerCase();
            File imgFile = new File(imgFileName+".png");
            BufferedImage hImage = ImageIO.read(imgFile);
            
            // Preserved scaling
            double scaled;
            // Get raw width/height
            double imgWidth = hImage.getWidth(), imgHeight = hImage.getHeight();

            System.out.println("::> Scale, Ht/Wid (type): " +scale+", "+imgHeight+"/"+imgWidth+" ("+hImage.getType()+")");

            // Scale body/planet for wheel display
            if( scale > imgWidth && scale > imgHeight )
                continue;
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
            
            // Set new width, height
            int newWidth = (int)(scaled*imgWidth), newHeight = (int)(scaled*imgHeight);

            // Deprecated version: img.getScaledInstance((int)(scaled*imgWidth),(int)(scaled*imgHeight),img.getImage().getType());
            BufferedImage resized = new BufferedImage( newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR );
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(hImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            System.out.println("ZI SSI, Ht/Wid: " +resized.getHeight()+"/"+resized.getWidth());

            // Change image type and write file
            ImageIO.write( resized, "png", new File(imgFileName+"-resized.png") );
        }
        
        System.out.println("\r\nLoaded zodiac images...");
    }
    
    
    /**-------------------------------------------------------------------------
     * Set all Cusp images to given type.
     * 
     * @param imageType
     * @throws IOException 
     */
    public static void setCuspImagesType( int imageType )
            throws IOException
    {
        System.out.println("Loading cusp images...");

        // Load cusp images
        for( int ii = 1; ii <= 2; ii++ )
        {
            // Read image from file
            System.out.print(".");
            File imgFile = new File(zodiacImagePath+"/c"+ii+".png");
            BufferedImage sign = ImageIO.read(imgFile);

            // Change image type and write file
            ImageIO.write(  ImagesUtil.changeImageType(sign,imageType),
                            "png", imgFile  );
        }
        
        System.out.println("\r\nLoaded zodiac images...");
    }

    /**
     * Set all Cusp images to 4BYTE_ABGR (6).
     * See: https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_4BYTE_ABGR
     * 
     * @throws IOException 
     */
    public static void setCuspImagesType4ByteABGR()
            throws IOException
    {
        setCuspImagesType( BufferedImage.TYPE_4BYTE_ABGR );
    }


    /**-------------------------------------------------------------------------
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
            throws IOException
    {
//        setHouseImagesType4ByteABGR();
        setHouseImagesSize(35);
    }
}

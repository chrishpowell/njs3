/*
 * Utils for images
 */
package eu.discoveri.predikt.utils;

import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Base64;
import javax.imageio.ImageIO;
import org.apache.batik.transcoder.TranscoderException;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ImagesUtil
{
    /**
     * Encode Image for HTML output (PNG).
     * Eg: <img src = '" +image2HTML(image)+ "' height='20' width = '20' />
     * 
     * @param bImage
     * @throws IOException
     * @return 
     */
    public static String image2HTML( BufferedImage bImage )
            throws IOException
    {
        ImageIO.write(bImage,"png",new File("tmpImage.png"));
        byte[] imageBytes = Files.readAllBytes(Paths.get("tmpImage.png"));
        
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/png;base64," + encoder.encodeToString(imageBytes);
    }
    
    /**
     * Change image type (eg: from TYPE_3BYTE_BGR to TYPE_4BYTE_ABGR).
     * 
     * @param bImage
     * @param newImageType
     * @return 
     */
    public static BufferedImage changeImageType( BufferedImage bImage, int newImageType )
    {
        BufferedImage bi = new BufferedImage(bImage.getWidth(), bImage.getHeight(), newImageType);
        
        // Keep colour/alpha
        for( int y = 0; y < bImage.getHeight(); ++y )
        {
            for( int x = 0; x < bImage.getWidth(); ++x )
            {
                int argb = bImage.getRGB(x, y);
                bi.setRGB(x, y, argb);                  // same color alpha 100%
            }
        }
        
        return bi;
    }
    
    /**
     * Convert SVG to PNG.
     * 
     * @param inSVG
     * @throws IOException 
     * @throws TranscoderException
     */
    public static void convertSVG2PNG( String inSVG )
            throws IOException, TranscoderException
    {
        //Step -1: We read the input SVG document into Transcoder Input
        //We use Java NIO for this purpose
        Path path = Paths.get(inSVG);
        String svgURIinput = path.toUri().toURL().toString();
        TranscoderInput inSVGimage = new TranscoderInput(svgURIinput);
        
        // Step-2: 
        try( OutputStream outPNGstream = new FileOutputStream(path+".png") )
        {
            TranscoderOutput outPNGimage = new TranscoderOutput(outPNGstream);

            // Step-3: Create PNGTranscoder and define hints if required
            PNGTranscoder converter = new PNGTranscoder();

            // Step-4: Convert and Write output
            converter.transcode(inSVGimage, outPNGimage);

            // Step 5- close / flush Output Stream
            outPNGstream.flush();
        }
    }
    
    /**
     * M A I N
     * =======
     * @param args
     * @throws IOException
     * @throws TranscoderException 
     */
    public static void main(String[] args)
            throws IOException, TranscoderException
    {
        convertSVG2PNG( "/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/lgp-test-add.svg" );
    }
}

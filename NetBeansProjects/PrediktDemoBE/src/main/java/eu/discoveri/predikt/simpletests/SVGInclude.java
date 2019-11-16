/*
 */
package eu.discoveri.predikt.simpletests;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import static org.apache.batik.dom.util.DOMUtilities.writeDocument;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SVGInclude
{
    /**
     * Load an svg.
     * 
     * @param uri
     * @return
     * @throws IOException 
     */
    private static SVGDocument loadSVGDocument( String file )
            throws IOException
    {
        // Include an SVG file
        SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
        return df.createSVGDocument(file);
    }
    
    /**
     * Dump an SVG document.
     * 
     * @param svgDoc 
     */
    private static void dumpSVGdoc( SVGGraphics2D svgGen )
            throws SVGGraphics2DIOException
    {
        OutputStream out = new PrintStream(System.out);
        Writer outWriter = new PrintWriter(out);
        
        svgGen.stream(outWriter);
    }

    
    public static void main(String[] args)
            throws IOException
    {
        // Set up graphics view 
        SVGDocument svgDoc = loadSVGDocument("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/lgp-test.svg");
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(svgDoc);
        SVGGraphics2D g2 = new SVGGraphics2D(ctx,false);
        
        dumpSVGdoc( g2 );
        
        // Set drawing
        g2.setSVGCanvasSize(new Dimension(600,600));
                
        // Make lines smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
//        // Set top element
//        Element top = svgDoc.getRootElement();
//        
//        // Nav to top level
//        NodeList nodes = svgDoc.getElementsByTagName("svg");
//        Node node = nodes.item(0);
//        Element el = null;
//        if( Node.ELEMENT_NODE == node.getNodeType()) {
//            el = (Element)node;
//        }
//        g2.setTopLevelGroup(el);

        
        // Draw an ellipse
        Ellipse2D ell = new Ellipse2D.Double(100, 100, 50, 50);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(ell);
        g2.setPaint(Color.lightGray);
        g2.fill(ell);
        
        // Attach it...?
        
        // Output it, what about g2??
//        boolean useCSS = false;
//        try( Writer out = new OutputStreamWriter(new FileOutputStream("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/lgp-test-add.svg"), "UTF-8") )
//        {
//                g2.stream(svgDoc.getDocumentElement(), out, useCSS, false);
//        }
//        writeDocument(svgDoc,new FileWriter("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/lgp-test-add.svg"));
        g2.stream(new FileWriter("/home/chrispowell/NetBeansProjects/PrediktDemoBE/src/main/java/resources/tests/lgp-test-add.svg"));
    }
}

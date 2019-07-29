/*
 * Test JCanvas stuff
 */

package eu.discoveri.predikt.test.horochart;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
import org.apache.batik.anim.dom.SVGDOMImplementation;

import org.apache.batik.swing.*;
import org.apache.batik.svggen.*;

import org.w3c.dom.*;
import org.w3c.dom.svg.*;

public class ViewGeneratedSVGDemo {

  public static void main(String[] args) {
    // Create an SVG document.
    DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
    SVGDocument doc = (SVGDocument) impl.createDocument("http://www.w3.org/2000/svg", "svg", null);

    // Create a converter for this document.
    SVGGraphics2D g = new SVGGraphics2D(doc);
    
    // Make lines smoother
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Do some drawing.
    Shape circle = new Ellipse2D.Double(0, 0, 50, 50);
    g.setPaint(Color.red);
    g.fill(circle);
    g.translate(60, 0);
    g.setPaint(Color.green);
    g.fill(circle);
    g.translate(60, 0);
    g.setPaint(Color.blue);
    g.fill(circle);
    g.setSVGCanvasSize(new Dimension(180, 50));

    // Populate the document root with the generated SVG content.
    Element root = doc.getDocumentElement();
    g.getRoot(root);

    // Display the document.
    JSVGCanvas canvas = new JSVGCanvas();
    JFrame f = new JFrame();
    f.getContentPane().add(canvas);
    canvas.setSVGDocument(doc);
    f.pack();
    f.setVisible(true);
  }
}

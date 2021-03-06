/*
 */
package eu.discoveri.predikt.simpletests;

import eu.discoveri.predikt.utils.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Path2D;

import java.io.File;
import java.io.IOException;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class MergeSVGs
{
    private static final double SCALED = 60.d;
    private static final float  STROKES = 2.0f;
    private static final BasicStroke bstroke = new BasicStroke(STROKES);
    
    /**
     * Adjust coords if necessary.
     * 
     * @param xOrigin
     * @param yOrigin
     * @param points
     * @return 
     */
    private static double[] fixPoints( double xOrigin, double yOrigin, double[] points )
    {
        double fixPts[] = new double[points.length];
        
        for( int ii=0; ii<points.length; ii+=2 )
        {
            fixPts[ii] = points[ii] - xOrigin;
            fixPts[ii+1] = points[ii+1] - yOrigin;
        }
        
        return fixPts;
    }
    
    /**
     * Dump points.
     * 
     * @param points 
     */
    private static void dumpPoints( double[] points )
    {
        for( double pt: points )
        {
            System.out.print(pt+" ");
        }
        System.out.println("");
    }

    /**
     * Test curve.
     * 
     * @param g2 
     */
    private static SVGGraphics2D curveC()
    {
        // Set up drawing
        SVGGraphics2D g2 = new SVGGraphics2D(60,60);
        
        double[] inPts = { 200.,90., 0.,0., 90.,300. };
        
        // Determine min/max. x,y
        WidthHeight wh = new WidthHeight().minmax(inPts);
        
        g2.setColor(Color.BLACK);
        Path2D.Double p = new Path2D.Double();
        
        // Refactor to 0,0
        double[] points = fixPoints( wh.getMinX(), wh.getMinY(), inPts );
        
        // Scale
        double height = wh.getMaxX()-wh.getMinX(), width = wh.getMaxY()-wh.getMinY();
        if( width >= height )
            g2.scale(SCALED/width,SCALED/width);
        else
            g2.scale(SCALED/height,SCALED/height);
        
        // Shift to 0,0
        g2.translate(0.,0.);
        
        // Adjust stroke
        float adjust = (float)(width/SCALED);
        g2.setStroke(new BasicStroke(STROKES*adjust));
        
        p.moveTo(points[0],points[1]);
        for( int ii=0; ii<points.length; ii+=6 )
        {
            p.curveTo( points[ii],points[ii+1], points[ii+2],points[ii+3], points[ii+4],points[ii+5] );
        }
        
        g2.draw(p);
        return g2;
    }
    
    /**
     * delta.
     * 
     * @param g2 
     */
    private static SVGGraphics2D deltaA()
    {
        // Set up drawing
        SVGGraphics2D g2 = new SVGGraphics2D(60,60);
        
        // As generated by inkscape
        double[] inPts = {  18.426745,-76.667372,
                            20.285023,-77.869867,
                            18.463381,-77.755056,
                            15.312086,-78.777405,
                            11.535775,-79.648132,
                            8.5543196,-77.697404,
                            6.5042641,-76.215454,
                            5.8487212,-73.299176,
                            6.8059611,-71.003256,
                            7.2363192,-69.115496,
                            8.5175179,-67.582302,
                            10.122013,-66.553805,
                            11.919002,-65.220559,
                            13.354341,-63.496995,
                            14.993327,-61.986963,
                            19.152108,-57.743106,
                            21.003227,-51.151755,
                            18.999482,-45.463733,
                            17.907313,-42.258656,
                            15.189341,-39.298141,
                            11.713119,-38.853757,
                            9.2719629,-38.747694,
                            6.684035,-39.304662,
                            4.7233003,-40.808492,
                            2.1451355,-43.238255,
                            0.76007127,-46.816406,
                            1.0549882,-50.349435,
                            1.1584658,-53.769289,
                            2.7957139,-56.947631,
                            4.8929805,-59.570715,
                            6.3492741,-61.316699,
                            8.5744303,-62.041221,
                            10.437301,-63.23498,
                            11.128489,-63.611286,
                            11.834874,-63.962402,
                            12.567708,-64.250743  };
        
        // Determine min/max. x,y
        WidthHeight wh = new WidthHeight().minmax(inPts);

        g2.setColor(Color.BLACK);
        Path2D.Double p = new Path2D.Double();
        
        // Refactor to 0,0
        double[] points = fixPoints( wh.getMinX(), wh.getMinY(), inPts );
        
        // Scale
        double height = wh.getMaxX()-wh.getMinX(), width = wh.getMaxY()-wh.getMinY();
        if( width >= height )
            g2.scale(SCALED/width,SCALED/width);
        else
            g2.scale(SCALED/height,SCALED/height);
        
        // Shift to 0,0
        g2.translate(0.,0.);
        
        // Adjust stroke
        float adjust = (float)(width/SCALED);
        g2.setStroke(new BasicStroke(STROKES*adjust));
        
        // And draw (this may be called multiple times)
        p.moveTo(points[0],points[1]);
        for( int ii=0; ii<points.length; ii+=6 )
        {
            p.curveTo( points[ii],points[ii+1], points[ii+2],points[ii+3], points[ii+4],points[ii+5] );
        }
        g2.draw(p);
        
        return g2;
    }
    
    /**
     * Aries.
     * 
     * @param g2 
     */
    private static void aries( SVGGraphics2D g2 )
    {
        double[] inPts = {  476.337,164.01245,
                            487.00747,111.28024,
                            456.69314,76.893751,
                            421.72907,44.277301,
                            355.35842,39.531938,
                            325.67107,81.890836,
                            287.94023,134.35512,
                            273.15265,198.93913,
                            261.40947,261.35718,
                            257.85884,307.4796,
                            261.0454,353.83483,
                            259.91198,400.04661,
                            262.1052,416.66673,
                            256.48135,442.32914,
                            251.27292,412.29794,
                            252.78936,383.30404,
                            251.65623,354.6787,
                            251.07153,325.65063,
                            252.67379,282.73435,
                            246.10853,240.09389,
                            232.19056,199.53002,
                            218.38542,151.14306,
                            201.12312,99.746247,
                            162.32115,65.543421,
                            126.3549,42.80148,
                            70.047187,45.611668,
                            42.263378,80.065159,
                            15.478207,116.50493,
                            33.481713,164.78123,
                            50.148738,201.42977  };
        double xOrigin = 459.25683, yOrigin = 202.30958;
        
        double[] in2Pts = { 468.30395,182.02415,
                            474.73822,160.32368,
                            475.74792,138.13518,
                            476.24575,127.1953,
                            475.32174,116.13282,
                            472.28447,105.61124,
                            469.23843,95.059272,
                            463.95602,85.13226,
                            456.69314,76.893751,
                            447.77292,68.57247,
                            437.06148,62.257154,
                            425.64529,57.957772 };
        double[] line1 =  { 425.64526,57.957761 };
        double[] in3Pts = { 414.02543,53.581689,
                            401.61987,51.260704,
                            389.20333,51.276538,
                            377.04065,51.292048,
                            364.81266,53.596814,
                            353.75083,58.65318  };
        double[] line2 =  { 353.7508,58.653193  };
        double[] in4Pts = { 342.56818,63.764776,
                            332.72784,71.822006,
                            325.67107,81.890836,
                            306.97904,107.88188,
                            293.65996,137.43471,
                            283.7949,167.89135,
                            273.91625,198.38993,
                            267.33691,229.85132,
                            261.40948,261.35715 };
        double[] line3 =  { 261.40947,261.35718 };
        double[] in5Pts = { 257.85989,307.46591,
                            261.04588,353.81536,
                            259.91198,400.04661,
                            260.48647,404.40007,
                            260.52751,408.81519,
                            260.21983,413.1956,
                            260.21983,413.19561,
                            260.21983,413.19562,
                            260.21983,413.19563,
                            260.07984,415.18861,
                            259.86135,417.1774,
                            259.51566,419.14515,
                            259.24626,420.67867,
                            258.91629,422.20922,
                            258.36807,423.66652,
                            258.36806,423.66653,
                            258.36806,423.66654,
                            258.36806,423.66655,
                            258.17423,424.18179,
                            257.95267,424.68951,
                            257.65814,425.15457,
                            257.65813,425.15458,
                            257.65812,425.15459,
                            257.65812,425.1546,
                            257.4505,425.48242,
                            257.20565,425.80045,
                            256.87352,426.00111,
                            256.62144,426.1534,
                            256.30588,426.20387,
                            256.02629,426.11129,
                            255.65689,425.98895,
                            255.36911,425.69612,
                            255.1285,425.39029,
                            254.15215,424.14926,
                            253.68709,422.58673,
                            253.22947,421.07544,
                            252.36025,418.20477,
                            251.78546,415.2532,
                            251.27292,412.29794 };
        double[] line4 =  { 251.27292,412.29788 };
        double[] in6Pts = { 252.78185,383.4475,
                            251.65333,354.53465,
                            251.07153,325.65069 };
        double[] line5 =  { 251.07153,325.65063 };
        double[] in7Pts = { 252.66646,282.93067,
                            246.06457,239.96578,
                            232.19056,199.53002,
                            225.16859,174.91803,
                            217.51656,150.38087,
                            206.62967,127.21767,
                            195.78548,104.14533,
                            181.44566,82.401133,
                            162.32115,65.543421,
                            162.32113,65.54341,
                            162.32111,65.543398,
                            162.3211,65.543387,
                            153.14835,59.743348,
                            142.88958,55.730056,
                            132.29596,53.373164,
                            121.47018,50.96462,
                            110.26115,50.271336,
                            99.223849,51.356003,
                            88.313053,52.428237,
                            77.540238,55.257982,
                            67.685945,60.062985,
                            57.915411,64.827147,
                            49.086965,71.603526,
                            42.263378,80.065159,
                            35.94645,88.658997,
                            31.719537,98.746816,
                            29.851671,109.24771,
                            28.009166,119.60603,
                            28.324266,130.26868,
                            29.911485,140.66918,
                            33.142211,161.83903,
                            41.283378,181.93604,
                            50.148738,201.42977 };
        double xOrigin2 = 459.25683, yOrigin2 = 202.30958;
        // Fix first set of points
        double[] points = fixPoints( xOrigin, yOrigin, inPts );

        Path2D.Double p = new Path2D.Double();
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        
        p.moveTo(0.0,0.0);
        for( int ii=0; ii<points.length; ii+=6 )
        {
            p.curveTo( points[ii],points[ii+1], points[ii+2],points[ii+3], points[ii+4],points[ii+5] );
        }

        g2.draw(p);
    }
    

    /**-------------------------------------------------------------------------
     * ==============
     *    M A I N
     * ==============
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args)
            throws IOException
    {
//        SVGGraphics2D g2 = new SVGGraphics2D(600,600);
        
        SVGGraphics2D cc = curveC();
        SVGGraphics2D da = deltaA();
        
        SVGUtils.writeToSVG(new File(Constants.RESOURCEPATH+"tests/pathcc-test.svg"), cc.getSVGElement("curvecc"));
        cc.dispose();
        SVGUtils.writeToSVG(new File(Constants.RESOURCEPATH+"tests/pathda-test.svg"), da.getSVGElement("curveda"));
        da.dispose();
    }
}


//------------------------------------------------------------------------------
class WidthHeight
{
    private double  minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY,
                    maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
    
            // Determine min/max. x,y
    public WidthHeight minmax( double[] inPts )
    {
        for( int ii=0; ii<inPts.length; ii+=6 )
        {
            // Determine max x,y
            if( inPts[ii]>maxX )
                maxX = inPts[ii];
            if( inPts[ii+2]>maxX )
                maxX = inPts[ii+2];
            if( inPts[ii+4]>maxX )
                maxX = inPts[ii+4];
            if( inPts[ii+1]>maxY )
                maxY = inPts[ii+1];
            if( inPts[ii+3]>maxY )
                maxY = inPts[ii+3];
            if( inPts[ii+5]>maxY )
                maxY = inPts[ii+5];
            
            if( inPts[ii]<minX )
                minX = inPts[ii];
            if( inPts[ii+2]<minX )
                minX = inPts[ii+2];
            if( inPts[ii+4]<minX )
                minX = inPts[ii+4];
            if( inPts[ii+1]<minY )
                minY = inPts[ii+1];
            if( inPts[ii+3]<minY )
                minY = inPts[ii+3];
            if( inPts[ii+5]<minY )
                minY = inPts[ii+5];
        }

        return this;
    }

    /*
     * Get dimensions
     */
    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }
    
    @Override
    public String toString()
    {
        return "Min x/y: " +minX+"/"+minY +", Max x/y: " +maxX+"/"+maxY;
    }
}

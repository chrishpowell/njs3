/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.snippets;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Ranking
{
    // Some constants
    private static final double TEXTRANK_DAMPING_FACTOR = 0.85D;
    private static final double SCORECONVERGE = 0.005D;
    private static final int    NUMITERS = 100;
    // Converged? flag
    private static double       totConverge = Double.MAX_VALUE;
    
    // Graph for Sentences
    private static final Graph<TestNode,DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private static final List<TestNode> ltns = Arrays.asList( new TestNode("Node-A",0.25d,0.25d),
                                                              new TestNode("Node-B",0.25d,0.25d),
                                                              new TestNode("Node-C",0.25d,0.25d),
                                                              new TestNode("Node-D",0.25d,0.25d),
                                                              new TestNode("Node-E",0.25d,0.25d),
                                                              new TestNode("Node-F",0.25d,0.25d),
                                                              new TestNode("Node-G",0.25d,0.25d)
                                                            );
    private static final int A=0,B=1,C=2,D=3,E=4,F=5,G=6;
    
    private static void loadGraph()
    {
        // Add all vertices
        ltns.forEach(n -> g.addVertex(n));
        
        // Add edges @TODO: Do this with Map so we keep track via name eg: Map<String,TestNode>
        DefaultWeightedEdge ab = g.addEdge(ltns.get(A),ltns.get(B));            // AB:2
        g.setEdgeWeight(ab,2.d);
        DefaultWeightedEdge be = g.addEdge(ltns.get(B),ltns.get(E));            // BE:9
        g.setEdgeWeight(be,9.d);
        DefaultWeightedEdge eg = g.addEdge(ltns.get(E),ltns.get(G));            // EG:5
        g.setEdgeWeight(eg,5.d);
        DefaultWeightedEdge gf = g.addEdge(ltns.get(G),ltns.get(F));            // GF:1
        g.setEdgeWeight(gf,1.d);
        DefaultWeightedEdge cf = g.addEdge(ltns.get(C),ltns.get(F));            // CF:4
        g.setEdgeWeight(cf,4.d);
        DefaultWeightedEdge ca = g.addEdge(ltns.get(C),ltns.get(A));            // CA:3
        g.setEdgeWeight(ca,3.d);
        DefaultWeightedEdge dc = g.addEdge(ltns.get(D),ltns.get(C));            // DC:2
        g.setEdgeWeight(dc,2.d);
        DefaultWeightedEdge de = g.addEdge(ltns.get(D),ltns.get(E));            // DE:2
        g.setEdgeWeight(de,2.d);
        DefaultWeightedEdge bd = g.addEdge(ltns.get(B),ltns.get(D));            // BD:3
        g.setEdgeWeight(bd,3.d);
        DefaultWeightedEdge ad = g.addEdge(ltns.get(A),ltns.get(D));            // AD:1
        g.setEdgeWeight(ad,1.d);
        DefaultWeightedEdge df = g.addEdge(ltns.get(D),ltns.get(F));            // DF:5
        g.setEdgeWeight(df,5.d);
        DefaultWeightedEdge dg = g.addEdge(ltns.get(D),ltns.get(G));            // DG:3
        g.setEdgeWeight(dg,3.d);
    }
    
    private static void displayGraph()
            throws IOException
    {
        JGraphXAdapter<TestNode,DefaultWeightedEdge> ga = new JGraphXAdapter<>(g);
        mxOrganicLayout layout = new mxOrganicLayout(ga);
        
        layout.execute(ga.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(ga, null, 2, Color.WHITE, true, null);
        File imgFile = new File("/home/chrispowell/NetBeansProjects/LanguageGraph/src/main/java/resources/graph/lang-graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }
    
    /**
     * TextRank.
     * See papers by Mihelacea et al on TextRank for algorithm.  Plus book:
     * Graph based Natural Language Processing and Information Retrieval.
     * 
     */
    private static void textRank1()
    {
        // Init nodes
        g.vertexSet().forEach((n) -> {
            System.out.format(n.getName()+": %6f ",n.getScore());
        });
        System.out.println("");
        
        // Score
        for( int ii= 0; ii<NUMITERS; ii++ )
        {
            for( TestNode n: g.vertexSet() )                                    // For all vertices V(i)
            {
                double rankVjTot = 0.d;

                // Incoming edges of Vi
                Set<DefaultWeightedEdge> edges = g.incomingEdgesOf(n);
//                System.out.println(""+n.getName()+":");

                // Sum over Vjs of Inc edges of Vi
                for( DefaultWeightedEdge dwe: edges )
                {
                    //System.out.println("  > Remote vertex: " +g.getEdgeSource(dwe)+ ", Weight: " +g.getEdgeWeight(dwe));
                    TestNode nJ = g.getEdgeSource(dwe);                         // vJ

                    double edgeWeightJI = g.getEdgeWeight(dwe);                 // Wji
                    Set<DefaultWeightedEdge> eJ = g.outgoingEdgesOf(nJ);

                    // Sum of weights of Outgoing edges of Vj
                    double sumOfVjOutEdgeWeightsJK = eJ.stream().mapToDouble((dweJ) -> g.getEdgeWeight(dweJ)).sum();

                    // WS(Vj): nJ.getScore(); Wji: edgeWeightJI; Sum Out Wjk: sumOfVjOutEdgeWeightsJK
//                    System.out.println(""+n.getName()+": Wji="+edgeWeightJI+", Vj="+nJ.getName()+", WS(Vj)="+nJ.getScore()+", sumWjk="+sumOfVjOutEdgeWeightsJK);
                    rankVjTot += edgeWeightJI * nJ.getScore() / sumOfVjOutEdgeWeightsJK;
                }
                
//                System.out.println("   > "+n.getName()+": "+(rankVjTot*TEXTRANK_DAMPING_FACTOR +(1 - TEXTRANK_DAMPING_FACTOR)/ltns.size()));
                
                // Update previous score with current score
                n.setPrevScore(n.getScore());
                // WS(Vi) update
                n.setScore(rankVjTot*TEXTRANK_DAMPING_FACTOR +(1 - TEXTRANK_DAMPING_FACTOR)/ltns.size());
            }
            
            // Show converging
            totConverge = 0.d;
            g.vertexSet().forEach((n) -> {
                System.out.format(n.getName()+": %6f ",n.getScore());
                totConverge += n.getPrevScore()-n.getScore();
            });
            System.out.println("");
            
            // Converged
            if( totConverge*g.vertexSet().size() <= SCORECONVERGE )
                break;
        }
    }
    
    public static void main(String[] args)
            throws IOException
    {
        loadGraph();
        //displayGraph();
        textRank1();
    }
}

class TestNode
{
    private String  name;
    private double  score, prevScore;

    public TestNode(String name, double score, double prevScore)
    {
        this.name = name;
        this.score = score;
        this.prevScore = prevScore;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getPrevScore() { return prevScore; }
    public void setPrevScore(double prevScore) { this.prevScore = prevScore; }
    
    @Override
    public String toString() { return name; }
}

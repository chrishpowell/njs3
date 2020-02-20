/*
 */
package eu.discoveri.languagegraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


/**
 * Build graphs, sentence graphs, score vertices etc.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Graphs
{
    // Graph for Sentences
    private static final Graph<Object,DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    
    /**
     * Add a number of (unique) nodes to graph.  Eg: tokens of sentences.
     * 
     * @param <T extends Comparable> Comparable used for filtering
     * @param nodes
     * @return 
     */
    public <T extends Predicate> Graph addListUniques2Graph( List<T> nodes )
    {
        // Check for unique entries
        Set<T> uniqueSet = new HashSet<>();
        
        // Add vertices to graph, filter by T test(), check for uniqueness of node
        Void v = null;
        nodes.stream()
                .filter(n -> n.test(v))
                .forEach(n -> {
                        if(!uniqueSet.contains(n))
                        { // NB: Not thread safe!
                            uniqueSet.add(n);
                            g.addVertex(n);
                            // Edges??
                        }
                });
        
        return g;
    }
    
    /**
     * Dump the vertices
     */
    public void dumpVertices()
    {
        g.vertexSet().forEach(System.out::println);
    }
    
    /**
     * Dump the edges
     */
    public void dumpEdges()
    {}
}

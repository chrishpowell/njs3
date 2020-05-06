/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graph;

import eu.discoveri.codesnippets.graphs.SentenceTokenEdge;
import eu.discoveri.codesnippets.graphs.SentenceTokenNode;
import eu.discoveri.codesnippets.graphs.SentenceTokenEdgeService;
import eu.discoveri.codesnippets.graphs.SentenceTokenNodeService;
import eu.discoveri.codesnippets.graphs.Token;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

import org.neo4j.ogm.session.Session;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GraphTest2a
{
    private static final List<Token> lts = Arrays.asList(new Token("quick"),new Token("brown"),new Token("fox"),new Token("jumps"),new Token("over"),new Token("lazy"),new Token("dog"));
    private static final List<Token> lts2 = Arrays.asList(new Token("Every"), new Token("good"), new Token("boy"), new Token("deserves"), new Token("fruit"));
    private static final Map<String,SentenceTokenNode> disc2Map = Map.ofEntries(
        entry("Sa",new SentenceTokenNode("Sa","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("Sb",new SentenceTokenNode("Sb","2 Every good boy deserves fruit",lts2,0.01)),
        entry("Sc",new SentenceTokenNode("Sc","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("Sd",new SentenceTokenNode("Sd","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("Se",new SentenceTokenNode("Se","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sf",new SentenceTokenNode("Sf","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sg",new SentenceTokenNode("Sg","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
        entry("Sx",new SentenceTokenNode("Sx","7 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sy",new SentenceTokenNode("Sy","8 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceTokenNode,SentenceTokenNode>,SentenceTokenEdge> disc2Edges = Map.ofEntries(
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sa"),disc2Map.get("Sb")),new SentenceTokenEdge(disc2Map.get("Sa"),disc2Map.get("Sb"),3.14159d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sa"),disc2Map.get("Sc")),new SentenceTokenEdge(disc2Map.get("Sa"),disc2Map.get("Sc"),0.9d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sb"),disc2Map.get("Sc")),new SentenceTokenEdge(disc2Map.get("Sb"),disc2Map.get("Sc"),0.09d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sb"),disc2Map.get("Se")),new SentenceTokenEdge(disc2Map.get("Sb"),disc2Map.get("Se"),1.25d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sc"),disc2Map.get("Sd")),new SentenceTokenEdge(disc2Map.get("Sc"),disc2Map.get("Sd"),1.37d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sd"),disc2Map.get("Sf")),new SentenceTokenEdge(disc2Map.get("Sd"),disc2Map.get("Sf"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sd"),disc2Map.get("Sg")),new SentenceTokenEdge(disc2Map.get("Sd"),disc2Map.get("Sg"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Se"),disc2Map.get("Sf")),new SentenceTokenEdge(disc2Map.get("Se"),disc2Map.get("Sf"),0.5d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sx"),disc2Map.get("Sy")),new SentenceTokenEdge(disc2Map.get("Sx"),disc2Map.get("Sy"),0.5d))
    );
    
    /**
     * Get corpus (sentences).
     * @return 
     */
    public static Map<String,SentenceTokenNode> getVertices()
    {
        return disc2Map;
    }
    
    public static Map<AbstractMap.SimpleEntry<SentenceTokenNode,SentenceTokenNode>,SentenceTokenEdge> getEdges()
    {
        return disc2Edges;
    }
    
    /**
     * Persist corpus from Maps.
     * @param sns
     * @param ses
     */
    public static void populateDbFromMaps(  SentenceTokenNodeService sns,
                                            SentenceTokenEdgeService ses )
    {
        getVertices().forEach((k,v) -> {
            v.persist(sns);
        });                                                                     // SentenceNodes
        getEdges().forEach((k,v) -> v.persist(ses));                            // SentenceEdges
    }
    
    public static void main(String[] args)
    {
        // Init
        GraphTest2a gt2a = new GraphTest2a();

        // Session
        DiscoveriSessionFactory discSess = DiscoveriSessionFactory.getInstance();
        Session sess = discSess.getSession();
        // Db service
        SentenceTokenNodeService sns = new SentenceTokenNodeService();
        SentenceTokenEdgeService ses = new SentenceTokenEdgeService();

        /*
         * Populate db (from corpus Maps)
         * ------------------------------
         */
        sess.purgeDatabase();                                                   // Clear database
        populateDbFromMaps( sns, ses );                                         // Populate from maps above
        
        // Close
        discSess.close();
    }
}

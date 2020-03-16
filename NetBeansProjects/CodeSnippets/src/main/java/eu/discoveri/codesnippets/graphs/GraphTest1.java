/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Set;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.TransactionConfig;
import org.neo4j.driver.net.ServerAddress;
import static org.neo4j.driver.Values.parameters;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GraphTest1
{
    // Test data
    private final List<String> lts = List.of("quick", "brown", "fox", "jump", "lazy", "dog");
    private final List<String> lts2 = List.of("every", "good", "boy", "deserve", "fruit", "vegetable", "console", "time");
    private final Map<String,SentenceNode> snMap = Map.ofEntries(
            entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S5",new SentenceNode("S5","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("S6",new SentenceNode("S6","6 The quick brown fox jumps over the lazy dog",lts,2.1))
        );
    private final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> simScores = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S2")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S3")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S4")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S3")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S4")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S5")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S5"),snMap.get("S6")),0.5d)
    );
    
    // Db driver
    private Driver driver = null;
    // Session config
    SessionConfig sessionCfg = SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build();
    // Tx config
    TransactionConfig txCfg = TransactionConfig.builder().withTimeout(Duration.ofSeconds(5)).build();
    
    
    /**
     * Build the driver.
     * @param vUri
     * @param user
     * @param pwd
     * @param addresses
     * @return 
     */
    private Driver createDriver( String vUri, String user, String pwd, ServerAddress... addresses )
    {
        // Build a config
        Config cfg = Config.builder()
                        .withResolver(address -> new HashSet<>(Arrays.asList(addresses))) 
                        .build();
        
        // Neo4j driver
        return GraphDatabase.driver( vUri, AuthTokens.basic(user,pwd), cfg);
    }
    
    /**
     * Get corpus (sentences).
     * @return 
     */
    private Map<String,SentenceNode> getCorpus()
    {
        return snMap;
    }
    
    private Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> getSimScores()
    {
        return simScores;
    }
    
    /**
     * Create a node with directed edge using SET
     * @return 
     */
    private Result createNodeWithDEdge()
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            Query q = new Query("CREATE (x:Summat)->[:HAS]-(y:Nasty) SET x={name:$xname,desc:$xdesc} SET y={name:$yname,desc:$ydesc}",
                                    parameters("xname","fred","xdesc","xdesc","yname","bill","ydesc","ydesc"));
            return sess.run( q, txCfg );
        }
    }
    
    /**
     * Create a node with undirected edge using SET
     * @return 
     */
    private Result createNodeWithUEdge()
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            // Both methods create a directed edge!
//            Query q = new Query("CREATE (x:Summat)-[:HAS]->(y:Nasty) SET x={name:$xname,desc:$xdesc} SET y={name:$yname,desc:$ydesc} SET w={weight:$weight}",
//                                    parameters("xname","fred","xdesc","xdesc","yname","bill","ydesc","ydesc","weight",3.14159));
            Query q = new Query("CREATE (x:Summat) CREATE (y:Nasty) MERGE (x)-[w:HAS]-(y) SET x={name:$xname,desc:$xdesc} SET y={name:$yname,desc:$ydesc} SET w={weight:$weight}",
                                parameters("xname","fred","xdesc","xdesc","yname","bill","ydesc","ydesc","weight",3.14159));
            
            return sess.run( q, txCfg );
        }
    }
    
    /**
     * Persist a sentence.
     * @param sn A sentence(node) a wrapper for a sentence.
     * @returns the result
     */
    private void persistSentence( SentenceNode sn )
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            Set<Query> qs = sn.buildSentenceQuerySet();
            qs.forEach(q -> sess.run(q,txCfg));
        }
    }
    
    /**
     * Persist an undirected weighted edge between two sentences.
     * @param source
     * @param target
     * @param weight
     * @return 
     */
    private Result persistUWEdge( SentenceNode source, SentenceNode target, double weight )
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            return sess.run( source.addWeightedEdge(target,weight) );
        }
    }
    
    private Result emptyDb()
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            return sess.run( new Query("MATCH (n) DETACH DELETE n") );
        }
    }
    
    /**
     * Close connections
     */
    private void close()
    {
        driver.close();
    }


    /**
     * M A I N
     * =======
     * @param args 
     */
    public static void main(String[] args)
    {
        final String NEO4J = "neo4j://localhost";
        final String USER = "neo4j", PWD = "karabiner";
        
        // Setup db
        GraphTest1 g = new GraphTest1();
        // Create the driver
        g.driver = g.createDriver(NEO4J, USER, PWD, ServerAddress.of("localhost", 7687));
        
        // Clear db
        g.emptyDb();
        
        // Get sentences/scores
        Map<String,SentenceNode> sNode = g.getCorpus();
        Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> snScores = g.getSimScores();
        
        // Clear all nodes/edges of db
        // ??
        
        // Populate db
        sNode.forEach((k,v) -> g.persistSentence(v));                           // Nodes
        snScores.forEach((k,v) -> {                                             // Edges
            g.persistUWEdge(k.getKey(),k.getValue(), v);
        });

        // Test Create with undirectededge
//        g.createNodeWithUEdge();
        
        // Close db
        g.close();
    }
}
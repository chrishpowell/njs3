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
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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
public class GraphTest11
{
    // Db driver
    private Driver driver = null;
    // Session config
    SessionConfig sessionCfg = SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build();
    // Tx config
    TransactionConfig txCfg = TransactionConfig.builder().withTimeout(Duration.ofSeconds(5)).build();
    
    // Get sentences/scores
    Map<String,SentenceNode> sNodes = Corpi.getVertices();
    Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> sEdges = Corpi.getEdges();
    
    
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
    
    private void betweennessUnWeight(String s)
    {
        // Limitless queue
        Queue<SentenceNode> q = new LinkedBlockingQueue();
        Stack<SentenceNode> stk = new Stack();
        
        SentenceNode start = sNodes.get(s);
        
        q.add(start);
        start.setVisited();
        start.setDist(0.d);     // delta-s
        start.setSpath(1.d);    // sigma-s (lowercase)
        
        sNodes.forEach((name,sn) -> {
//            q.forEach(System.out::print);
//            System.out.println("");
            SentenceNode snv = q.poll();
            stk.push(snv);
            
            sEdges.forEach((w,weight) -> {
                // Source and target vertices
                SentenceNode snsrc = w.getKey(), sntgt = w.getValue();
                
                // Process against opposite vertex on edge 
                if( sntgt.equals(snv) )
                {
                    if( !snsrc.isVisited() )
                    {
                        snsrc.setVisited();                         // Now set as visited
                        snsrc.setPrev(snv);                         // snv is 'previous' vertex
                        snsrc.setDist(snv.getDist()+1.d);           // One hop on from snv
                        q.add(snsrc);                               // Push on to queue
                    }
                    if( snsrc.getDist() == snv.getDist()+1.d )      // On shortest path?
                    {
                        snsrc.setSpath(snsrc.getSpath()+snv.getSpath());
                        snsrc.getPreds().add(snsrc);
                    }
                    snv.getAdjs().add(snsrc);
                }
                else
                    if( snsrc.equals(snv) )
                    {
                        if( !sntgt.isVisited() )
                        {
                            sntgt.setVisited();
                            sntgt.setPrev(snv);
                            sntgt.setDist(snv.getDist()+1.d);
                            q.add(sntgt);
                        }
                        if( sntgt.getDist() == snv.getDist()+1.d )  // Shortest path
                        {
                            sntgt.setSpath(sntgt.getSpath()+snv.getSpath());
                            sntgt.getPreds().add(snsrc);
                        }
                        snv.getAdjs().add(sntgt);
                    }
            });
        });

        // Accumulation
        while( !stk.isEmpty() )
        {
            SentenceNode snw = stk.pop();
            snw.getPreds().forEach(v -> v.setBetween(v.getBetween()+(v.getSpath()/snw.getSpath())*(1.d+snw.getBetween())));
            if( snw != start )
                snw.setCbetween(snw.getCbetween()+snw.getBetween());
        }
    }
    
    private void betweennessWeight(String s)
    {
        // Limitless queue
        Queue<SentenceNode> q = new LinkedBlockingQueue();
        Stack<SentenceNode> stk = new Stack();
        
        SentenceNode start = sNodes.get(s);
        
        q.add(start);
        start.setVisited();
        start.setDist(0.d);     // delta-s
        start.setSpath(1.d);    // sigma-s (lowercase)
        
        sNodes.forEach((name,sn) -> {
//            q.forEach(System.out::print);
//            System.out.println("");
            SentenceNode snv = q.poll();
            stk.push(snv);
            
            sEdges.forEach((w,weight) -> {
                // Source and target vertices
                SentenceNode snsrc = w.getKey(), sntgt = w.getValue();
                
                // Process against opposite vertex on edge 
                if( sntgt.equals(snv) )
                {
                    if( snsrc.getDist() > snv.getDist() + weight )
                    {
                        snsrc.setPrev(snv);                         // snv is 'previous' vertex
                        snsrc.setDist(snv.getDist() + weight);      // Shortest weighted hop on from snv
                        q.add(snsrc);                               // Push on to queue
                    }
                    if( snsrc.getDist() == snv.getDist() + weight )          // On shortest path?
                    {
                        snsrc.setSpath(snsrc.getSpath()+snv.getSpath());
                        snsrc.getPreds().add(snsrc);
                    }
                    snv.getAdjs().add(snsrc);
                }
                else
                    if( snsrc.equals(snv) )
                    {
                        if( sntgt.getDist() > snv.getDist() + weight  )
                        {
                            sntgt.setPrev(snv);
                            sntgt.setDist(snv.getDist() + weight);
                            q.add(sntgt);
                        }
                        if( sntgt.getDist() == snv.getDist() + weight )  // Shortest path
                        {
                            sntgt.setSpath(sntgt.getSpath()+snv.getSpath());
                            sntgt.getPreds().add(snsrc);
                        }
                        snv.getAdjs().add(sntgt);
                    }
            });
        });

        // Accumulation
        while( !stk.isEmpty() )
        {
            SentenceNode snw = stk.pop();
            snw.getPreds().forEach(v -> v.setBetween(v.getBetween()+(v.getSpath()/snw.getSpath())*(1.d+snw.getBetween())));
            if( snw != start )
                snw.setCbetween(snw.getCbetween()+snw.getBetween());
        }
    }
    
    private void dumpCB()
    {
        sNodes.forEach((name,sn) -> {
            System.out.println(sn.getName()+":"+sn.getPrev()+"- dist to start: "+sn.getDist()+", SPath: "+sn.getSpath()+", Betw: "+sn.getBetween()+", CB: "+sn.getCbetween());
        });
    }
    
    private void dumpAdjList()
    {
        sNodes.forEach((key,val) -> {
            System.out.print("("+val.getAdjs().size()+") ");
            val.getAdjs().forEach(al -> System.out.print(al.getName()+" "));
            System.out.println("");
        });
    }
    
    
    private Map<String,CDs> calcCBw()
    {
        Map<String,CDs> CDwtuned = new TreeMap<>();
        final double ALPHATUNING = 1.3d;                   // Must be positive
        
        sNodes.forEach((key,val) -> {
            double weight = 0.d, w = 0.d;
            
            for( SentenceNode sn: val.getAdjs() )
            {
                AbstractMap.SimpleEntry k1 = new AbstractMap.SimpleEntry<>(val,sn);
                if( sEdges.containsKey(k1) )
                    w = sEdges.get(k1);
                else
                {
                    AbstractMap.SimpleEntry k2 = new AbstractMap.SimpleEntry<>(sn,val);
                    w = sEdges.get(k2);
                }
                
                weight += w;
            }
            
            int size = val.getAdjs().size();
            CDwtuned.put( key,
                          new CDs(size, weight, Math.pow(size,1.d-ALPHATUNING)*Math.pow(weight,ALPHATUNING))  );
        });
        
        return CDwtuned;
    }
    
    private void populateDb()
    {
        sNodes.forEach((k,v) -> persistSentence(v));                            // Nodes
        sEdges.forEach((k,v) -> {                                               // Edges
            persistUWEdge(k.getKey(),k.getValue(), v);
        });
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
        final String STARTNODE = "Sa";
        final double ALPHATUNING = 1.3d;                   // Must be positive
        
        // Setup db
        GraphTest11 g = new GraphTest11();
        // Create the driver
        g.driver = g.createDriver(NEO4J, USER, PWD, ServerAddress.of("localhost", 7687));
        
        // Clear db
        g.emptyDb();
        
        // Build the graph
        g.populateDb();

        // Betweenness
        g.betweennessUnWeight(STARTNODE);        // sNodes.get("S2")
//        g.betweennessWeight(STARTNODE);

        // Central betweenness
        double sum = 0.d;
        Map<String,CDs> cbwtune = g.calcCBw();
        System.out.println("\r\nCentral Betweenness (weighted):");
        System.out.println("Node (CD):(CDw) (CDw tuned ["+ALPHATUNING+"])");
        for( Map.Entry<String,CDs> entry: g.calcCBw().entrySet() )
        {
            CDs cds = entry.getValue();
            System.out.println("> " +entry.getKey()+ " ("+cds.getCd()+") :("+cds.getCDw()+") ("+cds.getCDwtuned()+") ");
            sum += cds.getCDwtuned();
        }
        System.out.println("");
        
        DescriptiveStatistics stats = new DescriptiveStatistics();
        cbwtune.forEach((key,cds) -> {
            stats.addValue(cds.getCDwtuned());
        });
        System.out.println("20th percentile: " +stats.getPercentile(20.d));
        
        cbwtune.forEach((key,cds) -> {
            if( cds.getCDwtuned() > stats.getPercentile(20.d) )
                System.out.println("Kept: " +key);
        });
        
        // Close db
        g.close();
    }
}

/*-----------------------------------------------------------------------------*/
class Corpi
{
    // Test data
    private static final List<String> lts = List.of("quick", "brown", "fox", "jump", "lazy", "dog");
    private static final List<String> lts2 = List.of("every", "good", "boy", "deserve", "fruit", "vegetable", "console", "time");
    private static final Map<String,SentenceNode> centMap = Map.ofEntries(
            entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,1.99))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> centEdges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S1")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S3")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S4")),1.25d)
    );
    private static final Map<String,SentenceNode> wMap = Map.ofEntries(
        entry("Sa",new SentenceNode("Sa","a The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("Sb",new SentenceNode("Sb","b The quick brown fox jumps over the lazy dog",lts2,0.01)),
        entry("Sc",new SentenceNode("Sc","c The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("Sd",new SentenceNode("Sd","d The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("Se",new SentenceNode("Se","e The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sf",new SentenceNode("Sf","f The quick brown fox jumps over the lazy dog",lts,1.99))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> wEdges = Map.ofEntries(
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sa"),wMap.get("Sb")),4.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sa"),wMap.get("Sc")),4.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Sc")),2.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Se")),1.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Sd")),1.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Se"),wMap.get("Sf")),7.d)
    );
    private static final Map<String,SentenceNode> exMap = Map.ofEntries(
        entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
        entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("S5",new SentenceNode("S5","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("S6",new SentenceNode("S6","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
        entry("S7",new SentenceNode("S7","7 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("S8",new SentenceNode("S8","8 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("S9",new SentenceNode("S9","9 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> exEdges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S0"),exMap.get("S8")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S8"),exMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S8"),exMap.get("S9")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S9"),exMap.get("S1")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S2"),exMap.get("S1")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S2"),exMap.get("S4")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S4"),exMap.get("S3")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S1"),exMap.get("S3")),0.5d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S1"),exMap.get("S7")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S3"),exMap.get("S5")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S5"),exMap.get("S6")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S7"),exMap.get("S6")),1.25d)
    );
    private static final Map<String,SentenceNode> snMap = Map.ofEntries(
            entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S5",new SentenceNode("S5","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("S6",new SentenceNode("S6","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
            entry("S7",new SentenceNode("S7","7 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S8",new SentenceNode("S8","8 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S9",new SentenceNode("S9","9 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> simScores = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S0"),snMap.get("S8")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S8"),snMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S8"),snMap.get("S9")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S9"),snMap.get("S1")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S1")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S4")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S4"),snMap.get("S3")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S3")),0.5d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S7")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S5")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S5"),snMap.get("S6")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S7"),snMap.get("S6")),1.25d)
    );
    
    /**
     * Get corpus (sentences).
     * @return 
     */
    public static Map<String,SentenceNode> getVertices()
    {
        return wMap;
    }
    
    public static Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> getEdges()
    {
        return wEdges;
    }
}

/*-----------------------------------------------------------------------------*/
class CDs
{
    private final int     Cd;
    private final double  CDw, CDwtuned;

    public CDs(int Cd, double CDw, double CDwtuned)
    {
        this.Cd = Cd;
        this.CDw = CDw;
        this.CDwtuned = CDwtuned;
    }

    public int getCd() { return Cd; }
    public double getCDw() { return CDw; }
    public double getCDwtuned() { return CDwtuned; }
}
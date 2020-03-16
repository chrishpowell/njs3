/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */

package eu.discoveri.codesnippets.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.driver.Query;
import static org.neo4j.driver.Values.parameters;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */

/**
 * Neo4j node for a sentence.
 * @author Chris Powell, Discoveri OU
 */
class SentenceNode
{
    private final String                sid;
    private final String                name;
    private final String                sentence;
    private double                      score;
    private final List<String>          tokens;
    private boolean                     visited = false;
    private boolean                     queued = false;
    
    // Distance
    private double                      dist = Double.MAX_VALUE;
    // Between
    private double                      between = 0.d, cbetween = 0.d;
    // Shortest paths (will be changed by edge weight)
    private double                      spath = 0.d;
    // First time
    private boolean                     pathFound = false;
    
    private SentenceNode                prev;

    // Betweeness
    private double                      bc = 0.d;
    // Predecessors
    private List<SentenceNode>          preds = new ArrayList<>();
    // Adjacencies
    private List<SentenceNode>          adjs = new ArrayList<>();

    // Neo4j params
    private Map<String,Object>          params = new HashMap<>();

    /**
     * Constructor.
     * 
     * @param name
     * @param sentence
     * @param score 
     */
    public SentenceNode(String sid, String sentence, List<String> tokens, double score)
    {
        this.sid = sid;
        this.name = sid;
        this.sentence = sentence;
        this.score = score;
        this.tokens = tokens;

        // Build map for graph (tokens need to be processed separately)
        params.put("sid",this.sid);
        params.put("name", this.name);
        params.put("sentence",this.sentence);
        params.put("score",this.score);
    }

    
    public String getSID() { return sid; }
    public String getName() { return name; }
    public String getSentence() { return sentence; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }
    public void setVisited() { this.visited = true; }

    public boolean isQueued() { return queued; }
    public void setQueued(boolean queued) { this.queued = queued; }
    public void setQueued() { this.queued = true; }

    public List<SentenceNode> getPreds() { return preds; }
    public void setPreds(List<SentenceNode> preds) { this.preds = preds; }
    
    public List<SentenceNode> getAdjs() { return adjs; }
    public void setAdjs(List<SentenceNode> adjs) { this.adjs = adjs; }
    
    public double getDist() { return dist; }
    public void setDist(double dist) { this.dist = dist; }

    public double getBetween() { return between; }
    public void setBetween(double between) { this.between = between; }

    public double getCbetween() { return cbetween; }
    public void setCbetween(double cbetween) { this.cbetween = cbetween; }

    public double getSpath() { return spath; }
    public void setSpath(double spath) { this.spath = spath; }
    public void incrSPath(double vpath) { this.spath += vpath; }

    public boolean isPathFound() { return pathFound; }
    public void setPathFound(boolean pathFound) { this.pathFound = pathFound; }

    public SentenceNode getPrev() { return prev; }
    public void setPrev(SentenceNode prev) { this.prev = prev; }
    

    // ...Not sure about this:
//    public List<SNBetween> getSnb() { return snb; }
//    public void setSnb(List<SNBetween> snb) { this.snb = snb; }

    public List<String> getTokens() { return tokens; }
    
    /**
     * Create a new sentence node, ready for persisting.
     * @return Ordered set of Queries
     */
    public Set<Query> buildSentenceQuerySet()
    {
        // Ordered Set
        Set<Query> lhs = new LinkedHashSet<>();
        // Map for tokens
        Map<String,Object> ps = new HashMap<>();
        
        // Add first query
        String s = "CREATE ("+this.sid+":Sentence) SET "+this.sid+"={sid:$sid,name:$name,sentence:$sentence,score:$score}";
        lhs.add( new Query(s,params) );
//        System.out.println("S..> " +s);

        // Create tokens of the sentence
        StringBuilder q0 = new StringBuilder();
        int idx = 0;
        for( String t: tokens )
        {
            q0.append("CREATE (t").append(idx).append(this.sid)
                    .append(":Token {text:$text").append(idx)
                    .append(",tid:$tid").append(idx).append("}) ");

            ps.put("text"+idx,t);
            ps.put("tid"+idx,"t"+idx+this.sid);
            // Incr idx
            ++idx;
        }
        // Create the query
        lhs.add( new Query(q0.toString(),ps) );
//        System.out.println("0..> " +q0.toString());
//        ps.forEach((k,v) -> System.out.println("ps..> " +k+ ":" +v));
        
        // Build edges
        idx = 0;
        for( String t: tokens )
        {
            StringBuilder q1 = new StringBuilder();
            q1.append(" MATCH (src:Sentence {sid:$sid}) MATCH (tgt:Token {tid:$tid})")
                    .append(" MERGE (src)-[:TOK]-(tgt)");
//            System.out.println("1..> " +q1.toString());
            
            // Create the query
            lhs.add( new Query(q1.toString(),parameters("sid",this.sid,"tid","t"+idx+this.sid)) );
            // Incr idx
            ++idx;
        }
//        q1.append(" RETURN " +idx);

        return lhs;
    }
    
    /**
     * Create a weighted edge between sentences.
     * @param otherNode
     * @param weight
     * @return 
     */
    public Query addWeightedEdge( SentenceNode otherNode, double weight )
    {
        return new Query( "MATCH (src:Sentence {name:$srcName}) MATCH (tgt:Sentence {name:$tgtName}) MERGE (src)-[:SIMILARTO {weight:$weight}]-(tgt)",
                          parameters("srcName",sid,"tgtName",otherNode.getSID(),"weight",weight)  );
    }
    
    @Override
    public String toString()
    {
        return name+" ("+tokens.size()+")";
    }
}
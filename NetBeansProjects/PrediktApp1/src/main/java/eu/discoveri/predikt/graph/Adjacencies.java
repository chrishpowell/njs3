/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.predikt.graph;

import java.util.ArrayList;
import java.util.List;


/**
 * This ridiculous class is necessary because Neo4j OGM cannot return edges properly!!
 * That is each node/vertex with a list of its adjacencies. A hokey Cypher query
 * is required which returns RelationshipModel, not an Edge.  Furthermore, the Cypher
 * query returns a list separate from the node/vertex. 
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Adjacencies
{
    private final List<Vertex>          eNodes;
    // Weights should be a class... someday.
    private final List<Double>          weights;
    private final List<Adjacency>       adjs = new ArrayList<>();
    
    public Adjacencies( List<Vertex> eNodes, List<Double> weights )
    {
        this.eNodes = eNodes;
        this.weights = weights;
        
        for( int ii=0; ii < eNodes.size(); ii++ )
        {
            adjs.add(new Adjacency(eNodes.get(ii),weights.get(ii)));
        }
    }

    public List<Vertex> getENodes() { return eNodes; }
    public List<Double> getWeights() { return weights; }
    public List<Adjacency> getAdjacencies() { return adjs; }
}

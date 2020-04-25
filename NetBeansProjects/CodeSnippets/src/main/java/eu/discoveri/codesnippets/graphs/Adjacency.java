/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

/**
 * Edge and its "end" node.
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Adjacency
{
    private final Vertex    eNode;
    private final double    weight;
    
    public Adjacency( Vertex eNode, Double weight )
    {
        this.eNode = eNode;
        this.weight = weight;
    }

    public Vertex getENode() { return eNode; }
    public double getWeight() { return weight; }
}

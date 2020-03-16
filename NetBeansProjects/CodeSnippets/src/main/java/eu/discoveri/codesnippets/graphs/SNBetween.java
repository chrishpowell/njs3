/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SNBetween
{
    // Predecessors in betweenness calc.
    private SentenceNode    predecessors;
    // Shortest path
    private SentenceNode    spath;
    // Distance this node to source node
    private double          dist = Double.MAX_VALUE;

    public SentenceNode getPredecessors() { return predecessors; }
    public void setPredecessors(SentenceNode predecessors) { this.predecessors = predecessors; }

    public SentenceNode getSpath() { return spath; }
    public void setSpath(SentenceNode spath) { this.spath = spath; }

    public double getDist() { return dist; }
    public void setDist(double dist) { this.dist = dist; }
}

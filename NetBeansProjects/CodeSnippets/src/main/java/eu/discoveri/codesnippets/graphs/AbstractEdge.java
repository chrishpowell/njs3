/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AbstractEdge extends GraphEntity implements Edge
{
    @StartNode
    private Vertex    n1 = null;
    @EndNode
    private Vertex    n2 = null;
    
    public AbstractEdge( String namespace, String name, Vertex n1, Vertex n2 )
    {
        super(namespace,name);
        this.n1 = n1;
        this.n2 = n2;
    }
    
    public AbstractEdge()
    {
        this("eu.discoveri.predikt","",null,null);
    }
    
    /**
     * Get sentence on first end.
     * @return 
     */
    public Vertex getN1() { return n1; }
    /**
     * Get sentence on second end.
     * @return 
     */
    public Vertex getN2() { return n2; }
}

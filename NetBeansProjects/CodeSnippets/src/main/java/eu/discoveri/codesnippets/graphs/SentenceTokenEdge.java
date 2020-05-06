/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
@RelationshipEntity(type="SIMILARTO")
public class SentenceTokenEdge extends AbstractEdge
{
    // Edge weight.  Default 1, equiv. to no weighting.
    @Property
    private double          weight = 1.0d;
    
        
    /**
     * Constructor.
     * 
     * @param name
     * @param namespace
     * @param weight 
     * @param s1 
     * @param s2 
     */
    public SentenceTokenEdge( String namespace,String name, double weight, SentenceTokenNode s1, SentenceTokenNode s2 )
    {
        super(namespace,name, s1, s2);
        this.weight = weight;
    }
    
    /**
     * Constructor.
     * Default name (merge s1,s2 names), namespace.
     * @param s1
     * @param s2
     * @param weight 
     */
    public SentenceTokenEdge( SentenceTokenNode s1, SentenceTokenNode s2, double weight )
    {
        this("eu.discoveri.predikt",s1.getName()+s2.getName(), weight,s1,s2);
    }
    
    /**
     * Constructor.
     * Default name (merge s1,s2 names), namespace, weight
     * @param s1
     * @param s2 
     */
    public SentenceTokenEdge( SentenceTokenNode s1, SentenceTokenNode s2 )
    {
        this("eu.discoveri.predikt",s1.getName()+s2.getName(), 1.0d,s1,s2);
    }
    
    /**
     * Save on session
     * @param ss
     * @return 
     */
    public SentenceTokenEdge persist( SentenceTokenEdgeService ss )
    {
        return ss.createOrUpdate(this);
    }
}

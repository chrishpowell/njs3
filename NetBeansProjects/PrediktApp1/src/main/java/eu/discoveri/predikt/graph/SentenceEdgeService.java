/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.predikt.graph;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SentenceEdgeService extends GenericService<SentenceEdge>
{
    @Override
    public Class<SentenceEdge> getEntityType() { return SentenceEdge.class; }
}

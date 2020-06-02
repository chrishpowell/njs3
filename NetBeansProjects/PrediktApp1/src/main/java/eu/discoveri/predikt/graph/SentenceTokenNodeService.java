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
public class SentenceTokenNodeService extends GenericService<SentenceTokenNode>
{
    @Override
    public Class<SentenceTokenNode> getEntityType() { return SentenceTokenNode.class; }
}

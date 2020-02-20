/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.languagegraph;

import eu.discoveri.config.Constants;
import eu.discoveri.elements.Sentence;
import java.util.UUID;


/**
 * Sentence node.  A (graph vertex) node for containing the supplied sentence and
 * its rank or score in the graph.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SentenceNode
{
    // Node holds a sentence
    private final Sentence  sentence;
    
    // Node name (graph reference)
    private String          namespace;
    private String          name;
    // Unique ref.
    private UUID            uuid;
    // Scoring for this node
    private double          score, prevScore;

    
    /**
     * Constructor.
     * 
     * @param name Supplied node name (Note: could be duplicate with other nodes)
     * @param sentence Sentence of node
     * @param score Node score/rank initial value (default=0.25)
     * @param namespace Namespace used in UUID generation
     */
    public SentenceNode(String name, Sentence sentence, double score, String namespace)
    {
        this.sentence = sentence;
        this.score = score;
        this.prevScore = score;
        this.namespace = namespace;
        this.name = name;
        this.uuid = UUID.nameUUIDFromBytes((namespace+name).getBytes());
    }

    /**
     * Constructor.
     * 
     * @param name Supplied node name (Note: could be duplicate with other nodes)
     * @param sentence Sentence of node
     * @param score Node score/rank initial value (default=0.25)
     */
    public SentenceNode(String name, Sentence sentence, double score)
    {
        this(name,sentence,score,Constants.GRAPHNAMESPACE);
    }
    
    /**
     * Constructor.  Probably the most used with default values.
     * 
     * @param name Node name
     * @param sentence Sentence of node.
     */
    public SentenceNode(String name, Sentence sentence)
    {
        this(name,sentence,Constants.NODESCOREDEF,Constants.GRAPHNAMESPACE);
    }
    
    /**
     * Simple constructor.  No node name supplied (relies on UUID).
     * 
     * @param sentence 
     */
    public SentenceNode(Sentence sentence)
    {
        this("SNode",sentence,Constants.NODESCOREDEF,Constants.GRAPHNAMESPACE);
    }

    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getPrevScore() { return prevScore; }
    public void setPrevScore(double prevScore) { this.prevScore = prevScore; }

    public Sentence getSentence() { return sentence; }

    public String getNamespace() { return namespace; }

    public String getName() { return name; }

    public UUID getUuid() { return uuid; }
    
    @Override
    public String toString()
    {
        return name+": ("+score+")";
    }
}

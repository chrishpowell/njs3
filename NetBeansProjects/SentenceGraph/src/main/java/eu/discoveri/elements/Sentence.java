/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.elements;

import eu.discoveri.config.Constants;
import eu.discoveri.exceptions.ListLengthsDifferException;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensCountInSentencesIsZeroException;
import eu.discoveri.exceptions.TokensListIsEmptyException;
import eu.discoveri.lemmatizer.SimpleLemmatizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;
import org.neo4j.driver.Query;
import static org.neo4j.driver.Values.parameters;


/**
 * A sentence and its component parts.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Sentence 
{
    // Sentence
    private String                      text = "";
    // Name/id
    private String                      name = "";
    // Node namespace (graph reference)
    private String                      namespace;
    // Unique ref.
    private UUID                        uuid;

    // Scoring for this node
    private double                      score, prevScore;
    // Tokens/lemmas of the sentence
    private List<Token>                 tokens = new ArrayList<>();
    // Spans
    private final List<Span>            spans = new ArrayList<>();
    // Sequences
    private final List<Sequence>        seqs = new ArrayList<>();
    // Sentence probability
    private List<Double>                sprobs = new ArrayList<>();
    // This object attrs mapped to neo4j parameters
    private final Map<String,Object>    params;

    
    /**
     * Constructor.
     * Note: Removes all punctuation, sets a UUID.
     * 
     * @param name Reference name.
     * @param text Sentence text
     * @param initscore Sentence initial score
     * @param namespace To enable 'duplicate' names
     */
    public Sentence( String name, String text, double initscore, String namespace )
    {
        // Ditch punctuation, just characters and whitespace
        for( Character c: text.toCharArray() )
        {
            if(Character.isLetterOrDigit(c) || Character.isWhitespace(c))
                this.text += c;
        }
        
        this.name = name;
        this.score = initscore;
        this.prevScore = initscore;
        this.namespace = namespace;
        this.uuid = UUID.nameUUIDFromBytes((namespace+name).getBytes());
        
        // Map the attributes
        params = Map.of("name",this.name,
                        "namespace",this.namespace,
                        "uuid",this.uuid.toString(),
                        "text",this.text,
                        "score",this.score,
                        "prevScore",this.prevScore );
    }

    /**
     * Constructor.  With default namespace.
     * Note: Removes all punctuation, sets a UUID.
     * 
     * @param name
     * @param text
     * @param score 
     */
    public Sentence(String name, String text, double score)
    {
        this(name,text,score,Constants.GRAPHNAMESPACE);
    }
    
    /**
     * Constructor. With default namespace and initial score.
     * Note: Removes all punctuation, sets a UUID.
     * 
     * @param name Name or Id
     * @param text Sentence text
     */
    public Sentence( String name, String text )
    {
        this(name,text,Constants.NODESCOREDEF,Constants.GRAPHNAMESPACE);
    }


    /**
     * Tokenise the sentence.
     * 
     * @param tme
     * @return
     * @throws SentenceIsEmptyException 
     */
    public List<Token> tokenizeThisSentence( TokenizerME tme )
            throws SentenceIsEmptyException
    {
        // No sentence?
        if( text.isEmpty() )
            throw new SentenceIsEmptyException("tokenizeThisSentence() ");
        
        // Add tokens to (Token) List with probabilites
        Arrays.asList(tme.tokenize(this.text.toLowerCase())).forEach(tok-> {
            // New token
            Token t = new Token(tok);
            
            // Add token to list
            tokens.add(t);
        });
        
        
        return tokens;
    }
    
    /**
     * Get the POS tags (as in PennPOS) for this sentence.
     * 
     * @param pme
     * @return 
     * @throws eu.discoveri.exceptions.TokensListIsEmptyException 
     */
    public List<String> posTagThisSentence( POSTaggerME pme )
            throws TokensListIsEmptyException
    {
        // No tokens?
        if( tokens.isEmpty() )
            throw new TokensListIsEmptyException("No tokens, postagThisSentence()");
        
        // Get list of POStags
        Token[] toks = tokens.toArray(new Token[0]);
        String[] ptags = pme.tag(tokens.stream().map(t -> t.getToken()).toArray(String[]::new));
        
        // Map into Token class
        for( int ii=0; ii<ptags.length; ii++ )
            toks[ii].setPos(ptags[ii]);
        
        // Convert to List
        return Arrays.asList(ptags);
    }
    
    /**
     * Ditch tags and tokens not matching POS tag list (test()).
     * 
     * @return 
     * @throws TokensListIsEmptyException 
     */
    public List<Token> cullPOSTagsNToks()
            throws TokensListIsEmptyException
    {
        // No tokens?
        if( tokens.isEmpty() )
            throw new TokensListIsEmptyException("No tokens, cullPOSTagsNToks()");
    
        for( Iterator<Token> iter = tokens.listIterator(); iter.hasNext(); )
        {
            Token t = iter.next();
            if( !t.test(Void.TYPE) )
            {
                iter.remove();
            }
        }
        
        System.out.println("...> " +this.name+ ": " +this.text);
        System.out.println("...> Tokens: ");
        tokens.forEach(t -> System.out.println("   "+t.getToken()));
        
        return tokens;
    }
    
    /**
     * Tokenise and get POS tags for a sentence.See tokeniseThisSentence and
     * posTagThisSentence.
     * 
     * @param tme
     * @param pme
     * @return
     * @throws SentenceIsEmptyException
     * @throws TokensListIsEmptyException 
     */
    public List<Token> tokPosThisSentence( TokenizerME tme, POSTaggerME pme )
            throws SentenceIsEmptyException, TokensListIsEmptyException
    { // NB: Not thread safe.
        tokens = tokenizeThisSentence( tme );
        posTagThisSentence( pme );
        
        return tokens;
    }

    /**
     * Sequences/outcomes of the sentence.
     * 
     * @param pme
     * @return
     * @throws TokensListIsEmptyException 
     */
    public List<Sequence> addSequences2Sentence( POSTaggerME pme )
            throws TokensListIsEmptyException
    {
        // No tokens?
        if( tokens.isEmpty() )
            throw new TokensListIsEmptyException("No tokens, addSequences2Sentence(): " +this.name);

        // Get tokens into String array
        String[] stoks = new String[tokens.size()];
        int idx = 0;
        for( Token t: tokens )
        {
            stoks[idx++] = t.getToken();
        }
        
        // Get list of sequences/outcomes
        return Arrays.asList(pme.topKSequences(stoks));
    }

    /**
     * Tokenise the sentence into spans (position in sentence).
     * 
     * @param tme
     * @return
     * @throws SentenceIsEmptyException 
     */
    public List<Span> spanThisSentence( TokenizerME tme )
            throws SentenceIsEmptyException
    {
        // No sentence?
        if( text.isEmpty() )
            throw new SentenceIsEmptyException("Empty sentence, spanThisSentence() " +this.name);
        
        // Put spans in List
        spans.addAll(Arrays.asList(tme.tokenizePos(this.text)));
        
        return spans;
    }
    
    /**
     * Add sentence probabilities to Sentence.  Note: must be done after sentDetect.
     * 
     * @param inprobs
     * @return 
     */
    public List<Double> addSentenceProbs( double[] inprobs )
    {
        // Convert to list
        sprobs = DoubleStream.of(inprobs).boxed().collect(Collectors.toList());
        
        return sprobs;
    }
    
    /**
     * Get lemmas of the tokens.
     * 
     * @param sl
     * @param tokens
     * @param match2NN
     * @param posTags
     * @return 
     * @throws TokensListIsEmptyException 
     * @throws ListLengthsDifferException 
     */
    public Map<String,String> lemmatizeThisSentence( SimpleLemmatizer sl, List<Token> tokens, List<String> posTags, boolean match2NN )
            throws TokensListIsEmptyException, ListLengthsDifferException
    {
        // No tokens?
        if( tokens.isEmpty() )
            throw new TokensListIsEmptyException("No tokens, lemmatizeThisSentence() " +this.name);
        
        // Get token strings into List
        List<String> toks = tokens.stream().map(t -> t.getToken()).collect(Collectors.toList());

        /*
         * Form lemmas from <token string,POStag> 'key' into dictionary.
         * Throws exception if List lengths differ
         */
        Map<String,String> lems = sl.lemmatize(toks, posTags, match2NN);
        
        // Update Tokens (class) with lemmas
        tokens.forEach(t -> {
            t.setLemma(lems.get(t.getToken()));
        });

        return lems;
    }
    
    /**
     * Create a new sentence with tokens, ready for persisting.
     * @throws TokensCountInSentencesIsZeroException
     * @TODO: Add probabilities etc.
     * @return Ordered set of Queries
     */
    public Set<Query> buildSentenceQuerySet()
            throws TokensCountInSentencesIsZeroException
    {
        // Has sentence been processed?
        if( this.getTokens().isEmpty() )
            throw new TokensCountInSentencesIsZeroException("No tokens, buildSentenceQuerySet()/process sentence: " +this.name+ "!");
        
        // Ordered Set of Queries (same order out as entered)
        Set<Query> lhs = new LinkedHashSet<>();
        // Map for tokens
        Map<String,Object> ps = new HashMap<>();
        
        // Add first query (create)
        String s = "CREATE ("+params.get("name")+":Sentence) SET "+params.get("name")+"={name:$name,namespace:$namespace,uuid:$uuid,text:$text,score:$score,prevScore:$prevScore}";
        lhs.add( new Query(s,params) );

        // Create token nodes of the sentence
        StringBuilder q0 = new StringBuilder();
        int idx = 0;
        for( Token t: tokens )
        {
            // Include-only adjectives or nouns
            if( t.test(Void.TYPE) )
            {
                q0.append("CREATE (t").append(idx).append(this.name)
                        .append(":Token {text:$text").append(idx)
                        .append(",tid:$tid").append(idx).append("}) ");

                ps.put("text"+idx,t.getToken());                   // Token data
                ps.put("tid"+idx,"t"+idx+this.name);

                // Incr idx
                ++idx;
            }
        }

        // Add the query to the query list
        lhs.add( new Query(q0.toString(),ps) );
        
        // Build edges sentence->tokens
        idx = 0;
        for( Token t: tokens )
        {
            // Build the query
            StringBuilder q1 = new StringBuilder();
            q1.append(" MATCH (src:Sentence {uuid:$uuid}) MATCH (tgt:Token {tid:$tid})")
                    .append(" MERGE (src)-[:TOK]-(tgt)");
            
            // Add the query to the query list
            lhs.add( new Query(q1.toString(),parameters("uuid",params.get("uuid"),"tid","t"+idx+this.name)) );
            
            // Incr idx
            ++idx;
        }

        return lhs;
    }
    
    /**
     * Create a weighted edge between sentences, if not already existing.  There
     * should only be one or zero weighted edges between sentences.
     * @param otherNode
     * @param weight
     * @return 
     */
    public Query addWeightedEdge( Sentence otherNode, double weight )
    {
        if( weight < Constants.EDGEWEIGHTMIN )
            return new Query( "RETURN NULL" );
        else
            return new Query( "MATCH (src:Sentence {name:$srcName}) MATCH (tgt:Sentence {name:$tgtName}) MERGE (src)-[:SIMILARTO {weight:$weight}]-(tgt)",
                          parameters("srcName",name,"tgtName",otherNode.getName(),"weight",weight)  );
    }

    /**
     * Sentences/tokens etc are NOT update-able.  Tokens, spans, edges, weights,
     * clusters etc. need all to be recalculated. 
     */
    public static void update()
    {
        throw new UnsupportedOperationException("Sentence and all sub-objects cannot be updated");
    }

    
    /**
     * Get this sentence text.
     * @return 
     */
    public String getText() { return text; }
    public String getSentence() { return text; }

    /**
     * Get this sentence's tokens.
     * 
     * @return 
     */
    public List<Token> getTokens() { return tokens; }

    /**
     * Get the sentence score (TextRank)
     * @return 
     */
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    /**
     * Stored for iterative scoring.
     * @return 
     */
    public double getPrevScore() { return prevScore; }
    public void setPrevScore(double prevScore) { this.prevScore = prevScore; }

    /**
     * Namespace to allow 'duplicate' names.
     * @return 
     */
    public String getNamespace() { return namespace; }

    /**
     * Generated unique ID.
     * @return 
     */
    public UUID getUuid() { return uuid; }
    
    /**
     * Get spans for this sentence.
     * 
     * @return 
     */
    public List<Span> getSpans() { return spans; }

    /**
     * Get sequences of this sentence
     * @return 
     */
    public List<Sequence> getSeqs() { return seqs; }

    /**
     * Get probabilities of this sentence.
     * @return 
     */
    public List<Double> getProbabilities() { return sprobs; }
    
    /**
     * Get name (same thing).
     * @return 
     */
    public String getName() { return name; }

    /**
     * Output text section.
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return "("+name+") "+text;
    }
}

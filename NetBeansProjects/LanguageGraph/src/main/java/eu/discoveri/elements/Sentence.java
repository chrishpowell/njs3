/*
 */
package eu.discoveri.elements;

import eu.discoveri.exceptions.ListLengthsDifferException;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensListIsEmptyException;
import eu.discoveri.lemmatizer.SimpleLemmatizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;


/**
 * A sentence and its component parts.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Sentence 
{
    // Sentence
    private String                  text = "";
    // Tokens/lemmas of the sentence
    private List<Token>             tokens = new ArrayList<>();
    // Spans
    private final List<Span>        spans = new ArrayList<>();
    // Sequences
    private final List<Sequence>    seqs = new ArrayList<>();
    // Sentence probability
    private List<Double>            sprobs = new ArrayList<>();

    
    /**
     * Constructor.  Removes all punctuation etc.
     * @param text
     */
    public Sentence( String text )
    {
        // Ditch punctuation, just characters and whitespace
        for( Character c: text.toCharArray() )
        {
            if(Character.isLetterOrDigit(c) || Character.isWhitespace(c))
                this.text += c;
        }
    }


    /**
     * Tokenise the sentence into tokens.
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
            throw new SentenceIsEmptyException();
        
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
            throw new TokensListIsEmptyException();
        
        // Get list of POStags
        Token[] toks = tokens.toArray(new Token[0]);
        String[] pts = pme.tag(tokens.stream().map(t -> t.getToken()).toArray(String[]::new));
        
        // Map into Token class
        for( int ii=0; ii<pts.length; ii++ ) { toks[ii].setPos(pts[ii]); }

        // Convert to List
        return Arrays.asList(pts);
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
            throw new TokensListIsEmptyException();

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
            throw new SentenceIsEmptyException();
        
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
            throw new TokensListIsEmptyException();
        
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
     * Get this sentence text.
     * @return 
     */
    public String getText() { return text; }

    /**
     * Get this sentence's tokens.
     * 
     * @return 
     */
    public List<Token> getTokens() { return tokens; }

    /**
     * Get spans for this sentence.
     * 
     * @return 
     */
    public List<Span> getSpans() { return spans; }

    /**
     * Output text section.
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return text;
    }
}

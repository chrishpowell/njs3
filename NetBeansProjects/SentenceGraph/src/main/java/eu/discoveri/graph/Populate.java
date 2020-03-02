/*
 */
package eu.discoveri.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import eu.discoveri.config.Constants;
import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;
import eu.discoveri.lemmatizer.SimpleLemmatizer;

import eu.discoveri.exceptions.ListLengthsDifferException;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensListIsEmptyException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Sequence;


/**
 * Get text to populate Graph nodes.
 * Note: Dictionaries are structured as follows:
 *  token\tab\POStag\tab\lemma
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Populate
{
    // OpenNLP functions
    private final SentenceDetectorME    sdme;
    private final TokenizerME           tme;
    private final POSTaggerME           pme;
    private final SimpleLemmatizer      sl;
    
    
    /**
     * Constructor.Instantiate all NLP Max.Entropy (ME) processes.
     * 
     * This is a heavy constructor, call once only. Note: Model files are found
     * in class Language EnumMap.
     * @param langCode Which language is being processed.
     * @throws FileNotFoundException
     */
    public Populate( LangCode langCode )
            throws FileNotFoundException, IOException
    {
        // Sentence model, model file
        SentenceModel sm = new SentenceModel(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(langCode).get(Constants.SENTMODEL)));
        // Feed model to detector
        sdme = new SentenceDetectorME(sm);
        
        // Tokenizer model, model file
        TokenizerModel tm = new TokenizerModel(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(langCode).get(Constants.TOKENMODEL)));
        // Feed model to Max. Entropy tokenizer
        tme = new TokenizerME(tm);
        
        // POS Tagger model, model file
        POSModel pm = new POSModel(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(langCode).get(Constants.SEQMODEL)));
        // Feed model to Max. Entropy tokenizer
        pme = new POSTaggerME(pm);
        
        // Discoveri version of dictionary lemmatizer
        sl = new SimpleLemmatizer(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(langCode).get(Constants.LEMMATIZE)));
    }

    /**
     * Extract list of sentences from doc.  Get Sentence probability at same time.
     * 
     * @param doc
     * @return 
     */
    public List<Sentence> extractSentences( String doc )
    {
        List<Sentence> sents = new ArrayList<>();
        
        // Extract sentences (creating a unique name)
        int idx = 0;
        for( String sent: sdme.sentDetect(doc) )
        {
            // Create a roughly unique name
            String uname = "S"+idx+"T"+System.currentTimeMillis();
            
            // Add sentence text to new Sentence
            Sentence s = new Sentence(uname,sent);
            
            // Add sentence probability
            s.addSentenceProbs( sdme.getSentenceProbabilities() );
            
            // Add to overall list of sentences
            sents.add(s);
            
            // 'Increment' sentence
            ++idx;
        }
        
        return sents;
    }
    
    /**
     * Extract tokens from sentence.
     * 
     * @param s sentence
     * @return
     * @throws SentenceIsEmptyException 
     */
    public List<Token> extractTokens( Sentence s )
            throws SentenceIsEmptyException
    {
        return s.tokenizeThisSentence(tme);
    }
    
    /**
     * Get POS tags of a sentence.
     * 
     * @param s
     * @return
     * @throws TokensListIsEmptyException 
     */
    public List<String> posTags( Sentence s )
            throws TokensListIsEmptyException
    {
        return s.posTagThisSentence(pme);
    }
    
    /**
     * Tokens and tags of the sentence.
     * 
     * @param s
     * @return
     * @throws SentenceIsEmptyException
     * @throws TokensListIsEmptyException 
     */
    public List<Token> tokensAndTags( Sentence s )
            throws SentenceIsEmptyException, TokensListIsEmptyException
    {
        return s.tokPosThisSentence(tme, pme);
    }
    
    /**
     * Sequences of the sentence.
     * 
     * @param s
     * @return 
     * @throws eu.discoveri.exceptions.TokensListIsEmptyException 
     */
    public List<Sequence> sequenceTokens( Sentence s )
            throws TokensListIsEmptyException
    {
        return s.addSequences2Sentence(pme);
    }
    
    /**
     * Get the lemmas of tokens of the sentence (via SimpleLemmatizer).
     * 
     * @param s
     * @param tokens
     * @param posTags
     * @param match2NN
     * @return
     * @throws TokensListIsEmptyException 
     * @throws ListLengthsDifferException 
     */
    public Map<String,String> lemmasOfSentence( Sentence s, List<Token> tokens, List<String> posTags, boolean match2NN )
            throws TokensListIsEmptyException, ListLengthsDifferException
    {
        return s.lemmatizeThisSentence(sl, tokens, posTags, match2NN);
    }

    
    public SentenceDetectorME getSdme() { return sdme; }

    public TokenizerME getTme() { return tme; }

    public POSTaggerME getPme() { return pme; }

    public SimpleLemmatizer getSl() { return sl; }
}

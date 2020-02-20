/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.snippets;

import eu.discoveri.config.Constants;
import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.languagegraph.LangCode;
import eu.discoveri.languagegraph.Language;
import eu.discoveri.languagegraph.SentenceNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


/**
 * Similarity between sentences check.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SimilarSent
{
    private static final List<SentenceNode> sents1 = Arrays.asList(
        new SentenceNode("S1",new Sentence("American inventor Philo T. Farnsworth, a pioneer of television, was accorded what many believe was long overdue glory Wednesday when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol.")),
        new SentenceNode("S2",new Sentence("American inventor Philo T. Farnsworth, a pioneer of television, was honored when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol.")),
        new SentenceNode("S3",new Sentence("With his 81-year-old widow, Elma Farnsworth, looking on, the inventor was extolled as the father of television and his statue was placed in the pantheon of famous Americans of the Capitol’s National Statuary Hall")),
        new SentenceNode("S4",new Sentence("The clear favorite was one Philo T. Farnsworth, an inventor who is considered the father of television")),
        new SentenceNode("S5",new Sentence("If Utahans have their way, Philo T. Farnsworth will become a household name")),
        new SentenceNode("S6",new Sentence("The crew worked for more than two hours to separate the 8.5-foot bronze likeness of the city’s fictitious boxer from the steps of the Philadelphia Museum of Art, which has repeatedly insisted it doesn’t want the statue."))  );
    private static final List<SentenceNode> sents = Arrays.asList(
            new SentenceNode("S1",new Sentence("One two three one four one five.")),
            new SentenceNode("S2",new Sentence("Eight nine one atheist.")),
            new SentenceNode("S3",new Sentence("Nine bee sea deaf elephant three three three."))
    );
    // List of common words per sentence
    private static Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Map<String,CountQR>> commonWords = new HashMap<>();
    // Similarity score
    private static Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Integer> similarityScore = new HashMap<>();
    
    
    /**
     * Similarity calc.
     */
    private static void counting(TokenizerME tme)
            throws SentenceIsEmptyException
    {
        Map<String,CountQR> wCountQR;
        
        /*
         * Sort sentences bytokens count (long to short)
         * Allows sentence matching to work efficiently
         */
        // Tokenize all sentences (stored in Sentence)
        for( SentenceNode sn: sents )
            sn.getSentence().tokenizeThisSentence(tme);
        
        // Sort sentences by size (largest first hence s2 - s1)
        List<SentenceNode> nodeList = sents.stream()
                        .sorted((s1,s2) -> s2.getSentence().getTokens().size()-s1.getSentence().getTokens().size())
                        .collect(Collectors.toList());
        
        /*
         * Count common words per sentence pair (Q,R).
         * [Num sentence pairs = (sents.size*sents.size-1)/2.]
         */
        // For each sentence (Q) 'Source' sentence
        for( SentenceNode nodeQ: nodeList )
        {
            // List of common words between two sentences
            wCountQR = new HashMap<>();
            // Count of common words in each sentence of pair
            CountQR cqr; 
            
            // 'Name of this Sentence(Node)
            String nameQ = nodeQ.getName();
            
            // Get words/tokens of sentenceQ
            List<Token> wordsQ = nodeQ.getSentence().getTokens();

            // For each sentence (R) 'Target' sentence
            for( SentenceNode nodeR: nodeList )
            {
                String nameR = nodeR.getName();
                if( nameR.equals(nameQ) ) continue;                             // Not on same sentence
                
                                System.out.println("Q: "+nameQ+", R: "+nameR);

                // Get words/tokens of sentenceR
                List<Token> wordsR = nodeR.getSentence().getTokens();

                /*
                 * Now compare tokens of sentences Q:R
                 */
                // For each token in sentenceQ
                for( Token tQ: wordsQ )
                {
                    String wordQ = tQ.getToken();
                    if( wCountQR.containsKey(wordQ) )   // Is this token common between Q&R?
                    { // Yes
                        cqr = wCountQR.get(wordQ);      // Get current count for this token
                        int count = cqr.getQ();         // Count in R
                        cqr.setQ(++count);              // Increment
                        wCountQR.replace(wordQ, cqr);   // Update
                    }

                    // Match Q token against each token in target sentenceR
                    for( Token tR: wordsR )
                    {
                        String wordR = tR.getToken();
                        if( wordR.equals(wordQ) )                               // Words match between sentences?
                        { //Yes
                            // First match for token in both Q&R?
                            if( !wCountQR.containsKey(wordQ) )                  // Create Map entry
                            {//Yes
                                wCountQR.put(wordQ, new CountQR(nameQ,1,nameR,1));  // Init count of both sentences
                            }
                            else
                            {//No
                                // Ok, get the counts for this token
                                cqr = wCountQR.get(wordR);
                                int count = cqr.getR();         // Count for R sentence
                                cqr.setR(++count);              // Increment R count
                                wCountQR.replace(wordR, cqr);   // Update
                            }
                        }
                    }
                }
                
            // Ok, store the common words for the sentence pairs
            commonWords.put(new AbstractMap.SimpleEntry(nodeQ,nodeR), wCountQR);
            }
        }
        
        commonWords.forEach((k,v) -> {
            System.out.println("Sent: "+k.getKey().getName()+"/"+k.getValue().getName());
            v.forEach((s,c) -> {
                System.out.println("   word: " +s+"> "+c.getqName()+"(Q count): " +c.getQ()+" / "+c.getrName()+"(R count): " +c.getR());
            });
        });
    }
    
    public static void similarity()
    {
        
    }
    
    public static void main(String[] args)
           throws IOException, FileNotFoundException, SentenceIsEmptyException
    {
        // Tokenizer model, model file
        TokenizerModel tm = new TokenizerModel(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(LangCode.en).get(Constants.TOKENMODEL)));
        // Feed model to Max. Entropy tokenizer
        TokenizerME tme = new TokenizerME(tm);
        
        counting(tme);
        similarity();
    }
}

class CountQR
{
    private int     q = 0, r = 0;
    private String  qName, rName;

    public CountQR( String qName, int q, String rName, int r )
    {
        this.q = q;
        this.r = r;
        this.qName = qName;
        this.rName = rName;
    }

    public int getQ() { return q; }
    public void setQ(int q) { this.q = q; }

    public int getR() { return r; }
    public void setR(int r) { this.r = r; }

    public String getqName() { return qName; }

    public String getrName() { return rName; }
}

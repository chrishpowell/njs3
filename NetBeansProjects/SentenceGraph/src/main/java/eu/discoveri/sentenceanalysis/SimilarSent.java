/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.sentenceanalysis;

import eu.discoveri.config.Constants;
import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;

import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensCountInSentencesIsZeroException;

import eu.discoveri.graph.LangCode;
import eu.discoveri.graph.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


/**
 * Similarity between sentences check.
 * See Metzler et al, "Similarity Measures for Tracking Information Flow" and
 * Allan et al. "Retrieval and Novelty Detection at the Sentence Level"
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SimilarSent
{
    // Map of common words per sentence pair:<String,CountQR> (Word/token + counts)
    private static Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Map<String,CountQR>> commonWords = new HashMap<>();
    // Sentence similarity score
    private static Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Double> QRscore = new HashMap<>();
    // Similarity score
    private static double   score = 0.d;
    
    // Number sentences in which each word appears.  The mapping allows multikey, eg: A:B, A:C etc.
    private static Map<String,Map<String,Nul>>  tokSentCount = new HashMap<>();
    
    // Prevent sentence process duplication, ie: A:B obviates B:A.  The mapping allows multikey, eg: A:B, A:C etc.
    private static Map<String,Map<String,Nul>>  noDups = new HashMap<>();
    private static Nul      nul = new Nul();
    
    // Sentences to process
    private List<Sentence>  sents;


    /**
     * Constructor.
     * 
     * @param sents 
     */
    public SimilarSent( List<Sentence> sents )
    {
        this.sents = sents;
    }
    
    /**
     * Same word/token counting per sentence pair.
     * 
     * @param tme
     * @throws SentenceIsEmptyException 
     */
    public void counting(TokenizerME tme)
            throws SentenceIsEmptyException
    {
        /*
         * Sort sentences by tokens count (long to short)
         * Allows sentence matching to work efficiently
         */
        // Tokenize all sentences (stored in Sentence)
        for( Sentence sn: sents )
            { sn.tokenizeThisSentence(tme); }
        
        // Sort sentences by size (largest first hence s2 - s1)
        List<Sentence> nodeList = sents.stream()
                        .sorted((s1,s2) -> s2.getTokens().size()-s1.getTokens().size())
                        .collect(Collectors.toList());
        
        /*
         * Count common words per sentence pair (Q,R).
         * [Num sentence pairs = (sents.size*sents.size-1)/2.]
         */
        // ---> For each sentence (Q) 'Source' sentence
        for( Sentence nodeQ: nodeList )
        {
            // Count of common words in each sentence of pair
            CountQR cqr; 
            
            // 'Name of source Sentence(Node) Q
            String nameQ = nodeQ.getName();
            
            // Get words/tokens of sentenceQ (of NodeQ)
            List<Token> wordsQ = nodeQ.getTokens();

            // ---> For each sentence (R) 'Target' sentence
            for( Sentence nodeR: nodeList )
            {
                // Name of target Sentence(Node) R
                String nameR = nodeR.getName();
                
                /*
                 * Do not process sentences if...
                 */
                // ...same sentence
                if( nameR.equals(nameQ) ) continue;
                
                // ...pair already processed. That is, processing B:A but A:B done already
                if( !noDups.containsKey(nameQ) )
                {
                    if( !noDups.containsKey(nameR) )
                    {
                        // Start this sentence pair Q:R
                        Map<String,Nul> nulMap = new HashMap<>();
                        nulMap.put(nameR,nul); 
                        noDups.put(nameQ,nulMap);
                    }
                    else
                        // Key already exists, in future process as else below
                        continue;
                }
                else
                    if( !noDups.get(nameQ).containsKey(nameR) )
                    {
                        noDups.get(nameQ).put(nameR, nul);
                    }

                /*
                 * Ok, got two candidate sentences.  Do matching token counting.
                 */
                // Get words/tokens of sentenceR (of NodeR)
                List<Token> wordsR = nodeR.getTokens();

                /*
                 * Now compare tokens of sentences Q:R
                 */
                // Map of count of common words between two sentences <Word,count of Word per sentence pair>
                Map<String,CountQR> wCountQR = new HashMap<>();
                
                // For each token in sentenceQ (Note: Q token count >= R token count)
                for( Token tQ: wordsQ )
                {
                    String wordQ = tQ.getToken();

                    if( wCountQR.containsKey(wordQ) )   // Is this token common between Q&R?
                    { // Yes
                        cqr = wCountQR.get(wordQ);      // Get current count for this token
                        int count = cqr.getQ();         // Count in Q
                        cqr.setQ(++count);              // Increment
                        wCountQR.replace(wordQ, cqr);   // Update
                        
                        // Already counted all of R (where token key is created below), so skip R
                        continue;
                    }

                    // Match Q token against each token in target sentenceR
                    for( Token tR: wordsR )
                    {
                        String wordR = tR.getToken();
                        if( wordR.equals(wordQ) )                                   // Words match between sentences?
                        { //Yes
                            // First match for token in both Q&R?
                            if( !wCountQR.containsKey(wordQ) )                      // Create Map entry
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

        /*
         * Number sentences in which token appears (from commonWords map)
         */
        commonWords.forEach((k,v) -> {
            String pSrc = k.getKey().getName();
            String pTgt = k.getValue().getName();
            
            // For common words build key:subkey (Sentence:Word)
            v.forEach((s,c) -> {
                // Word/token appeared before?
                if( !tokSentCount.containsKey(s) )
                {// No
                    Map<String,Nul> nulMap = new HashMap<>();
                    nulMap.put(pSrc,nul);
                    nulMap.put(pTgt, nul);
                    tokSentCount.put(s, nulMap);
                }
                else
                {// Yes
                    if( !tokSentCount.get(s).containsKey(pSrc) )
                    {
                        tokSentCount.get(s).put(pSrc, nul);
                    }
                    if( !tokSentCount.get(s).containsKey(pTgt) )
                    {
                        tokSentCount.get(s).put(pTgt, nul);
                    }
                }
            });
        });
    }
    
    /**
     * Similarity scores.  Determines topically similar sentences (pairs) and is
     * used as weight of connecting edge between pairs in graph.
     * 
     * IDF-weighted word overlap. For algorithm, see Allan et al.
     *   "Retrieval and Novelty Detection at the Sentence Level" and
     * Metzler et al, "Similarity Measures for Tracking Information Flow"
     */
    public void similarity()
            throws TokensCountInSentencesIsZeroException
    {
        // This method requires a token count
        if( tokSentCount.isEmpty() )
            throw new TokensCountInSentencesIsZeroException();
                    
        // Get scores into QRscore.
        commonWords.forEach((k,v) -> {
            // Init score
            score = 0.d;
            
            // (Word/token + counts)
            v.forEach((w,c) -> {
                // Number of times token/word appears across all sentences
                double sft = tokSentCount.get(w).values().stream().count();
                // Similarity score for this pair (k: <SentenceNode,SentenceNodeREDUNDANT>
                score += Math.log(c.getQ()+1.d)*Math.log(c.getR()+1.d)*Math.log((sents.size()+1.d)/(sft+0.5d));
            });
            
            // Store similarity score for each sentence pair
            QRscore.put(k, score);
        });
    }
    
    /**
     * Get list of sentences (nodes).
     * 
     * @return 
     */
    public List<Sentence> getSentences()
    {
        return sents;
    }
    
    /**
     * Dump common words map
     */
    public static void dumpCommonWords()
    {
        // <<Sentence Pair>,<Word,Count>>
        commonWords.forEach((k,v) -> {
            // Sentence pair
            String pSrc = k.getKey().getName();                 // Source sentence
            String pTgt = k.getValue().getName();               // Target sentence

            System.out.println("Pair: "+pSrc+"/"+pTgt);
            // Common word/token count
            v.forEach((s,c) -> {
                System.out.println("   word: " +s+"> "+c.getqName()+"(Q count): " +c.getQ()+" / "+c.getrName()+"(R count): " +c.getR());
                });
        });
    }
    
    /**
     * Dump the sentences
     */
    public void dumpSentences()
    {
        sents.forEach(s -> System.out.println("Key: " +s.getNamespace()+":"+s.getName()+ ", Sentence: " +s.getSentence() ));
    }
    
    /**
     * Dump sentence names and count by token/word
     */
    public static void dumpTokSentCount()
    {
        // Number sentences in which token appears
        tokSentCount.forEach((k,v) -> {
            System.out.println("...> Word: " +k+ ", Count sents: " +v.values().stream().count());
            System.out.print("     Sentences: ");
            v.keySet().stream().sorted().forEach(k1 -> System.out.print(k1+" "));
            System.out.println("");
        });
    }
    
    /**
     * Show similarity score between sentences (QR).
     * @param full true for full dump
     */
    public static void dumpQRscore( boolean full )
    {
        if( !full )
        {
            QRscore.forEach((k,v) -> {
                System.out.println("Pair: " +k.getKey().getName()+"/"+k.getValue().getName() +", Score: " +v);
            });
        }
        else
            QRscore.forEach((k,v) -> {
                System.out.format("Pair> Q:(" +k.getKey().getName()+")/R:("+k.getValue().getName() +"), SimScore: %9.5f%n",v);
                System.out.println("   Sentence Q: " +k.getKey().getSentence());
                System.out.println("   Sentence R: " +k.getValue().getSentence());
                System.out.println("");
            });
    }

    
    /**
     * M A I N
     * =======
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SentenceIsEmptyException 
     * @throws TokensCountInSentencesIsZeroException 
     */
    public static void main(String[] args)
           throws IOException, FileNotFoundException, SentenceIsEmptyException, TokensCountInSentencesIsZeroException
    {
            // Test sentences
        List<Sentence> sents1 = Arrays.asList(
            new Sentence("S1","American inventor Philo T. Farnsworth, a pioneer of television, was accorded what many believe was long overdue glory Wednesday when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol."),
            new Sentence("S2","American inventor Philo T. Farnsworth, a pioneer of television, was honored when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol."),
            new Sentence("S3","With his 81-year-old widow, Elma Farnsworth, looking on, the inventor was extolled as the father of television and his statue was placed in the pantheon of famous Americans of the Capitol’s National Statuary Hall"),
            new Sentence("S4","The clear favorite was one Philo T. Farnsworth, an inventor who is considered the father of television"),
            new Sentence("S5","If Utahans have their way, Philo T. Farnsworth will become a household name"),
            new Sentence("S6","The crew worked for more than two hours to separate the 8.5-foot bronze likeness of the city’s fictitious boxer from the steps of the Philadelphia Museum of Art, which has repeatedly insisted it doesn’t want the statue.") );
        List<Sentence> sents = Arrays.asList(
                new Sentence("S1","One two three one three five five four one argle five."),
                new Sentence("S2","Eight nine one atheist."),
                new Sentence("S3","Nine nine three bee sea deaf elephant five three three argle bargle atheist three."),
                new Sentence("S4","Eight nine one the contrarian."),
                new Sentence("S5","One two three four five six sevsn eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty thity forty fifty sixty seventy eighty ninety hundred thousand million billion trillion numbers in this sentence."),
                new Sentence("S6","One two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty thity forty fifty sixty seventy eighty ninety hundred thousand million billion trillion numbers in this sentence."),
                new Sentence("S7","Push towards the cliff edge."),
                new Sentence("S8","Push towards the cliff edge.")
        );

        // Tokenizer model, model file
        TokenizerModel tm = new TokenizerModel(new FileInputStream(Constants.RESMODELS+Language.getLangModels().get(LangCode.en).get(Constants.TOKENMODEL)));
        // Feed model to Max. Entropy tokenizer
        TokenizerME tme = new TokenizerME(tm);
        
        SimilarSent ss = new SimilarSent( sents );
        
        // Counting and arrangements of tokens/words
        ss.counting(tme);
        // Calculate sentence similarities
        ss.similarity();
                
//        System.out.println("----------------------------------");
//        dumpCommonWords();
//        dumpTokSentCount();
//        dumpSentences();
        dumpQRscore(true);
    }
}

/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.graph;

import eu.discoveri.exceptions.EmptySentenceListException;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensCountInSentencesIsZeroException;
import eu.discoveri.exceptions.TokensListIsEmptyException;

import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;
import eu.discoveri.exceptions.ListLengthsDifferException;
import eu.discoveri.exceptions.POSTagsListIsEmptyException;
import eu.discoveri.lemmatizer.Lemmatizer;
import eu.discoveri.sentenceanalysis.CountQR;
import eu.discoveri.sentenceanalysis.Nul;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GraphAlgos
{
    // Map of common words per sentence pair:<String,CountQR> (Word/token + counts)
    private static Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Map<String,CountQR>>  commonWords = new HashMap<>();
    // Sentence similarity score
    private static Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Double>               QRscore = new HashMap<>();
    // Similarity score
    private static double                       score = 0.d;
    // Converged? flag
    private static double                       totConverge = Double.MAX_VALUE;
    
    // Number sentences in which each word appears.  The mapping allows multikey, eg: A:B, A:C etc.
    private static Map<String,Map<String,Nul>>  tokSentCount = new HashMap<>();
    
    // Prevent sentence process duplication, ie: A:B obviates B:A.  The mapping allows multikey, eg: A:B, A:C etc.
    private static Map<String,Map<String,Nul>>  noDups = new HashMap<>();
    private static Nul                          nul = new Nul();
    
    // Sentences to analyze
    private final List<Sentence>                sents;
    
    
    /**
     * Constructor.
     * @param sents 
     */
    public GraphAlgos( List<Sentence> sents )
    {
        this.sents = sents;
    }
    
    /**
     * Get POS for each token.
     * 
     * @param pme POSTagger
     * @return
     * @throws EmptySentenceListException
     * @throws TokensListIsEmptyException 
     */
    public List<Sentence> posTagSentenceCorpus(POSTaggerME pme)
            throws EmptySentenceListException, TokensListIsEmptyException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:tokenizeSentenceCorpus()");
        
        for( Sentence s: sents )
            s.posTagThisSentence(pme);
        
        return sents;
    }
    
    /**
     * Tokenize all sentences.
     * 
     * @param tme Tokenizer
     * @return
     * @throws EmptySentenceListException
     * @throws SentenceIsEmptyException 
     */
    public List<Sentence> tokenizeSentenceCorpus(TokenizerME tme)
            throws EmptySentenceListException, SentenceIsEmptyException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:tokenizeSentenceCorpus()");
        
        for( Sentence s: sents )
            s.tokenizeThisSentence(tme);
        
        return sents;
    }
    
    /**
     * Lemmatize sentence corpus.
     * @param lemmer
     * @param match2NN
     * @return
     * @throws EmptySentenceListException
     * @throws SentenceIsEmptyException
     * @throws TokensListIsEmptyException
     * @throws ListLengthsDifferException
     * @throws POSTagsListIsEmptyException 
     */
    public List<Sentence> lemmatizeSentenceCorpus(Lemmatizer lemmer, boolean match2NN)
             throws EmptySentenceListException, SentenceIsEmptyException, TokensListIsEmptyException, ListLengthsDifferException, POSTagsListIsEmptyException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:lemmatizeSentenceCorpus");
        
        for( Sentence s: sents )
            s.lemmatizeThisSentence(lemmer,match2NN);
        
        return sents;
    }
    
    /**
     * Lemmatize sentences using default lemmatizer in Populate.
     * 
     * @param popl
     * @param match2NN
     * @return
     * @throws EmptySentenceListException
     * @throws SentenceIsEmptyException
     * @throws TokensListIsEmptyException
     * @throws ListLengthsDifferException
     * @throws POSTagsListIsEmptyException 
     */
    public List<Sentence> lemmatizeSentenceCorpus(Populate popl,boolean match2NN)
            throws EmptySentenceListException, SentenceIsEmptyException, TokensListIsEmptyException, ListLengthsDifferException, POSTagsListIsEmptyException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:lemmatizeSentenceCorpus");
        
        for( Sentence s: sents )
        {
            popl.lemmasOfSentence(s,match2NN);
        }
        
        return sents;
    }
    
    /**
     * Remove tokens that do not match reqd. POS tags.
     * 
     * @return
     * @throws EmptySentenceListException
     * @throws TokensListIsEmptyException 
     */
    public List<Sentence> filterPOStags()
            throws EmptySentenceListException, TokensListIsEmptyException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:filterPOStags()");
        
        for( Sentence s: sents )
            s.cullPOSTagsNToks();
        
        return sents;
    }
    
    /**
     * Same word/token counting per sentence pair.
     * 
     * @return 
     * @throws EmptySentenceListException 
     */
    public List<Sentence> countingTokens()
            throws EmptySentenceListException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:countingTokens()");
        
        /*
         * Sort sentences by tokens' count (long to short hence s2 - s1)
         * Allows sentence matching to work efficiently/properly
         */
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
                    String wordQ = tQ.getToken();       // *MATCH* this token

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
                        String wordR = tR.getToken();   // *MATCH* this token
                        
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
            String pSrc = k.getKey().getName();             // tuple 1: src
            String pTgt = k.getValue().getName();           // tuple 2: tgt
            
            // For common words build key:subkey (Sentence:Word)
            v.forEach((s,c) -> {                            // Map words-count
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
        
        return sents;
    }
    
    /**
     * Same word/lemma counting per sentence pair.
     * 
     * @return 
     * @throws EmptySentenceListException 
     */
    public List<Sentence> countingLemmas()
            throws EmptySentenceListException
    {
        // Check we have sentences
        if( sents.isEmpty() )
            throw new EmptySentenceListException("Need sentences to process! GraphAlgos:countingLemmas()");
        
        /*
         * Sort sentences by tokens' count (long to short hence s2 - s1)
         * Allows sentence matching to work efficiently/properly
         */
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
                
                // For each lemma in sentenceQ (Note: Q lemma count >= R lemma count)
                for( Token tQ: wordsQ )
                {
                    String wordQ = tQ.getLemma();       // *MATCH* this lemma

                    if( wCountQR.containsKey(wordQ) )   // Is this lemma common between Q&R?
                    { // Yes
                        cqr = wCountQR.get(wordQ);      // Get current count for this token
                        int count = cqr.getQ();         // Count in Q
                        cqr.setQ(++count);              // Increment
                        wCountQR.replace(wordQ, cqr);   // Update
                        
                        // Already counted all of R (where token key is created below), so skip R
                        continue;
                    }

                    // Match Q token against each lemma in target sentenceR
                    for( Token tR: wordsR )
                    {
                        String wordR = tR.getLemma();   // *MATCH* this lemma
                        
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
         * Number sentences in which lemma appears (from commonWords map)
         */
        commonWords.forEach((k,v) -> {
            String pSrc = k.getKey().getName();             // tuple 1: src
            String pTgt = k.getValue().getName();           // tuple 2: tgt
            
            // For common words build key:subkey (Sentence:Word)
            v.forEach((s,c) -> {                            // Map words-count
                // Word/lemma appeared before?
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
        
        return sents;
    }
    
    /**
     * Similarity scores.Determines topically similar sentences (pairs) and is
     * used as the weight of connecting edge between pairs in graph.
     * IDF-weighted word overlap. For algorithm, see Allan et al.
     * "Retrieval and Novelty Detection at the Sentence Level" and
     * Metzler et al, "Similarity Measures for Tracking Information Flow"
     * 
     * @throws eu.discoveri.exceptions.TokensCountInSentencesIsZeroException
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
                score += Math.log(c.getQ()+1.d)*Math.log(c.getR()+1.d)*Math.log((getSentences().size()+1.d)/(sft+0.5d));
            });
            
            // Store similarity score for each sentence pair
            QRscore.put(k, score);
        });
    }
    
    /**
     * TextRank.
     * See papers by Mihelacea et al on TextRank for algorithm.  Plus book:
     * "Graph based Natural Language Processing and Information Retrieval".
     * 
     */
    // Cypher idea, incoming, outgoing
//    String inout = "MATCH (s:Sentence {name:'S1'}) WITH size((s)-[:HAS]->()) as out, size((s)<-[:HAS]-()) as in, s  RETURN s, i, out (out-in) as diff ORDER BY diff DESC";
//
//    public static void textRank()
//    {
//        // Init nodes
//        g.vertexSet().forEach((n) -> {
//            System.out.format(n.getName()+": %6f ",n.getScore());
//        });
//        System.out.println("");
//        
//        // Score
//        for( int ii= 0; ii<Constants.NUMITERS; ii++ )
//        {
//            for( TestNode n: g.vertexSet() )                                    // For all vertices V(i)  [TestNode === SentenceNodeREDUNDANT/Sentence]
//            {
//                double rankVjTot = 0.d;
//
//                // Incoming edges of Vi
//                Set<DefaultWeightedEdge> edges = g.incomingEdgesOf(n);
//
//                // Sum over Vjs of Inc edges of Vi
//                for( DefaultWeightedEdge dwe: edges )
//                {
//                    //System.out.println("  > Remote vertex: " +g.getEdgeSource(dwe)+ ", Weight: " +g.getEdgeWeight(dwe));
//                    TestNode nJ = g.getEdgeSource(dwe);                         // vJ
//
//                    double edgeWeightJI = g.getEdgeWeight(dwe);                 // Wji
//                    Set<DefaultWeightedEdge> eJ = g.outgoingEdgesOf(nJ);
//
//                    // Sum of weights of Outgoing edges of Vj
//                    double sumOfVjOutEdgeWeightsJK = eJ.stream().mapToDouble((dweJ) -> g.getEdgeWeight(dweJ)).sum();
//
//                    // WS(Vj): nJ.getScore(); Wji: edgeWeightJI; Sum Out Wjk: sumOfVjOutEdgeWeightsJK
////                    System.out.println(""+n.getName()+": Wji="+edgeWeightJI+", Vj="+nJ.getName()+", WS(Vj)="+nJ.getScore()+", sumWjk="+sumOfVjOutEdgeWeightsJK);
//                    rankVjTot += edgeWeightJI * nJ.getScore() / sumOfVjOutEdgeWeightsJK;
//                }
//                
//                // Update previous score with current score
//                n.setPrevScore(n.getScore());
//                // WS(Vi) update
//                n.setScore(rankVjTot*Constants.TEXTRANK_DAMPING_FACTOR +(1 - Constants.TEXTRANK_DAMPING_FACTOR)/ltns.size());
//            }
//            
//            // Show converging
//            totConverge = 0.d;
//            g.vertexSet().forEach((n) -> {
//                System.out.format(n.getName()+": %6f ",n.getScore());
//                totConverge += n.getPrevScore()-n.getScore();
//            });
//            System.out.println("");
//            
//            // Converged
//            if( totConverge*g.vertexSet().size() <= Constants.SCORECONVERGE )
//                break;
//        }
//    }

    
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
     * Return QR sentence pair scores.
     * 
     * @return 
     */
    public Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Double> getQRscores()
    {
        return QRscore;
    }
    
    /**
     * Dump common words map
     */
    public static void dumpCommonWords()
    {
        System.out.println("Tot. common words: " +commonWords.size());
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
    public void dumpTokSentCount()
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
    public void dumpQRscore( boolean full )
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
}

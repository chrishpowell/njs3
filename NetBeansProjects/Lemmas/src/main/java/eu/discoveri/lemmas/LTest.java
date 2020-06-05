/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

import eu.discoveri.lemmas.db.LemmaDbBuild;
import eu.discoveri.louvaincluster.Clusters;
import eu.discoveri.predikt.config.Constants;
import eu.discoveri.predikt.config.EnSetup;
import eu.discoveri.predikt.graph.Corpi;
import eu.discoveri.predikt.graph.DiscoveriSessionFactory;
import eu.discoveri.predikt.graph.Populate;
import eu.discoveri.predikt.graph.SentenceEdge;
import eu.discoveri.predikt.graph.SentenceEdgeService;
import eu.discoveri.predikt.graph.SentenceNode;
import eu.discoveri.predikt.graph.SentenceNodeService;
import eu.discoveri.predikt.graph.Vertex;
import eu.discoveri.predikt.sentences.CorpusProcess;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.session.Session;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LTest
{
    static long idx = -1;
    static int  eIdx = -1;
    
    public static void main(String[] args)
            throws Exception
    {
        // Language/locale setup
        EnSetup enSetup = new EnSetup();
        
        // Set up OpenNLP (TokenizerME, SentenceDetectorME, POSTaggerME, [Simple]Lemmatizer) 
        Populate popl = new Populate( eu.discoveri.predikt.sentences.LangCode.en );

        // Connect to lemma db
        Connection conn = LemmaDbBuild.lemmaDb();
        
        /*
         * Process sentences
         * -----------------
         */
        // Edges
        List<SentenceEdge> ledges = new ArrayList<>();
        // Get sentences from raw text
        List<SentenceNode> lsents = Corpi.getVertices();
        
        // Set up sentence analysis on above sentences/language/locale
        CorpusProcess cp = new CorpusProcess( lsents, enSetup );
        
        // Tokenize sentences
        cp.rawTokenizeSentenceCorpus(popl.getPme());

        // Clean up tokens (punctuation,numbers,unwanted POStags etc.)
        cp.cleanTokensSentenceCorpus();
        
        // Get lemmas
        cp.lemmatizeSentenceCorpusViaDb(conn);
        
        // Now calculate common word count between sentences and
        // do the token/lemma counting per sentence pair (QRscore) [Updates each sentence]
        cp.countingLemmas();
        
        // Calculate the similarity score between sentences (QRscore) [Updates each sentence]
        cp.similarity();
        
        /*
         * Populate db (from corpus Maps)
         * ------------------------------
         */
        // Session
        DiscoveriSessionFactory discSess = DiscoveriSessionFactory.getInstance();
        Session sess = discSess.getSession();
        // Db service
        SentenceNodeService sns = new SentenceNodeService();
        SentenceEdgeService ses = new SentenceEdgeService();

        // Clear database
        sess.purgeDatabase();

        // Edges
        cp.getQRscores().forEach((k,v) -> {
//            System.out.println("-----> Weight edge ["+k.getKey()+"]-["+k.getValue()+"]: " +v);
            if( v > Constants.EDGEWEIGHTMIN )
            {
                SentenceEdge se = new SentenceEdge(k.getKey(),k.getValue(),v);
                ledges.add(se);
                se.persist(ses);
            }
        });
        
        // Dump nodes/edges for cluster analysis
        Map<Vertex,Long> vi = new HashMap<>();
        System.out.println("Num. nodes: " +lsents.size()+ ", num. edges: " +ledges.size());
        ledges.forEach(e -> {
            Vertex v1 = e.getN1();
            if( !vi.containsKey(v1) )
            {
                vi.put(v1, ++idx);
                v1.setLouvainIdx(idx);
            }

            Vertex v2 = e.getN2();
            if( !vi.containsKey(v2) )
            {
                vi.put(v2, ++idx);
                e.getN2().setLouvainIdx(idx);
            }
        });
        
        // Edge and edge weights
        double[] edgeWeights = new double[ledges.size()];
        int[][] edges = new int[2][ledges.size()];
        ledges.forEach(e -> {
            System.out.println(""+e.getN1().getLouvainIdx()+"\t"+e.getN2().getLouvainIdx()+"\t"+e.getWeight());
            edgeWeights[++eIdx] = e.getWeight();
            edges[0][eIdx] = (int)e.getN1().getLouvainIdx();
            edges[1][eIdx] = (int)e.getN2().getLouvainIdx();
        });
        
        // Nodes, update Louvain index
        lsents.forEach(s -> s.persist(sns));
        
        // Clustering
        Clusters.generate(lsents.size(), edges, edgeWeights);
        
        // Close
        System.out.println("");
        discSess.close();
    }
}

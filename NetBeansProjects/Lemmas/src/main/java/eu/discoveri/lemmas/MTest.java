/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

import eu.discoveri.predikt.exceptions.EmptySentenceListException;
import eu.discoveri.predikt.exceptions.NoLanguageSetEntriesException;
import eu.discoveri.predikt.exceptions.SentenceIsEmptyException;
import eu.discoveri.predikt.exceptions.TokensListIsEmptyException;
import eu.discoveri.predikt.graph.Corpi;

import eu.discoveri.predikt.graph.Populate;
import eu.discoveri.predikt.graph.SentenceNode;
import eu.discoveri.predikt.resources.LangResources;
import eu.discoveri.predikt.sentences.CorpusProcess;
import eu.discoveri.predikt.sentences.Language;
import eu.discoveri.predikt.sentences.LanguageLocaleSet;
import eu.discoveri.predikt.sentences.Token;
import eu.discoveri.predikt.sentences.LangCode;
import eu.discoveri.predikt.utils.CharacterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class MTest
{
    public static void main(String[] args)
            throws IOException, SentenceIsEmptyException, TokensListIsEmptyException, EmptySentenceListException, NoLanguageSetEntriesException
    {
                // Make sure LanguageLocaleSet is setup
        LanguageLocaleSet.setup();
        
//        List<Token> lt = new ArrayList<>();
//        lt.add(new Token("Don't",""));
//        lt.add(new Token("fear",""));
//        lt.add(new Token("the",""));
//        lt.add(new Token("reaper-man",""));
//        lt.add(new Token("doesn't",""));
//        lt.add(new Token("or,",""));
//        lt.add(new Token("shouldn't've",""));
//        lt.add(new Token("been",""));
//        lt.add(new Token("1989",""));
//        lt.add(new Token("recorded!",""));
        
//        SentenceNode sn = new SentenceNode("test1","Don't fear the reaper-man doesn't or, shouldn't've been, 1989 recorded!");
//        List<SentenceNode> lsn= List.of(sn);
//        
//        System.out.println(">>>>>  " +sn.getSentence());
//        // Set up OpenNLP (being: TokenizerME, SentenceDetectorME, POSTaggerME, [Simple]Lemmatizer) 
        Populate popl = new Populate( LangCode.en );
        
        // Get sentences from raw text
//        List<SentenceNode> lsn = Corpi.getVertices();
        List<SentenceNode> lsn = Arrays.asList(
            new SentenceNode("S1","American inventor Philo T. Farnsworth, a pioneer of television, was accorded what many believe was long overdue glory Wednesday when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol."),
            new SentenceNode("S2","American inventor Philo T. Farnsworth, a pioneer of television, was honored when a 7-foot bronze likeness of the electronics genius was dedicated in the U.S. Capitol."),
            new SentenceNode("S3","With his 81-year-old widow, Elma Farnsworth, looking on, the inventor was extolled as the father of television and his statue was placed in the pantheon of famous Americans of the Capitol’s National Statuary Hall"),
            new SentenceNode("S4","The clear favorite was one Philo T. Farnsworth, an inventor who is considered the father of television"),
            new SentenceNode("S5","If Utahans have their way, Philo T. Farnsworth will become a household name"),
            new SentenceNode("S6","The crew worked for more than two hours to separate the 8.5-foot bronze likeness of the city’s fictitious boxer from the steps of the Philadelphia Museum of Art, which has repeatedly insisted it doesn’t want the statue."),
            new SentenceNode("test1","Don't fear the reaper-man doesn't or, shouldn't've been, the city's 1989 recording!")
        );
        
        // Set up sentence analysis on above sentences
        CorpusProcess cp = new CorpusProcess( lsn );
        
        // Tokenize sentences
//        cp.tokenizeSentenceCorpus(popl.getTme(),popl.getPme());
//        cp.simpleTokenizeSentenceCorpus(popl.getSt(),popl.getPme());
        cp.rawTokenizeSentenceCorpus(popl.getPme());

        // Clean up tokens (punctuation,numbers,unwanted POStags)
        cp.cleanTokensSentenceCorpus(List.of("VB","NN"));
        
        // What we got?
        lsn.forEach(sn -> {
            sn.dumpTokenChars();
        });
        
        
    }
}

/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.main;

import eu.discoveri.exceptions.EmptySentenceListException;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensCountInSentencesIsZeroException;
import eu.discoveri.exceptions.TokensListIsEmptyException;

import eu.discoveri.config.Constants;
import eu.discoveri.exceptions.ListLengthsDifferException;
import eu.discoveri.exceptions.POSTagsListIsEmptyException;
import eu.discoveri.graph.GraphSentences;
import eu.discoveri.graph.Graphing;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.neo4j.driver.net.ServerAddress;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SentenceGraphMain 
{
    public static void main(String[] args)
    {
        // Create the driver on VSRV1/PORT1
        Graphing.createDriver(Constants.NEO4J, Constants.USER, Constants.PWD, ServerAddress.of(Constants.VSRV1, Constants.PORT1));
        
        // Clear all nodes/edges of db
        Graphing.clearDb();
        
        // Start the sentence graph
        try { GraphSentences.startGraph(); }
        catch(  TokensCountInSentencesIsZeroException |
                FileNotFoundException |
                SentenceIsEmptyException |
                EmptySentenceListException |
                TokensListIsEmptyException |
                ListLengthsDifferException |
                POSTagsListIsEmptyException ex )
            { System.out.println("FATAL!! "+ex.toString()); }
        catch( IOException iox )
            { System.out.println("FATAL!! "+iox.toString()); }
        
        // Close all connections
        Graphing.close();
    }
}

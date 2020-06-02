/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

import es.discoveri.lemmas.config.Constants;
import eu.discoveri.predikt.graph.SentenceNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BuildENdict
{
    /**
     * Read input lemma files in parallel.
     * 
     * @param numberOfFiles
     * @param degreeOfParallelism
     * @param conn
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException 
     */
    public void lineReaderParseParallel(final int numberOfFiles, final int degreeOfParallelism, Connection conn)
            throws IOException, ParseException, InterruptedException
    {
        Thread[] pool = new Thread[degreeOfParallelism];
        int batchSize = numberOfFiles / degreeOfParallelism;

        for (int b=0; b<degreeOfParallelism; b++)
        {
            pool[b] = new ENReaderParseThread(conn);
            pool[b].start();
        }

        for (int b=0; b<degreeOfParallelism; b++)
            { pool[b].join(); }
    }
}


//--------------------------[ Run Thread ]--------------------------------------
class ENReaderParseThread extends Thread
{
    private final Connection    conn;
    
    /**
     * Constructor.  Setup db connection.
     * 
     * @param conn 
     */
    public ENReaderParseThread( Connection conn )
    {
        this.conn = conn;
    }
    
    /**
     * Thread run.
     */
    @Override
    public void run()
    {
        // Preparared statements
        PreparedStatement wrd = null;
        PreparedStatement lem = null;
        PreparedStatement lid = null;
        
        // Statements for populating word/lemma tables
        try
        {
            // To populate word table
            wrd = conn.prepareStatement(Constants.WORDPS);
                        
            // To populate lemma table
            lem = conn.prepareStatement(Constants.LEMMAPS);
            
            // To get a lemma when updating Word table
            lid = conn.prepareStatement(Constants.LEMMA4WORDPS);
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
            System.exit(-9);
        }
        
        // Ok, now for each input lemma type file
        File in = new File(Constants.INPATH+"en-lemmatizer.dict");
        System.out.println("  ..> " +in.getName());

        // Read input file
        String line = "";
        try( FileReader frd = new FileReader(in) )
        {
            BufferedReader brd = new BufferedReader(frd);
            while( (line=brd.readLine()) !=null )
            {
                // Format: <word> \t <POS> \t <lemma>
                String[] lemmaPlusWord = line.split("\t");

                //... Lemma table
                int lemmid = 0;
                String lemma = lemmaPlusWord[2].replace("'", "\\'");            // Escape single quote

                // Get the lemma id, else write lemma and then get id
                lid.setString(1, lemma);
                ResultSet rs = lid.executeQuery();
                
                if( rs.next() )
                    lemmid = rs.getInt("id");
                else
                {
                    // Write
                    lem.setString(1, lemma);
                    lem.executeUpdate();
                    // And get the id (string set above)
                    rs = lid.executeQuery();
                    rs.next();
                    lemmid = rs.getInt("id");
                }

                //... Word table
                // Word
                String w = lemmaPlusWord[0].replace("'", "\\'");                // Escape single quote
                // POS tag
                String POS = SentenceNode.POSsimple(lemmaPlusWord[1]);

                // Store on Word table
                wrd.setString(Constants.WORD, w);
                wrd.setString(Constants.POSID, POS);
                wrd.setString(Constants.LANGID, LangCode.en.toString());
                wrd.setInt(Constants.LEMMID, lemmid);
                wrd.setInt(Constants.PAGEID, 1);
                wrd.executeUpdate();
            }
            brd.close();
        }
        catch( SQLException sqx )
        {
            System.out.println("!!!ERR(sqx) line: [" +line+ "]");
            sqx.printStackTrace();
            System.exit(-8);
        }
        catch( IOException iox )
        {
            System.out.println("!!!ERR(iox) line: [" +line+ "]");
            iox.printStackTrace();
            System.exit(-2);
        }
    }
}
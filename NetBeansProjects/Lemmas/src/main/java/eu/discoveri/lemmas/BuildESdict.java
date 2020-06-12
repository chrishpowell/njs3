/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

import eu.discoveri.predikt.config.Constants;
import eu.discoveri.lemmas.db.DictType;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.SQLException;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BuildESdict
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
            pool[b] = new ESReaderParseThread(conn);
            pool[b].start();
        }

        for (int b=0; b<degreeOfParallelism; b++)
            { pool[b].join(); }
    }
}


//--------------------------[ Run Thread ]--------------------------------------
class ESReaderParseThread extends Thread
{
    private final Connection    conn;
    
    /**
     * Constructor.  Setup db connection.
     * 
     * @param conn 
     */
    public ESReaderParseThread( Connection conn )
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
        
        // List of lemma type files
        List<DictType> files = Arrays.asList(   new DictType("es-verbDic.txt",PennPOSCode.VB,LangCode.es),
                                                new DictType("es-adjDic.txt",PennPOSCode.JJ,LangCode.es),
                                                new DictType("es-advDic.txt",PennPOSCode.RB,LangCode.es),
                                                new DictType("es-conjDic.txt",PennPOSCode.CC,LangCode.es),
                                                new DictType("es-detDic.txt",PennPOSCode.DT,LangCode.es),
                                                new DictType("es-nounDic.txt",PennPOSCode.NN,LangCode.es),
                                                new DictType("es-particlesDic.txt",PennPOSCode.RP,LangCode.es),
                                                new DictType("es-pronounDic.txt",PennPOSCode.WP,LangCode.es)      );
        
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
        for( DictType d: files )
        {
            File in = new File(Constants.INPATH+d.getfName());
            System.out.println("  ..> " +in.getName());

            // Read input file
            String line = "";
            try( FileReader frd = new FileReader(in) )
            {
                BufferedReader brd = new BufferedReader(frd);
                while( (line=brd.readLine()) !=null )
                {
                    // Format: <lemma>===List of <word>
                    String[] lemmaPlusList = line.split("===");

                    //... Lemma table
                    int lemmid = 0;
                    String lemma = lemmaPlusList[0].replace("'", "\\'");        // Escape single quote

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
                    for( String w: lemmaPlusList[1].split(";") )
                    {
                        // Word
                        w = w.replace("'", "\\'");                              // Ensure single quote escaped
                        
                        // Store on Word table
                        wrd.setString(Constants.WORD, w);
                        wrd.setString(Constants.LANGID, d.getLangCode().toString());
                        wrd.setString(Constants.POSID, d.getPOSCode().toString());
                        wrd.setInt(Constants.LEMMID, lemmid);
                        wrd.setInt(Constants.PAGEID, 1);
                        wrd.executeUpdate();
                    }
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
}

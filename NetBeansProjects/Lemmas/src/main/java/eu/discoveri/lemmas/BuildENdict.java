/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

import es.discoveri.lemmas.config.Constants;
import eu.discoveri.lemmas.db.DictType;
import eu.discoveri.lemmas.db.LemmaDbBuild;
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
    }
}

//--------------------------[ Run Thread ]--------------------------------------
class ENReaderParseThread extends Thread
{
    private final Connection    conn;
    private final String        INPATH = "/home/chrispowell/NetBeansProjects/Lemmas/src/main/java/es/discoveri/lemmas/txt/";
    
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
        
        // List of lemma type files
        List<DictType> files = Arrays.asList(   new DictType("en-verbDic.txt",PennPOSCode.VB,LangCode.en),
                                                new DictType("en-adjDic.txt",PennPOSCode.JJ,LangCode.en),
                                                new DictType("en-advDic.txt",PennPOSCode.RB,LangCode.en),
                                                new DictType("en-conjDic.txt",PennPOSCode.CC,LangCode.en),
                                                new DictType("en-detDic.txt",PennPOSCode.DT,LangCode.en),
                                                new DictType("en-nounDic.txt",PennPOSCode.NN,LangCode.en),
                                                new DictType("en-particlesDic.txt",PennPOSCode.RP,LangCode.en),
                                                new DictType("en-pronounDic.txt",PennPOSCode.WP,LangCode.en)      );
        
        // Statements for populating word/lemma tables
        try
        {
            // To populate word table
            wrd = conn.prepareStatement("insert into lemma.Word values(default,?,?,?,?,?) on duplicate key update word=word");
                        
            // To populate lemma table
            lem = conn.prepareStatement("insert into lemma.Lemma values(default,?) on duplicate key update lemma=lemma");
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

            // Read input file
            String line;
            try( FileReader frd = new FileReader(in) )
            {
                BufferedReader brd = new BufferedReader(frd);
                while( (line=brd.readLine()) !=null )
                {
                    // Format: <lemma>===List of <word>
                    String[] lemmaPlusList = line.split("===");

                    String lemma = lemmaPlusList[0];
                    String[] words = lemmaPlusList[1].split(";");

                    // Lemma table
                    lem.setString(1, lemma);
                    lem.executeUpdate();
                    PreparedStatement lid = conn.prepareStatement("select id from lemma.Lemma where lemma = '"+lemma+"'");
                    ResultSet rs = lid.executeQuery();
                    rs.next();
                    int lemmid = rs.getInt("id");

                    // Word table
                    for( String w: words )
                    {
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
                sqx.printStackTrace();
                System.exit(-8);
            }
            catch( IOException iox )
            {
                iox.printStackTrace();
                System.exit(-2);
            }
        }
    }
}
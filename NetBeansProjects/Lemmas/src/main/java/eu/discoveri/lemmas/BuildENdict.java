/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

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
}
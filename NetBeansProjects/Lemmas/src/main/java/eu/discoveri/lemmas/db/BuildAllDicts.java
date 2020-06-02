/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas.db;

import eu.discoveri.lemmas.BuildESdict;
import eu.discoveri.lemmas.BuildENdict;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BuildAllDicts
{
    public static void main(String[] args)
            throws Exception
    {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Connect to db and build
        try( Connection conn = LemmaDbBuild.lemmaDb() )
        {
            // English
//            System.out.println(LocalDateTime.now().format(df)+"> Building EN...");
//            BuildENdict bend = new BuildENdict();
//            bend.lineReaderParseParallel(1, 1, conn);
            
            // Spanish
            System.out.println(LocalDateTime.now().format(df)+"> Building ES...");
            BuildESdict besd = new BuildESdict();
            besd.lineReaderParseParallel(1, 1, conn);
            
            // Portuguese
            // French
            // German
            // Russian
            // Hindi
            // Chinese (Mandarin)
        }
    }
}

/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas.db;

import eu.discoveri.lemmas.BuildESdict;
import eu.discoveri.lemmas.BuildENdict;
import java.sql.Connection;


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
        Connection conn = LemmaDbBuild.lemmaDb();
        
        // English
        BuildENdict bend = new BuildENdict();
        bend.lineReaderParseParallel(1, 1, conn);
        
        // Spanish
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

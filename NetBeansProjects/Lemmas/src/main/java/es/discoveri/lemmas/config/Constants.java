/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */

package es.discoveri.lemmas.config;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constants
{
    public static int           WORD = 1, POSID = 2, LANGID = 3, LEMMID = 4, PAGEID = 5;
    public static String        INPATH = "/home/chrispowell/NetBeansProjects/Lemmas/src/main/java/es/discoveri/lemmas/txt/";
    
    // Prepared statements
    public static String        WORDPS = "insert into lemma.Word values(default,?,?,?,?,?)  on duplicate key update word=word,POSId=POSId";
    public static String        LEMMAPS = "insert into lemma.Lemma values(default,?) on duplicate key update lemma=lemma";
    public static String        LANGCODEPS = "insert into lemma.LangCode values(?,?)";
    public static String        PENNPOSCODEPS = "insert into lemma.PennPOSCode values(?,?)";
    public static String        PAGEPS = "";
    public static String        LEMMA4WORDPS = "select id from lemma.Lemma where lemma = ?";
    public static String        LEMMANULLPS = "insert into lemma.Lemma values(default,?)";
    public static String        PAGEZEROPS = "insert into lemma.Page values(default,?,?)";
}

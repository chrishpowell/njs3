/*
 */

package eu.discoveri.config;

/**
 * Constants.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constants
{
    // Search clusters
    public static final String    STORECLUSTERSPATH = "/home/chrispowell/NetBeansProjects/WebScrape/src/main/java/resources/model/";
    public static final String    STORECLUSTERSEXT = "cluster";
    
    // Num. clusters to process
    public static final int       TOPNUMCLUSTERS = 5;
    
    // Resource files
    public static final String    RESMODELS = "/home/chrispowell/NetBeansProjects/LanguageGraph/src/main/java/resources/model/";
    
    // English sentences lengths
    public static final int       ENGLISHGOODMAXLEN = 20;
    public static final int       ENGLISHDIFFICULTLEN = 29;
    public static final int       ENGLISHBADMAXLEN = 35;                        // Sentences longer than this get dropped.
    
    // OpenNLP model files (built via Language class)
    public static final String    SENTMODEL = "Sentence";
    public static final String    TOKENMODEL = "Token";
    public static final String    SEQMODEL = "Sequence";                        // POSTaggerME sequence/outcome
    public static final String    CHUNKMODEL = "Chunk";
    public static final String    PARSEMODEL = "Parse";
    public static final String    LEMMATIZE = "Lemmatize";
    
    public static final String    ENSENTFILE = "en-sent.bin";
    public static final String    ESSENTFILE = "es-sent.bin";
    public static final String    ENTOKENFILE = "en-token.bin";
    public static final String    ESTOKENFILE = "es-token.bin";
    public static final String    ENPOSMEFILE = "en-pos-maxent.bin";
    public static final String    ESPOSMEFILE = "es-pos-maxent.bin";
    public static final String    ENLEMMAFILE = "en-lemmatizer.dict";
    public static final String    ESLEMMAFILE = "es-lemmatizer.dict";
    
    // Node score default (Milhelcea et al)
    public static final double    NODESCOREDEF = 0.25;
    // Node UUID
    public static final String    GRAPHNAMESPACE = "eu.discoveri.languagegraph";
}

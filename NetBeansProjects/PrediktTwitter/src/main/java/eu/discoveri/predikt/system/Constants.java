/*
 * Constants (not strictly all constant, hence not all final)
 */
package eu.discoveri.predikt.system;


/**
 * Constants & constants for property file.  Note entries such as 
 * COLON = "COLON" are used to access the property file.  xxxDEF is the fallback.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Constants
{
    /*
     * Zodiac property constants
     */
    // Property file
    public static final String      ZODIACPROPSFILE = "/home/chrispowell/PrediktCommon/properties/zodiac.properties";
    public static final String      ZODIACPROPSKEY = "ZODIACPROPS";
    
    /*
     * 0MQ message schema
     */
    public static final String      MSGSCHEMA = "/home/chrispowell/NetBeansProjects/PrediktTwitter/src/main/java/eu/discoveri/predikt/resources/MsgTypeLocale.json";
    
    /*
     * ZMQ property constants (defaults set)
     */
    // Property file
    public static final String      ZMQPROPSFILE = "/home/chrispowell/PrediktCommon/properties/zmq.properties";
    public static final String      ZMQPROPSKEY = "ZMQPROPS";
    // Colon in URL
    public static String            COLONDEF = ":";
    public static String            COLON = "COLON";
    // Remote server
    public static final String      RSRVDEF = "localhost";
    public static final String      RSRV = "RSRV";
    // Tweet socket port
    public static final int         TPORTDEF = 8777;
    public static final String      TPORT = "TPORT";
    // I18N port
    public static final int         IPORTDEF = 8778;
    public static final String      IPORT = "IPORT";
    // ZMQ socket string (twitter)
    public static String            ZMQ1URLDEF = "tcp://"+RSRVDEF+COLONDEF+TPORTDEF;
    public static final String      ZMQ1URL = "ZMQ1URL";
    // ZMQ socket string (I18N)
    public static String            ZMQ2URLDEF = "tcp://"+RSRVDEF+COLONDEF+IPORTDEF;
    public static final String      ZMQ2URL = "ZMQ2URL";
    
    // Message types ('Channels')
    public static final String      I18NCHAN = "I18NCHAN";
    public static final String      TWTCHAN = "TWTCHAN";

    // I18N object names
    public static final String      ACCOUNTTYPELOCALE = "accountTypeLocale",
                                    FORMVALUESI18N = "formValuesI18N",
                                    INPVALUESI18N = "inpValuesI18N",
                                    ACCOUNTTYPELIST = "accountTypeList",
                                    ERRORMSGS = "errorMsgs";
    // Unicode version (I18N)
    public static final String      UNICODEVERS = "5.2";
        
    // Scheduler(tweets)
    public static final String      JOBID = "ZMQ Horoscope Tweets",
                                    GROUPID = "ZMQ Tweets";
    public static final int         DELAY = 3;  // Hours (see below)


    /*
     * Tweets constants
     */
    // How far back to look for tweets.
    // 36 hours as some tweet accts are not updated that
    // frequently and we could be on the other side of the world.
    public static final long        HRS = 24;
    public static final long        HRSINMS = 1000*60*60*HRS;
    // Tweet service (pool) shutdown wait (secs)
    public static final long        POOLWAIT = 20;
    // Limit to 2 tweets per
    public static final long        TWEETSLIM = 2;
    // Default tweet language
    public static final String      ENGBDEF = "en-GB",
                                    ENGBCL = "en-GB";
    public static final String      ENGB = "ENGB",
                                    EN = "en",
                                    ENREGEX = "/\b[a-zA-Z]+(?:['-]?[a-zA-Z]+)*\b/";
    // Language/Country locales
    public static final String      FRFRDEF = "fr-FR",
                                    FRFRCL = "fr-FR";
    public static final String      FRFR = "FRFR",
                                    FR = "fr",
                                    FRREGEX = "/\b[a-zA-Z]+(?:['-]?[a-zA-Z]+)*\b/";
    // Language/Country locales
    public static final String      ENUSDEF = "en-US",
                                    ENUSCL = "en-US";
    public static final String      ENUS = "ENUS";
        // Language/Country locales
    public static final String      ESESDEF = "es-ES",
                                    ESESCL = "es-ES";
    public static final String      ESES = "ESES";
        // Language/Country locales
    public static final String      ZHCNDEF = "zh-CN",
                                    ZHCNCL = "zh-CN";
    public static final String      ZHCN = "ZHCN";
        // Language/Country locales
    public static final String      RURUDEF = "ru-RU",
                                    RURUCL = "ru-RU";
    public static final String      RURU = "RURU";
}

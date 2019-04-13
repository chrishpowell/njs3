/*
 * Language Country 
 */
package eu.discoveri.predikt.i18n;

import eu.discoveri.predikt.exceptions.UnrecognizedLangCtryCodeException;
import eu.discoveri.predikt.system.Constants;
import eu.discoveri.predikt.system.TweetsCache;
import org.javatuples.Triplet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LanguageCountry
{
    // Current language/locale list (database)
    private static List<Triplet<String,String,String>>  lcTrips = Arrays.asList( new Triplet("en","GB",Constants.ENGBCL),
                                                                                 new Triplet("en","US",Constants.ENUSCL),
                                                                                 new Triplet("fr","FR",Constants.FRFRCL),
                                                                                 new Triplet("es","ES",Constants.ESESCL),
                                                                                 new Triplet("zh","CN",Constants.ZHCNCL),
                                                                                 new Triplet("ru","RU",Constants.RURUCL)  );
    // Index ('aa-BB') to country-language data (FullCountry)
    private static Map<String,FullCountry>  langCtryDb = new HashMap<>();
    
    private String          ctryLang;
    private FullCountry     ctry;

    public LanguageCountry(String ctryLang, FullCountry ctry)
            throws UnrecognizedLangCtryCodeException
    {
        boolean ok = false;
        for( Triplet trip: lcTrips )
        {
            if( ctryLang.equals(trip.getValue2()) )
                ok = true;
        }

        if( ok )
        {
            this.ctryLang = ctryLang;
            this.ctry = ctry;
        }
        else
        {
            throw new UnrecognizedLangCtryCodeException("Supplied code: " +ctryLang);
        }
    }

    public static Map<String, FullCountry> getLangCtry() {
        return langCtryDb;
    }

    public static void setLangCtry(Map<String, FullCountry> langCtryDb) {
        LanguageCountry.langCtryDb = langCtryDb;
    }

    public String getCtryLang() {
        return ctryLang;
    }

    public void setCtryLang(String ctryLang) {
        this.ctryLang = ctryLang;
    }

    public FullCountry getCtry() {
        return ctry;
    }

    public void setCtry(FullCountry ctry) {
        this.ctry = ctry;
    }
    
        
    /**
     * Get list of Language-Country.
     * @return 
     */
    public static List<Triplet<String,String,String>> getLangCtryTriple()
    {
        return lcTrips;
    }
    
    
    /**
     * 'Initialise' the Lang/Ctry database and the Tweet cache.
     * 
     * @throws Exception 
     */
    public static void initLangCtryDb()
            throws Exception
    {
        int ENGB = 0, ENUS = 1, FRFR = 2, ZHCN = 3, RURU = 4;
        
        // en/GB
        FullCountry engb = new FullCountry( "United Kingdom",
                                            "United Kingdom",
                                            new ISO31661("GB","GBR","826"),
                                            Arrays.asList(new ISO639("en","eng","eng")));
        langCtryDb.put( lcTrips.get(ENGB).getValue2(), engb );
        // Tweets cache initialise
        TweetsCache.initTweetCtryCache( lcTrips.get(ENGB).getValue2() );

        // en/US
        FullCountry enus = new FullCountry( "United States of America",
                                            "United States of America",
                                            new ISO31661("US","USA","840"),
                                            Arrays.asList(new ISO639("en","eng","eng"),new ISO639("es","spa","spa")));
        langCtryDb.put( lcTrips.get(ENUS).getValue2(), enus );
        // Tweets cache initialise
        TweetsCache.initTweetCtryCache( lcTrips.get(ENUS).getValue2() );
        
        
        // fr/FR
        FullCountry frfr = new FullCountry( "France",
                                            "La France",
                                            new ISO31661("FR","FRA","250"),
                                            Arrays.asList(new ISO639("fr","fra","fre")));
        langCtryDb.put( lcTrips.get(FRFR).getValue2(), frfr );
        // Tweets cache initialise
        TweetsCache.initTweetCtryCache( lcTrips.get(FRFR).getValue2() );
        
        
        // zh/CN
        FullCountry zhcn = new FullCountry( "China",
                                            "Zhong Guo",
                                            new ISO31661("CN","CHN","156"),
                                            Arrays.asList(new ISO639("zh","zho","chi")));
        langCtryDb.put( lcTrips.get(ZHCN).getValue2(), zhcn );
        // Tweets cache initialise
        TweetsCache.initTweetCtryCache( lcTrips.get(ZHCN).getValue2() );
        
        
        // ru/RU
        FullCountry ruru = new FullCountry( "Russia",
                                            "Rossiya",
                                            new ISO31661("RU","RUS","643"),
                                            Arrays.asList(new ISO639("ru","rus","rus")));
        langCtryDb.put( lcTrips.get(RURU).getValue2(), ruru );
        // Tweets cache initialise
        TweetsCache.initTweetCtryCache( lcTrips.get(RURU).getValue2() );
    }

    
    /**
     * Return the 'db'
     * @return Lang Ctry Db
     */
    public static Map<String,FullCountry> getLangCtryDb()
    {
        return langCtryDb;
    }
    

    //----------------------------------------
    //      M A I N (build 'db')
    //----------------------------------------
    public static void main(String[] args)
            throws Exception
    {
        initLangCtryDb();
        langCtryDb.forEach((k,v)->{
            System.out.println(""+k+": "+v.toString());
        });
    }
}

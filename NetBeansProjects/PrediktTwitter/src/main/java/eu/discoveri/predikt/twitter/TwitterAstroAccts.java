/*
 * A set of Twitter accounts for astrological tweets
 * @TODO: *** Needs to be database based ***
 */
package eu.discoveri.predikt.twitter;

import eu.discoveri.predikt.system.Constants;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@predikt.io
 */
public class TwitterAstroAccts
{
    // List of Twitter accounts to test for tweets
    private static Map<String,List<TwitterAcct>> pisces, aquarius, capricorn, sagittarius, scorpio, libra, virgo, leo, cancer, gemini, taurus, aries;
    // Group these accounts into: Sign->Language->List(TwitterAcct)
    private static Map<String,Map<String,List<TwitterAcct>>> grpUserAccts = new HashMap<>();
    
    /*
     * Build the mini Db
     */
    public static void taaInit()
    {
        // Pisces
        pisces = new HashMap<>();
        pisces.put(Constants.ENGBCL, Arrays.asList( new TwitterAcct("pisces_scope",3,true,"",""),
                                                    new TwitterAcct("pisces_scopes",2,true,"",""),
                                                    new TwitterAcct("Pisces_Horo",1,true,"","")   ));
        pisces.put(Constants.FRFRCL, Arrays.asList( new TwitterAcct("Poisson__Astro",1,true,"",""),
                                                    new TwitterAcct("Team_Poissons",2,true,"",""),
                                                    new TwitterAcct("Pisces_Horo",1,true,"","")   ));
        grpUserAccts.put("Pisces", pisces);
        
        // Aquarius
        aquarius = new HashMap<>();
        aquarius.put(Constants.ENGBCL, Arrays.asList( new TwitterAcct("aquarius_scope",2,true,"",""),
                                                      new TwitterAcct("aquarius_scopes",3,true,"",""),
                                                      new TwitterAcct("Aquarius_Horo",1,true,"",""))   );
        aquarius.put(Constants.FRFRCL, Arrays.asList( new TwitterAcct("aquarius_scope",2,true,"",""),
                                                      new TwitterAcct("aquarius_scopes",3,true,"",""),
                                                      new TwitterAcct("Aquarius_Horo",1,true,"",""))   );
        grpUserAccts.put("Aquarius", aquarius);
        
        // Capricorn
        capricorn = new HashMap<>();
        capricorn.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("capricorn_scope",2,true,"",""),
                                                      new TwitterAcct("capric_scopes",3,true,"",""),
                                                      new TwitterAcct("Capricorn_Horo",1,true,"",""))  );
        capricorn.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("capricorn_scope",2,true,"",""),
                                                      new TwitterAcct("capric_scopes",3,true,"",""),
                                                      new TwitterAcct("Capricorn_Horo",1,true,"",""))  );
        grpUserAccts.put("Capricorn", capricorn);
        
        // Sagittarius
        sagittarius = new HashMap<>();
        sagittarius.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("sagi_scope",2,true,"",""),
                                                        new TwitterAcct("TrueSagittarian",1,true,"",""),
                                                        new TwitterAcct("sagittar_scopes",3,true,"","")) );
        sagittarius.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("sagi_scope",2,true,"",""),
                                                        new TwitterAcct("TrueSagittarian",1,true,"",""),
                                                        new TwitterAcct("sagittar_scopes",3,true,"","")) );
        grpUserAccts.put("Sagittarius", sagittarius);
        
        // Scorpio
        scorpio = new HashMap<>();
        scorpio.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("scorpio_scope",2,true,"",""),
                                                    new TwitterAcct("scorpio_scopes",3,true,"",""),
                                                    new TwitterAcct("Scorpio_Horo",1,true,"",""))    );
        scorpio.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("scorpio_scope",2,true,"",""),
                                                    new TwitterAcct("scorpio_scopes",3,true,"",""),
                                                    new TwitterAcct("Scorpio_Horo",1,true,"",""))    );
        grpUserAccts.put("Scorpio", scorpio);
        
        // Libra
        libra = new HashMap<>();
        libra.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("libra_scope",2,true,"",""),
                                                  new TwitterAcct("libra_scopes",3,true,"",""),
                                                  new TwitterAcct("Libra_Horo",1,true,"",""))    );
        libra.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("libra_scope",2,true,"",""),
                                                  new TwitterAcct("libra_scopes",3,true,"",""),
                                                  new TwitterAcct("Libra_Horo",1,true,"",""))    );
        grpUserAccts.put("Libra", libra);
        
        // Virgo
        virgo = new HashMap<>();
        virgo.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("virgo_scope",2,true,"",""),
                                                  new TwitterAcct("virgo_scopes",3,true,"",""),
                                                  new TwitterAcct("Virgo_Horo",1,true,"",""))    );
        virgo.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("virgo_scope",2,true,"",""),
                                                  new TwitterAcct("virgo_scopes",3,true,"",""),
                                                  new TwitterAcct("Virgo_Horo",1,true,"",""))    );
        grpUserAccts.put("Virgo", virgo);
        
        // Leo
        leo = new HashMap<>();
        leo.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("leo_scope",2,true,"",""),
                                                new TwitterAcct("leo_scopes",3,true,"",""),
                                                new TwitterAcct("Leo_Horo",1,true,"",""))      );
        leo.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("leo_scope",2,true,"",""),
                                                new TwitterAcct("leo_scopes",3,true,"",""),
                                                new TwitterAcct("Leo_Horo",1,true,"",""))      );
        grpUserAccts.put("Leo", leo);
        
        // Cancer
        cancer = new HashMap<>();
        cancer.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("cancer_scope",2,true,"",""),
                                                   new TwitterAcct("cancer_scopes",3,true,"",""),
                                                   new TwitterAcct("Cancer_Horo",1,true,"",""))   );
        cancer.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("cancer_scope",2,true,"",""),
                                                   new TwitterAcct("cancer_scopes",3,true,"",""),
                                                   new TwitterAcct("Cancer_Horo",1,true,"",""))   );
        grpUserAccts.put("Cancer", cancer);
        
        // Gemini
        gemini = new HashMap<>();
        gemini.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("gemini_scope",2,true,"",""),
                                                   new TwitterAcct("gemini_scopes",3,true,"",""),
                                                   new TwitterAcct("Gemini_Horo",1,true,"",""))   );
        gemini.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("gemini_scope",2,true,"",""),
                                                   new TwitterAcct("gemini_scopes",3,true,"",""),
                                                   new TwitterAcct("Gemini_Horo",1,true,"",""))   );
        grpUserAccts.put("Gemini", gemini);
        
        // Taurus
        taurus = new HashMap<>();
        taurus.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("taurus_scope",2,true,"",""),
                                                   new TwitterAcct("taurus_scopes",3,true,"",""),
                                                   new TwitterAcct("Taurus_Horo",1,true,"",""))   );
        taurus.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("LeVraiHoroscope",2,true,"",""),
                                                   new TwitterAcct("Signe_Taureau",3,true,"Petit_Horoscope","taureau"),
                                                   new TwitterAcct("HQ_Taureau",1,true,"",""))   );
        grpUserAccts.put("Taurus", taurus);
                
        // Aries astro___belier belier__astro
        aries = new HashMap<>();
        aries.put(Constants.ENGBCL, Arrays.asList(new TwitterAcct("aries_scope",2,true,"",""),
                                                  new TwitterAcct("aries_scopes",3,true,"",""),
                                                  new TwitterAcct("Aries_Horo",1,true,"",""))   );
        aries.put(Constants.FRFRCL, Arrays.asList(new TwitterAcct("belier__astro",2,true,"Petit_Horoscope","belier"),
                                                  new TwitterAcct("UnBelier",3,true,"",""),
                                                  new TwitterAcct("HoroscopeBelier",1,true,"",""))   );
        grpUserAccts.put("Aries", aries);
    }


    /**
     * Grouped twitter horoscope accounts.
     * Keys: Lang->Sign->List(TwAccts)
     * 
     * @return 
     */
    public static Map<String,Map<String,List<TwitterAcct>>> getGrpUserAccts()
    {
        return grpUserAccts;
    }

    
    /**
     * Check if twitter account exists.
     * (Why this is not part of twitter4j escapes me)
     * 
     * @param sname
     * @return
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static boolean acctExists(String sname)
            throws MalformedURLException, IOException
    {
        URL obj = new URL("https://twitter.com/"+sname);
	URLConnection conn = obj.openConnection();
        
        return( !conn.getHeaderField("status").contains("404") );
    }
    
    
    /**
     * Check the twitter acct 'db', flag false if not
     */
    public static void checkTwits()
    {
        try
        { //Map<String,Map<String,List<TwitterAcct>>>: Map.Entry<String,Map<List<TwitterAcct>>
            for( Map.Entry<String,Map<String,List<TwitterAcct>>> entry : grpUserAccts.entrySet() )
            {
                for( Map.Entry<String,List<TwitterAcct>> eachLang : entry.getValue().entrySet() )
                {
                    for( TwitterAcct eachUser : eachLang.getValue() )
                    {
                        // Progress bar
                        System.out.print(".");
                        
                        // Get the user screen name
                        String eu = eachUser.getUserAcct();
                        // Flag if non-existent
                        if( !acctExists(eu) )
                            eachUser.setAcctNonActive();
                    }
                }
            }
        }
        catch( IOException ex )
        {
            java.util.logging.Logger.getLogger(PrediktHoroTweets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Dump the 'database.
     * [Map<String,Map<String,List<TwitterAcct>>>: Key:Lang, Key:Sign, List:TWAccts]
     */
    public static void dumpTwitterAstroAccts()
    {
        grpUserAccts.entrySet().stream().map((entry) -> {
            System.out.println("Sign: " +entry.getKey()+ ", ");
            return entry;
        }).forEachOrdered((entry) -> {
            entry.getValue().entrySet().stream().map((eachLang) -> {
                System.out.println("  Lang: "+eachLang.getKey()+ ",  ");
                return eachLang;
            }).forEachOrdered((eachLang) -> {
                eachLang.getValue().forEach((eachUser) -> {
                    System.out.println("    Acct: "+eachUser.getUserAcct());
                });
            });
        });
    }
}


/*
 * A MOCK "database" class
 * -----------------------
 */
class TwitterAcct
{
    private boolean         acctActive;
    private final int       rank;
    private final String    userAcct, searchAcct;
    private final String    searchTerms;

    public TwitterAcct( String userAcct, int rank, boolean acctActive, String searchAcct, String searchTerms )
    {
        this.acctActive = acctActive;
        this.rank = rank;
        this.userAcct = userAcct;
        this.searchAcct = searchAcct;
        this.searchTerms = searchTerms;
    }
    
    public int getRank()
        { return rank; }

    public boolean isAcctActive()
        { return acctActive; }
    
    public void setAcctNonActive()
        { acctActive = false; }

    public String getUserAcct()
        { return userAcct; }

    public String getSearchTerms() {
        return searchTerms;
    }

    public String getSearchAcct() {
        return searchAcct;
    }
}


/**
 * Sort accounts in the TwitterAcct class
 */
class SortByRank implements Comparator<TwitterAcct>
{
    @Override
    public int compare(TwitterAcct a, TwitterAcct b)
    {
        return a.getRank() - b.getRank();
    }
}
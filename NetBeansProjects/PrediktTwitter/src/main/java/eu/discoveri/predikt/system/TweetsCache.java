/*
 * Cache for tweets (multi-lingual)
 */
package eu.discoveri.predikt.system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TweetsCache
{
    // Tweets 'database' per country (see LanguageCountry class re populating)
    private static Map<String,String> tweetCtryCache = new HashMap<>();
    public static void initTweetCtryCache(String ctryLang)
        { tweetCtryCache.put(ctryLang,""); }
    
    // Shared memory (get/read tweets) (JSON object) key: aa-BB, eg: zh-CN
    private static volatile AtomicReference<Map<String,String>>  horoTweets = new AtomicReference();

    public static AtomicReference<Map<String, String>> getHoroTweets()
        { return horoTweets; }

    // Format: {sign:"<sign>",ctryLang:"en-GB",tweets:[]}
    public static void setHoroTweets( String ctryLang, String tweets )
    {
        tweetCtryCache.put(ctryLang,tweets);
        horoTweets.set(tweetCtryCache);
    }
    
    public static void tweetCacheDump()
    {
        System.out.println("Tweets Cache:");
        horoTweets.get().forEach((k,v) -> {
            System.out.println("Key: " +k+ ", Value: " +v);
        });
    }
}

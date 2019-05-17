/*
 * Get horoscope tweets as a cron job
 * @TODO *** Update to OpenJDK v12/13 ***
 */
package eu.discoveri.predikt.twitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import eu.discoveri.predikt.i18n.FullCountry;
import eu.discoveri.predikt.i18n.LanguageCountry;
import eu.discoveri.predikt.system.Constants;
import eu.discoveri.predikt.utils.BasicTSLogger;
import eu.discoveri.predikt.system.TweetsCache;
import eu.discoveri.predikt.utils.PrediktProperties;
import eu.discoveri.predikt.utils.SimpleZodiacDates;
import eu.discoveri.zmq.scheduler.ZMQJob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Properties;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.javatuples.Triplet;
import org.quartz.Scheduler;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;


/**
 * Quartz job populates tweets (slow), 0MQ code reads tweets for display.
 * Hence the tweets are held in, and read from, a volatile in a separate thread.
 * NB: 0MQ/JeroMQ does not document or explain the (Z)Poller event handling
 * sufficiently to make sense of how to use it.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PrediktHoroTweets implements Job
{
    // Ready state
    private static boolean                  ready = false;
    // Running state
    private static boolean                  running = true;
    // Semaphore
    private static Semaphore                mutex;
    // Tweet service
    private static ExecutorService          tweetService;
    // Twitter handle
    private static Twitter                  t = buildTwitter();
    // Socket for twitter
    private static ZContext                 zcontext = new ZContext();
    private static ZMQ.Socket               socket = null;
    // JSON mapper
    private final static ObjectMapper       mapper = new ObjectMapper();
    // @TODO: Mock Country list (db)
    List<Triplet<String,String,String>>     ctryList = LanguageCountry.getLangCtryTriple();
    // @TODO: Mock lang/ctry db
    private static Map<String,FullCountry>  lcDb = null;
    // @TODO: Move Properties init?
    private static Properties               zmqProps;


    //... Build Twitter access
    private static Twitter buildTwitter()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0LNtKkDTzgAlkO7SdaBsSFzCl")
                .setOAuthConsumerSecret("inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB")
                .setOAuthAccessToken("1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx")
                .setOAuthAccessTokenSecret("y8tk4p9WWw3pzbzEZWW9pGo41JXQ3rRSZj3R1q8neYixN")
                .setTweetModeExtended(true);
        
        return new TwitterFactory(cb.build()).getInstance();
    }


    /**
     * 'Homeline'?
     * 
     * @throws Exception 
     */
    public static void homeline()
            throws Exception
    {
        t.setOAuthConsumer("0LNtKkDTzgAlkO7SdaBsSFzCl", "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB");

        RequestToken requestToken = t.getOAuthRequestToken();
        System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());

        AccessToken accessToken = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Hit above Authorization URL and Input PIN here: ");
            String pin = br.readLine();

            accessToken = t.getOAuthAccessToken(requestToken, pin);

        } catch (TwitterException te) {

            System.out.println("Failed to get access token, caused by: "
                    + te.getMessage());
        }

        System.out.println("Access Token: " + accessToken.getToken());
        System.out.println("Access Token Secret: " + accessToken.getTokenSecret());

        // updating twitter status
        t.updateStatus("hi.. im updating this using Namex Tweet for Demo");

        System.out.println("\nReading Twitter Timeline:");

        // I'm reading your timeline
        ResponseList<Status> list = t.getHomeTimeline();
        list.forEach((each) -> {
            System.out.println("Sent by: @" + each.getUser().getScreenName()
                    + " - " + each.getUser().getName() + "\n" + each.getText()
                    + "\n");
        });
    }


    /**
     * Search for tweets.
     * 
     * @param search Search string
     * @return List of searches
     * @throws TwitterException 
     */
    public static List<String> searcher(String search)
            throws TwitterException
    {
        List<String> searchStr = new ArrayList<>();
        
        AccessToken accessToken = new AccessToken("1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx", "y8tk4p9WWw3pzbzEZWW9pGo41JXQ3rRSZj3R1q8neYixN");
        t.setOAuthConsumer("0LNtKkDTzgAlkO7SdaBsSFzCl", "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB");
        t.setOAuthAccessToken(accessToken);

        try {
            QueryResult result = t.search(new Query(search).resultType(Query.ResultType.popular));
            result.getTweets().forEach((tweet) -> {
                searchStr.add("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            });
        } catch ( TwitterException te )
        {
            throw te;
        }
        
        return searchStr;
    }
        

    /**
     * Try to get the timeline for a user.
     * This is essentially the main point for getting (non-cached) tweets.
     * 
     * @param twitterHoroAcc Twitter horoscope account
     * @param oldestMs in mS (now - this time) [def: Constants.HRSINMS]
     * @param maxTweets Max number tweets to return [def: Constants.TWEETSLIM]
     * @return 
     * @throws TwitterException 
     */
    public static List<String> getTimeLine( TwitterAcct twitterHoroAcc, long oldestMs, long maxTweets )
            throws TwitterException
    {
        List<String> tweetsList;
        
        // The user account
        String tuser = twitterHoroAcc.getUserAcct();
        
        // How far back do we go?
        Date oldest = new Date(System.currentTimeMillis() - oldestMs);
        
        // Quick status check on this user
        ResponseList<User> rlu = t.lookupUsers(tuser);
        if( rlu.get(0).getStatus() == null )
            return null;
        
        // Get tweets from Twitter filter on datetime, limit number returned
        tweetsList = t.getUserTimeline(tuser).stream()
                    .filter(item -> item.getCreatedAt().compareTo(oldest)>0)
                    .limit(maxTweets)
                    .map(item -> item.getText())
                    .collect(Collectors.toList());

        // If specific accounts are getting lazy (few/no recent tweets) try generic searches
        if( tweetsList.isEmpty() )
        {
            Query query = new Query("from:"+twitterHoroAcc.getSearchAcct()+" "+twitterHoroAcc.getSearchTerms());
            // Idiotically the following returns void (better would be Query)
            query.setResultType(Query.ResultType.recent);
            
            List<Status> tweetsSearch = t.search(query).getTweets();
            tweetsList = tweetsSearch.stream()
                                     .map(status->status.getText())
                                     .collect(Collectors.toList());
        }
        
        return tweetsList;
    }


    /**
     * Nonce generator. (Not used atm)
     * @return 
     */
    public static String nonceGenerator()
    {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }


    /**
     * Get tweets for given (active) account.  See getTimeLine().
     * 
     * @param twitterHoroAcct
     * @return List of tweets
     */
    public static List<String> getTweets( TwitterAcct twitterHoroAcct )
    {
        // List of tweets
        List<String> lss = new ArrayList<>();
        
        // Get the tweets
        try
        {
            // Check horoscope accounts as being active
            if( twitterHoroAcct.isAcctActive() )
            {
                // Get the user timeline, if poss.
                List<String> tl = getTimeLine(twitterHoroAcct,Constants.HRSINMS,Constants.TWEETSLIM);
                if( tl != null )
                    tl.forEach((status)->{lss.add(status);});
                else
                    java.util.logging.Logger.getLogger(PrediktHoroTweets.class.getName()).log(Level.SEVERE, null, "Invalid: " +twitterHoroAcct.getUserAcct());
            }
        }
        catch( TwitterException tex )
        {
            java.util.logging.Logger.getLogger(PrediktHoroTweets.class.getName()).log(Level.SEVERE, null, tex);
        }
        
        return lss;
    }


    /*
     * Get timeline for twitter accounts for this month/zodiac sign for given
     * country/language.  Returns JSON structure.
     */
    public String getHoroTweets( String ctryLang )
    { //System.out.println("In PrediktHoroTweets.getHoroTweets... " +ctryLang);
        // JSON array (of JSON objects)
        ArrayNode ja = mapper.createArrayNode();
        
        // Get zodiac sign for today
        String zSign = SimpleZodiacDates.zsign(LocalDate.now());

        // Build tweets list for each language for 'now' zodiac sign (eg: 7Mar: Pisces)
        // Check language exists for acct: Sign->Language->List(TwitterAcct)
        // Map<String,Map<String,List<TwitterAcct>>>
        if( TwitterAstroAccts.getGrpUserAccts().get(zSign).containsKey(ctryLang) )
        {
            List<TwitterAcct> twAccts = TwitterAstroAccts.getGrpUserAccts().get(zSign).get(ctryLang);
            Collections.sort(twAccts,new SortByRank());

            twAccts.forEach((eachUser) ->
            {
                //System.out.println("::> Getting: " +eachUser.getUserAcct()+ " / " +eachUser.getSearchAcct() );
                if( !getTweets(eachUser).isEmpty() )
                {                
                    ObjectNode jo = mapper.createObjectNode();
                    jo.put("acct",eachUser.getUserAcct());
                    jo.put("tweet",getTweets(eachUser).get(0));
                    ja.add(jo);
                }
            });
        }
        else
        {
            // No content, so say so  @TODO: Improve!
            return "{\"sign\":\""+zSign+"\",\"ctryLang\":\""+ctryLang+"\",\"tweets\":\"[None for "+ctryLang+"]\"}";
        }
        
        // Stuff objects into array, wrap in 'object'
        //System.out.println("Tweet content: " +ja.toString());
        return "{\"sign\":\""+zSign+"\",\"ctryLang\":\""+ctryLang+"\",\"tweets\":"+ja.toString()+"}";
    }

    
    /**
     * Get tweets for all country/languages for right now.
     * 
     * @return List of tweets
     */
    public static List<String> getTweets4Now()
    {
        // Simple list of tweets
        List<String> tweetsList = new ArrayList<>();
        
        // Get zodiac sign for today
        String zSign = SimpleZodiacDates.zsign(LocalDate.now());
        //System.out.println("Zodiac...> " +zSign);
        
        // Build tweets list for each language for 'now' zodiac sign (eg: 7Mar: Pisces)
        TwitterAstroAccts.getGrpUserAccts().get(zSign).forEach((langCtry,twAccts) -> {
            Collections.sort(twAccts,new SortByRank());
            //System.out.println("CtryLang: " +langCtry+ ", sign: " +zSign+ ", count of twAccts: " +twAccts.size());
            
            twAccts.forEach((eachUser) ->
            {
                //System.out.println("::> GettingNow: " +eachUser.getUserAcct()+ " / " +eachUser.getSearchAcct() );
                if( !getTweets(eachUser).isEmpty() )
                {
                    tweetsList.add(getTweets(eachUser).get(0));
                    //System.out.println("   ..> " +getTweets(eachUser).get(0));
                }
            });
        });

        return tweetsList;
    }


    /**
     * Fire off 0MQ:REP socket thread.
     * Reads tweets from cache. (See ZMQJob)
     * 
     * @throws ProcessingException
     */
    public static void threadTweetSocket()
            throws ProcessingException
    {
        // JSON Schema
        final Path p = Paths.get(Constants.MSGSCHEMA);    
        // Validate msg via schema
        final JsonSchema msgSchema = JsonSchemaFactory.byDefault().getJsonSchema(p.toUri().toString());
        
        // Just the one thread (for now)
        mutex = new Semaphore(1);
        //System.out.println("...In PrediktHoroTweets.threadTweetSocket");
        
        // Thread for socket
        tweetService = Executors.newFixedThreadPool(1);

        Runnable getNSendTweets = () ->
        {
            // Blocking read until a request made (to tweet cache)
            // Get AtomicReference, get Map (multiple languages)
            // recvStr(0) defines Lang/Ctry (aa-BB: Map key, eg:zh-CN)
            try
            {
                // Lock the thread
                mutex.acquire();
                
                while( running )
                {
                    String s = socket.recvStr(0);
                    System.out.println("::> Socket recv: " +s);

                    // Check we've been sent a decent message and key
                    JsonNode root = mapper.readTree(s);  // root of JSON

                    // Validate incoming message request against schema
                    ProcessingReport prep = msgSchema.validate(root);
                    System.out.println("--[inc msg valid]--> " +prep);
        
                    // Expect twee channel request
                    if( !root.at("/msgType").textValue().equalsIgnoreCase(zmqProps.getProperty(Constants.TWTCHAN)) )
                    {
                        System.exit(3);  // *** @TODO change!!
                    }

                    String ctryLang = root.at("/message/ctryLang").textValue();
                    // In case the user somehow writes a wrong URL attribute...
                    if( !LanguageCountry.getLangCtry().containsKey(ctryLang) )
                    { System.out.println("Sending default--------> " +TweetsCache.getHoroTweets().get().get(Constants.ENGB));
                        socket.send(TweetsCache.getHoroTweets().get().get(Constants.ENGB));
                    }
                    else
                    {
                        //System.out.println("ctryLang: " +ctryLang);
                        String tweets = TweetsCache.getHoroTweets().get().get(ctryLang);
                        TweetsCache.tweetCacheDump();
                        BasicTSLogger.Logger(" Tweets going back: " +tweets );
                        if( tweets.length() != 0 )
                            socket.send(tweets);
                        else // Send default language
                            socket.send(TweetsCache.getHoroTweets().get().get(Constants.ENGB));
                    }
                }
            }
            catch( ProcessingException pex )
            {
                // Shutdown the thread
                mutex.release();
                tweetService.shutdown();
                
                // Log
                System.out.println("!ERROR (threadTweetSocket): " +pex.getMessage() );
                System.exit(5);
            }
            catch( IOException | InterruptedException exx )
            {
                // Shutdown the thread
                mutex.release();
                tweetService.shutdown();
                
                // Log
                System.out.println("!ERROR (threadTweetSocket): " +exx.getMessage() );
                System.exit(4);
            }
            catch( ZMQException zex )
            {
                zex.printStackTrace();
                running = false;
            }
        };
        
        // Execute the get/send tweets
        if( running ) { tweetService.execute(getNSendTweets); }
        
        //System.out.println("...Out PrediktHoroTweets.threadTweetSocket");
    }
    
    
    /**
     * Access to mutex
     */
    private static Semaphore getMutex()
    {
        return mutex;
    }
    
    
    /**
     * Access to tweet service
     */
    private static ExecutorService getTweetService()
    {
        return tweetService;
    }

    
    /**
     * Initialise access to Twitter, set up 0MQ socket (type: REP)
     * 
     * @throws Exception
     */
    public static void initHoroTweets()
            throws Exception
    {
        // Read the ZMQ properties (must have read file first!)
        zmqProps = PrediktProperties.getPropsMap().get(Constants.ZMQPROPSKEY);
        
        // 0MQ setup (usually: bind for server, connect for client)
        socket = zcontext.createSocket(SocketType.REP);
        System.out.println("Get tweets socket at: "+zmqProps.getProperty(Constants.ZMQ1URL,Constants.ZMQ1URLDEF));
        if( !socket.bind(zmqProps.getProperty(Constants.ZMQ1URL,Constants.ZMQ1URLDEF)) )
        {
            throw new ZMQException("Socket didn't bind",0);
        }
        
        // Wait for initialise to complete
        while( !ready )
            Thread.sleep(1000);
        System.out.println("Get tweets scheduler, first time run completed...");
    }


    /**
     * Get tweets (scheduled run).  Stored in Atomic string.
     * @param jobExecCtx
     * @throws JobExecutionException
     */
    @Override
    public void execute( JobExecutionContext jobExecCtx )
            throws JobExecutionException
    {
        // Cache tweets... for ctry/language set (from triplet)
        ctryList.forEach(trip->{
            String ctryLang = trip.getValue2();
            TweetsCache.setHoroTweets(ctryLang,getHoroTweets(ctryLang));
        });
        
        // Flag ready to go!
        ready = true;
    }

    /**
     * Get set of countries
     * @return 
     */
    public List<Triplet<String, String, String>> getCtryList() {
        return ctryList;
    }

    /**
     * Get full country data
     * @return 
     */
    public static Map<String, FullCountry> getLcDb() {
        return lcDb;
    }
    


    /*
     *------------------------------------
     *          M A I N  (Test)
     *------------------------------------
     */
    public static void main(String[] args)
            throws Exception
    {
        // Initialise 0MQ and twitter system
        ZMQJob.init();
        ZMQJob.tweetsInit();
        
        // Let's se what we have
        TwitterAstroAccts.dumpTwitterAstroAccts();

        // Get tweets for each language for 'now' zodiac sign (eg: 7Mar: Pisces), trip.getValue2 is Lang
        getTweets4Now().forEach(tweet -> {
            System.out.println("Today's tweets..> " +tweet);
        });
        
        // Need an object for following...
        PrediktHoroTweets pht = new PrediktHoroTweets();
        
        // Show JSON representation of these tweets
        pht.ctryList.forEach(trip->{
            String ctryLang = trip.getValue2();
            TweetsCache.setHoroTweets(ctryLang,pht.getHoroTweets(ctryLang));
            System.out.println(ctryLang+"> " +pht.getHoroTweets(ctryLang));
        });
        
        // Test cache
        System.out.println("Tweet cache test...");
        String tweets = TweetsCache.getHoroTweets().get().get("fr-FR");
        if( tweets != null )
            System.out.println("FR> " +tweets);
        tweets = TweetsCache.getHoroTweets().get().get("en-GB");
        if( tweets != null )
            System.out.println("EN> " +tweets);
        
        /*
         * Shutdown system
         */
        // Message frontend to close sockets
        // TBD
        
        // Kill all threads etc.
        System.out.println("Mutex release...");
        getMutex().release();
        
        // Ignore FE requests
        System.out.println("Stop receiving tweet socket requests...");
        running = false;
        socket.close();
        zcontext.close();

        
        // Scheduler shutdown
        System.out.println("Get all scheduled jobs and stop...");
        Scheduler scheduler = ZMQJob.getScheduler();
        List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();
        for( JobExecutionContext job: jobs )
        {
            scheduler.deleteJob(job.getJobDetail().getKey());
        }
        
        System.out.println("Scheduler stop...");
        scheduler.shutdown();

        // Tweet service shutdown
        System.out.println("Tweet service shutdown (this could take a minute)...");
        ExecutorService pool = getTweetService();
        try
        {
            pool.shutdown();
            // Wait a while for existing tasks to terminate
            if( !pool.awaitTermination(Constants.POOLWAIT, TimeUnit.SECONDS) )
            {
              pool.shutdownNow(); // Cancel currently executing tasks
              // Wait a while for tasks to respond to being cancelled
              if( !pool.awaitTermination(Constants.POOLWAIT, TimeUnit.SECONDS) )
                  System.err.println("Pool did not terminate");
            }
        }
        catch( InterruptedException iex )
        {
            pool.shutdownNow(); // Force shutdown
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
        
        // Wind up
        System.out.println("System has been shutdown...");
    }
}

/*
 * Fire off a scheduled job to get horoscope tweets regularly
 */
package eu.discoveri.zmq.scheduler;

import eu.discoveri.predikt.i18n.LanguageCountry;
import eu.discoveri.predikt.twitter.PrediktHoroTweets;
import eu.discoveri.predikt.i18n.PrediktI18N;
import eu.discoveri.predikt.system.Constants;
import eu.discoveri.predikt.utils.PrediktProperties;
import eu.discoveri.predikt.twitter.TwitterAstroAccts;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Trigger;
import org.quartz.JobDetail;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ZMQJob
{
    private static Scheduler scheduler;
    
    /*
     * Start Tweet scheduler
     */
    private static void tweetScheduler()
            throws SchedulerException
    {
        // Grab the Scheduler instance from the Factory...
        scheduler = StdSchedulerFactory.getDefaultScheduler();

        // What are we scheduling?
        JobDetail job = newJob(PrediktHoroTweets.class)
            .withIdentity(Constants.JOBID, Constants.GROUPID)
            .build();

        Trigger trigger = newTrigger()
            .withIdentity(Constants.JOBID, Constants.GROUPID)
            .startNow()
            .withSchedule(simpleSchedule()
                .withIntervalInHours(Constants.DELAY) // TEST: .withIntervalInSeconds(20)
                .repeatForever())            
            .build();

        // ...and start it off
        scheduler.scheduleJob( job, trigger );
        scheduler.start();
    }
    
    /**
     * Access to scheduler
     * @return 
     */
    public static Scheduler getScheduler()
    {
        return scheduler;
    }

    
    /**
     * Initialise ctrylang, properties etc.
     */
    public static void init()
            throws Exception
    {
        // @TODO: Mock initialise
        // Build and read the 'database'
        LanguageCountry.initLangCtryDb();
        //lcDb = LanguageCountry.getLangCtryDb();
        
        // Load properties from file
        PrediktProperties.readPropertiesFile(Constants.ZMQPROPSKEY,Constants.ZMQPROPSFILE);
    }
    
    /**
     * Init Twitter get/send
     */
    public static void tweetsInit()
            throws Exception
    {
        // Build the horoscope accounts list
        System.out.print("  Build and check Twitter horoscope accounts list (this takes time)...");
        TwitterAstroAccts.taaInit();
        
        // Check twitter acct 'db'
        TwitterAstroAccts.checkTwits();
        System.out.println("   done!");
        
        // *** Fire off process to go out regularly and update tweets in cache
        System.out.println("  Start GetTweets scheduler...");
        tweetScheduler();
        
        // Initialise Twitter socket (NB: waits for first schedule to complete)
        System.out.println("  Start incoming FE twitter socket...");
        PrediktHoroTweets.initHoroTweets();

        // Start 0MQ tweets socket thread
        System.out.println("  Initialise Twitter server, BE socket...");
        PrediktHoroTweets.threadTweetSocket();
    }
    
    /**
     * Init I18N sending
     */
    public static void i18nInit()
            throws Exception
    {
        // Initialise I18N server
        PrediktI18N.initI18N();
    }
    
    
    /*---------------------------------------
     *         M A I N
     *---------------------------------------
     * @TODO: Change from MAIN sometime
     * (Initialise from main server)
     *
     * Order (important):
     *  0. Get ctry/lang db data
     *  1. Load properties
     *  2. Init I18N/0MQ socket
     *  3. Init Twitter accounts list & check
     *  4. Start Tweets cron job
     *  5. Init Twitter/0MQ socket
     */
    public static void main(String[] args)
            throws Exception
    {
        // Let user know to wait
        System.out.println("*** Nota Bene: Wait for 'Finally!'");
        
        // Initialise
        System.out.println("Init CtryLang db and Properties...");
        init();
        
        // Initialise database server
        //PrediktDb.graphQLsrv();
        
        // Start tweets scheduling
        System.out.println("Start tweets scheduling...");
        tweetsInit();
        
        // Initialise I18N server
        System.out.println("Initialise I18N server/socket, accept requests...");
        i18nInit();
        
        // OK! Tell user we're up and running
        System.out.println("Finally!  Accept remote requests...");
    }
}

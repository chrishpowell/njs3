/*
 * Twitter feed
 */
package eu.discoveri.predikttwitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PrediktTwitter {

    public static void homeline()
            throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("0LNtKkDTzgAlkO7SdaBsSFzCl", "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB");

        RequestToken requestToken = twitter.getOAuthRequestToken();
        System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());

        AccessToken accessToken = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Hit above Authorization URL and Input PIN here: ");
            String pin = br.readLine();

            accessToken = twitter.getOAuthAccessToken(requestToken, pin);

        } catch (TwitterException te) {

            System.out.println("Failed to get access token, caused by: "
                    + te.getMessage());
        }

        System.out.println("Access Token: " + accessToken.getToken());
        System.out.println("Access Token Secret: " + accessToken.getTokenSecret());

        // updating twitter status
        twitter.updateStatus("hi.. im updating this using Namex Tweet for Demo");

        System.out.println("\nReading Twitter Timeline:");

        // I'm reading your timeline
        ResponseList<Status> list = twitter.getHomeTimeline();
        for (Status each : list) {
            System.out.println("Sent by: @" + each.getUser().getScreenName()
                    + " - " + each.getUser().getName() + "\n" + each.getText()
                    + "\n");
        }
    }

    public static void searcher()
            throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        AccessToken accessToken = new AccessToken("1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx", "y8tk4p9WWw3pzbzEZWW9pGo41JXQ3rRSZj3R1q8neYixN");
        twitter.setOAuthConsumer("0LNtKkDTzgAlkO7SdaBsSFzCl", "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB");
        twitter.setOAuthAccessToken(accessToken);

        try {
            QueryResult result = twitter.search(new Query("#Pisces"));
            for (Status tweet : result.getTweets()) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

    public static List<String> getter(String tuser)
            throws Exception
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0LNtKkDTzgAlkO7SdaBsSFzCl")
                .setOAuthConsumerSecret("inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB")
                .setOAuthAccessToken("1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx")
                .setOAuthAccessTokenSecret("y8tk4p9WWw3pzbzEZWW9pGo41JXQ3rRSZj3R1q8neYixN");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        return getTimeLine(twitter,tuser);
    }
    
    public static List<String> getTimeLine( Twitter twitter, String tuser )
            throws TwitterException
    {
        return twitter.getUserTimeline(tuser).stream()
          .map(item -> item.getText())
          .collect(Collectors.toList());
    }

    public static String nonceGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }

    public static void main(String[] args)
            throws Exception
    {
//        System.out.println("Nonce: " +nonceGenerator() );
//        System.out.println("Milli-secs: " +System.currentTimeMillis()/1000);
//        searcher();
        for( String status: getter("Nasa") )
        {
            System.out.println("> " +status);
        }
        
        searcher();
    }
}

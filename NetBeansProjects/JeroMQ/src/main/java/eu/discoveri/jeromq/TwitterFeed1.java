/*
 * TwitterFeed over 0MQ
 */
package eu.discoveri.jeromq;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TwitterFeed1
{
    static String   oauth_consumer_key = "0LNtKkDTzgAlkO7SdaBsSFzCl",
                    oauth_consumer_secret = "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB",
                    oauth_access_token = "1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx",
                    oauth_access_secret = "y8tk4p9WWw3pzbzEZWW9pGo41JXQ3rRSZj3R1q8neYixN";
    static String   server = "tcp://127.0.0.1:8777";
    static String[] twitterUser = {"astrologyshow","Nasa"};
            
    /**
     * Setup Twitter
     * @return
     * @throws Exception 
     */
    public static Twitter setupTwitter()
            throws Exception
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(oauth_consumer_key)
                .setOAuthConsumerSecret(oauth_consumer_secret)
                .setOAuthAccessToken(oauth_access_token)
                .setOAuthAccessTokenSecret(oauth_access_secret);
        TwitterFactory tf = new TwitterFactory(cb.build());

        return tf.getInstance();
    }

    /**
     * Setup ZeroMQ.  Not a 0MQ socket is NOT a standard socket
     * @param uri
     * @return
     * @throws ZMQException 
     */
    public static ZMQ.Socket setupZMQ(URI uri)
            throws ZMQException
    {
        ZMQ.Socket socket;
        
        try( ZContext context = new ZContext() )
        {
            // Socket to talk to clients: "tcp://127.0.0.1:8777"
            socket = context.createSocket(ZMQ.REP);
            if (!socket.bind(uri.toString()))
                {  throw new ZMQException("Socket didn't bind", 0); }
        }

        return socket;
    }
    
    /**
     * Get a Twitter account timeline.
     * @param twitter
     * @param tuser
     * @return
     * @throws TwitterException 
     */
    public static List<String> getTimeLine( Twitter twitter, String tuser )
            throws TwitterException
    {
        return twitter.getUserTimeline(tuser).stream()
          .map(item -> item.getText())
          .collect(Collectors.toList());
    }
    
    public static void graphQLsetup()
    {}
    
    
    /**
     * M A I N
     * =======
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args)
            throws Exception
    {
        ZMQ.Socket socket = setupZMQ(new URI(server));
        Twitter twitter = setupTwitter();
        
        while( !Thread.currentThread().isInterrupted() )
        {
            String response = null;
            
            // Block until a message is received
            byte[] reply = socket.recv();

            // Print the message
            System.out.println( "Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]" );

            // Send a response
            for( String tuser: twitterUser )
            {
                List<String> userTL = getTimeLine(twitter,tuser);
                response += userTL.get(0);
            }
            
            socket.send(response.getBytes(ZMQ.CHARSET), 0);
        }

    }
}

/*
 */
package eu.discoveri.predikttwitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import sun.util.logging.PlatformLogger;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TwitterOAuth
{
    final static String searchItem = "Nasa";
    static String source = "https://api.twitter.com/1.1/search/tweets.json?q=" + searchItem + "&result_type=popular";
    static String oauth_consumer_key = "0LNtKkDTzgAlkO7SdaBsSFzCl";
    static String oauth_cons_secret = "inrVncqXYao01KeBTKK5HJZNb7T6K4Wqy5dxBWgXsxwoBRoXbB";
    static String oauth_tokn_secret = "";
    static String oauth_signature = null;
    static String oauth_signature_method = "'HMAC-SHA1'";
    static String oauth_timestamp = null;
    static String oauth_token = "1008662854118137861-MTWBfSJD88G8Xrzssx3RTnF8kjieDx";
    
    // Percent encoding
    public static String pctEncoding( String inp )
    {
        String hex = null;
        StringBuilder sb = new StringBuilder();
        char[] sArr = inp.toCharArray();
        
        for( char ch: sArr )
        {
            if( !( (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '-' || ch == '.' || ch == '_' || ch == '~') )
            {
                hex = Integer.toHexString(ch);  // Convert to hex
                sb.append('%'+hex);
            }
            else
            {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    // Nonce generator
    public static String nonceGenerator()
    {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++)
        {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }

    /*
     * M A I N
     * =======
     */
    public static void main(String[] args)
            throws Exception
    {
        // include_entities 	true
        // oauth_consumer_key 	xvz1evFS4wEEPTGEFPHBog
        // oauth_nonce 	kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg
        // oauth_signature_method 	HMAC-SHA1
        // oauth_timestamp 	1318622958
        // oauth_token 	370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb
        // oauth_version 	1.0
        
        sun.util.logging.PlatformLogger.getLogger("sun.net.www.protocol.http.HttpURLConnection").setLevel(PlatformLogger.Level.ALL);
        
        int ts = Double.valueOf(System.currentTimeMillis() / 1000.).intValue();
        StringBuilder authSb = new StringBuilder();
//        String oauth_signature = pctEncoding(oauth_cons_secret) +"&"+ pctEncoding(oauth_tokn_secret);

        //Prepend ' OAuth '
        authSb.append("include_entities=true")
                .append("&oauth_consumer_key=").append(oauth_consumer_key)
                .append("&oauth_nonce=").append(nonceGenerator())
                .append("&oauth_signature_method=HMAC-SHA1")
                .append("&oauth_timestamp=").append(ts)
                .append("&oauth_token=").append(oauth_token)
//                .append("&oauth_signature=").append(oauth_signature)
                .append("&oauth_version=1.0");

        System.out.println("Sending: " +authSb );
        
//        URLConnection url = new URL(source).openConnection();
//        HttpsURLConnection connection = (HttpsURLConnection) url;
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("Authorization", authSb.toString());
//
//        int responseCode = connection.getResponseCode();
//        System.out.println("Resp code: " + responseCode);
//
//        StringBuffer response;
//        try( BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())) )
//        {
//            String inputLine;
//            response = new StringBuffer();
//            while( (inputLine = in.readLine()) != null )
//            {
//                response.append(inputLine);
//            }
//        }
//
//        System.out.println(response.toString());
    }
}

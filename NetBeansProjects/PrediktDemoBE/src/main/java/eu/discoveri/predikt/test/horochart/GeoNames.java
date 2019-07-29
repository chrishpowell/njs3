/*
 * Lat/Lon to Timezone test
 */
package eu.discoveri.predikt.test.horochart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import org.json.JSONObject;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GeoNames
{   
    private static JSONObject getGeoname( URL url )
    {
        String output, concat = "";
        
        try 
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if( conn.getResponseCode() != 200 )
            {
                throw new RuntimeException("Failed : HTTP error code : " +conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while( (output = br.readLine()) != null )
            {
                System.out.println(output);
                concat += output;
            }
            conn.disconnect();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        
        return new JSONObject(concat);
    }
    
    
    public static void main(String[] args)
            throws Exception
    {
        DecimalFormat df = new DecimalFormat("###.####");
        JSONObject jo = null;
                
        URL url = new URL("http://api.geonames.org/search?q=London,Ontario&maxRows=1&username=discoveri");
        jo = getGeoname(url).getJSONArray("geonames").getJSONObject(0);
        
        String latitude = df.format(Double.parseDouble(jo.getString("lat")));
        String longitude = df.format(Double.parseDouble(jo.getString("lng")));

        url = new URL("http://api.geonames.org/timezoneJSON?lat="+latitude+"&lng="+longitude+"&username=discoveri");
        jo = getGeoname(url);

        System.out.println("timezone---> " +jo.getString("timezoneId"));
    }
}


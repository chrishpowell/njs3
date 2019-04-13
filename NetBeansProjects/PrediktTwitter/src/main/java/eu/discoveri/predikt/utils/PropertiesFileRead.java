/*
 * Read properties file
 */
package eu.discoveri.predikt.utils;

import eu.discoveri.predikt.exceptions.PropertyKeyExistsException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties; 


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PropertiesFileRead
{
    private static Map<String,Properties> propsMap = new HashMap<>();
    
    /**
     * Read a properties file, park in Map.
     * 
     * @param key
     * @param propsFile
     * @return
     * @throws IOException 
     * @throws PropertyKeyExistsException The key already exists
     */
    public static Map<String,Properties> readPropertiesFile( String key, String propsFile )
            throws IOException, PropertyKeyExistsException
    {
        Properties props = new Properties();
	InputStream input = null;

        // Load the properties file
	try
        {
            input = new FileInputStream(propsFile);
            props.load(input);
	}
        catch( IOException iox )
        {
            throw iox;
	} finally {
            if( input != null )
            {
                try
                {
                    input.close();
                }
                catch( IOException iox )
                {
                    throw iox;
                }
            }
	}
        
        if( propsMap.containsKey(key) )
            throw new PropertyKeyExistsException(key);
        else
            propsMap.put(key, props);
        
        return propsMap;
    }

    /**
     * Get the Properties map
     * @return 
     */
    public static Map<String, Properties> getPropsMap()
        { return propsMap; }
}

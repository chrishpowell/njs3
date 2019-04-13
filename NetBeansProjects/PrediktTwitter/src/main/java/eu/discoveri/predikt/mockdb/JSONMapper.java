/*
 * Maps a class to JSON for this mock db
 */
package eu.discoveri.predikt.mockdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class JSONMapper
{
    // JSON Object mapper
    ObjectMapper mapper = new ObjectMapper();
    
        
    /**
     * Convert this to JSONObject
     * @throws JsonProcessingException
     * @return 
     */
    public String toJSON()
            throws JsonProcessingException
    {
        return mapper.writeValueAsString(this);
    }
}

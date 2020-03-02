/*
 */
package eu.discoveri.graph;

import eu.discoveri.config.Constants;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


/**
 * Languages handled.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Language
{
    // Language to model file
    private final static EnumMap<LangCode,Map<String,String>> langMap = new EnumMap<>(LangCode.class);
    // Populate the map
    static
    {
        // English
        Map<String,String> enModel = new HashMap<>();
        enModel.put(Constants.SENTMODEL, Constants.ENSENTFILE);
        enModel.put(Constants.TOKENMODEL, Constants.ENTOKENFILE);
        enModel.put(Constants.SEQMODEL, Constants.ENPOSMEFILE);
        enModel.put(Constants.LEMMATIZE, Constants.ENLEMMAFILE);
        langMap.put(LangCode.en,enModel);
        
        // Spanish
        Map<String,String> esModel = new HashMap<>();
        esModel.put(Constants.SENTMODEL, Constants.ESSENTFILE);
        esModel.put(Constants.TOKENMODEL, Constants.ESTOKENFILE);
        esModel.put(Constants.SEQMODEL, Constants.ESPOSMEFILE);
        esModel.put(Constants.LEMMATIZE, Constants.ESLEMMAFILE);
        langMap.put(LangCode.es,esModel);
        
        // Portuguese
        
        // French
        
        // German
        
        // Russian
        
        // Hindi
        
        // Mandarin
    }
    
    /**
     * Get language models.
     * Map<LngCode,Map<String,String>>, eg: <LangCode.en,<"Sentence","en-sent.bin">>
     * @return 
     */
    public static EnumMap<LangCode,Map<String,String>> getLangModels() { return langMap; }
}

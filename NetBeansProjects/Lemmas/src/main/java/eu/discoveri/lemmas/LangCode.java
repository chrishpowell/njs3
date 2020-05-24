/*
 */
package eu.discoveri.lemmas;

import java.util.stream.Stream;


/**
 * Language code to model.
 * 
 * @author Chris Powell, Discoveri OU
 */
public enum LangCode
{
    en("english"),
    es("spanish"),
    pt("portuguese"),
    fr("french"),
    de("german"),
    ru("russian"),
    hi("hindi"),
    zh("chinese");          // Mandarin

    private final String    name;
    
    LangCode(String name)
    {
        this.name = name;
    }

    /**
     * Get name/descrip.
     * @return 
     */
    public String getName() { return name; }
    
    /**
     * Stream the enum.
     * @return 
     */
    public static Stream<LangCode> stream()
    {
        return Stream.of(LangCode.values()); 
    }
}

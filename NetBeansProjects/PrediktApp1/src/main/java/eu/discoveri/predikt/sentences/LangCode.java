/*
 */
package eu.discoveri.predikt.sentences;


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

    public String getName() { return name; }
}

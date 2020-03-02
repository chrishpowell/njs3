/*
 */
package eu.discoveri.graph;

/**
 * Language code to model.
 * 
 * @author Chris Powell, Discoveri OU
 */
public enum LangCode
{
    en("English"),
    es("Spanish"),
    pt("Portuguese"),
    fr("French"),
    de("German"),
    ru("Russian"),
    hi("Hindi"),
    zh("Chinese");          // Mandarin

    private final String name;
    LangCode(String name) { this.name = name; }

    public String getName() { return name; }
}

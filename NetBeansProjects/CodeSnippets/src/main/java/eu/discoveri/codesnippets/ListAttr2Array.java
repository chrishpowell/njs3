/*
 */
package eu.discoveri.codesnippets;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ListAttr2Array
{
    static List<Container> lc = Arrays.asList(  new Container("fred"),
                                                new Container("fred"),
                                                new Container("fred"),
                                                new Container("fred")  );

    public static void main(String[] args)
    {
        String[] stoks = new String[lc.size()];
        int idx = 0;
        for( Container c: lc )
        {
            stoks[idx++] = c.getStr();
        }
    }
}

class Container
{
    private final String  str;
    
    public Container(String str) { this.str = str; }
    public String getStr() { return str; }
}
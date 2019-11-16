/*
 */
package eu.discoveri.predikt.simpletests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Add in a 'triangular' fashion to Map with two 'keys'.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class TriangularAdd
{
    static Map<P1P2Key,P1P2> triMap = P1P2.getTriMap();
    
    // Initialise all planets
    private final static List<P> pList = new ArrayList<P>() {{
        add(new P("Sun",true));
        add(new P("Moon",true));
        add(new P("Mars",true));
        add(new P("NAP",false));
        add(new P("Jupiter",true));
    }};
    
    public static void main(String[] args)
    {

        // Check for (major) aspects
        pList.forEach((p1) -> {
            pList.forEach((p2) -> {
                // Don't compare same planet against itself; 
                // AND ignore display==false either planet;
                if( !p1.equals(p2) &&
                    (p1.isOnoff() && p2.isOnoff()) )
                {
                    //... If a map entry, don't redo (eg: Sun:Moon/Moon:Sun);
                    if( !P1P2.checkFlippedKey(p1.getName(), p2.getName()) )
                    {
                        System.out.println("1--> p1: " +p1.getName()+ ", p2: " +p2.getName());
                        triMap.put(new P1P2Key(p1.getName(),p2.getName()),new P1P2(0,0,p1.getName(),p2.getName(),p1.isOnoff(),p2.isOnoff()));
                    }
                }
            });
        });
        
        P1P2.dumpTriMap();
    }
}
                
class P
{
    private String  name;
    private boolean onoff;

    public P(String name, boolean onoff)
    {
        this.name = name;
        this.onoff = onoff;
    }

    public String getName() { return name; }
    public boolean isOnoff() { return onoff; }
}

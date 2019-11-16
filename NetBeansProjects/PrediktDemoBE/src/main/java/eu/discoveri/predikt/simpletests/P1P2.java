/*
 * Test Map
 */
package eu.discoveri.predikt.simpletests;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class P1P2
{
    private final int   p1i, p2i;
    private final String p1s, p2s;
    private final boolean p1b, p2b;

    /**
     * Constructor.
     * @param p1i
     * @param p2i
     * @param p1s
     * @param p2s
     * @param p1b
     * @param p2b 
     */
    public P1P2(int p1i, int p2i, String p1s, String p2s, boolean p1b, boolean p2b)
    {
        this.p1i = p1i;
        this.p2i = p2i;
        this.p1s = p1s;
        this.p2s = p2s;
        this.p1b = p1b;
        this.p2b = p2b;
    }

    // Map to this
    private final static Map<P1P2Key, P1P2> triMap = new HashMap<>();
    public static Map<P1P2Key,P1P2> getTriMap() { return triMap; }
    public static void dumpTriMap()
    {
        if( triMap.isEmpty() ) return;
        triMap.forEach((k,v) -> {
            System.out.println("triMap..> " +k+":"+v);
        });
    }
    
    // Check key - and flipped key
    public static boolean checkFlippedKey( String p1, String p2 )
    {
        return triMap.containsKey(new P1P2Key(p1,p2)) || triMap.containsKey(new P1P2Key(p2,p1) ); 
    }

    // Getters
    public int getP1i() { return p1i; }
    public int getP2i() { return p2i; }
    public String getP1s() { return p1s; }
    public String getP2s() { return p2s; }
    public boolean isP1b() { return p1b; }
    public boolean isP2b() { return p2b; }
    
    @Override
    public String toString()
    {
        return "[{"+p1i+","+p2i+"}{"+p1s+","+p2s+"}{"+p1b+","+p2b+"}]";
    }
}

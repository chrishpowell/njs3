/*
 * Key for PlanetsAspect map
 */
package eu.discoveri.predikt.graphics;

import java.util.Objects;


/**
 * Map key (NB: HashMap keys are unique [and get overwritten]).
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class P1P2KeyString implements Comparable<P1P2KeyString>
{
    private final String    p1, p2;

    public P1P2KeyString(String p1, String p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public String getP1() { return p1; }
    public String getP2() { return p2; }
    
    /**
     * Logical key equality (not reference)
     * @param o  (P1P2KeyString)
     * @return 
     */
    @Override
    public boolean equals(Object o)
    {
        if( o == null ) return false;
        if( this.getClass() != o.getClass() ) return false;
        
        return (this.p1.equals(((P1P2KeyString)o).p1) && this.p2.equals(((P1P2KeyString)o).p2) );
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.p1);
        hash = 37 * hash + Objects.hashCode(this.p2);
        return hash;
    }
    
    @Override
    public String toString()
    {
        return "["+p1+","+p2+"]";
    }

    /**
     * Compare: first, this.p1 to o.p1; second this.p2 to o.p2.
     * For TreeMap(ping) to work
     * @param o
     * @return 
     */
    @Override
    public int compareTo(P1P2KeyString o)
    {
        /* 
         * compareTo() will return:
         *  < 0 if this(keyword) is "less" than object o,
         *  > 0 if this(keyword) is "greater" than object o and
         *  0 if they are equal.
         */
        int key1 = p1.compareTo(o.getP1());
        if( key1 == 0 )
            return p2.compareTo(o.getP2());
        else
            return key1;
    }
}

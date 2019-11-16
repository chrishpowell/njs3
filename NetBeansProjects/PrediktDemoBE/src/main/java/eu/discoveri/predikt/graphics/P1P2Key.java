/*
 * Key for PlanetsAspect map
 */
package eu.discoveri.predikt.graphics;

import eu.discoveri.prediktdemobe.Planet;
import java.util.Objects;


/**
 * Map key (NB: HashMap keys are unique [and get overwritten]).
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class P1P2Key implements Comparable<P1P2Key>
{
    private final Planet    p1, p2;

    public P1P2Key(Planet p1, Planet p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Planet getP1() { return p1; }
    public Planet getP2() { return p2; }
    
    /**
     * Logical key equality (not reference)
     * @param o  (P1P2Key)
     * @return 
     */
    @Override
    public boolean equals(Object o)
    {
        if( o == null ) return false;
        if( this.getClass() != o.getClass() ) return false;
        
        return (this.p1.getName().equals(((P1P2Key)o).p1.getName()) && this.p2.getName().equals(((P1P2Key)o).p2.getName()) );
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.p1);
        hash = 37 * hash + Objects.hashCode(this.p2);
        return hash;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return "["+p1.getName()+","+p2.getName()+"]";
    }

    /**
     * Compare: first, this.p1 to o.p1; second this.p2 to o.p2.
     * For TreeMap(ping) to work
     * @param o
     * @return 
     */
    @Override
    public int compareTo(P1P2Key o)
    {
        /* 
         * compareTo() will return:
         *  < 0 if this(keyword) is "less" than object o,
         *  > 0 if this(keyword) is "greater" than object o and
         *  0 if they are equal.
         */
        int key1 = p1.getName().compareTo(o.getP1().getName());
        if( key1 == 0 )
            return p2.getName().compareTo(o.getP2().getName());
        else
            return key1;
    }
}

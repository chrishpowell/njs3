/*
 * Key for Map
 */
package eu.discoveri.predikt.simpletests;

import java.util.Objects;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */

// Map key (HashMap keys are unique [or get overwritten])
public class P1P2Key
{
    private final String    p1, p2;

    public P1P2Key(String p1, String p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public String getP1() { return p1; }
    public String getP2() { return p2; }
    
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
        if( this.p1.equals(((P1P2Key)o).p1) && this.p2.equals(((P1P2Key)o).p2) ) return true;
        
        return false;
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
}

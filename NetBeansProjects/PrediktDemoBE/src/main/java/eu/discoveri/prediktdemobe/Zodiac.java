/*
 * Zodiac
 */
package eu.discoveri.prediktdemobe;

//import {elements} from "./elements";

import eu.discoveri.predikt.exception.ZSignDegreeException;
import eu.discoveri.predikt.exception.ZSignRangeException;
import java.nio.file.Paths;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Zodiac
{
    private ZRing   ring;

    
    public Zodiac( double outer, double inner, double aux )
    {
        ring = new ZRing( outer, inner, aux );
    }

    public ZRing getRing() {
        return ring;
    }


    public int getStartSignIndex( int sign )
        throws ZSignRangeException
    {
        if (sign < 0 || sign > 11)
        {
            throw new ZSignRangeException("Zodiac start sign index must be in the range between 0 and 11.");
        }

        // *** This'll become a Map
        int index = 0;
        for( Sign s: Sign.getSigns() )
        {
            if( s.getOrder() == sign )
                return index;
            index++;
        }
      
        return -1;
    }


    public int getStartSignIndex( String sign )
    {
        int index = 0;
        for( Sign s: Sign.getSigns() )
        {
            if( s.getName() == sign )
                return index;
            index++;
        }
      
        return -1;
    }


    public double validateSignDegree( double degree )
            throws ZSignDegreeException
    {
      if( degree < 0.f || degree > 30.f )
      {
        throw new ZSignDegreeException("Zodiac degree must be in the range between 0 and 30.");
      }

      return degree;
    }
}


class ZRing
{
    private final double  inner, outer;
    private final double  aux;

    public ZRing( double inner, double outer, double aux )
    {
        this.inner = inner;
        this.outer = outer;
        this.aux = aux;
    }
    
    public double getInnerAuxiliary()
    {
        return inner + aux;
    }
    
    public double betweenInnerOuter()
    {
        return outer - ((outer - inner)/2.);
    }
}

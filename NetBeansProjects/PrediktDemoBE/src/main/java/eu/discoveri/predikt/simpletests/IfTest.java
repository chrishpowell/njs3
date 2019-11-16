/*
 */
package eu.discoveri.predikt.simpletests;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class IfTest
{
    public static void main(String[] args)
    {
        P1P2 p1p2a = new P1P2(1,1,"a","b",true,true);
        P1P2 p1p2b = new P1P2(1,1,"a","b",true,false);
        
        System.out.println("1st assertion: " +(p1p2a.getP1i()!=p1p2b.getP1i()) );
        
        if( p1p2a.getP1i()!=p1p2b.getP1i() &&  p1p2a.isP1b() )
        {
            System.out.println("1..>  OK, true");
        }
        
        if( p1p2a.getP1i()==p1p2b.getP1i() &&  p1p2a.isP1b() )
        {
            System.out.println("2..>  OK, true");
        }
        
        // Does not fail 'cos first element fails
        if( p1p2a.getP1i()!=p1p2b.getP1i() || p1p2a.isP1b() )
        {
            System.out.println("3..>  OK, true?");
        }
    }
}


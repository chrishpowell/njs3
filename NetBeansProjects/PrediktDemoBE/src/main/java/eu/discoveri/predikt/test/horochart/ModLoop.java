/*
 * How to loop and mod (%) at same time
 */

package eu.discoveri.predikt.test.horochart;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ModLoop
{
    private static void modLoop()
    {
        int SIZE = 7;
        int offs = 3;
        for( int ii = 0; ii<SIZE; ii++ )
        {
            int jj = (ii+offs)%SIZE;
            if( jj == 0 ) jj = SIZE;
            System.out.println("> " +jj);
        }
    }

    private static void jumpLoop()
    {
        for( int ii=1; ii<=4; ii++ )
        {
            for( int jj=0; jj<=1; jj++ )
            {
                int zz = 2*ii+(ii-1)+jj;
                System.out.println("..> " +zz);
            }
        }
    }

    public static void main(String[] args)
    {
//        modLoop();
        jumpLoop();
    }
}

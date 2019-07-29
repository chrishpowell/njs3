/*
 * test array char substitution
 */

package eu.discoveri.predikt.test.horochart;

import java.util.Arrays;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ArraySubstTst
{
    static String vars = "     0.2287184500000000e+07    0.2287216500000000e+07   -0.4533759924232486e+08";

    public static void main(String[] args) 
    {
        String interm = vars.trim().replaceAll(" +",":");
        System.out.println("..> "+interm);
        String[] arrOfVals = interm.split(":");
        for( String v: arrOfVals )
        {
            System.out.println(v);
            //System.out.println("..> " +Double.parseDouble(v));
        }
    }
}

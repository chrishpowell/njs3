/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */

package eu.discoveri.codesnippets.graphs;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BigO
{
    private static double  NUMPAGES = 50000.d;
    private static double  SENTSPERPAGE = 20.d;
    private static double  TIMEPEROP = 0.0000001d;
    
    public static void main(String[] args)
    {
        double numSents = NUMPAGES * SENTSPERPAGE;
        double approxEdges = numSents * numSents / 2.d;
        double proctime = numSents * approxEdges + numSents * numSents * Math.log(numSents);
        double procsecs = proctime*TIMEPEROP;
        
        System.out.println("Time (secs) to process: " + procsecs + ", days: " + procsecs/86400);
    }
}

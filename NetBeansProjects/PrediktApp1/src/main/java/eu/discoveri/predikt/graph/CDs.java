/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.predikt.graph;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class CDs
{
    private final int     Cd;
    private final double  CDw, CDwtuned;

    public CDs(int Cd, double CDw, double CDwtuned)
    {
        this.Cd = Cd;
        this.CDw = CDw;
        this.CDwtuned = CDwtuned;
    }

    public int getCd() { return Cd; }
    public double getCDw() { return CDw; }
    public double getCDwtuned() { return CDwtuned; }
}

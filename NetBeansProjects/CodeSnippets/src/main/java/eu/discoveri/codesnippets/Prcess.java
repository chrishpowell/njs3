/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Prcess
{
    public static void main(String[] args)
    {
        Token t = new Token("Elephant","nose");
        Count c = new Count();
        LemTok l;
        
        c.count(t.getLemma());
        c.count(t::getToken);
    }
}

/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LHSEx
{
    public static void main(String[] args)
    {
        Set lhs = new LinkedHashSet();
        
        lhs.add("B");
        lhs.add("A");
        lhs.add("C");
        lhs.add("A1");
        
        lhs.forEach(System.out::println);
    }
}

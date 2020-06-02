/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */

package eu.discoveri.predikt.tests;

import eu.discoveri.predikt.sentences.LangCode;
import java.util.EnumMap;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LCTest
{
    static EnumMap<LangCode,String> mapLC = new EnumMap<LangCode,String>(LangCode.class);
    
    public static void main(String[] args)
    {
        mapLC.put(LangCode.en, "English");
    }
}

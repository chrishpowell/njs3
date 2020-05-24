/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LTest
{
    public static void main(String[] args)
    {
        LangCode.stream().forEach(System.out::println);
//        System.out.println("Lang code: " +LangCode.en.toString());
    }
}

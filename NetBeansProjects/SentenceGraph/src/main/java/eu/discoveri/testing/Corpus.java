/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.testing;

import eu.discoveri.elements.Sentence;
import eu.discoveri.graph.Populate;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;


/**
 * This class arranges the sentences with edge weights etc.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Corpus
{
    private final static String  inStr = "2020 Astrology Predictions: The Beginning of a New Astrological Era\n" +
"2020 is the beginning of not only a new decade, but a new astrological era. As with any birthing process, the year will involve enduring labor pains in order to introduce a new life. The year 2020 is a threshold to cross, a pause between the way it has been and the way it will be, necessitating a confrontation with all the difficult challenges in need of a remedy within civilization. There is nothing easy about the transits of 2020, yet they also contain a potent force within the dissolution and volatility they will incite. By actively asserting the role you wish to play in molding the world to come, the tests you will face in 2020 will help you cultivate new levels of inner strength as well as new skills to share with others.\n" +
"\n" +
"At the center of change brought by 2020 will be the cycles of Jupiter, Saturn, and Pluto. The year begins with a union between Saturn and Pluto in Capricorn and ends with a conjunction of Saturn and Jupiter in Aquarius, with Jupiter uniting with Pluto three times in between. The conjunction between Jupiter and Saturn on December 21 is the defining astrological event of the year, as it will establish an astrological era of Jupiter and Saturn uniting in air signs that will continue until 2159. The influence of Pluto upon Jupiter and Saturn will dredge up issues with societal structures in need of innovative reform, particularly involving global economics and systematic oppression.\n" +
"\n" +
"What do the stars have in store for you in 2020?\n" +
"\n" +
"The intensifying nature of 2020 will be evident at the beginning of the year, as January hosts not only a conjunction between Saturn and Pluto, but also a lunar eclipse combined with a series of conjunctions between the sun with Mercury, Ceres, Saturn, and Pluto within a few days between January 10–13. Since Jupiter will simultaneously be conjoining the south node of the moon in Capricorn, there will be a gravity of contraction and consolidation in January that will hone aspirations and draw attention toward the essential purpose you wish to initiate in the year ahead. January can be utilized for adjusting ambitions to contend with the limitations of material resources, while revisioning what you need to cultivate and what you need to shed.\n" +
"\n" +
"\n" +
"\n" +
"The Aries equinox will initiate the volatile second quarter of 2020 with a catalyzing conjunction between Mars, Jupiter, and Pluto in Capricorn that signals a shake-up of societal structures. The following day, Saturn will enter Aquarius on March 21 where it will remain until returning to Capricorn on July 1. The initial entrance of Saturn into Aquarius combined with the first conjunction between Jupiter and Pluto in early April can help expand the scope of long-term goals beyond previous restrictions, while discovering how to reorder priorities in accordance.\n" +
"\n" +
"In May, the lunar nodes will shift into Gemini and Sagittarius and Venus will station retrograde in Gemini on May 13. With the north node of the moon as well as Venus retrograde occupying Gemini, it will be important to value curiosity and be willing to consider new perspectives and values. As Venus will station direct on June 25 in the middle of a sequence of three eclipses in a row, Mercury moving retrograde in Cancer, and Jupiter uniting with Pluto for the second time, there will be social unrest as well as vital changes to adjust to within personal relationships during June and July.\n" +
"\n" +
"Whatever changes in storylines will be set in motion during the eclipse season of June and July will be further intensified when Mars stations retrograde in Aries on September 9. The slowly burning retrograde fire of Mars will be the dominant theme of the final quarter of the year, as Mars retrograde will form intense square aspects with Saturn, Pluto, and Jupiter in Capricorn before stationing direct on November 13. Though Mars retrograde periods can coincide with frustrating blocks to motivation, the inward focus of Mars also creates the potential of realigning with your most deeply felt desires.\n" +
"\n" +
"Prepare for the year ahead with your 2020 Tarot reading\n" +
"\n" +
"2020 will come to a fittingly dramatic conclusion with a lunar eclipse in Gemini on November 30 followed by a solar eclipse in Sagittarius on December 14, setting the stage for the long-anticipated conjunction of Jupiter and Saturn in the first degree of Aquarius on the Capricorn solstice. As the conjunction between Jupiter and Saturn on December 21 is also forming a catalytic square aspect with Uranus in Taurus, it will release the tension that has been building throughout the year between the death of old forms and the emergence of new aspirations. Throughout 2020, ask yourself what you need to release in order to make space for what truly matters, as there will be unknown potential and challenges arising as we collectively enter an era of Jupiter and Saturn uniting in air signs at the end of the year.";
    // Test data
//    private final static Map<String,Sentence> snMap = Map.ofEntries(
//            entry("S1",new Sentence("S1","1 The quick brown fox jumps over the lazy dog",0.9876)),
//            entry("S2",new Sentence("S2","2 The quick brown fox jumps over the lazy dog",0.01)),
//            entry("S3",new Sentence("S3","3 The quick brown fox jumps over the lazy dog",0.25)),
//            entry("S4",new Sentence("S4","4 The quick brown fox jumps over the lazy dog",3.15)),
//            entry("S5",new Sentence("S5","5 The quick brown fox jumps over the lazy dog",1.99)),
//            entry("S6",new Sentence("S6","6 The quick brown fox jumps over the lazy dog",2.1))
//        );
//    private final static Map<AbstractMap.SimpleEntry<Sentence,Sentence>,Double> simScores = Map.ofEntries(
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S2")),3.14159d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S3")),0.9d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S4")),0.09d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S3")),1.25d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S4")),1.37d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S5")),2.222d),
//            entry(new AbstractMap.SimpleEntry<>(snMap.get("S5"),snMap.get("S6")),0.5d)
//    );

    /**
     * Get the raw data.
     * @return 
     */
    public static String getRaw()
    {
        return inStr;
    }

    /**
     * Get a list of sentences.
     * 
     * @param popl
     * @return 
     */
    public static List<Sentence> getSentList( Populate popl )
    {
        return popl.extractSentences( inStr );
    }
}

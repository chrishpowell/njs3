/*
 */
package eu.discoveri.snippets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;


/**
 * Check SentenceDetectorME.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SDCheck
{
    public static void main(String[] args)
            throws FileNotFoundException, IOException
    {
        String paragraph = "The observation of natural phenomena in order to develop explanatory theories and laws about them.\n" +
"Agenbite of inwit. Science is scientists doing sciency stuff in the sciences 987654321 times daily '', de en es yo!\n" +
"A body of knowledge about natural phenomena that can be tested through further investigations\n";

        SentenceModel sm = new SentenceModel(new FileInputStream("/home/chrispowell/NetBeansProjects/LanguageGraph/src/main/java/resources/model/en-sent.bin"));
        SentenceDetectorME sdme = new SentenceDetectorME(sm);
        
        String[] sents = sdme.sentDetect(paragraph);
        for( String s: sents )
            System.out.println("> " +s);
    }
}

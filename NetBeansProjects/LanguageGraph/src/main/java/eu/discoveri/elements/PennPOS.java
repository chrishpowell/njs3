/*
 */
package eu.discoveri.elements;

import java.util.Map;


/**
 * Penn Treebank POS (as used by Apache OpenNLP).
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PennPOS
{
    private static final Map<PennPOSCode,String>  openPOS =
            Map.ofEntries(
                Map.entry(PennPOSCode.CC,"Coordinating conjunction"),
                Map.entry(PennPOSCode.CD,"Cardinal number"),
                Map.entry(PennPOSCode.DT,"Determiner"),
                Map.entry(PennPOSCode.EX,"Existential there"),
                Map.entry(PennPOSCode.FW,"Foreign word"),
                Map.entry(PennPOSCode.IN,"Preposition or subordinating conjunction"),
                Map.entry(PennPOSCode.JJ,"Adjective"),
                Map.entry(PennPOSCode.JJR,"Adjective, comparative"),
                Map.entry(PennPOSCode.JJS,"Adjective, superlative"),
                Map.entry(PennPOSCode.LS,"List item marker"),
                Map.entry(PennPOSCode.MD,"Modal"),
                Map.entry(PennPOSCode.NN,"Noun, singular or mass"),
                Map.entry(PennPOSCode.NNS,"Noun, plural"),
                Map.entry(PennPOSCode.NNP,"Proper noun, singular"),
                Map.entry(PennPOSCode.NNPS,"Proper noun, plural"),
                Map.entry(PennPOSCode.PDT,"Predeterminer"),
                Map.entry(PennPOSCode.POS,"Possessive ending"),
                Map.entry(PennPOSCode.PRP,"Personal pronoun"),
                Map.entry(PennPOSCode.PRP$,"Possessive pronoun"),
                Map.entry(PennPOSCode.RB,"Adverb"),
                Map.entry(PennPOSCode.RBR,"Adverb, comparative"),
                Map.entry(PennPOSCode.RBS,"Adverb, superlative"),
                Map.entry(PennPOSCode.RP,"Particle"),
                Map.entry(PennPOSCode.SYM,"Symbol"),
                Map.entry(PennPOSCode.TO,"to"),
                Map.entry(PennPOSCode.UH,"Interjection"),
                Map.entry(PennPOSCode.VB,"Verb, base form"),
                Map.entry(PennPOSCode.VBD,"Verb, past tense"),
                Map.entry(PennPOSCode.VBG,"Verb, gerund or present participle"),
                Map.entry(PennPOSCode.VBN,"Verb, past participle"),
                Map.entry(PennPOSCode.VBP,"Verb, non-3rd person singular present"),
                Map.entry(PennPOSCode.VBZ,"Verb, 3rd person singular present"),
                Map.entry(PennPOSCode.WDT,"Wh-determiner"),
                Map.entry(PennPOSCode.WP,"Wh-pronoun"),
                Map.entry(PennPOSCode.WP$,"Possessive wh-pronoun"),
                Map.entry(PennPOSCode.WRB,"Wh-adverb")
            );
    
    /**
     * Get OpenNLP POS tags and explanation.
     * @return 
     */
    public static Map<PennPOSCode,String> getOpenPOS() { return openPOS; }
}

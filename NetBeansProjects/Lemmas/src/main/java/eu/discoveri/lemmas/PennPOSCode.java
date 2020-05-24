/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum PennPOSCode
{
    CC  ("Coordinating conjunction"),
    CD	("Cardinal number"),
    DT	("Determiner"),
    EX	("Existential there"),
    FW	("Foreign word"),
    IN	("Preposition or subord. conjunction"),
    JJ	("Adjective"),
    JJR	("Adjective, comparative"),
    JJS	("Adjective, superlative"),
    LS	("List item marker"),
    MD	("Modal"),
    NN	("Noun, singular or mass"),
    NNS	("Noun, plural"),
    NNP	("Proper noun, singular"),
    NNPS("Proper noun, plural"),
    PDT	("Predeterminer"),
    POS	("Possessive ending"),
    PRP	("Personal pronoun"),
    PRP$	("Possessive pronoun"),
    RB	("Adverb"),
    RBR	("Adverb, comparative"),
    RBS	("Adverb, superlative"),
    RP	("Particle"),
    SYM	("Symbol"),
    TO	("to"),
    UH	("Interjection"),
    VB	("Verb, base form"),
    VBD	("Verb, past tense"),
    VBG	("Verb, gerund or present participle"),
    VBN	("Verb, past participle"),
    VBP	("Verb, non-3rd person singular present"),
    VBZ	("Verb, 3rd person singular present"),
    WDT	("Wh-determiner"),
    WP	("Wh-pronoun"),
    WP$	("Possessive wh-pronoun"),
    WRB	("Wh-adverb");

    private final String    descr;

    PennPOSCode( String descr )
    {
        this.descr = descr;
    }

    public String getDescr() { return descr; }
}

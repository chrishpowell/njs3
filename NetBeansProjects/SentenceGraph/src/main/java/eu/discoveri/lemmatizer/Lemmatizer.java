/*
 */
package eu.discoveri.lemmatizer;

import eu.discoveri.exceptions.ArrayLengthsDifferException;
import eu.discoveri.exceptions.ListLengthsDifferException;

import java.util.List;
import java.util.Map;


/**
 * Copy of OpenNLP Lemmatizer interface but with Exceptions thrown.
 * 
 * @author Chris Powell, Discoveri OU
 */
public interface Lemmatizer
{
    /**
     * Generates lemmas for the word and POStag returning the result in a
     * list containing every possible lemma for each token and postag.(NB: OpenNLP would make better sense if tokens and tags were held in a
 class associating tokens with tags one-2-one)
     * 
     * @param toks Tokens of the sentence
     * @param tags POS tags of the tokens of the sentence
     * @param match2NN Match Pell NNP/NNPS to NN on dictionary
     * @return The token as key and the associated lemma
     * @throws ListLengthsDifferException 
     */
    public Map<String,String> lemmatize(List<String> toks, List<String> tags, boolean match2NN)
            throws ListLengthsDifferException;
    
    /**
     * Generates lemmas for the word and POStag returning the result in a
     * array containing every possible lemma for each token and postag.(NB: OpenNLP would make better sense if tokens and tags were held in a
 class associating tokens with tags one-2-one)
     * 
     * @param toks Tokens of the sentence
     * @param tags POS tags of the tokens of the sentence
     * @param match2NN Match Pell NNP/NNPS to NN on dictionary
     * @return The token as key and the associated lemma
     * @throws ArrayLengthsDifferException 
     * @throws ListLengthsDifferException (Should never happen)
     */
    public Map<String,String> lemmatize(String[] toks, String[] tags, boolean match2NN)
            throws ArrayLengthsDifferException, ListLengthsDifferException;
}

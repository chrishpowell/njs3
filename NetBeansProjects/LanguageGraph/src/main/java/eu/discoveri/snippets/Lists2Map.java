/*
 */
package eu.discoveri.snippets;

import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;
import eu.discoveri.exceptions.SentenceIsEmptyException;
import eu.discoveri.exceptions.TokensListIsEmptyException;
import eu.discoveri.languagegraph.LangCode;
import eu.discoveri.languagegraph.Populate;
import eu.discoveri.utils.Utils;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Lists2Map
{
    static List<String> l1 = new ArrayList<>(Arrays.asList("Word1","Word2","Word1","Word4","Word5","Word6","Word1","Word7"));
    static List<String> l2 = new ArrayList<>(Arrays.asList("Token1","Token2","Token1","Token4","Token5","Token6","Token1","Token7"));
    static List<Integer> ll1 = new ArrayList<>(Arrays.asList(1,2,1,4,5,6,1,7));
    static List<String> ll2 = new ArrayList<>(Arrays.asList("Token1","Token2","Token1","Token4","Token5","Token6","Token1","Token7"));
    static String str = "Science in Alaska is science scientists doing sciency stuff in the sciences 987654321 times daily de en es.";
    
    public static void main(String[] args) throws IOException, SentenceIsEmptyException, TokensListIsEmptyException
    {
        // Class for populating entities
        Populate popl = new Populate( LangCode.en );
        
        Map<AbstractMap.SimpleEntry,String> dm = popl.getSl().getDictMap();
        AbstractMap.SimpleEntry find = new AbstractMap.SimpleEntry("sciences","NNS");
        System.out.println("Lemma for sciences: " +dm.get(find) );
        
        // Test sentence
//        Sentence s = new Sentence(str);
//        List<Token> toks = popl.extractTokens(s);
//        List<String> tags = new ArrayList<>(popl.posTags(s));
//
//        Utils.removeDupsFromLists(toks, tags);
//        toks.forEach(System.out::println);
        
//        Utils.removeDupsFromLists(ll1, ll2);
//        ll1.forEach(System.out::println);
        
//        Utils.removeDupsFromLists( l1, l2 );
//        l1.forEach(l -> System.out.println("1> " +l));
//        l2.forEach(l -> System.out.println("2> " +l));
        // This fails with duplicate key if duplicate entries:
//        Map<String,String> tokTag = IntStream.range(0, l1.size())
//                            .boxed()
//                            .collect(Collectors.toMap(l1::get,l2::get));
    }
}

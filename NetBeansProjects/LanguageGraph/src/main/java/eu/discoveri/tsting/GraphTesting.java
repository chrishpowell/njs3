/*
 */
package eu.discoveri.tsting;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import eu.discoveri.elements.Sentence;
import eu.discoveri.elements.Token;
import eu.discoveri.languagegraph.LangCode;
import eu.discoveri.languagegraph.Populate;


/**
 * Test language graph stuff.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GraphTesting
{
    /**
     * M A I N
     * =======
     * @param args 
     * @throws java.lang.Exception 
     */
    public static void main(String[] args)
            throws Exception
    {
        final boolean MATCH2NN = true;
        final String example = "There are several legitimate definitions of science today:\n" +
"The observation of natural phenomena in order to develop explanatory theories and laws about them.\n" +
"Science in Alaska is science scientists doing sciency stuff in the sciences 987654321 times daily de en es.\n" +
"There are 604800 seconds in a week.\n" +
"A body of knowledge about natural phenomena that can be tested through further investigations.\n" +
"Specific branches of knowledge derived from systematic observation and testing.\n" +
"Science is distinguished from other branches of knowledge by the scientific method, whereby scientists pose hypotheses, devise methods and collect data to test them, and then determine whether or not their results confirm or negate the hypotheses. Their data are usually quantitative (numerical) or are rendered into quantitative form for analysis. Scientific research often takes place under carefully controlled laboratory conditions, but it can occur in outdoor field sites, and via telescopes trained on distant galaxies.\n" +
"\n" +
"The natural sciences include biology and geology. Physics, some branches of chemistry, and astronomy represent the physical sciences. The social sciences such as sociology and political science may or may not be strictly scientific in the conduct of research, because much of their work is more qualitative and does not follow the scientific method. Psychology is often termed the “behavioral science.” Some of its sub-fields overlap with neuroscience, whereas the themes best known to astrologers are more humanistic and qualitative.\n" +
"\n" +
"The history of science shows that its definition has varied considerably over time. The term “scientist” was not coined until 1834.[1] Before then, scientists might be called philosophers, mathematicians, or naturalists. The science of Aristotle, Galileo, and even Charles Darwin are only precursors of the way scientific research is conducted today in universities, government research facilities, and corporate laboratories. Although science stresses the accumulation of facts and understanding how those facts fit into patterns, scientific “truths” are frequently subject to scrutiny and revision.\n" +
"\n" +
"It is important to distinguish science from scientism, or a belief popular in the mid-twentieth century, that science was the only valid form of knowledge and that science could solve all of society’s problems. Science as a practice should also be separated from scientists as human beings. Although they are educated to be rational, critical thinkers, they are only human and thus susceptible to the range of opinions and prejudices that affect the ordinary run of humanity. Science is also differentiated from fields in the humanities and fine arts, such as literature and philosophy. Some humanities fields are highly empirical and investigative, such as history, but they do not use the scientific method or focus specifically on natural processes.\n";
    
        Populate popl = new Populate( LangCode.en );
        
        // All sentences
        List<Sentence> sents = popl.extractSentences( example );
        for( Sentence s: sents )
        {
            List<Token> toks = popl.extractTokens(s);
            // Note: new() needed for lemmatize/removeDupsFromLists() to work.  See removeDups... for details. 
            List<String> tags = new ArrayList<>(popl.posTags(s));

            Map<String,String> lemmas = popl.lemmasOfSentence(s, toks, tags, MATCH2NN);
            System.out.println(" > " +s.getText());
            lemmas.forEach((k,v) -> System.out.print("     Token: "+k+", Lemma: "+v+"  "));
        }
        
        // Sequences
        // -2.128999455172619 [DT, JJ, NNS, JJ, IN, NN, CC, JJ, NN, MD, CC, ...]

//        toks.forEach(System.out::println);
//        List<Sequence> seqs = popl.sequenceTokens(s);
//        seqs.stream().forEach(sq -> { // DT, JJ, NNS, JJ, IN, NN, CC, JJ, NN, MD, CC, ...
//            sq.getOutcomes().stream().forEach(o -> System.out.println("..> "+o));
//        });
        
//        Map<String,String> ls = popl.lemmasOfSentence( s, t, pts );
//        t.forEach(System.out::println);

        // Populate each Sentence with its tags and sequences
//        Graphs g = new Graphs();
//        for( Sentence sn: sents )
//        {
//            List<Token> tokens = popl.tokensAndTags(sn);
//            //tokens.stream().map(t -> t.getPOS()).forEach(System.out::print);
//            g.addListUniques2Graph( tokens );
//        }
//        
//        g.dumpVertices();
    }
}
/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Corpi
{
    // Test data
    private static final List<String> lts = List.of("quick", "brown", "fox", "jump", "lazy", "dog");
    private static final List<String> lts2 = List.of("every", "good", "boy", "deserve", "fruit", "vegetable", "console", "time");
    private static final Map<String,SentenceNode> centMap = Map.ofEntries(
            entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,1.99))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> centEdges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S1")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S3")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(centMap.get("S0"),centMap.get("S4")),1.25d)
    );
    private static final Map<String,SentenceNode> wMap = Map.ofEntries(
        entry("Sa",new SentenceNode("Sa","a The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("Sb",new SentenceNode("Sb","b The quick brown fox jumps over the lazy dog",lts2,0.01)),
        entry("Sc",new SentenceNode("Sc","c The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("Sd",new SentenceNode("Sd","d The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("Se",new SentenceNode("Se","e The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sf",new SentenceNode("Sf","f The quick brown fox jumps over the lazy dog",lts,1.99))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> wEdges = Map.ofEntries(
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sa"),wMap.get("Sb")),4.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sa"),wMap.get("Sc")),4.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Sc")),2.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Se")),1.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Sb"),wMap.get("Sd")),1.d),
        entry(new AbstractMap.SimpleEntry<>(wMap.get("Se"),wMap.get("Sf")),7.d)
    );
    private static final Map<String,SentenceNode> exMap = Map.ofEntries(
        entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
        entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("S5",new SentenceNode("S5","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("S6",new SentenceNode("S6","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
        entry("S7",new SentenceNode("S7","7 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("S8",new SentenceNode("S8","8 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("S9",new SentenceNode("S9","9 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> exEdges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S0"),exMap.get("S8")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S8"),exMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S8"),exMap.get("S9")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S9"),exMap.get("S1")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S2"),exMap.get("S1")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S2"),exMap.get("S4")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S4"),exMap.get("S3")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S1"),exMap.get("S3")),0.5d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S1"),exMap.get("S7")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S3"),exMap.get("S5")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S5"),exMap.get("S6")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(exMap.get("S7"),exMap.get("S6")),1.25d)
    );
    private static final Map<String,SentenceNode> snMap = Map.ofEntries(
            entry("S1",new SentenceNode("S1","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("S2",new SentenceNode("S2","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("S3",new SentenceNode("S3","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S4",new SentenceNode("S4","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S5",new SentenceNode("S5","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("S6",new SentenceNode("S6","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
            entry("S7",new SentenceNode("S7","7 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("S8",new SentenceNode("S8","8 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("S9",new SentenceNode("S9","9 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("S0",new SentenceNode("S0","0 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> simScores = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S0"),snMap.get("S8")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S8"),snMap.get("S2")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S8"),snMap.get("S9")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S9"),snMap.get("S1")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S1")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S2"),snMap.get("S4")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S4"),snMap.get("S3")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S3")),0.5d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S1"),snMap.get("S7")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S3"),snMap.get("S5")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S5"),snMap.get("S6")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(snMap.get("S7"),snMap.get("S6")),1.25d)
    );
    private static final Map<String,SentenceNode> discMap = Map.ofEntries(
            entry("Sa",new SentenceNode("Sa","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("Sb",new SentenceNode("Sb","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("Sc",new SentenceNode("Sc","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("Sd",new SentenceNode("Sd","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("Se",new SentenceNode("Se","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("Sf",new SentenceNode("Sf","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("Sg",new SentenceNode("Sg","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
            entry("Sh",new SentenceNode("Sh","7 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("Si",new SentenceNode("Si","8 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("Sj",new SentenceNode("Sj","9 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("Sk",new SentenceNode("Sk","0 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> discEdges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sa"),discMap.get("Sb")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sa"),discMap.get("Sc")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sb"),discMap.get("Sc")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sb"),discMap.get("Se")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sc"),discMap.get("Sd")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sd"),discMap.get("Sf")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sd"),discMap.get("Sg")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Se"),discMap.get("Sf")),0.5d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Sh"),discMap.get("Si")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(discMap.get("Si"),discMap.get("Sj")),0.9d)
    );
    private static final Map<String,SentenceNode> disc1Map = Map.ofEntries(
            entry("Sa",new SentenceNode("Sa","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
            entry("Sb",new SentenceNode("Sb","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
            entry("Sc",new SentenceNode("Sc","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
            entry("Sd",new SentenceNode("Sd","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
            entry("Se",new SentenceNode("Se","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("Sf",new SentenceNode("Sf","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
            entry("Sg",new SentenceNode("Sg","6 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,Double> disc1Edges = Map.ofEntries(
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sa"),disc1Map.get("Sb")),3.14159d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sa"),disc1Map.get("Sc")),0.9d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sb"),disc1Map.get("Sc")),0.09d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sb"),disc1Map.get("Se")),1.25d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sc"),disc1Map.get("Sd")),1.37d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sd"),disc1Map.get("Sf")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sd"),disc1Map.get("Sg")),2.222d),
            entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Se"),disc1Map.get("Sf")),0.5d)
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,SentenceEdge> disc1aEdges = Map.ofEntries(
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sa"),disc1Map.get("Sb")),new SentenceEdge(disc1Map.get("Sa"),disc1Map.get("Sb"),3.14159d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sa"),disc1Map.get("Sc")),new SentenceEdge(disc1Map.get("Sa"),disc1Map.get("Sc"),0.9d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sb"),disc1Map.get("Sc")),new SentenceEdge(disc1Map.get("Sb"),disc1Map.get("Sc"),0.09d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sb"),disc1Map.get("Se")),new SentenceEdge(disc1Map.get("Sb"),disc1Map.get("Se"),1.25d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sc"),disc1Map.get("Sd")),new SentenceEdge(disc1Map.get("Sc"),disc1Map.get("Sd"),1.37d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sd"),disc1Map.get("Sf")),new SentenceEdge(disc1Map.get("Sd"),disc1Map.get("Sf"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Sd"),disc1Map.get("Sg")),new SentenceEdge(disc1Map.get("Sd"),disc1Map.get("Sg"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc1Map.get("Se"),disc1Map.get("Sf")),new SentenceEdge(disc1Map.get("Se"),disc1Map.get("Sf"),0.5d))
    );
    private static final Map<String,SentenceNode> disc2Map = Map.ofEntries(
        entry("Sa",new SentenceNode("Sa","1 The quick brown fox jumps over the lazy dog",lts,0.9876)),
        entry("Sb",new SentenceNode("Sb","2 The quick brown fox jumps over the lazy dog",lts2,0.01)),
        entry("Sc",new SentenceNode("Sc","3 The quick brown fox jumps over the lazy dog",lts,0.25)),
        entry("Sd",new SentenceNode("Sd","4 The quick brown fox jumps over the lazy dog",lts,3.15) ),
        entry("Se",new SentenceNode("Se","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sf",new SentenceNode("Sf","5 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sg",new SentenceNode("Sg","6 The quick brown fox jumps over the lazy dog",lts,2.1)),
        entry("Sx",new SentenceNode("Sx","7 The quick brown fox jumps over the lazy dog",lts,1.99)),
        entry("Sy",new SentenceNode("Sy","8 The quick brown fox jumps over the lazy dog",lts,2.1))
    );
    private static final Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,SentenceEdge> disc2Edges = Map.ofEntries(
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sa"),disc2Map.get("Sb")),new SentenceEdge(disc2Map.get("Sa"),disc2Map.get("Sb"),3.14159d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sa"),disc2Map.get("Sc")),new SentenceEdge(disc2Map.get("Sa"),disc2Map.get("Sc"),0.9d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sb"),disc2Map.get("Sc")),new SentenceEdge(disc2Map.get("Sb"),disc2Map.get("Sc"),0.09d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sb"),disc2Map.get("Se")),new SentenceEdge(disc2Map.get("Sb"),disc2Map.get("Se"),1.25d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sc"),disc2Map.get("Sd")),new SentenceEdge(disc2Map.get("Sc"),disc2Map.get("Sd"),1.37d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sd"),disc2Map.get("Sf")),new SentenceEdge(disc2Map.get("Sd"),disc2Map.get("Sf"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sd"),disc2Map.get("Sg")),new SentenceEdge(disc2Map.get("Sd"),disc2Map.get("Sg"),2.222d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Se"),disc2Map.get("Sf")),new SentenceEdge(disc2Map.get("Se"),disc2Map.get("Sf"),0.5d)),
        entry(new AbstractMap.SimpleEntry<>(disc2Map.get("Sx"),disc2Map.get("Sy")),new SentenceEdge(disc2Map.get("Sx"),disc2Map.get("Sy"),0.5d))
    );
    
    /**
     * Get corpus (sentences).
     * @return 
     */
    public static Map<String,SentenceNode> getVertices()
    {
        return disc2Map;
    }
    
    public static Map<AbstractMap.SimpleEntry<SentenceNode,SentenceNode>,SentenceEdge> getEdges()
    {
        return disc2Edges;
    }
}

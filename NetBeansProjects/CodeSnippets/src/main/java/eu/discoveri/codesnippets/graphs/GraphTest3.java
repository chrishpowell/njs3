/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.TransactionConfig;
import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.net.ServerAddress;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class GraphTest3
{
    // Db driver
    private Driver driver = null;
    // Session config
    SessionConfig sessionCfg = SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build();
    // Tx config
    TransactionConfig txCfg = TransactionConfig.builder().withTimeout(Duration.ofSeconds(5)).build();

    private Driver createDriver( String vUri, String user, String pwd, ServerAddress... addresses )
    {
        // Build a config
        Config cfg = Config.builder()
                        .withResolver(address -> new HashSet<>(Arrays.asList(addresses))) 
                        .build();
        
        // Neo4j driver
        return GraphDatabase.driver( vUri, AuthTokens.basic(user,pwd), cfg);
    }

    private Result emptyDb()
    {
        try( Session sess = driver.session(sessionCfg) )
        {
            return sess.run( new Query("MATCH (n) DETACH DELETE n") );
        }
    }

    private void close()
    {
        driver.close();
    }
        
        
        
    public static void main(String[] args)
    {
        final String NEO4J = "neo4j://localhost";
        final String USER = "neo4j", PWD = "karabiner";
        
        // Setup db
        GraphTest3 g = new GraphTest3();
        // Create the driver
        g.driver = g.createDriver(NEO4J, USER, PWD, ServerAddress.of("localhost", 7687));
        
        // Clear db
        g.emptyDb();
        
        List<Query> lsq = List.of(  new Query("CREATE (S0:Sentence) SET S0={name:$name,sentence:$sentence}",
                                                parameters("name","S0","sentence","The quick brown fox jumps")),
                                    new Query("CREATE (S1:Sentence) SET S1={name:$name,sentence:$sentence}",
                                                parameters("name","S1","sentence","The quick brown fox jumps")),
                                    new Query("CREATE (t0S0:Token) SET t0S0={text:$text,tid:$tid}",
                                                parameters("text","quick","tid","t0S0")),
                                    new Query("CREATE (t1S0:Token) SET t1S0={text:$text,tid:$tid}",
                                                parameters("text","brown","tid","t1S0")),
                                    new Query("CREATE (t2S0:Token) SET t2S0={text:$text,tid:$tid}",
                                                parameters("text","fox","tid","t2S0")),
                                    new Query("CREATE (t0S1:Token) SET t0S1={text:$text,tid:$tid}",
                                                parameters("text","quick","tid","t0S1")),
                                    new Query("CREATE (t1S1:Token) SET t1S1={text:$text,tid:$tid}",
                                                parameters("text","brown","tid","t1S1")),
                                    new Query("CREATE (t2S1:Token) SET t2S1={text:$text,tid:$tid}",
                                                parameters("text","fox","tid","t2S1")),
                                    new Query("MATCH(src:Sentence {name:$name0}) MATCH(tgt:Sentence {name:$name1}) MERGE (src)-[:SIMILARTO]-(tgt)",
                                                parameters("name0","S0","name1","S1"))  );
        List<Query> ltq = List.of(  new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S0","tid","t0S0")),
                                    new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S0","tid","t1S0")),
                                    new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S0","tid","t2S0")),
                                    new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S1","tid","t0S1")),
                                    new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S1","tid","t1S1")),
                                    new Query("MATCH(src:Sentence {name:$name}) MATCH(tgt:Token {tid:$tid}) MERGE (src)-[:TOK]-(tgt)",parameters("name","S1","tid","t2S1"))  );
        
        try( Session sess = g.driver.session(g.sessionCfg) )
        {
            lsq.forEach(q -> sess.run(q,g.txCfg));
            ltq.forEach(q -> sess.run(q,g.txCfg));
        }
        
        g.close();
    }
}

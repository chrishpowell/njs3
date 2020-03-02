/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.graph;

import eu.discoveri.config.Constants;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

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
import org.neo4j.driver.net.ServerAddress;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Graphing
{
        // Db driver
    private static Driver driver = null;
    // Session config
    private static SessionConfig sessionCfg = SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build();
    // Tx config
    private static TransactionConfig txCfg = TransactionConfig.builder().withTimeout(Duration.ofSeconds(Constants.TXDURATION)).build();
    
    
    /**
     * Build the driver.
     * @param vUri Neo4j virtual URI (eg: "neo4j://localhost")
     * @param user
     * @param pwd
     * @param addresses
     * @return 
     */
    public static Driver createDriver( String vUri, String user, String pwd, ServerAddress... addresses )
    {
        // Build a config
        Config cfg = Config.builder()
                        .withResolver(address -> new HashSet<>(Arrays.asList(addresses))) 
                        .build();
        
        // Neo4j driver
        driver = GraphDatabase.driver( vUri, AuthTokens.basic(user,pwd), cfg);
        return driver;
    }

    /**
     * Delete all nodes and edges.
     * @return 
     */
    public static Result clearDb()
    {
        try( Session sess = driver.session(Graphing.getSessionCfg()) )
        {
            return sess.run( new Query("MATCH (n) DETACH DELETE n") );
        }
    }
        
   /**
     * Close connections
     */
    public static void close()
    {
        driver.close();
    }

    
    /**
     * Get the Neo4j driver.
     * @return 
     */
    public static Driver getDriver() { return driver; }
    /**
     * Get a session config
     * @return 
     */
    public static SessionConfig getSessionCfg() { return sessionCfg; }
    /**
     * Get a transaction config.
     * @return 
     */
    public static TransactionConfig getTxCfg() { return txCfg; }
}

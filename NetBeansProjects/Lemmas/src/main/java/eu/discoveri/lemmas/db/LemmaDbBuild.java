/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
//<editor-fold defaultstate="collapsed" desc="Database SQL">
/*=== SQL to init database

DROP TABLE IF EXISTS PennPOSCode;
create table PennPOSCode
(
	code CHAR(4) NOT NULL,
	descr VARCHAR(48) NOT NULL,
	PRIMARY KEY (code)
) ENGINE = INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS LangCode;
create table LangCode
(
	code CHAR(2) NOT NULL,
	descr VARCHAR(16) NOT NULL,
	PRIMARY KEY (code)
) ENGINE = INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS Lemma;
create table Lemma
(
	id INT unsigned NOT NULL AUTO_INCREMENT,
	lemma VARCHAR(48) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE KEY(lemma)
) ENGINE = INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS Word;
create table Word
(
	id INT unsigned NOT NULL AUTO_INCREMENT,
	word VARCHAR(64) NOT NULL,
	POSId CHAR(4) NOT NULL,
	langId CHAR(2) NOT NULL,
	lemmaId INT unsigned NOT NULL,
	pageId INT unsigned,
	UNIQUE KEY (id),
	PRIMARY KEY wordPOS (word,POSId),
	FOREIGN KEY (POSId) REFERENCES PennPOSCode(code),
	FOREIGN KEY (langId) REFERENCES LangCode(code),
	FOREIGN KEY (lemmaId) REFERENCES Lemma(id),
	FOREIGN KEY (pageId) REFERENCES Page(pageId)
) ENGINE = INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS Page;
create table Page
(
 	pageId INT unsigned NOT NULL AUTO_INCREMENT,
 	pageNamespace INT(11) NOT NULL,
 	pageTitle VARCHAR(255) COLLATE utf8_bin NOT NULL,
 	PRIMARY KEY (pageId),
 	UNIQUE KEY nameTitle (pageNamespace,pageTitle)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

*/
//</editor-fold>
package eu.discoveri.lemmas.db;

import es.discoveri.lemmas.config.Constants;
import eu.discoveri.lemmas.LangCode;
import eu.discoveri.lemmas.PennPOSCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class LemmaDbBuild
{   
    /**
     * Connection for Lemma db
     * @return
     * @throws Exception 
     */
    public static Connection lemmaDb()
            throws Exception
    {
        String URL = "jdbc:mysql://localhost:3306/lemma?useSSL=false&serverTimezone=CET";
        String USER = "chrispowell";
        String PWD = "karabiner";
        
        return DriverManager.getConnection(URL,USER,PWD);
    }
    
    /**
     * LangCode table.  Built from LangCode enum.
     * @param conn
     * @throws Exception 
     */
    public void tableLangCode( Connection conn )
            throws Exception
    {
        // Empty the table
        PreparedStatement empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        empty.executeUpdate();
        empty = conn.prepareStatement("truncate lemma.LangCode");
        empty.executeUpdate();
        empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        empty.executeUpdate();
        
        // Populate the table
        PreparedStatement ps = lemmaDb().prepareStatement(Constants.LANGCODEPS);
        for( LangCode lc: LangCode.values() )
        {
            ps.setString(1, lc.toString());
            ps.setString(2, lc.getName());
            ps.executeUpdate();
        }
    }
    
    /**
     * POOSCode table.  Built from PennPOSCode enum.
     * @param conn
     * @throws Exception 
     */
    public void tablePOSCode( Connection conn )
            throws Exception
    {
        // Empty the table
        PreparedStatement empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        empty.executeUpdate();
        empty = conn.prepareStatement("truncate lemma.PennPOSCode");
        empty.executeUpdate();
        empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        empty.executeUpdate();
        
        // Populate the table
        PreparedStatement ps = lemmaDb().prepareStatement(Constants.PENNPOSCODEPS);

        for( PennPOSCode pc: PennPOSCode.values() )
        {
            ps.setString(1, pc.toString());
            ps.setString(2, pc.getDescr());
            ps.executeUpdate();
        }
    }
    
    /**
     * Lemma table, set a NULL field.
     * @param conn
     * @throws Exception 
     */
    public void tableLemmaNull( Connection conn )
            throws Exception
    {
        // Empty the table
        PreparedStatement empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        empty.executeUpdate();
        empty = conn.prepareStatement("truncate lemma.Lemma");
        empty.executeUpdate();
        empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        empty.executeUpdate();
        
        // Populate the table
        PreparedStatement ps = lemmaDb().prepareStatement(Constants.LEMMANULLPS);
        
        // Adjective
        ps.setString(1, "NULL");
        ps.executeUpdate();
    }
    
    /**
     * Lemma table, set a NULL field.
     * @param conn
     * @throws Exception 
     */
    public void tablePageZero( Connection conn )
            throws Exception
    {
        // Empty the table
        PreparedStatement empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        empty.executeUpdate();
        empty = conn.prepareStatement("truncate lemma.Page");
        empty.executeUpdate();
        empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        empty.executeUpdate();
        
        // Populate the table
        PreparedStatement ps = lemmaDb().prepareStatement(Constants.PAGEZEROPS);
        
        // Adjective
        ps.setInt(1, 0);
        ps.setString(2, "NULL");
        ps.executeUpdate();
    }
    
    /**
     * Word table.
     *
     * @param conn
     * @throws java.lang.Exception
     */
    public void tablesLemmaWord( Connection conn )
            throws Exception
    {   
        // Empty the table
        PreparedStatement empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        empty.executeUpdate();
        empty = conn.prepareStatement("truncate lemma.Word");
        empty.executeUpdate();
        empty = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        empty.executeUpdate();
    }
    
    
    /**
     * M A I N
     * =======
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args)
            throws Exception
    {
        // Setup Db connection
        LemmaDbBuild lemmaBld = new LemmaDbBuild();
        LemmaDbBuild.lemmaDb();
        
        // Db commection
        Connection conn = lemmaDb();
        
        // LangCode, POStags, Null lemma
        lemmaBld.tableLangCode(conn);
        lemmaBld.tablePOSCode(conn);
        lemmaBld.tableLemmaNull(conn);
        lemmaBld.tablePageZero(conn);
        
        // Word
        lemmaBld.tablesLemmaWord(conn);
        
        // And close
        LemmaDbBuild.lemmaDb().close();
    }
}

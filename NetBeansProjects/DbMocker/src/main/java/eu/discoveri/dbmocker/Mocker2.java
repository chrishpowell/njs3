/*
 */
package eu.discoveri.dbmocker;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Loader;
import org.jooq.LoaderError;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.meta.h2.information_schema.InformationSchema;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.jooq.util.xml.jaxb.Column;


/**
 * Mock database in order to test GraphQL
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Mocker2
{
    // Table names
    static final String ETOILE = "STAR";
    static final String CONST  = "CONSTELLATION";
    
    // JooQ context
    static DSLContext ctx;
    // Init tables
    static Table<Record> STAR = DSL.table(DSL.name(ETOILE));
    static Table<Record> CONSTELLATION = DSL.table(DSL.name(CONST));
    // Load stats
    static Loader<Record> constLoad, starLoad;
    
    /*
     * Create some tables
     */
    private static void loadTables()
            throws Exception
    {
        // Init columns
        List<org.jooq.Field<?>> starFields = new ArrayList<>();
        starFields.add(DSL.field(DSL.name(ETOILE,"Id"),Integer.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"Name"),String.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"altname"),String.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"ConstlID"),Integer.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"idlinks"),String.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"mag"),Float.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"colour"),String.class));
        starFields.add(DSL.field(DSL.name(ETOILE,"display"),Boolean.class));
        
        List<org.jooq.Field<?>> constFields = new ArrayList<>();
        constFields.add(DSL.field(DSL.name(CONST,"Id"),Integer.class));
        constFields.add(DSL.field(DSL.name(CONST,"Name"),String.class));
        constFields.add(DSL.field(DSL.name(CONST,"Shortname"),String.class));
        
        // Create tables
        ctx.createTable(STAR)
           .columns(starFields)
           .execute();

        ctx.createTable(CONSTELLATION)
           .columns(constFields)
           .execute();
        
                
        // Check tables
        org.jooq.util.xml.jaxb.InformationSchema is = ctx.informationSchema(STAR);
        is.getColumns().forEach((col) -> {
            System.out.println(">> Col: " +col.getColumnName() );
        });

        // Load data into the STAR & CONSTELLATION table from an input stream CSV
        File starCSV = new File("/home/chrispowell/Documents/AstroTurf/DbMocker/Star.csv");
        starLoad = ctx.loadInto(STAR)
           .loadCSV(starCSV)
           .fields(starFields)
           .execute();

        File constCSV = new File("/home/chrispowell/Documents/AstroTurf/DbMocker/Constellation.csv");
        constLoad = ctx.loadInto(CONSTELLATION)
           .loadCSV(constCSV)
           .fields(constFields)
           .execute();
        
        List<LoaderError> les = starLoad.errors();
        List<LoaderError> lec = constLoad.errors();
        if( !les.isEmpty() )
        {
            System.out.println(">>> Start load error!");
            les.forEach((le) -> {
//                le.exception().printStackTrace();
                for( String r: le.row() )
                    { System.out.println("..Star> " +r); }
            });

        }
        if( !lec.isEmpty() )
        {
            System.out.println(">>> Constellation load error!");
            lec.forEach((le) -> {
//            le.exception().printStackTrace();
            for( String r: le.row() )
                { System.out.println("..Const> " +r); }
            });
        }
    }
    
    /*
     * DataProvider
     */
    private static MockDataProvider getProvider()
    {
        return new MockDataProvider()
        {
            @Override
            public MockResult[] execute(MockExecuteContext ctx)
                    throws SQLException
            {
                return new MockResult[]{};
            }
        };
    }
    
    /*
     * Select some data
     */
    private static Object[] runSelect( String select )
    {
        return DSL.selectFrom(select).fetchOneArray();
    }
    
    /**
     * M A I N
     * -------
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args)
            throws Exception
    {
        // Connection
        Connection conn = new MockConnection(getProvider());
        
        // JooQ context
        ctx = DSL.using(conn); //(new DefaultConfiguration());
        
        // Load tables
        loadTables();
        
        // Stats
        System.out.println("> Num of: Constellations loaded=" +constLoad.stored()+ ", Stars loaded=" +starLoad.stored() );
        
        // Select data
//        Object[] list = runSelect( "select * from STAR" );
//        for( Object o: list )
//            {  System.out.println(">> " +o.toString());  }

        //runSelect( "select * from CONSTELLATION,STAR where STAR.ConstlID=CONSTELLATION.Id" );
    }
}

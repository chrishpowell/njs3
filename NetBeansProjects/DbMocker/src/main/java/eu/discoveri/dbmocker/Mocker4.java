/*
 */

package eu.discoveri.dbmocker;

import java.io.File;
import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.tools.jdbc.*;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 * 
 * NOTA BENE: buildConstellations() is not thread safe (but should be built only once anyway)
 */
public class Mocker4
{
    private static final Map<Integer,Star> stars = new HashMap<>();          // Index into Star
    private static final List<Constellation> constls = new ArrayList<>();
    private static final File mockprediktDb = new File("/home/chrispowell/NetBeansProjects/DbMocker/src/main/java/mockdb1/const-stars.txt");

    /**
     * See if we've built OK
     * @param c List of constellations
     * @param idx Index into Constellations list (start:0)
     */
    public static void testClassBuild(List<Constellation> c, int idx)
    {
        // How many constellations...
        System.out.println("Num. constellations: " +c.size());
        c.forEach((con) -> {
            System.out.print("[" +con.getId()+ "] " +con.getName()+ ", ");
        });
        System.out.println("\n");
        
        // Get a constellation
        Constellation c1 = c.get(idx);
        System.out.println("[" +c1.getId()+ "] " +c1.getName()+ ":");
        
        // Dump star stuff
        c1.getStars().stream().map((sc) -> {
            System.out.println("  [" +sc.getId()+ "] Name> " +sc.getName());
            return sc;
        }).map((sc) -> {
            System.out.println("    Links to:");
            return sc;
        }).forEachOrdered((sc) -> {
            for( int ii: sc.getLink2Stars() )
            {
                System.out.println("    [" +stars.get(ii).getId()+ "]: " +stars.get(ii).getName());
            }
        });
    }
    
    /*
     * Get list of stars for a Constellation
     */
    private static List<Star> findConstlStars(int constlID)
    {
        // Init star list for this constellation
        List<Star> cs = new ArrayList<>();

        // Find all Stars for this Constellation Id
        stars.entrySet().stream().filter((star) -> ( star.getValue().getConstlID() == constlID )).forEachOrdered((star) -> {
            // Add to list
            cs.add(star.getValue());
        });
        
        return cs;
    }
    
    /*
     * Build Star and Constellation lists from db
     */
    private static List<Constellation> buildConstellations()
            throws Exception
    {
        MockDataProvider db = new MockFileDatabase( mockprediktDb );
        
        try( Connection c = new MockConnection(db); Statement s = c.createStatement() )
        {
            try( ResultSet rs = s.executeQuery("select * from star") )
            {
                while( rs.next() )
                {                   
                    // Build stars list
                    String[] slinks = rs.getString(5).split(",");
                    int[] links = Arrays.stream(slinks).mapToInt(Integer::parseInt).toArray();
                    int starID = rs.getInt(1);
                    stars.put( starID, new Star(starID,rs.getString(2),rs.getString(3),rs.getInt(4),links,rs.getFloat(6),rs.getString(7),rs.getBoolean(8)) );
                }
            }

            try( ResultSet rs = s.executeQuery("select constellation.Id, constellation.Name, star.Id from constellation, star where star.ConstlID=constellation.Id"))
            {
                // Get first Constellation ID, reset ResultSet
                rs.first();
                int constlID = rs.getInt(1);  // ID 'store'
                rs.beforeFirst();
                
                while( rs.next() )
                {
                    // The ResultSet constlID
                    int rsConstlID = rs.getInt(1);
                    
                    // Change of constellation
                    if( constlID != rsConstlID )
                    {
                        // Find stars for 'previous' constellation
                        List<Star> cs = findConstlStars(constlID);
                        
                        // Set new ID 'store'
                        constlID = rsConstlID;
                        
                        // Move back 1
                        rs.relative(-1);
                        
                        // Put star list into new/prev Constellati0n
                        constls.add( new Constellation(rs.getInt(1),rs.getString(2),cs) );
                        
                        // Back to where we were (next constellation)
                        rs.relative(1);
                    }
                }
                
                // Last Constellation
                rs.last();
                constls.add( new Constellation(rs.getInt(1),rs.getString(2),findConstlStars(constlID)) );
            }
        }
        
        return constls;
    }
    
    /**
     * Get the list of constellations
     * 
     * @return 
     */
    public List<Constellation> getConstellations()
    {
        return constls;
    }
    
    /**
     * Get the 'list' of stars: <StarId,Star>
     * @return 
     */
    public Map<Integer,Star> getStars()
    {
        return stars;
    }
    
    /** GraphQL interface here **/
    
    /**
     * M A I N
     * =======
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args)
        throws Exception
    {
        testClassBuild( buildConstellations(), 1 );
    }
}

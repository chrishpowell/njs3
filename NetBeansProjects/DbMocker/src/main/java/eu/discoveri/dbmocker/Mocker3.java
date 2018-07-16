/*
 */

package eu.discoveri.dbmocker;

import java.io.File;
import static java.lang.System.out;
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
 */
public class Mocker3
{
    public static void main(String[] args)
            throws Exception
    {
        Map<Integer,Star> stars = new HashMap<>();          // Index into Star
        List<Constellation> constls = new ArrayList<>();
        
        File mockprediktDb = new File("/home/chrispowell/NetBeansProjects/DbMocker/src/main/java/mockdb1/const-stars.txt");
        MockDataProvider db = new MockFileDatabase( mockprediktDb );
        
        try( Connection c = new MockConnection(db); Statement s = c.createStatement() )
        {
            out.println("Stars:");
            out.println("------");
            try( ResultSet rs = s.executeQuery("select * from star") )
            {
                while( rs.next() )
                {
                    out.print(rs.getInt(1));
                    out.print(", " +rs.getString(2));
                    out.print(", " +rs.getString(3)); out.print(", " +rs.getInt(4));
                    out.print(", " +rs.getString(5)); out.print(", " +rs.getFloat(6));
                    out.print(", " +rs.getString(7)); out.print(", " +rs.getBoolean(8));
                    out.println("");
                    
                    // Build stars list
                    String[] slinks = rs.getString(5).split(",");
                    int[] links = Arrays.stream(slinks).mapToInt(Integer::parseInt).toArray();
                    stars.put( rs.getInt(1),new Star(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),links,rs.getFloat(6),rs.getString(7),rs.getBoolean(8)) );
                }
            }
            out.println("Total stars: " +stars.size());

            out.println();
            out.println("Constellations");
            out.println("--------------");
            try( ResultSet rs = s.executeQuery("select * from constellation"))
            {
                while( rs.next() )
                {
                    out.print(rs.getInt(1)); out.print(", " +rs.getString(2));
                    out.print(", " +rs.getString(3));
                    out.println("");
                }
            }
            
            out.println();
            out.println("Constellations and their stars:");
            out.println("-------------------------------");
            try( ResultSet rs = s.executeQuery("select constellation.Id, constellation.Name, star.Id from constellation, star where star.ConstlID=constellation.Id"))
            {
                List<Star> cs = new ArrayList<>();
                while( rs.next() )
                {
                    out.print(rs.getInt(1));
                    out.print(", " +rs.getString(2));
                    out.print(", " +rs.getInt(3));
                    out.println("");
                    
                    // Build constellation list
                    for( Map.Entry<Integer,Star> star: stars.entrySet() )
                    {
                        if( star.getValue().getConstlID() == rs.getInt(1) )
                        {
                            cs.add(star.getValue());
                        }
                    }
                    constls.add( new Constellation(rs.getInt(1),rs.getString(2),new ArrayList<Star>(cs)) );
                    cs.clear();
                }
            }
            
            out.println("\nConstellation class");
            out.println("-------------------");
            Constellation c1 = constls.get(0);
            out.println("[" +c1.getId()+ "] Name> " +c1.getName());
            out.println("  Stars: (" +c1.getStars().size()+ ")");
            for( Star sc: c1.getStars() )
            {
                out.println("  [" +sc.getId()+ "] Name> " +sc.getName());
                out.println("    Links to:");
                for( int ii: sc.getLink2Stars() )
                {
                    out.println("    [" +stars.get(ii).getId()+ "]: " +stars.get(ii).getName());
                }
            }
        }
    }
}

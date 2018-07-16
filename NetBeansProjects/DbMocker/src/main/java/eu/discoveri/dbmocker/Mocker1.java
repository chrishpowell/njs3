/*
 */
package eu.discoveri.dbmocker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.jooq.DSLContext;

import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.tools.jdbc.Mock;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Mocker1 {

    public static void main(String[] args)
            throws Exception
    {
        DSLContext ctx = DSL.using(new DefaultConfiguration());
        MockDataProvider provider = Mock.of(ctx.fetchFromCSV(
            "Id,Name,altname,ConstlID,idlinks,mag,colour,display\n" +
            "1,Schedar,Alpha Cas,2,\"3,1\",2.24,0,TRUE\n" +
            "2,Caph,Beta Cas,\"2\",1,2.28,0,TRUE\n" +
            "3,Tsih,Gamma Cas,2,\"4,1\",2.15,0,TRUE\n" +
            "4,Ruchbah,Delta Cas,2,\"5,3\",2.68,0,TRUE\n" +
            "5,Segin,Eps Cas,2,\"4\",3.35,0,TRUE\n" +
            "6,Metallah,Alpha Tri,1,\"7,8\",3.42,0,TRUE\n" +
            "7,Deltotan,Beta Tri,1,\"8,6\",3,0,TRUE\n" +
            "8,Gamma Tri,Gamma Tri,1,\"6,7\",1.24,0,TRUE\n"
        ));

        try( Connection c = new MockConnection(provider);
             PreparedStatement s = c.prepareStatement("SELECT foo");
             ResultSet rs = s.executeQuery() )
        {
            while( rs.next() )
            {
                System.out.println("ID            : " + rs.getInt(1));
                System.out.println("Star name     : " + rs.getString(2));
                System.out.println("Altv name     : " + rs.getString(3));
                System.out.println("Constellation : " + rs.getInt(4));
                System.out.println("Links (ID)    : " + rs.getString(5));
                System.out.println("Magnitude     : " + rs.getFloat(6));
                System.out.println("Colour        : " + rs.getString(7));
                System.out.println("Display       : " + rs.getBoolean(8));
                System.out.println("");
            }
        }
    }
}

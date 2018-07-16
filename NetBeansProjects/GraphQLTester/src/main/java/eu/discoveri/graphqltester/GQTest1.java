/*
 */
package eu.discoveri.graphqltester;

import com.coxautodev.graphql.tools.SchemaParser;
import javax.servlet.annotation.WebServlet;
import graphql.servlet.SimpleGraphQLServlet;

/**
 *
 * @author chrispowell
 */
public class GQTest1 extends SimpleGraphQLServlet
{
    public GQTest1()
    {
    super(SchemaParser.newParser()
            .file("schema.graphqls") //parse the schema file created earlier
            .build()
            .makeExecutableSchema());
    }
}

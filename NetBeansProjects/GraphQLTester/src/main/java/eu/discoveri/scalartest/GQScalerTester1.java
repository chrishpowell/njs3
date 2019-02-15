/*
 */
package eu.discoveri.scalartest;

import graphql.*;
import graphql.schema.*;
import static graphql.schema.GraphQLSchema.newSchema;

import java.util.List;


/**
 *
 * @author chrispowell
 */
public class GQScalerTester1
{
    /*
     * Build the schema
     */
    private static GraphQL schemaBuild()
    {
        final String SCHEMA = "/home/chrispowell/NetBeansProjects/GraphQLTester/src/main/java/resources/cst.graphqls";

//        GraphQLSchema graphQLSchema = newSchema().query().build();
//        
//        return GraphQL.newGraphQL(graphQLSchema).build();
return null;
    }
    
    /*
     * Run a query
     */
    private static ExecutionResult runQuery( GraphQL graphQL, String query )
    {
        ExecutionInput execInput = ExecutionInput.newExecutionInput().query(query).build();
        return graphQL.execute(execInput);
    }

    
    /**
     * M A I N
     * -------
     */
    public static void main(String[] args)
    {
        // Get a Person
        ExecutionResult execResult = runQuery( schemaBuild(), "{\nperson(identifier:\"EH\"){\nidentifier\nname\nzdt\n}\n}" );
        
        // Output result
        List<GraphQLError> errors = execResult.getErrors();
        if( errors.isEmpty() )
        {
            Object personData = execResult.getData();
            System.out.println("JSON>>> " +personData);
        }
        else
        {
            errors.forEach((gqlErr) -> {
                System.out.println("  " +gqlErr.getMessage());
            });
        }
    }
}

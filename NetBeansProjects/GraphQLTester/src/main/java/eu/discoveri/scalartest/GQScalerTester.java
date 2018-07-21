/*
 */
package eu.discoveri.scalartest;

import graphql.*;
import graphql.schema.*;
import graphql.schema.idl.*;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import java.io.File;
import java.util.List;


/**
 *
 * @author chrispowell
 */
public class GQScalerTester
{
    private static final DataFetcher p1DataFetcher = new P1DataFetcher();
    
    /*
     * Build the schema
     */
    private static GraphQL schemaBuild()
    {   
        File schemaFile = new File("/home/chrispowell/NetBeansProjects/GraphQLTester/src/main/java/resources/cst.graphqls");
       
        RuntimeWiring wiring = newRuntimeWiring()
                .scalar(CustScalar.ZONEDDATETIME)
                .type("QueryEndPoint", typeWiring -> typeWiring
                    .dataFetcher("person", p1DataFetcher))
                .build();
        
//        java.util.Map<String,GraphQLScalarType> stMap = wiring.getScalars();
//        stMap.forEach((s,v)->
//                { System.out.println("..>KeyString: " +s+ ", Val: " +v.getDescription()); }
//        );
        
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        
        return GraphQL.newGraphQL(graphQLSchema).build();
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
        ExecutionResult execResult = runQuery( schemaBuild(), "{\nperson(identifier:\"EH\"){\nidentifier\nname\nDoB\n}\n}" );
        
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

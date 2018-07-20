/*
 */
package eu.discoveri.graphqltester;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.TypeResolutionEnvironment;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import graphql.schema.idl.EnumValuesProvider;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
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
     * Entity resolver
     */
    private static TypeResolver resolveEntity()
    {
        return (TypeResolutionEnvironment env) -> {
            if( env.getObject() instanceof Person) { return (GraphQLObjectType)env.getSchema().getType("Person"); }
            return null;    
        };
    }
    
    /*
     * Build the schema
     */
    private static GraphQL schemaBuild()
    {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        
        File schemaFile = new File("/home/chrispowell/NetBeansProjects/GraphQLTester/src/main/java/resources/AstroSchemaHalf.graphqls");
       
        RuntimeWiring wiring = newRuntimeWiring()
                .scalar(CustScalar.ZONEDDATETIME)
                .type("Entity",typeWiring->typeWiring.typeResolver(resolveEntity()))
                .type("QueryEndPoint", typeWiring -> typeWiring
                    .dataFetcher("person", p1DataFetcher))
                .build();
        
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
        
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
     * @param args 
     */
    public static void main(String[] args)
    {
        List<GraphQLError> errors;
        ExecutionResult execResult;
        GraphQL graphQL = schemaBuild();
        
        // Get a Person
        execResult = runQuery( graphQL, "{\nperson(identifier:\"EH\"){\nidentifier\nname\nDoB\n}\n}" );
        errors = execResult.getErrors();
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

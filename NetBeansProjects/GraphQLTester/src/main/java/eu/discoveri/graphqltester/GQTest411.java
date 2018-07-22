/*
 */
package eu.discoveri.graphqltester;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.TypeResolutionEnvironment;

import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;

import graphql.schema.idl.EnumValuesProvider;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import graphql.servlet.SimpleGraphQLServlet;

import java.io.File;
import java.util.List;
import javax.servlet.annotation.WebServlet;


/**
 *
 * @author chrispowell
 */
@WebServlet("/astrology")
public class GQTest411 extends SimpleGraphQLServlet.Builder
{
    final static String SCHEMA = "/home/chrispowell/NetBeansProjects/GraphQLTester/src/main/java/resources/AstroSchema.graphqls";
    
    public GQTest411(GraphQLSchema schema) {
        super(schema);
    }

//    private static final EnumValuesProvider LUResolver = LengthUnit::valueOf;
    private static TypeResolver resolveEntity()
    {
        return (TypeResolutionEnvironment env) -> {
            if( env.getObject() instanceof Person) { return (GraphQLObjectType)env.getSchema().getType("Person"); }
            return null;    
        };
    }
    
    private static GraphQL schemaBuild()
    {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
    
        RuntimeWiring wiring = newRuntimeWiring()
                .scalar(CustomScalar.ZONEDDATETIME)
                .type("Entity",typeWiring->typeWiring.typeResolver(resolveEntity()))
//                .type("LengthUnit",typeWiring->typeWiring.enumValues(LUResolver))
                .type("QueryEndPoint", typeWiring -> typeWiring
                    .dataFetcher("person", new PersonDataFetcher())
                    .dataFetcher("sign", new SignDataFetcher()))
                .build();
        
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(new File(SCHEMA));
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
        
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
    
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
        execResult = runQuery( graphQL, "{\nperson(identifier:\"EH\"){\nidentifier\nname\ndob\n}\n}" );
        errors = execResult.getErrors();
        if( errors.isEmpty() )
        {
            Object personData = execResult.getData();
            System.out.println("JSON>>> " +personData);
        }
        else
        {
            System.out.println("***Errors:");
            // ** Do this with functional...
            errors.forEach((gqlErr) -> {
                System.out.println("  " +gqlErr.getMessage());
            });
        }
    }
}

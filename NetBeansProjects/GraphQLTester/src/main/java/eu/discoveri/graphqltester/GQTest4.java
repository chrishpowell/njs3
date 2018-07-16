/*
 */
package eu.discoveri.graphqltester;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.annotations.GraphQLDataFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author chrispowell
 */
public class GQTest4
{
    static SchemaParser schemaParser = new SchemaParser();
    static SchemaGenerator schemaGenerator = new SchemaGenerator();

    static File schemaFile = new File("/home/chrispowell/NetBeansProjects/AstroTurf3/src/main/java/eu/discoveri/astroturf3/AstroSchema.graphqls");

    static TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
    static RuntimeWiring wiring = buildRuntimeWiring();
    static GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    static GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

    static RuntimeWiring buildRuntimeWiring()
    {
        return RuntimeWiring.newRuntimeWiring()
                .type("AstrologySystem", typeWiring -> typeWiring
                        .dataFetcher("signs", GQTest4.signsDataFetcher)
                )
                .build();
    }
    
    //@GraphQLDataFetcher
    static DataFetcher<List<ZodiacSign>> signsDataFetcher = signsList -> {
        List<ZodiacSign> signs = new ArrayList<>();
        return signs;
    };
    
    public static void main(String[] args)
    {
        String GQLquery = "{\nastrology(id:9){\nname\n signs{\nid\n name\n}\n}\n}";
        ExecutionInput execInput = ExecutionInput.newExecutionInput().query(GQLquery).build();
        ExecutionResult execResult = graphQL.execute(execInput);
        Object data = execResult.getData();
        List<GraphQLError> errors = execResult.getErrors();
    }
}

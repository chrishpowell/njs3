/*
 */
package eu.discoveri.graphqltester;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import graphql.TypeResolutionEnvironment;

import graphql.schema.DataFetcher;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import graphql.schema.GraphQLTypeReference;
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
    public GQTest411(GraphQLSchema schema) {
        super(schema);
    }

    private static final EnumValuesProvider LUResolver = LengthUnit::valueOf;
    private static TypeResolver resolveEntity()
    {
        return (TypeResolutionEnvironment env) -> {
            if( env.getObject() instanceof Person) { return (GraphQLObjectType)env.getSchema().getType("Person"); }
            return null;    
        };
    }
    private static final DataFetcher signDataFetcher = new SignDataFetcher();
    private static final DataFetcher personDataFetcher = new PersonDataFetcher();
    
    private static GraphQL schemaBuild()
    {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        
        File schemaFile = new File("/home/chrispowell/NetBeansProjects/GraphQLTester/src/main/java/resources/AstroSchemaHalf.graphqls");
        
        // Test schema
//        # Person
//        type Person implements Entity {
//            # ID
//            id: ID!
//            # identifier
//            identifier: String
//            # name (username)
//            name: String!
//            # DoB
//            DoB: DateTime
//        }
//        GraphQLObjectType person = newObject()
//                .name("Person")
//                .description("This is a Person object")
//                .field(newFieldDefinition().name("id")
//                        .type(GraphQLID).build())
//                .field(newFieldDefinition().name("identifier")
//                        .type(GraphQLString).build())
//                .field(newFieldDefinition().name("name")
//                        .type(GraphQLString).build())
//                .field(newFieldDefinition().name("DoB")
//                        .type(CustomScalar.ZONEDDATETIME).build()).build();
//                .field(newFieldDefinition().name("friends")
//                    .type(GraphQLList.list(GraphQLTypeReference.typeRef("Person")))).build();
        
        RuntimeWiring wiring = newRuntimeWiring()
                .scalar(CustomScalar.ZONEDDATETIME)
                .type("Entity",typeWiring->typeWiring.typeResolver(resolveEntity()))
                .type("LengthUnit",typeWiring->typeWiring.enumValues(LUResolver))
                .type("QueryEndPoint", typeWiring -> typeWiring
                    .dataFetcher("person", personDataFetcher))
//                    .dataFetcher("sign", signDataFetcher))
                .build();
        
        /* Remove */
        //wiring.getScalars().forEach((k,v)->System.out.println("...> Key: " +k+ ", Val: " +v.getName()));
        
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
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
        execResult = runQuery( graphQL, "{\nperson(identifier:\"EH\"){\nidentifier\nname\nDoB\n}\n}" );
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
            for( GraphQLError gqlErr: errors )
                { System.out.println("  " +gqlErr.getMessage()); }
        }
        
        // Get a ZodiacSign
//        execResult = runQuery( graphQL, "{\nsign(id: 2){\nid\n name\n}\n}" ); // {\nastrology(id:9){\nname\n signs{\nid\n name\n}\n}\n}
//        errors = execResult.getErrors();
//        if( errors.isEmpty() )
//        {
//            Object signData = execResult.getData();
//            System.out.println("JSON>>> " +signData);
//        }
//        else
//        {
//            System.out.println("***Errors:");
//            // ** Do this with functional...
//            for( GraphQLError gqlErr: errors )
//                { System.out.println("  " +gqlErr.getMessage()); }
//        }
    }
}

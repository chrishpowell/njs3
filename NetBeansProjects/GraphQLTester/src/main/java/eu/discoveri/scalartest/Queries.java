/*
 */
package eu.discoveri.scalartest;

import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.schema.DataFetchingEnvironment;

import java.util.Optional;
import javax.validation.constraints.NotNull;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
@GraphQLName("query")
public class Queries
{
    @GraphQLField
    public static Optional<Person> getPerson( DataFetchingEnvironment dfe,
                                              @NotNull @GraphQLName("ID") final String id )
    {
        return Optional.of(new P1DataFetcher().get(dfe));
    }
}

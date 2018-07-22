/*
 */
package eu.discoveri.graphqltester;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Optional;


/**
 *
 * @author chrispowell
 */
public class PersonDataFetcher implements DataFetcher
{
    // This'll be a database fetch sometime...
    @Override
    public Optional<Person> get(final DataFetchingEnvironment dataFE)
    {
        for( Person person: Person.getPersonsList() )
        {
            if( person.getIdentifier().equals(dataFE.getArgument("identifier")) )
            {
                return Optional.of(person);
            }
        }
        
        return null;
    }
}

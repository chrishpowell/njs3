/*
 */
package eu.discoveri.graphqltester;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 *
 * @author chrispowell
 */
public class PersonDataFetcher implements DataFetcher
{
    @Override
    public Person get(final DataFetchingEnvironment dataFE)
    {
        for( Person person: Person.getPersonsList() )
        {
            if( person.getIdentifier().equals(dataFE.getArgument("identifier")) )
                return person;
        }
        return null;
    }
}

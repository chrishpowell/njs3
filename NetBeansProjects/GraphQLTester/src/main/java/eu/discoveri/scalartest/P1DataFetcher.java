/*
 */
package eu.discoveri.scalartest;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.time.ZonedDateTime;
import java.util.UUID;


/**
 *
 * @author chrispowell
 */
public class P1DataFetcher implements DataFetcher
{
    @Override
    public Person get(final DataFetchingEnvironment dataFE)
    {
        return new Person(  UUID.randomUUID(),
                            "EH","Edwin Hubble",
                            ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]") );
    }
}

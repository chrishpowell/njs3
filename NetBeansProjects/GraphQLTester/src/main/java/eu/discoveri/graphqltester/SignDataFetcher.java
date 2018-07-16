/*
 */
package eu.discoveri.graphqltester;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 *
 * @author chrispowell
 */
public class SignDataFetcher implements DataFetcher
{
    
    @Override
    public ZodiacSign get(final DataFetchingEnvironment dataFE)
    {
        // This'll be a db access in future
        for( ZodiacSign sign: ZodiacSign.getSignsList() )
        {
            if( sign.getId() == (Integer)dataFE.getArgument("id") )
                return sign;
        }
        return null;
    }
}

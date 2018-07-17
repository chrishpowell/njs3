/*
 */
package eu.discoveri.graphqltester;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;


/**
 * Custom scalars
 * Note!!  Remember to add 'scalar <custom-scalar>' if schema first.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class CustomScalar
{
    private static boolean looksLikeADateTime(String possibleDTValue)
    {
        // Parse the datetime string
        try
        {
            ZonedDateTime.parse(possibleDTValue);
        }
        catch( DateTimeParseException dtpe )
        {
            return false;
        }
        return true;
    }
    
    /**
     * datetime
     */
    public static final GraphQLScalarType DATETIME =
            new GraphQLScalarType("datetime", "A custom scalar that handles datetimes", new Coercing()
    {
        @Override
        public Object serialize(Object dataFetcherResult)
        {
            String possibleDTValue = String.valueOf(dataFetcherResult);
            if( looksLikeADateTime(possibleDTValue) )
            {
                return possibleDTValue;
            }
            else
            {
                throw new CoercingSerializeException("Unable to serialize " + possibleDTValue + " as a (zoned) datetime");
            }
        }

        @Override
        public Object parseValue(Object input)
        {
            if( input instanceof String )
            {
                String possibleDTValue = input.toString();
                if( looksLikeADateTime(possibleDTValue) )
                {
                    return possibleDTValue;
                }
            }
            
            throw new CoercingParseValueException("Unable to parse variable value " + input + " as a (zoned) datetime");
        }

        @Override
        public Object parseLiteral(Object input)
        {
            if( input instanceof StringValue )
            {
                String possibleDTValue = ((StringValue) input).getValue();
                if( looksLikeADateTime(possibleDTValue) )
                {
                    return possibleDTValue;
                }
            }
            throw new CoercingParseLiteralException("Value is not any email address : '" + String.valueOf(input) + "'");
        }
    });
}
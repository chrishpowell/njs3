/*
 */
package eu.discoveri.graphqltester;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
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
    /*
     * Parse string to ZonedDateTime
     */
    private static ZonedDateTime zonedDateTime(String possibleDTValue)
    {
        // Parse the datetime string
        ZonedDateTime zdt;
        try
        {
            zdt = ZonedDateTime.parse(possibleDTValue);
        }
        catch( DateTimeParseException dtpe )
        {
            return ZonedDateTime.parse("0000-01-01T00:00:00-18:00");
        }
        
        return zdt;
    }
    
    /**
     * Zoned datetime
     */
    public static final GraphQLScalarType ZONEDDATETIME =
            new GraphQLScalarType("ZonedDateTime", "Custom scalar that handles zoned datetimes", new Coercing<ZonedDateTime,String>()
    {
        @Override
        public String serialize(Object dataFetcherResult)
        {
            if( dataFetcherResult instanceof ZonedDateTime )
                { return ((ZonedDateTime)dataFetcherResult).toString(); }
            
            return null;
        }

        @Override
        public ZonedDateTime parseValue(Object input)
        {
            if( input instanceof String )
                { return zonedDateTime(input.toString()); }
            
            throw new CoercingParseValueException("Unable to parse variable value " + input.toString() + " as a (zoned) datetime");
        }

        @Override
        public ZonedDateTime parseLiteral(Object input)
        {
            if( input instanceof StringValue )
                { return zonedDateTime(((StringValue) input).getValue()); }
            
            throw new CoercingParseLiteralException("Value is not a graphql.language.StringValue: '" + input.toString() + "'");
        }
    });
}
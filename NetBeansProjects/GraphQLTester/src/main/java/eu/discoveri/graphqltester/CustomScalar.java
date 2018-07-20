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
    private static ZonedDateTime looksLikeADateTime(String possibleDTValue)
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
            new GraphQLScalarType("DateTime", "Custom scalar that handles zoned datetimes", new Coercing<ZonedDateTime,String>()
    {
        @Override
        public String serialize(Object dataFetcherResult)
        {System.out.println("...> (serialize) ZonedDateTime date fetch....");
            if( dataFetcherResult instanceof ZonedDateTime )
            {
                return ((ZonedDateTime)dataFetcherResult).toString();
            }
            
            return null;
        }

        @Override
        public ZonedDateTime parseValue(Object input)
        {System.out.println("...> (parseValue) Person date fetch....");
            if( input instanceof String )
            {
                return looksLikeADateTime(input.toString());
            }
            
            throw new CoercingParseValueException("Unable to parse variable value " + input + " as a (zoned) datetime");
        }

        @Override
        public ZonedDateTime parseLiteral(Object input)
        {System.out.println("...> (parseLiteral) Person date fetch....");
            if( input instanceof StringValue )
            {
                return looksLikeADateTime(((StringValue) input).getValue());
            }
            
            throw new CoercingParseLiteralException("Value is not a zoned datetime : '" + String.valueOf(input) + "'");
        }
    });
}
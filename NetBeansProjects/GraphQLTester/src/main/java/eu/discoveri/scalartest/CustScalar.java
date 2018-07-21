/*
 */
package eu.discoveri.scalartest;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;


/**
 * Custom scalars
 */
public class CustScalar
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
                {  System.out.println("..>Serialize(");return ((ZonedDateTime)dataFetcherResult).toString();  }

            @Override
            public ZonedDateTime parseValue(Object input)
                {  System.out.println("..>parseValue()");return zonedDateTime(input.toString());  }

            @Override
            public ZonedDateTime parseLiteral(Object input)
                {  System.out.println("parseLiteral()");return zonedDateTime(((StringValue) input).getValue());  }
        });
}
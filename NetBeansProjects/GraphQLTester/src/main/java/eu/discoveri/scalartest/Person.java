/*
 */
package eu.discoveri.scalartest;

import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author chrispowell
 */
@GraphQLName("person")
public class Person
{
    @GraphQLField
    private final java.util.UUID    ID;
    @GraphQLField
    private String                  identifier;
    @GraphQLField
    private String                  name; // username
    @GraphQLField
    private ZonedDateTime           zdt;
    @GraphQLField
    private List<Person>            friends;

    public Person(UUID ID, String identifier, String name, ZonedDateTime zdt)
    {
        this.ID = ID;
        this.identifier = identifier;
        this.name = name;
        this.zdt = zdt;
    }

    public UUID getID() { return ID; }
    public String getIdentifier() { return identifier; }
    public String getName() { return name; }
    public ZonedDateTime getZdt() { return zdt; }
    public List<Person> getFriends() { return friends; }
}

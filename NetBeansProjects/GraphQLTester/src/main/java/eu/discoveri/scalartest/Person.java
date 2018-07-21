/*
 */
package eu.discoveri.scalartest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author chrispowell
 */
public class Person
{
    private final java.util.UUID    ID;
    private String                  identifier;
    private String                  name; // username
    private ZonedDateTime           zdt;
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

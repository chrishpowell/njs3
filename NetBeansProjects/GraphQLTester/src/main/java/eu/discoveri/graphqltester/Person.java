/*
 */
package eu.discoveri.graphqltester;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private String                  lastname, firstname;
    private String                  email;
    private ZonedDateTime           dob;
    private List<Person>            friends;
    private static List<Person>     preBuiltPersonList = createPersonsList();

    public Person(UUID ID, String identifier, String name, ZonedDateTime dob)
    {
        this.ID = ID;
        this.identifier = identifier;
        this.name = name;
        this.dob = dob;
    }

    public UUID getID() {
        return ID;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public ZonedDateTime getDob() {
        return dob;
    }

    /* Testing */
    public static List<Person> getPersonsList()
    {
        return preBuiltPersonList;
    }
    
    private static List<Person> createPersonsList()
    {
        List<Person> persons = new ArrayList<>();
        
        persons.add(new Person(UUID.randomUUID(),"JB","Joe Bloggs", ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        persons.add(new Person(UUID.randomUUID(),"AE","Albert Einstein",ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        persons.add(new Person(UUID.randomUUID(),"HF","Henry Ford",ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        persons.add(new Person(UUID.randomUUID(),"IN","Isaac Newton",ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        persons.add(new Person(UUID.randomUUID(),"FG","Friedrich Gauss",ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        persons.add(new Person(UUID.randomUUID(),"EH","Edwin Hubble",ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")));
        
        return persons;
    }
}

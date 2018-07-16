/*
 */
package eu.discoveri.graphqltester;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author chrispowell
 */
public class Person
{
    private java.util.UUID ID;
    private String identifier;
    private String name; // username
    private String lastname, firstname;
    private String email;
    private List<Person> friends;
    private static List<Person> preBuiltPersonList = createPersonsList();

    public Person(UUID ID, String identifier, String name) {
        this.ID = ID;
        this.identifier = identifier;
        this.name = name;
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
    
    public static List<Person> getPersonsList()
    {
        return preBuiltPersonList;
    }
    
    private static List<Person> createPersonsList()
    {
        List<Person> persons = new ArrayList<>();
        
        persons.add(new Person(UUID.randomUUID(),"JB","Joe Bloggs"));
        persons.add(new Person(UUID.randomUUID(),"AE","Albert Einstein"));
        persons.add(new Person(UUID.randomUUID(),"HF","Henry Ford"));
        persons.add(new Person(UUID.randomUUID(),"IN","Isaac Newton"));
        persons.add(new Person(UUID.randomUUID(),"FG","Friedrich Gauss"));
        persons.add(new Person(UUID.randomUUID(),"EH","Edwin Hubble"));
        
        return persons;
    }
}

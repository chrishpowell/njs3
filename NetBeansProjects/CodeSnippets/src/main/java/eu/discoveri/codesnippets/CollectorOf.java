/*
 */
package eu.discoveri.codesnippets;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class CollectorOf
{
    interface SJ { StringJoiner1 create(); }
    
    public static void main(String[] args)
    {
        //Supplier<StringJoiner1> sj = new StringJoiner1(":");  ERR!! StringJoiner cannot be converted to Supplier<StringJoiner>
        // XXX::new is a Functional Interface
        SJ sj9 = StringJoiner1::new;
        StringJoiner1 sj99 = sj9.create();
        
        Supplier<StringJoiner1> sj0 = StringJoiner1::new;
        StringJoiner1 sj00 = sj0.get();

        // OR...
        Supplier<StringJoiner1> sja = () -> new StringJoiner1(":");
        StringJoiner1 sjaa = sja.get();

        Function<String,StringJoiner1> sj1 = StringJoiner1::new;
        StringJoiner1 sj11 = sj1.apply(":");
    }
}

//------------------------------------------------------------------------------
class Person1
{
    String name;
    int age;

    Person1(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}

//------------------------------------------------------------------------------
class StringJoiner1
{
    String         joiner;
    StringBuilder  out = new StringBuilder();
    
    // Constructor
    public StringJoiner1(){}
    public StringJoiner1( String joiner ) { this.joiner = joiner; }
    
    // Aggregate
    public void add( String a ) { out.append(a).append(joiner); }
    
    // Getter
    public String getJoiner() { return joiner; }
    
    // Merge StringJoiners (usually map.merge())
    public StringJoiner1 nullBack(StringJoiner1 s) { return this; }
    
    @Override
    public String toString() { return out.toString(); }
}

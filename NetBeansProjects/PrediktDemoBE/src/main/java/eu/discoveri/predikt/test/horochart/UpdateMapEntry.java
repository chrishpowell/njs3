/*
 * Update a map entry
 */
package eu.discoveri.predikt.test.horochart;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class UpdateMapEntry
{
    private String  key;
    private Entry1  e1;
    
    private static Map<String, UpdateMapEntry> map = new HashMap<>();
    
    public UpdateMapEntry( String key, Entry1 e1 )
    {
        this.key = key;
        this.e1 = e1;
    }
    
    public static void main(String[] args)
    {
        Entry2 e21 = new Entry2(1.d,2.d);
        Entry1 e11 = new Entry1(e21,"one",21);
        UpdateMapEntry ume1 = new UpdateMapEntry("one", e11);
        map.put("one", ume1);
        
        Entry2 e22 = new Entry2(1.d,2.d);
        Entry1 e12 = new Entry1(e21,"two",21);
        UpdateMapEntry ume2 = new UpdateMapEntry("two", e12);
        map.put("two",ume2);
        
        // Get from map
        System.out.println("1. Map one: " +map.get("one").e1.getName() + ", " +map.get("one").e1.getAmount());
        
        e11.setName("update");
        e11.setAmount(109);
        
        // Check map
        System.out.println("2. Map one: " +map.get("one").e1.getName() + ", " +map.get("one").e1.getAmount());
    }
}

class Entry1
{
    private Entry2  e2;
    private String  name;
    private int     amount;
    
    public Entry1( Entry2 e2, String name, int amount )
    {
        this.e2 = e2;
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Entry2 getE2() {
        return e2;
    }

    public void setE2(Entry2 e2) {
        this.e2 = e2;
    }
}

class Entry2
{
    private final double  d0;
    private final double  d1;
    
    public Entry2( double d0, double d1 )
    {
        this.d0 = d0;
        this.d1 = d1;
    }
}
/*
 */
package eu.discoveri.graphqltester;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chrispowell
 */
public class ZodiacSign
{
    private int id;
    private String name;
    private String additional;
    private static List<ZodiacSign> preBuiltSignList = createSignsList();

    public ZodiacSign(int id, String name, String additional)
    {
        this.id = id;
        this.name = name;
        this.additional = additional;
    }
    
    public int getId() { return id; }

    public String getName() { return name; }
    
    public String outputZodiacSign()
    {
        return id+ ": " +name;
    }
    
    public static List<ZodiacSign> getSignsList()
    {
        return preBuiltSignList;
    }
    
    private static List<ZodiacSign> createSignsList()
    {
        List<ZodiacSign> signs = new ArrayList<>();
        
        signs.add(new ZodiacSign(1,"Cancer","Cancer"));
        signs.add(new ZodiacSign(2,"Libra","Libra"));
        signs.add(new ZodiacSign(3,"Capricorn","Capricorn"));
        signs.add(new ZodiacSign(4,"Pisces","Pisces"));
        signs.add(new ZodiacSign(5,"Aries","Aries"));
        
        return signs;
    }
}

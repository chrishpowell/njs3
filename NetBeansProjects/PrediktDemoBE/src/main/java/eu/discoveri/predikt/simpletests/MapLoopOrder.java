/*
 */
package eu.discoveri.predikt.simpletests;

import java.util.TreeMap;
import java.util.Map;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class MapLoopOrder
{
    public static void main(String[] args)
    {
        Map<String, Integer> items = new TreeMap<>();
        items.put("key 1", 1);
        items.put("key 2", 2);
        items.put("key 3", 3);

        items.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
        System.out.println("-------");
        items.keySet().iterator().forEachRemaining(key->System.out.println(key+"-"+items.get(key)));
    }
}

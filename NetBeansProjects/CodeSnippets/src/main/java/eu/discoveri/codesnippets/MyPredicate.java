/*
 */
package eu.discoveri.codesnippets;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @param <T>
 */
public interface MyPredicate<T>
{
    boolean test (T t);
}

class Filter
{
    // Filtering
    public static <T> List<T> filters( List<T> inventory, MyPredicate<T> p )
    {
        List<T> res = new ArrayList<>();
        // If predicate test passed, add to new list
        inventory.stream().filter(a -> (p.test(a))).forEachOrdered(a -> {
            res.add(a);
        });
        
        return res;
    }
}

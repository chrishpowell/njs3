/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Utils
{
    /**
     * Remove duplicates from list1 and its associated list2. Note: Custom classes
     * of type T should express equals()/hashCode() otherwise duplicates may not
     * be compared and hence deemed not to exist.  Note also that a 'copy' List
     * is required for the List.remove() method to work.  Eg:
     * List&left;X&right; x = somemethod.genX; // new ArrayList<>() for X in somemethod
     * List&left;Y&right; y = new ArrayList<>(othermethod.genY()); // No new ArrayList in
     * othermethod.
     * 
     * The arguments themselves are
     * altered, there are no return values.
     * 
     * @param <T> (List of) type implementing equals()
     * @param <U> (List of) type
     * @param list1 'Main' list
     * @param list2 List in one-to-one relation with list1
     */
    public static <T,U> void removeDupsFromLists( List<T> list1, List<U> list2 )
    {
        List<T> dups = new ArrayList<>();
        int ii = 0;
        for( Iterator<T> iter = list1.listIterator(); iter.hasNext(); )
        {
            T l = iter.next();
            if(!dups.contains(l))
            {
                dups.add(l);                    // Add unique entry
                ii++;
            }
            else
            {                                   // It's a duplicate, remove
                iter.remove();
                list2.remove(ii);
            }
        }
    }
}

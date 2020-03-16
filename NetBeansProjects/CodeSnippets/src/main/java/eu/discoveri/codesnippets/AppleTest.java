/*
 */
package eu.discoveri.codesnippets;

import java.awt.Color;
import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AppleTest
{
    public static void main(String[] args)
    {
        List<Apple> listApple = List.of( new Apple(Color.GREEN,175),
                                         new Apple(Color.PINK,162),
                                         new Apple(Color.PINK,125),
                                         new Apple(Color.RED,100),
                                         new Apple(Color.RED,170),
                                         new Apple(Color.GREEN,170),
                                         new Apple(Color.GREEN,105));
        
        System.out.println("> " +Filter.filters(listApple, new AppleGreenHeavyPredicate()));
    }
}

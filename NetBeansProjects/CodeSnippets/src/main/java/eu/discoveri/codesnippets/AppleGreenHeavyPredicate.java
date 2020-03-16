/*
 */
package eu.discoveri.codesnippets;

import java.awt.Color;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AppleGreenHeavyPredicate implements MyPredicate<Apple>
{
    @Override
    public boolean test(Apple apple)
    {
        return (apple.getColor().equals(Color.GREEN) || apple.getColor().equals(Color.PINK))&& apple.getWeight() > 150;
    }
}

/*
 */
package eu.discoveri.codesnippets;

import java.awt.Color;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AppleWeightPredicate implements MyPredicate<Apple>
{
    @Override
    public boolean test(Apple apple) { return apple.getColor().equals(Color.GREEN); }
}

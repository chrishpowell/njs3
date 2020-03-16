/*
 */
package eu.discoveri.codesnippets;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AppleGreenPredicate implements MyPredicate<Apple>
{
    @Override
    public boolean test(Apple apple) { return apple.getWeight() > 150; }
}

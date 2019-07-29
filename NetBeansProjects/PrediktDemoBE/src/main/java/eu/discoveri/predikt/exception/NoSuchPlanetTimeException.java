/*
 * A planet and a julDay exception
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class NoSuchPlanetTimeException extends Exception
{
    /**
     * Creates a new instance of <code>NoSuchPlanetTimeException</code> without
     * detail message.
     */
    public NoSuchPlanetTimeException() {}

    /**
     * Constructs an instance of <code>NoSuchPlanetTimeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchPlanetTimeException(String msg) { super(msg); }
}

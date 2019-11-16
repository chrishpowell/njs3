/*
 * Invalid Lat/Lon (location)
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class InvalidLocationException extends Exception
{
    /**
     * Creates a new instance of <code>InvalidLocationException</code> without
     * detail message.
     */
    public InvalidLocationException() {}

    /**
     * Constructs an instance of <code>InvalidLocationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidLocationException(String msg) { super(msg); }
}

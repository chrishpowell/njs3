/*
 * Zodiac signs runs 0 to 11
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class ZSignRangeException extends Exception
{

    /**
     * Creates a new instance of <code>ZSignRangeException</code> without detail
     * message.
     */
    public ZSignRangeException() {}

    /**
     * Constructs an instance of <code>ZSignRangeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ZSignRangeException(String msg)
    {
        super(msg);
    }
}

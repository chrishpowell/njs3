/*
 * Range of degrees allowable for a zodiac sign
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class ZSignDegreeException extends Exception
{
    /**
     * Creates a new instance of <code>ZSignDegreeException</code> without
     * detail message.
     */
    public ZSignDegreeException() {}

    /**
     * Constructs an instance of <code>ZSignDegreeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ZSignDegreeException(String msg) {
        super(msg);
    }
}

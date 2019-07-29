/*
 * Degree/Minute/Second not structured correctly
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class DegreeMinSecException extends Exception
{

    /**
     * Creates a new instance of <code>DegreeMinSecException</code> without
     * detail message.
     */
    public DegreeMinSecException() {}

    /**
     * Constructs an instance of <code>DegreeMinSecException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DegreeMinSecException(String msg)
    {
        super(msg);
    }
}

/*
 * RealTime conversion failed.
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class RealTimeConversionException extends Exception
{
    /**
     * Creates a new instance of <code>RealTimeConversionException</code>
     * without detail message.
     */
    public RealTimeConversionException() {}

    /**
     * Constructs an instance of <code>RealTimeConversionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RealTimeConversionException(String msg)
    {
        super(msg);
    }
}

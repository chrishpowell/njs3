/*
 * Wheel/Ring geometry doesn't work.
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class WheelGeometryException extends Exception
{
    /**
     * Creates a new instance of <code>WheelGeometryException</code> without
     * detail message.
     */
    public WheelGeometryException() {}

    /**
     * Constructs an instance of <code>WheelGeometryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WheelGeometryException(String msg) {
        super(msg);
    }
}

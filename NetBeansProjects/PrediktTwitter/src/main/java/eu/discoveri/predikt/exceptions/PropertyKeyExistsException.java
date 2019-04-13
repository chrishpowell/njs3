/*
 * Map duplicate key exception.
 */
package eu.discoveri.predikt.exceptions;

/**
 * Property files are read and keyed into Map<String,Properties>.  Duplicate
 * keys are checked and disallowed (default is overwrite).
 * 
 * @author Chris Powell, Discoveri OU
 */
public class PropertyKeyExistsException extends Exception
{
    /**
     * Creates a new instance of <code>PropertyKeyExistsException</code> without
     * detail message.
     */
    public PropertyKeyExistsException() {}

    /**
     * Constructs an instance of <code>PropertyKeyExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PropertyKeyExistsException(String msg)
        { super(msg); }
}

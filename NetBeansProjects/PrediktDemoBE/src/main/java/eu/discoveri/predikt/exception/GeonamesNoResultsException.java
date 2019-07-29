/*
 * Geonames returns no results.
 */
package eu.discoveri.predikt.exception;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class GeonamesNoResultsException extends Exception
{
    /**
     * Creates a new instance of <code>GeonamesNoResultsException</code> without
     * detail message.
     */
    public GeonamesNoResultsException() {}

    /**
     * Constructs an instance of <code>GeonamesNoResultsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public GeonamesNoResultsException(String msg) {
        super(msg);
    }
}

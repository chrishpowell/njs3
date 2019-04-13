/*
 * Lang/Ctry code needs to be of form aa-BB (eg:zh-CN) and on db list
 */
package eu.discoveri.predikt.exceptions;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public class UnrecognizedLangCtryCodeException extends Exception {

    /**
     * Creates a new instance of <code>UnrecognizedLangCtryCodeException</code>
     * without detail message.
     */
    public UnrecognizedLangCtryCodeException() {
    }

    /**
     * Constructs an instance of <code>UnrecognizedLangCtryCodeException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnrecognizedLangCtryCodeException(String msg) {
        super(msg);
    }
}

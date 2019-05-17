/*
 * Print out string with timestamp
 */
package eu.discoveri.predikt.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BasicTSLogger
{
    // Datetime stamp format
    private final static String DTF = "yyyy-MM-dd HH:mm:ss";

    public static void Logger( String log )
    {
        System.out.println("[" +DateTimeFormatter.ofPattern(DTF).format(LocalDateTime.now())+ "] " + log );
    }
}

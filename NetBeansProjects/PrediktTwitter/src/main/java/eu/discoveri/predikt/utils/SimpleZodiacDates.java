/*
 * A simple (Western) zodiac date calculator
 */
package eu.discoveri.predikt.utils;

import java.time.LocalDate;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class SimpleZodiacDates
{
    /**
     * Get sign from date.
     * @TODO: Needs fixing for cusps (hence 'Simple...')
     * 
     * @param mthDay
     * @return 
     */
    public static String zsign( LocalDate mthDay )
    {
        // Ok, extract month and day from date
        int month = mthDay.getMonthValue();
        int day = mthDay.getDayOfMonth();
        
        // Get sign
        if( (month == 12 && day >= 22 && day <= 31) || (month ==  1 && day >= 1 && day <= 19) )
            return "Capricorn";
        else if( (month ==  1 && day >= 20 && day <= 31) || (month ==  2 && day >= 1 && day <= 17) )
            return "Aquarius";
        else if( (month ==  2 && day >= 18 && day <= 29) || (month ==  3 && day >= 1 && day <= 19) )
            return "Pisces";
        else if( (month ==  3 && day >= 20 && day <= 31) || (month ==  4 && day >= 1 && day <= 19) )
            return "Aries";
        else if( (month ==  4 && day >= 20 && day <= 30) || (month ==  5 && day >= 1 && day <= 20) )
            return "Taurus";
        else if( (month ==  5 && day >= 21 && day <= 31) || (month ==  6 && day >= 1 && day <= 20) )
            return "Gemini";
        else if( (month ==  6 && day >= 21 && day <= 30) || (month ==  7 && day >= 1 && day <= 22) )
            return"Cancer";
        else if( (month ==  7 && day >= 23 && day <= 31) || (month ==  8 && day >= 1 && day <= 22) )
            return "Leo";
        else if( (month ==  8 && day >= 23 && day <= 31) || (month ==  9 && day >= 1 && day <= 22) )
            return"Virgo";
        else if( (month ==  9 && day >= 23 && day <= 30) || (month == 10 && day >= 1 && day <= 22) )
            return "Libra";
        else if( (month == 10 && day >= 23 && day <= 31) || (month == 11 && day >= 1 && day <= 21) )
            return "Scorpio";
        else if( (month == 11 && day >= 22 && day <= 30) || (month == 12 && day >= 1 && day <= 21) )
            return "Sagittarius";
        else
            return "";
    }
}

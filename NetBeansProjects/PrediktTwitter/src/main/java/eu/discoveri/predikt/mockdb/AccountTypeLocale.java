/*
 * User account locale
 */
package eu.discoveri.predikt.mockdb;

import java.util.Currency;
import java.util.Locale;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AccountTypeLocale extends JSONMapper
{
    // Default values
    private Locale          locale;
    private String          perMonth,
                            perUser,
                            unicodeVersion,
                            regex;

    /**
     * Constructor.
     * 
     * @param locale
     * @param perMonth
     * @param perUser
     * @param unicodeVersion
     * @param regex
     */
    public AccountTypeLocale(   Locale locale,
                            String perMonth,
                            String perUser,
                            String unicodeVersion,
                            String regex )
    {
        this.locale = locale;
        this.perMonth = perMonth;
        this.perUser = perUser;
        this.unicodeVersion = unicodeVersion;
        this.regex = regex;
    }

    //-------------------------------
    //      Mutators
    //-------------------------------
    /**
     * Get Locale.
     * @return 
     */
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCurrency() {
        return Currency.getInstance(locale).getCurrencyCode();
    }

    public String getLang() {
        return locale.getLanguage();
    }

    public String getPerMonth() {
        return perMonth;
    }

    public void setPerMonth(String perMonth) {
        this.perMonth = perMonth;
    }

    public String getPerUser() {
        return perUser;
    }

    public void setPerUser(String perUser) {
        this.perUser = perUser;
    }

    public String getUnicodeVersion() {
        return unicodeVersion;
    }

    public void setUnicodeVersion(String unicodeVersion) {
        this.unicodeVersion = unicodeVersion;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
    
    @Override
    public String toString()
    {
        return locale.toString()+", "+perMonth+", "+perUser+", "+unicodeVersion+", "+regex;
    }
}


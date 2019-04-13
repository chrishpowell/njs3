/*
 * I18N cache
 */
package eu.discoveri.predikt.i18n;

import java.util.List;

import eu.discoveri.predikt.mockdb.AccountTypeLocale;
import eu.discoveri.predikt.mockdb.AccountType;
import eu.discoveri.predikt.mockdb.ErrorMsgs;
import eu.discoveri.predikt.mockdb.RegFormInputs;
import eu.discoveri.predikt.mockdb.RegFormValues;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class I18NCache
{
    private final AccountTypeLocale   accountTypeLocale;
    private final RegFormValues       regFormValues;
    private final RegFormInputs       regFormInputs;
    private final List<AccountType>   accountTypeList;
    private final ErrorMsgs           errorMsgs;


    /**
     * Constructor.
     * 
     * @param accountTypeLocale
     * @param regFormValues
     * @param regFormInputs
     * @param accountTypeList
     * @param errorMsgs 
     */
    public I18NCache( AccountTypeLocale accountTypeLocale,
                      RegFormValues regFormValues,
                      RegFormInputs regFormInputs,
                      List<AccountType> accountTypeList,
                      ErrorMsgs errorMsgs)
    {
        this.accountTypeLocale = accountTypeLocale;
        this.regFormValues = regFormValues;
        this.regFormInputs = regFormInputs;
        this.accountTypeList = accountTypeList;
        this.errorMsgs = errorMsgs;
    }


    /*
     * Mutators
     * --------
     */
    public AccountTypeLocale getAccountTypeLocale() {
        return accountTypeLocale;
    }

    public RegFormValues getRegFormValues() {
        return regFormValues;
    }

    public RegFormInputs getRegFormInputs() {
        return regFormInputs;
    }

    public List<AccountType> getAccountTypeList() {
        return accountTypeList;
    }

    public ErrorMsgs getErrorMsgs() {
        return errorMsgs;
    }
}

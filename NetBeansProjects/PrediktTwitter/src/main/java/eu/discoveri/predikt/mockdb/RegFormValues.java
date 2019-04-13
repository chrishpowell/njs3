/*
 * Registration display fields initial values
 */
package eu.discoveri.predikt.mockdb;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class RegFormValues extends JSONMapper
{
    private final String    mainTitle,
                            confMsg,
                            closeButton,
                            acctDetails,
                            acctTypes,
                            prefLangChoose,
                            famGrpTypesMsg,
                            loginButton,
                            registerButton,
                            buttonText;

    public RegFormValues( String mainTitle,
                          String confMsg,
                          String closeButton,
                          String acctDetails,
                          String acctTypes,
                          String prefLangChoose,
                          String famGrpTypesMsg,
                          String loginButton,
                          String registerButton,
                          String buttonText)
    {
        this.mainTitle = mainTitle;
        this.confMsg = confMsg;
        this.closeButton = closeButton;
        this.acctDetails = acctDetails;
        this.acctTypes = acctTypes;
        this.prefLangChoose = prefLangChoose;
        this.famGrpTypesMsg = famGrpTypesMsg;
        this.loginButton = loginButton;
        this.registerButton = registerButton;
        this.buttonText = buttonText;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getConfMsg() {
        return confMsg;
    }

    public String getCloseButton() {
        return closeButton;
    }

    public String getAcctDetails() {
        return acctDetails;
    }

    public String getAcctTypes() {
        return acctTypes;
    }

    public String getPrefLangChoose() {
        return prefLangChoose;
    }

    public String getFamGrpTypesMsg() {
        return famGrpTypesMsg;
    }

    public String getLoginButton() {
        return loginButton;
    }

    public String getRegisterButton() {
        return registerButton;
    }

    public String getButtonText() {
        return buttonText;
    }
};


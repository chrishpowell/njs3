/*
 * Error messages
 */
package eu.discoveri.predikt.mockdb;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ErrorMsgs extends JSONMapper
{
    private final String    userNameIsReqd,
                            userNameInvalid,
                            emailReqd,
                            pwdRequired,
                            pwdInvalid,
                            pwdConfirm,
                            pwdNotSame,
                            dateInvalid,
                            loginFail,
                            browserNoLocation;

    public ErrorMsgs(   String userNameIsReqd,
                        String userNameInvalid,
                        String emailReqd,
                        String pwdRequired,
                        String pwdInvalid,
                        String pwdConfirm,
                        String pwdNotSame,
                        String dateInvalid,
                        String loginFail,
                        String browserNoLocation  )
    {
        this.userNameIsReqd = userNameIsReqd;
        this.userNameInvalid = userNameInvalid;
        this.emailReqd = emailReqd;
        this.pwdRequired = pwdRequired;
        this.pwdInvalid = pwdInvalid;
        this.pwdConfirm = pwdConfirm;
        this.pwdNotSame = pwdNotSame;
        this.dateInvalid = dateInvalid;
        this.loginFail = loginFail;
        this.browserNoLocation = browserNoLocation;
    }

    public String getUserNameIsReqd() {
        return userNameIsReqd;
    }

    public String getUserNameInvalid() {
        return userNameInvalid;
    }

    public String getEmailReqd() {
        return emailReqd;
    }

    public String getPwdRequired() {
        return pwdRequired;
    }

    public String getPwdInvalid() {
        return pwdInvalid;
    }

    public String getPwdConfirm() {
        return pwdConfirm;
    }

    public String getPwdNotSame() {
        return pwdNotSame;
    }

    public String getDateInvalid() {
        return dateInvalid;
    }

    public String getLoginFail() {
        return loginFail;
    }

    public String getBrowserNoLocation() {
        return browserNoLocation;
    }
}

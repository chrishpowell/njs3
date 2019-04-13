/*
 * Input fields initial values
 */
package eu.discoveri.predikt.mockdb;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class RegFormInputs extends JSONMapper
{
    private final String    familyname,
                            othername,
                            email,
                            password,
                            password2,
                            dob,
                            lob,
                            locn;

    public RegFormInputs(String familyname, String othername, String email, String password, String password2, String dob, String lob, String locn) {
        this.familyname = familyname;
        this.othername = othername;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.dob = dob;
        this.lob = lob;
        this.locn = locn;
    }

    public String getFamilyname() {
        return familyname;
    }

    public String getOthername() {
        return othername;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public String getDob() {
        return dob;
    }

    public String getLob() {
        return lob;
    }

    public String getLocn() {
        return locn;
    }
}


/*
 * Account Types (Radio Buttons)
 */
package eu.discoveri.predikt.mockdb;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class AccountType extends JSONMapper
{
    private String        id,
                          desc;
    private BigDecimal    price;
    private boolean       perUser;
    

    /**
     * Constructor.
     * 
     * @param id
     * @param desc
     * @param price
     * @param perUser 
     */
    public AccountType(String id, String desc, BigDecimal price, boolean perUser)
    {
        this.id = id;
        this.desc = desc;
        this.price = price;
        this.perUser = perUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getPrice() {
        return price.setScale(2,RoundingMode.CEILING);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPerUser() {
        return perUser;
    }

    public void setPerUser(boolean perUser) {
        this.perUser = perUser;
    }
}

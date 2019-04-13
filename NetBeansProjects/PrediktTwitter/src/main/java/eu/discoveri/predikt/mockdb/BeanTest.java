/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.discoveri.predikt.mockdb;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BeanTest
{
    private int     id;
    private String  name;
    
    public BeanTest(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal("3.99");
//        MathContext mc = new MathContext(2,Rounding);
        
        System.out.println("..> " +bd+ " : " +bd.setScale(2,RoundingMode.CEILING));
    }
}

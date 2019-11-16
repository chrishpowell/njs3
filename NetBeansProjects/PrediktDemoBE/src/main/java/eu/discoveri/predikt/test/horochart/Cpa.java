/*
 * @TODO: Maybe delete?
 */

package eu.discoveri.predikt.test.horochart;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Cpa
{
    private final double    angle;
    private final int       num;
    private final String    name;

    // Borth, Wales
    private final static Map<Integer,Cpa> cpaMap0 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Dublin, Ireland
    private final static Map<Integer,Cpa> cpaMap1 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Recife, Brazil
    private final static Map<Integer,Cpa> cpaMap2 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.380,3,"Can"));
            put(1,new Cpa(0.1248,4,"Leo"));
            put(2,new Cpa(0.4516,4,"Leo"));
            put(3,new Cpa(0.3781,5,"Vir"));
            put(4,new Cpa(0.5135,6,"Lib"));
            put(5,new Cpa(0.2713,8,"Sag"));
            put(6,new Cpa(0.3801,9,"Cap"));
            put(7,new Cpa(0.12488,10,"Aqu"));
            put(8,new Cpa(0.4516,10,"Aqu"));
            put(9,new Cpa(0.378,11,"Pis"));
            put(10,new Cpa(0.5135,0,"Ari"));
            put(11,new Cpa(0.2713,2,"Gem"));
        }};
    
    // Gaborone, Botswana
    private final static Map<Integer,Cpa> cpaMap3 = new HashMap<Integer,Cpa>()
        {{
            put(0,new Cpa(0.407215,2,"Gem"));
            put(1,new Cpa(0.442845,3,"Can"));
            put(2,new Cpa(0.00491497,5,"Vir"));
            put(3,new Cpa(0.07845,6,"Lib"));
            put(4,new Cpa(0.07615,7,"Sco"));
            put(5,new Cpa(0.52112779,7,"Sco"));
            put(6,new Cpa(0.407215,8,"Sag"));
            put(7,new Cpa(0.442845,9,"Cap"));
            put(8,new Cpa(0.0049149,11,"Pis"));
            put(9,new Cpa(0.07845,0,"Ari"));
            put(10,new Cpa(0.07615475,1,"Tau"));
            put(11,new Cpa(0.52112779,1,"Tau"));
        }};
    
    /**
     * Constructor.
     * 
     * @param angle
     * @param num
     * @param name 
     */
    public Cpa( double angle, int num, String name )
    {
        this.angle = angle;
        this.num = num;
        this.name = name;
    }
    
    public static Map<Integer,Cpa> getCpa() { return cpaMap3; }
    public double getAngle() { return angle; }
    public int getNum() { return num; }
    public String getName() { return name; }
}

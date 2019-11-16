/*
 */
package eu.discoveri.predikt.simpletests;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ReduceHierarchy
{
//    static Map<P1P2Key,String> p1p2Map = new HashMap<P1P2Key,String>() {{
//        put(new P1P2Key("a1","a2"),"a-first");
//        put(new P1P2Key("b1","b2"),"b-first");
//        put(new P1P2Key("b1","b3"),"b-second");
//        put(new P1P2Key("b1","b4"),"b-third");
//        put(new P1P2Key("c1","a2"),"c-first");
//        put(new P1P2Key("c2","a2"),"c-second");
//        put(new P1P2Key("d1","f1"),"d-first");
//        put(new P1P2Key("d1","f2"),"d-second");
//        put(new P1P2Key("d2","f3"),"d-third");
//    }};
    
    static List<PA> psa = Arrays.asList(   new PA(new P12("a1",1.0),new P12("a2",2.0),new A("aspect-1"),15.5),
                                    new PA(new P12("a1",1.0),new P12("a3",2.0),new A("aspect-2"),25.5),
                                    new PA(new P12("b1",1.0),new P12("a1",2.0),new A("aspect-3"),35.5),
                                    new PA(new P12("c1",1.0),new P12("a2",2.0),new A("aspect-4"),45.5),
                                    new PA(new P12("c1",1.0),new P12("c2",2.0),new A("aspect-5"),55.5),
                                    new PA(new P12("c1",1.0),new P12("c3",2.0),new A("aspect-6"),65.5),
                                    new PA(new P12("c1",1.0),new P12("c4",2.0),new A("aspect-7"),75.5),
                                    new PA(new P12("d1",1.0),new P12("b1",2.0),new A("aspect-8"),85.5),
                                    new PA(new P12("d1",1.0),new P12("d2",2.0),new A("aspect-9"),95.5),
                                    new PA(new P12("e1",1.0),new P12("e2",2.0),new A("aspect-a"),150.5),
                                    new PA(new P12("f1",1.0),new P12("f2",2.0),new A("aspect-b"),151.5),
                                    new PA(new P12("f1",1.0),new P12("f3",2.0),new A("aspect-c"),152.5)    );
    
    public static void main(String[] args)
    {
        Map<P12,List<PA>> psaByP12 = psa.stream().collect(Collectors.groupingBy(PA::getP1));
        psaByP12.keySet().forEach(k->System.out.println("P1: "+k.getName()));
    }
}


class PA
//------------------------------------------------------------------------------
{
    private final P12       p1, p2;
    private final A         a;
    private final double    degsDiff;

    /**
     * Constructor.
     * 
     * @param p1
     * @param p2
     * @param aspect 
     */
    public PA(P12 p1, P12 p2, A aspect, double degsDiff)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.a = aspect;
        this.degsDiff = degsDiff;
    }

    public P12 getP1() { return p1; }
    public P12 getP2() { return p2; }
}

class P12
//------------------------------------------------------------------------------
{
    private final String name;
    private final double num;
    
    public P12( String name, double num )
    {
        this.name = name;
        this.num = num;
    }

    public String getName() { return name; }
    public double getNum() { return num; }
}

class A
//------------------------------------------------------------------------------
{
    private final String aspect;

    public A(String aspect) {
        this.aspect = aspect;
    }

    public String getAspect() { return aspect; }
}
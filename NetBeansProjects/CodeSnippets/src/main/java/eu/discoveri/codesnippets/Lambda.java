/*
 */
package eu.discoveri.codesnippets;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
//import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class Lambda
{
    static UnaryOperator<Integer> factorial = UnaryOperator.identity();         // Must be static or instance var (not local)
    
    public static void bic()
    {
        BiConsumer<Integer,Integer> bc = (a,t) -> System.out.println("1> " +(a+t));
        bc.accept(21,34);
    }
    
    public static void cr()
            throws Exception
    {
        // Target type derived from another target type
        Callable<Runnable> c = () -> () -> { System.out.println("Yo!"); };
        c.call().run();
    }
    
    public static void ulogic()
    {
        UnaryOperator<Integer> xor = a -> a ^ 1; 
        UnaryOperator<Integer> and = a -> a & 1;
        
        System.out.println("xor: " +xor.apply(231));
        System.out.println("and: " +and.apply(230));
        Function<Integer, Integer> compose = xor.compose(and); 
        System.out.println("xor & and: " +compose.apply(231)); 
    }
    
    static List<SkyPosn> pts =
        Arrays.asList(
            new SkyPosn(17f, 18f),
            new SkyPosn(4f, 26.26f),
            new SkyPosn(23f, -1f),
            new SkyPosn(-2f, 43f));
    public static void transp()
    {
        pts.forEach(SkyPosn::transpose);  // Transpose coords
        pts.forEach(System.out::println);
    }
    
    public static void last()
    {
        //Optional<SkyPosn> ll = pts.stream().reduce((a,b)->b); // .orElse(new SkyPosn(0f,0f)); // [then not Optional]
        // OR... using identity
        SkyPosn ll = pts.stream().reduce(new SkyPosn(0f,0f), (a,b)->b);         // Iterate until at last entry
        System.out.println("last: " +ll);
    }
    
    // Reopeat an action
    public static void rpt( int count, Runnable act )
    {
        IntStream.range(0,count).forEach(a -> act.run());
    }
    
    public static void main(String[] args)
            throws Exception
    {
//        bic();
//        cr();
//        
//        //Object o = () -> { System.out.println(""); };  ERR!! Object is not a functional interface (and could be Runnable or Callable)
//        Object o = (Runnable) () -> { System.out.println(""); };        // Above line disambiguated (but how to execute?)
//
//        // Factorial
//        factorial = i -> i == 0 ? 1 : i * factorial.apply( i - 1 );
//        System.out.println("UO: " +factorial.apply(7));
//        
//        // Unary op
//        ulogic();
//        // Transpose
//        transp();
//        // Return last in collection
//        last();
        
        // Generate 20 chars
//        System.out.println("str#: " +IntStream.range(0,20).mapToObj(r -> "#").reduce("", (hash,str)->hash+str));
        
        // Run action n times
        rpt(6,()->System.out.println("Yo!"));
    }
}

class SkyPosn extends Point2D.Float
{
    public SkyPosn( float x, float y ) { super(x,y); }
    public void transpose() { float t = x; x = y; y = t; }
}

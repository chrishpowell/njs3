/*
 */
package eu.discoveri.codesnippets;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class FIf
{
    static int num1 = 23;
    int num2 = 32;
    static Converter<String,Integer> converter = Integer::valueOf;
    static Converter<Integer,String> sConverter = String::valueOf;
    static Converter<Boolean,String> bConverter = from -> from?"Aye":"Nay";
    static Converter<Integer,String> s1Converter = from -> String.valueOf(from+num1);
//    static Converter<Integer,String> s2Converter = from -> String.valueOf(from+num2);  // num2 not static
    static Predicate<String> pred = p -> p.equals("Aye");
    static Function<Integer,Boolean> fBool = ii -> ii<0?Boolean.TRUE:Boolean.FALSE;
    
    static Comparator<X> xUpComp = (x1, x2) ->
    {
        if( x1.getVal() == x2.getVal() ) return 0;
        if( x1.getVal() > x2.getVal() ) return -1;
        return 1;
    };
    static Comparator<X> xComp = (X x1, X x2) ->
    {
        if( x1.getVal() == x2.getVal() ) return 0;
        if( x1.getVal() < x2.getVal() ) return -1;
        return 1;
    };
    
    static List<Person> ps =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));
    
    
    private void s2()
    {
        Converter<Integer,String> s2Converter = from -> String.valueOf(from+num2);
        System.out.println("0> " +s2Converter.convert(56));
    }
    
    private static void sTest()
    {
        System.out.println("6> ");
//        Stream.of(new X(11), new X(2), new X(3)).forEachOrdered(System.out::println);
        Stream.of(new X(11), new X(2), new X(3)).sorted((x1,x2)->xComp.compare(x1,x2)).forEach(x->System.out.println("   > "+x));  // 2, 3, 11
    }
    
    private static void s1Test()
    {
        Stream.of("d2", "a2", "b1", "b3", "c")
            .filter(s -> {
                return s.startsWith("a");                           // filter before map (or sorted etc.) to reduce executions
            })
            .map(s -> {
                return s.toUpperCase();
            })
            .forEach(s -> System.out.println("8> " + s));           // Called ONCE only: A2
    }
    
    private static void tTest()
    {
        IntStream.rangeClosed(31,37).map(n->n-30).average().ifPresent(a->System.out.println("7>  "+a));  // 4
    }
    
    private void duff()
    {
        Stream<String> str = Stream.of("a2","d1","d2","c0").filter(s->s.startsWith("d"));
        str.anyMatch(s->true);        // ok
        str.noneMatch(s->true);       // Throws exception: stream already operated on
        
        // Fix
        Supplier<Stream<String>> sstr = () -> Stream.of("a2","d1","d2","c0").filter(s->s.startsWith("d"));
        sstr.get().anyMatch(s->true);   // ok
        sstr.get().noneMatch(s->true);  // ok
    }
    
    private static void persons()
    {
        List<Person> pf =
            ps.stream()
              .filter(p -> p.name.startsWith("P"))
              .collect(Collectors.toList());

        System.out.println(pf);    // [Peter, Pamela]
    }
    
    private static void cTest()
    {
        Supplier<StringJoiner> sj = () -> new StringJoiner(":");
        BiConsumer<StringJoiner,Person> bc = (j,p) -> j.add(p.getName().toUpperCase());       // BiConsumer<A,T>
        //BinaryOperator<StringJoiner> bo = (j1,j2) -> j1.merge(j2);
        BinaryOperator<StringJoiner> bo = (j1,j2) -> j1.nullBack(j2);
        
        // Collector<T,A,R>
//        Collector<Person,StringJoiner,String> pnc = Collector.of( sj.get(),           // Supplier
//                                                                  (t,u) -> bc.accept(t, u),           // accumulator: Biconsumer<A,T>
//                                                                  (t1,t2) -> bo.apply(t1, t2),
//                                                                  StringJoiner::toString);
    }
    
    private static void supp()
    {
        IntSupplier y = () -> new Random().nextInt(5);                          // Runs random every time
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }
    
    public static void main(String[] args)
    {
        int c1 = converter.convert("123");
        System.out.println("1> " +c1);  // 123
        String s0 = sConverter.convert(-1);
        System.out.println("2> " +s0);  // -1
        String b1 = bConverter.convert(Boolean.TRUE);
        System.out.println("3> " +b1);  // Aye
        
        System.out.println("3a> " +pred.test("Aye"));   // Predicate
        
        String s1 = s1Converter.convert(-3);
        System.out.println("4> " +s1);  // 20
        
        System.out.println("5> " +fBool.apply(-1));
        
        sTest();
        tTest();
        s1Test();
        persons();
    }
}

//------------------------------------------------------------------------------
class X
{
    int val;
    
    // Constructor
    X( int val ) { this.val = val; }
    
    // Getter
    public int getVal() { return val; }
    
    @Override
    public String toString() { return String.valueOf(val); }
}

//------------------------------------------------------------------------------
class Y
{
    
}

//------------------------------------------------------------------------------
class Person
{
    String name;
    int age;

    Person(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}

//------------------------------------------------------------------------------
class StringJoiner
{
    String         joiner;
    StringBuilder  out = new StringBuilder();
    
    // Constructor
    public StringJoiner( String joiner ) { this.joiner = joiner; }
    
    // Aggregate
    public void add( String a ) { out.append(a).append(joiner); }
    
    // Getter
    public String getJoiner() { return joiner; }
    
    // Merge StringJoiners (usually map.merge())
    public StringJoiner nullBack(StringJoiner s) { return this; }
    
    @Override
    public String toString() { return out.toString(); }
}

//------------------------------------------------------------------------------
@FunctionalInterface
interface Converter<F,T> { T convert(F from); }

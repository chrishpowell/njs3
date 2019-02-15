/*
 */
package eu.discoveri.graphqltester;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author chrispowell
 */
public class ZodiacSign
{
//        # Ordering
//    ord: Int!
//    # sign
//    sign: String!
//    # latin name
//    latin: String
//    # english name
//    eng: String
//    # title
//    title: String
//    # associated element
//    element: Element
//    # Symbol
//    # Start date
//    # End date
//    # Stone
//    # Colour list
//    # Day (Thursday)
//    day: String
//    # Metal list
//    # Planet list
//    # Flower list
//    # Lucky numbers
//    # other characteristics
//    charac: ZodiacCharac
//    # associated constellation
//    constellation: Constellation
//    # general description
//    descrip: String
//    # external associated url (further description)
//    extUrl: Url
    private final int           id;
    private final String        sign;
    private final int           order;
    private List<ZDates>    zDates;
    private final String        englishname;
    private final String        latinname;
    private String              title;
    //private Element         element;
    //private Symbol          symbol;
    //private Stone           stone;
    //    # Colour list
    //    # Day (Thursday)
    //    day: String
    //private Metal list
    //private Planet list
    //private Flower list
    //private Lucky numbers
    private String              additional;
    private static List<ZodiacSign> preBuiltSignList = createSignsList();

    public ZodiacSign(  int id, String sign, int order,
                        List<ZDates> zDates,
                        String englishname, String latinname,
                        String title, String additional      )
    {
        this.id = id;
        this.sign = sign;
        this.order = order;
        this.zDates = zDates;
        this.englishname = englishname;
        this.latinname = latinname;
        this.title = title;
        this.additional = additional;
    }
    
    public int getId() { return id; }
    public String getSign() { return sign; }
    public String getAdditional() { return additional; }
    public int getOrder() { return order; }
    public List<ZDates> getZDates() { return zDates; }
    public String getEnglishname() { return englishname; }
    public String getLatinname() { return latinname; }
    public String getTitle() { return title; }
    public static List<ZodiacSign> getPreBuiltSignList() { return preBuiltSignList; }

    /*
     * Set Start/End date for given year
     */
    public void setZDate(List<ZDates> zDates) { this.zDates = zDates; }
    
    @Override
    public String toString()
    {
        return id+ ": " +sign;
    }
    
    /*
     * Testing
     */
    public static List<ZodiacSign> getSignsList()
    {
        return preBuiltSignList;
    }
    
    private static List<ZodiacSign> createSignsList()
    {
        List<ZodiacSign> signs = new ArrayList<>();
        
        List<ZDates> cancer = new ArrayList<>();
        cancer.add(new ZDates(2017,new MonthDays(MonthDay.of(6,21),MonthDay.of(7,22))));
        cancer.add(new ZDates(2018,new MonthDays(MonthDay.of(6,21),MonthDay.of(7,22))));
        cancer.add(new ZDates(2019,new MonthDays(MonthDay.of(6,21),MonthDay.of(7,22))));
        
        List<ZDates> libra = new ArrayList<>();
        libra.add(new ZDates(2017,new MonthDays(MonthDay.of(9,23),MonthDay.of(10,22))));
        libra.add(new ZDates(2018,new MonthDays(MonthDay.of(9,23),MonthDay.of(10,22))));
        libra.add(new ZDates(2019,new MonthDays(MonthDay.of(9,23),MonthDay.of(10,22))));
        
        List<ZDates> capricorn = new ArrayList<>();
        capricorn.add(new ZDates(2017,new MonthDays(MonthDay.of(12,22),MonthDay.of(1,19))));
        capricorn.add(new ZDates(2018,new MonthDays(MonthDay.of(12,22),MonthDay.of(1,19))));
        capricorn.add(new ZDates(2019,new MonthDays(MonthDay.of(12,22),MonthDay.of(1,19))));
        
        List<ZDates> pisces = new ArrayList<>();
        pisces.add(new ZDates(2017,new MonthDays(MonthDay.of(2,19),MonthDay.of(3,20))));
        pisces.add(new ZDates(2018,new MonthDays(MonthDay.of(2,19),MonthDay.of(3,20))));
        pisces.add(new ZDates(2019,new MonthDays(MonthDay.of(2,19),MonthDay.of(3,20))));
        
        List<ZDates> aries = new ArrayList<>();
        aries.add(new ZDates(2017,new MonthDays(MonthDay.of(3,21),MonthDay.of(4,19))));
        aries.add(new ZDates(2018,new MonthDays(MonthDay.of(3,21),MonthDay.of(4,19))));
        aries.add(new ZDates(2019,new MonthDays(MonthDay.of(3,21),MonthDay.of(4,19))));
        
        signs.add(new ZodiacSign(1,"Cancer", 1,
                                    cancer,
                                    "Crab","Cancer",
                                    "Cancer","CancerAdd"));
        signs.add(new ZodiacSign(2,"Libra", 2,
                                    libra,
                                    "Scales","Libra",
                                    "Libra","LibraAdd"));
        signs.add(new ZodiacSign(3,"Capricorn", 3,
                                    capricorn,
                                    "Goat","Capricorn",
                                    "Capricorn","CapricornAdd"));
        signs.add(new ZodiacSign(4,"Pisces", 4,
                                    pisces,
                                    "Fish","Pisces",
                                    "Pisces","PiscesAdd"));
        signs.add(new ZodiacSign(5,"Aries", 5,
                                    aries,
                                    "Ram","Aries",
                                    "Aries","AriesAdd"));
        return signs;
    }
}

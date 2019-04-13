/*
 * Full country details (database) with attached Language list
 */
package eu.discoveri.predikt.i18n;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class FullCountry
{
    private String      ctryNameEng,
                        ctryNameLocal;
    private ISO31661    iso31661;
    List<ISO639>        lcList = new ArrayList<>();

    /**
     * Constructor.
     * 
     * @param ctryNameEng
     * @param ctryNameLocal
     * @param iso31661
     * @param ctryNC
     * @param lcList (Language Code list) 
     */
    public FullCountry( String ctryNameEng,
                        String ctryNameLocal,
                        ISO31661 iso31661,
                        List<ISO639> lcList ) {
        this.ctryNameEng = ctryNameEng;
        this.ctryNameLocal = ctryNameLocal;
        this.iso31661 = iso31661;
        this.lcList = lcList;
    }

    public String getCtryNameEng() {
        return ctryNameEng;
    }

    public void setCtryNameEng(String ctryNameEng) {
        this.ctryNameEng = ctryNameEng;
    }

    public String getCtryNameLocal() {
        return ctryNameLocal;
    }

    public void setCtryNameLocal(String ctryNameLocal) {
        this.ctryNameLocal = ctryNameLocal;
    }

    public ISO31661 getISO31661() {
        return iso31661;
    }

    public void setISO31661(ISO31661 iso31661) {
        this.iso31661 = iso31661;
    }

    public List<ISO639> getLcList() {
        return lcList;
    }

    public void setLcList(List<ISO639> lcList) {
        this.lcList = lcList;
    }
    
    @Override
    public String toString()
    {
        String lcls = "";
        for( ISO639 lcs: lcList )
            lcls += lcs.toString();
        return ctryNameEng+", "+ctryNameLocal+", "+iso31661.toString()+", "+lcls;
    }
}


/*
 * ISO3166-1 being Alpha-2, Alpha-3, Numeric code
 */
class ISO31661
{
    private String  A2, A3, ctryNC;

    public ISO31661(String A2, String A3, String ctryNC)
    {
        this.A2 = A2;
        this.A3 = A3;
        this.ctryNC = ctryNC;
    }

    public String getA2() {
        return A2;
    }

    public void setA2(String A2) {
        this.A2 = A2;
    }

    public String getA3() {
        return A3;
    }

    public void setA3(String A3) {
        this.A3 = A3;
    }

    public String getCtryNC() {
        return ctryNC;
    }

    public void setCtryNC(String ctryNC) {
        this.ctryNC = ctryNC;
    }
    
    @Override
    public String toString()
    {
        return A2+", "+A3+", "+ctryNC;
    }
}


/*
 * ISO639 being Alpha-1, Alpha-2, Alpha-3
 */
class ISO639
{
    private String  ISO6391,
                    ISO6392,
                    ISO6393;

    public ISO639(String ISO6391, String ISO6392, String ISO6393)
    {
        this.ISO6391 = ISO6391;
        this.ISO6392 = ISO6392;
        this.ISO6393 = ISO6393;
    }

    public String getISO6391() {
        return ISO6391;
    }

    public void setISO6391(String ISO6391) {
        this.ISO6391 = ISO6391;
    }

    public String getISO6392() {
        return ISO6392;
    }

    public void setISO6392(String ISO6392) {
        this.ISO6392 = ISO6392;
    }

    public String getISO6393() {
        return ISO6393;
    }

    public void setISO6393(String ISO6393) {
        this.ISO6393 = ISO6393;
    }
    
    @Override
    public String toString()
    {
        return ISO6391+", "+ISO6392+", "+ISO6393;
    }
}

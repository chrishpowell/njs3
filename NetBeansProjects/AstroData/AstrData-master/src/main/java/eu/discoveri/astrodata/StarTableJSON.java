/*
 * Copyright (C) 2018 chris
 *
 */
package eu.discoveri.astrodata;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.simpleframework.xml.Path;

/**
 *
 * @author chris
 */
@Path("/get")
public class StarTableJSON
{
    @GET
    @Path("/bodies")
    @Produces({MediaType.APPLICATION_JSON})
    public String getStarTable()
    {
        return "[{\"id\":\"1\",\"properName\":\"Rigel\",\"magnitude\":0.18,\"spectralType\":\"A0m...\",\"colorIndex\":-0.03,\"rightAscension\":\"5.242298\",\"declination\":\"-8.20164\"}," +
                    "{\"id\":\"2\",\"properName\":\"Betelguese\",\"magnitude\":0.45,\"spectralType\":\"A0m...\",\"colorIndex\":1.85,\"rightAscension\":\"5.919529\",\"declination\":\"7.407063\"},"+
                    "{\"id\":\"3\",\"properName\":\"Bellatrix\",\"magnitude\":1.64,\"spectralType\":\"A0m...\",\"colorIndex\":-0.21,\"rightAscension\":\"5.418851\",\"declination\":\"6.349702\"},"+
                    "{\"id\":\"4\",\"properName\":\"Alnilam\",\"magnitude\":1.69,\"spectralType\":\"A0m...\",\"colorIndex\":-0.18,\"rightAscension\":\"5.603559\",\"declination\":\"-1.20192\"},"+
                    "{\"id\":\"5\",\"properName\":\"Alnitak\",\"magnitude\":1.74,\"spectralType\":\"A0m...\",\"colorIndex\":-0.15,\"rightAscension\":\"5.679313\",\"declination\":\"-1.942572\"},"+
                    "{\"id\":\"6\",\"properName\":\"Saiph\",\"magnitude\":2.07,\"spectralType\":\"A0m...\",\"colorIndex\":-0.18,\"rightAscension\":\"5.795941\",\"declination\":\"-9.669605\"},"+
                    "{\"id\":\"7\",\"properName\":\"Mintaka\",\"magnitude\":2.25,\"spectralType\":\"A0m...\",\"colorIndex\":-0.22,\"rightAscension\":\"5.533445\",\"declination\":\"-0.299092\"},"+
                    "{\"id\":\"8\",\"properName\":\"Hatsya\",\"magnitude\":2.75,\"spectralType\":\"A0m...\",\"colorIndex\":-0.24,\"rightAscension\":\"5.590551\",\"declination\":\"-5.909901\"},"+
                    "{\"id\":\"9\",\"properName\":\"28Eta\",\"magnitude\":3.35,\"spectralType\":\"A0m...\",\"colorIndex\":-0.17,\"rightAscension\":\"5.407949\",\"declination\":\"-2.397146\"},"+
                    "{\"id\":\"10\",\"properName\":\"Sun\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"-0.217464\",\"declination\":\"-8.20164\"},"+
                    "{\"id\":\"11\",\"properName\":\"Moon\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"0.024607\",\"declination\":\"0.063511\"},"+
                    "{\"id\":\"12\",\"properName\":\"Mercury\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"0.401602\",\"declination\":\"0.263656\"},"+
                    "{\"id\":\"13\",\"properName\":\"Venus\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"0.330888\",\"declination\":\"0.137337\"},"+
                    "{\"id\":\"14\",\"properName\":\"Mars\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"0.77649899\",\"declination\":\"0.279882\"},"+
                    "{\"id\":\"15\",\"properName\":\"Jupiter\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"-2.6950399\",\"declination\":\"-0.13448\"},"+
                    "{\"id\":\"16\",\"properName\":\"Saturn\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"1.847143\",\"declination\":\"0.402833\"},"+
                    "{\"id\":\"17\",\"properName\":\"Uranus\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"-2.7519643\",\"declination\":\"-0.130865\"},"+
                    "{\"id\":\"18\",\"properName\":\"Neptune\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"2.58130\",\"declination\":\"0.405542\"},"+
                    "{\"id\":\"19\",\"properName\":\"Pluto\",\"magnitude\":0.01,\"spectralType\":\"SSystem\",\"colorIndex\":99.99,\"rightAscension\":\"-2.16603\",\"declination\":\"-0.433481\"}]";
    }
}

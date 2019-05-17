/*
 * Send back I18N objects
 * ====> This will be database
 */
package eu.discoveri.predikt.i18n;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.discoveri.predikt.mockdb.*;
import eu.discoveri.predikt.system.Constants;
import eu.discoveri.predikt.utils.BasicTSLogger;
import eu.discoveri.predikt.utils.PrediktProperties;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
@JsonIgnoreProperties({"socket","zmqProps"})
public class PrediktI18N
{
    // Socket for I18N
    private static ZMQ.Socket                   socket = null;
    // @TODO: Move Properties init?
    private static Properties                   zmqProps;
    // JSON mapper
    private final static ObjectMapper           mapper = new ObjectMapper();
    // Locale (default)
    private static String                       ctryLang = Constants.ENGBDEF;
    // List of I18Ns
    private final static Map<String,I18NCache>  listI18Ns = new HashMap<>();


    /*---------------------------------------------------------------
     *
     *      Needs to access database for I18N objects below
     *      -----------------------------------------------
     *
     *---------------------------------------------------------------*/
    public static void getI18NFromDataBase()
            throws JsonProcessingException
    {
        // Mock db get
        switch( ctryLang )
        {
            case Constants.ENGBCL:
                List<AccountType> acctTypeListENGB = new ArrayList<>();
                
                // Nota Bene: NEVER construct BigDecimal from float or double, it just won't work
                acctTypeListENGB.add( new AccountType("individual","Individual [1 user, secure Home Page]",new BigDecimal("3.99"),false) );
                acctTypeListENGB.add( new AccountType("family","Family [2 to 5 users, secure Home Page]",new BigDecimal("7.99"),false) );
                acctTypeListENGB.add( new AccountType("team","Group [6 to 22 users, secure Home Page]",new BigDecimal("23.99"),false) );
                acctTypeListENGB.add( new AccountType("smallbus","Small business window [Global home page, no charting]",new BigDecimal("29.99"),false) );
                acctTypeListENGB.add( new AccountType("syndicate","Syndicated per user access [GraphQL/JSON data feed, min 20 users]",new BigDecimal("1.99"),true) );
                acctTypeListENGB.add( new AccountType("corporate","Corporate window [Global home page and marketing, no charting]",new BigDecimal("99.99"),false) );

                listI18Ns.put(Constants.ENGBCL, new I18NCache(
                                    new AccountTypeLocale( Locale.UK,
                                                            "per month",
                                                            "per user",
                                                            Constants.UNICODEVERS,
                                                            Constants.ENREGEX ),
                                    new RegFormValues("Registration",
                                                            "Note: On successfully registering, you will receive an email with a link which you select for confirmation.",
                                                            "Close... didn't want this page!",
                                                            "Account details",
                                                            "Account types",
                                                            "Your preferred language",
                                                            "Selecting Family or Group types means you will be the Group Manager managing members of the group. You can add or change members at any time.",
                                                            "Login",
                                                            "Register",
                                                            "Show map"  ),
                                    new RegFormInputs("Family name...",
                                                            "Other/given names...",
                                                            "Email...",
                                                            "Password...",
                                                            "Password validate...",
                                                            "Date of Birth...",
                                                            "Birth locn...",
                                                            "Curr locn..."  ),
                                    acctTypeListENGB,
                                    new ErrorMsgs( "Family name is required",
                                                         "Not a recognised name string",
                                                         "A valid email is required",
                                                         "A password is required",
                                                         "Your password must be 6 characters or more",
                                                         "Password confirmation is required!",
                                                         "Passwords do not match!",
                                                         "A valid date is required",
                                                         "Your username (email) or password is not valid",
                                                         "Your browser does not support geolocation.  Manual entry will be required.")
                ));
                break;
                
            case Constants.FRFRCL:
                List<AccountType> acctTypeListFRFR = new ArrayList<>();

                // Nota Bene: NEVER construct BigDecimal from float or double, it just won't work
                acctTypeListFRFR.add( new AccountType("individual","Individuel [1 utilisateur, Page d'accueil sécurisée]",new BigDecimal("3.99"),false) );
                acctTypeListFRFR.add( new AccountType("family","Famille [2 à 5 utilisateurs, Page d'accueil sécurisée]",new BigDecimal("7.99"),false) );
                acctTypeListFRFR.add( new AccountType("team","Groupe [6 à 22 utilisateurs, Page d'accueil sécurisée]",new BigDecimal("23.99"),false) );
                acctTypeListFRFR.add( new AccountType("smallbus","Fenêtre Petite entreprise [Page d'accueil globale, pas de graphique]",new BigDecimal("29.99"),false) );
                acctTypeListFRFR.add( new AccountType("syndicate","Accès syndiqué par utilisateur [Flux de données GraphQL / JSON, min 20 utilisateurs]",new BigDecimal("1.99"),true) );
                acctTypeListFRFR.add( new AccountType("corporate","Fenêtre d'entreprise [Page d'accueil globale et marketing, pas de graphiques]",new BigDecimal("99.99"),false) );
                
                listI18Ns.put(Constants.FRFRCL, new I18NCache(
                                    new AccountTypeLocale( Locale.FRANCE,
                                                            "par mois",
                                                            "par utilisateur",
                                                            Constants.UNICODEVERS,
                                                            Constants.FRREGEX ),
                                    new RegFormValues("Enregistrement",
                                                            "Remarque: lors de votre inscription, vous recevrez un courrier électronique avec un lien que vous sélectionnerez pour confirmation.",
                                                            "Fermer ... je ne voulais pas de cette page!",
                                                            "Details du compte",
                                                            "Types de compte",
                                                            "Votre langue préférée",
                                                            "La sélection des types de famille ou de groupe signifie que vous serez le responsable du groupe et les membres du groupe. Vous pouvez ajouter ou modifier des membres à tout moment.",
                                                            "Se connecter",
                                                            "Registre",
                                                            "Montrer carte"  ),
                                    new RegFormInputs("Nom de famille...",
                                                            "Autres/prénoms...",
                                                            "Email...",
                                                            "Mot de passe...",
                                                            "Mot de passe valider...",
                                                            "Date de Naissance...",
                                                            "Lieu de naissance...",
                                                            "Emplace. actuel..."  ),
                                    acctTypeListFRFR,
                                    new ErrorMsgs( "Le nom de famille est obligatoire",
                                                         "Pas un nom de chaîne reconnu",
                                                         "Une adresse courriel valide est requise",
                                                         "Un mot de passe est requis",
                                                         "Votre mot de passe doit comporter 6 caractères ou plus",
                                                         "La confirmation du mot de passe est requise!",
                                                         "Les mots de passe ne correspondent pas!",
                                                         "Une date valide est requise",
                                                         "Votre nom d'utilisateur (email) ou votre mot de passe n'est pas valide",
                                                         "Votre navigateur ne supporte pas la géolocalisation. La saisie manuelle sera nécessaire.")
                ));
                break;
        }
    }


   /*
    * Form the return JSON message
    */
    public static String formReturnI18NJSON( String ctryLang )
            throws JsonProcessingException
    {
        return  "{\"ctryLang\":\""+ctryLang+"\", \"I18N\":"+mapper.writeValueAsString(listI18Ns.get(ctryLang))+"}";
    }
    

    /**
     * Fire off 0MQ:REP socket thread
     * @throws JsonProcessingException
     */
    public static void initI18N()
            throws Exception
    { //System.out.println("...In PrediktI18N.initI18N()");
        // Just the one thread (for now)
        Semaphore mutex = new Semaphore(1);
        
        // Read properties (must have read properties file first!)
        zmqProps = PrediktProperties.getPropsMap().get(Constants.ZMQPROPSKEY);
        
        // 0MQ setup (usually, bind: server, connect: client)
        socket = new ZContext().createSocket(SocketType.REP);
        System.out.println("Get I18N socket at: "+zmqProps.getProperty(Constants.ZMQ2URL,Constants.ZMQ2URLDEF));
        if( !socket.bind(zmqProps.getProperty(Constants.ZMQ2URL,Constants.ZMQ2URLDEF)) )
        {
            throw new ZMQException("Socket didn't bind",0);
        }
        
        // Thread for socket
        ExecutorService i18nService = Executors.newFixedThreadPool(1);
        
        // Socket for I18N
        Runnable getI18NFromDb = () ->
        { //System.out.println("In PrediktI18N.getI18NFromDb thread...");
            // Blocking read until a request made
            // GraphQL not necessary?
            try
            {
                // Lock the thread
                mutex.acquire();
                    
                while( true )
                {
                    String s = socket.recvStr(0);
                    System.out.println("::> Socket recv: " +s);

                    // Check we've been sent a decent message and key
                    JsonNode root = mapper.readTree(s);  // root of JSON
                    if( !root.at("/msgType").textValue().equalsIgnoreCase(zmqProps.getProperty(Constants.I18NCHAN)) )
                    {
                        System.exit(3);  // *** @TODO change!!
                    }
                    
                    // What locale?
                    ctryLang = root.at("/message/ctryLang").textValue();
                    // Get I18N data from Db
                    getI18NFromDataBase();

                    BasicTSLogger.Logger(" JSON going back: " +formReturnI18NJSON(ctryLang) );
                    socket.send( formReturnI18NJSON(ctryLang) );
                }
            } catch( InterruptedException | IOException exx )
            {
                // Shutdown the thread
                mutex.release();
                i18nService.shutdown();
                
                // Log
                System.out.println("!ERROR: " +exx.getMessage() );
            }
        };
        
        // Execute the get (thread) from Db
        i18nService.execute(getI18NFromDb);
    }

    
    //--------------------------------------------
    //          M A I N  (TEST)
    //--------------------------------------------
    public static void main(String[] args)
            throws JsonProcessingException
    {}
}



/*
 */
package eu.discoveri.jeromq;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.ZMQException;


/**
 *
 * @author chris
 */
public class ZMQTestLocale
{
    static String regForm = "{\n" +
"	\"RegistrationForm\": {\n" +
"		\"formValues\": {\n" +
"			\"mainTitle\": \"Registration\",\n" +
"			\"confMsg\": \"Note: On successfully registering, you will receive an email with a link which you select for confirmation.\",\n" +
"			\"closeButton\": \"Close... didn't want this page!\",\n" +
"			\"acctDetails\": \"Account details\",\n" +
"			\"acctTypes\": \"Account types\",\n" +
"			\"prefLangChoose\": \"Your preferred language\",\n" +
"			\"famGrpTypesMsg\": \"Selecting Family or Group types means you will be the Group Manager managing members of the group. You can add or change members at any time.\",\n" +
"			\"registerButton\": \"Register\"\n" +
"		},\n" +
"		\"inpValuesI18N\": {\n" +
"			\"familyname\": \"Family name...\",\n" +
"			\"othername\": \"Other/given names...\",\n" +
"			\"email\": \"Email...\",\n" +
"			\"password\": \"Password...\",\n" +
"			\"password2\": \"Password validate...\",\n" +
"			\"dob\": \"Date of Birth...\",\n" +
"			\"lob\": \"Location of Birth...\",\n" +
"			\"locn\": \"Current location...\"\n" +
"		},\n" +
"		\"accountTypeList\": [{\n" +
"				\"id\": \"individual\",\n" +
"				\"desc\": \"Individual [1 user, secure Home Page]\",\n" +
"				\"price\": 3.99\n" +
"			},\n" +
"			{\n" +
"				\"id\": \"family\",\n" +
"				\"desc\": \"Family [2 to 5 users, secure Home Page]\",\n" +
"				\"price\": 7.99\n" +
"			},\n" +
"			{\n" +
"				\"id\": \"team\",\n" +
"				\"desc\": \"Group [6 to 22 users, secure Home Page]\",\n" +
"				\"price\": 23.99\n" +
"			},\n" +
"			{\n" +
"				\"id\": \"smallbus\",\n" +
"				\"desc\": \"Small business window [Global home page, no charting]\",\n" +
"				\"price\": 29.99\n" +
"			},\n" +
"			{\n" +
"				\"id\": \"syndicate\",\n" +
"				\"desc\": \"Syndicated per user access [GraphQL/JSON data feed, min 20 users]\",\n" +
"				\"price\": 1.99\n" +
"			},\n" +
"			{\n" +
"				\"id\": \"corporate\",\n" +
"				\"desc\": \"Corporate window [Global home page and marketing, no charting]\",\n" +
"				\"price\": 99.99\n" +
"			}\n" +
"		],\n" +
"		\"errorMsgs\": {\n" +
"			\"userNameIsReqd\": \"Family name is required\",\n" +
"			\"userNameInvalid\": \"Not a recognised name string\",\n" +
"			\"emailReqd\": \"A valid email is required\",\n" +
"			\"pwdRequired\": \"A password is required\",\n" +
"			\"pwdInvalid\": \"Your password must be 6 characters or more\",\n" +
"			\"pwdConfirm\": \"Password confirmation is required!\",\n" +
"			\"pwdNotSame\": \"Passwords do not match!\",\n" +
"			\"dateInvalid\": \"A valid date is required\"\n" +
"		}\n" +
"	}\n" +
"}";
    static String languages = "{\n" +
"\"languages\": {\n" +
"	\"European:\": {\n" +
"		\"items\": [{\n" +
"				\"lang\": \"en\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"en-GB\",\n" +
"				\"label\": \"en-GB [British English]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"cy\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"cy-GB\",\n" +
"				\"label\": \"cy-GB [UK Cymraeg]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"el\",\n" +
"				\"regex\": \"Greek\",\n" +
"				\"value\": \"el-GR\",\n" +
"				\"label\": \"el-GR [Ελληνικά]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"en\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"en-US\",\n" +
"				\"label\": \"en-US [American English]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"en\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"en-CA\",\n" +
"				\"label\": \"en-CA [Canadian English]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"fr\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"fr-BE\",\n" +
"				\"label\": \"fr-BE [Française de Belgique]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"fr\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"fr-FR\",\n" +
"				\"label\": \"fr-FR [Français standard (notamment en France)]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"es\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"es-ES\",\n" +
"				\"label\": \"es-ES [Castellano Español (Centro-norte de España)]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"es\",\n" +
"				\"regex\": \"Latin\",\n" +
"				\"value\": \"es-MX\",\n" +
"				\"label\": \"es-MX [Español Mexicano]\"\n" +
"			}\n" +
"		]\n" +
"	},\n" +
"	\"Asian:\": {\n" +
"		\"items\": [{\n" +
"				\"lang\": \"zh\",\n" +
"				\"regex\": \"Chinese\",\n" +
"				\"value\": \"zh-CN\",\n" +
"				\"label\": \"zh-CN [中国大陆, 简化字符]\"\n" +
"			},\n" +
"			{\n" +
"				\"lang\": \"zh\",\n" +
"				\"regex\": \"Chinese\",\n" +
"				\"value\": \"zh-HK\",\n" +
"				\"label\": \"zh-HK [Hong Kong, traditional characters]\"\n" +
"			}\n" +
"		]\n" +
"	}\n" +
"    }\n" +
"}";
    static String locale = "{\n" +
"	\"locale\": {\n" +
"		\"localeCode\": \"en-GB\",\n" +
"		\"lang\": \"en\",\n" +
"		\"format\": {\n" +
"			\"style\": \"currency\",\n" +
"			\"currency\": \"GBP\"\n" +
"		},\n" +
"		\"perMonth\": \"per month\",\n" +
"		\"perPerson\": \"per person\",\n" +
"		\"unicodeVersion\": \"5.2\",\n" +
"		\"regex\": \"/\\b[a-zA-Z]+(?:['-]?[a-zA-Z]+)*\\b/\"\n" +
"	}\n" +
"}";
    static ZContext context;
    
    public static void main(String[] args)
            throws Exception
    {
        try
        {
            context = new ZContext();
            
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            if( !socket.bind("tcp://127.0.0.1:8777") )
            {
                throw new ZMQException("Socket didn't bind",0);
            }

            while( !Thread.currentThread().isInterrupted() )
            {
                // Block until a message is received
                byte[] reply = socket.recv();

                // Print the message
                System.out.println( "Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]" );

                // Send a response
                String response = "Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }
}

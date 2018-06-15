/*
 */
package eu.discoveri.jeromq;

import java.net.*;
import java.io.*;


/**
 *
 * @author chris
 */
public class WhoIsClient
{
    public static void main(String[] args)
    {
        String domainName = "discoveri.eu";
        String hostname = "whois.internic.net";
        int port = 43;
 
        try( Socket socket = new Socket(hostname, port) )
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(domainName);
 
            InputStream input = socket.getInputStream();
 
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            String line;
 
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

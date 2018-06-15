/*
 */
package eu.discoveri.jeromq;

import java.net.*;
import java.io.*;


/**
 *
 * @author chris
 */
public class SimpleClient
{
    public static void main(String[] args)
    {
        String hostname = "tcp://127.0.0.1";
        int port = 8777;
 
        try( Socket socket = new Socket(hostname, port) )
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            InputStream input = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
 
            int character;
            StringBuilder data = new StringBuilder();
 
            // Write
            writer.println("Yo!");
            // Read
            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }
 
            System.out.println(data);
 
 
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

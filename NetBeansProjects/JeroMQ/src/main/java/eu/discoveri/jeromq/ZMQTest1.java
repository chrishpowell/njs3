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
public class ZMQTest1
{
    public static void main(String[] args)
            throws Exception
    {
        try( ZContext context = new ZContext() )
        {
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
    }
}

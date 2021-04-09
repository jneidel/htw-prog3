package net;

import routing.EventHandler;

import java.io.*;
import java.net.*;
import java.util.EventObject;

public class TCPServer extends Thread implements Server {
    EventHandler handler;
    public TCPServer( EventHandler handler ) {
        this.handler = handler;
    }

    ServerSocket serverSocket;
    public int init() {
        // random ports for consistent testing
        try {
            serverSocket = new ServerSocket( 0 );
            return serverSocket.getLocalPort();
        } catch ( IOException e ) {
            e.printStackTrace();
            System.exit( 1 );
            return -1;
        }
    }

    public void run() {
        // src: http://www.coderpanda.com/java-socket-programming-using-tcp
        try {
            Socket socket = serverSocket.accept();
            ObjectInputStream inStream = new ObjectInputStream( socket.getInputStream() );
            OutputStream outStream = socket.getOutputStream();

            while( socket.isConnected() ) {
                try {
                    EventObject event = (EventObject) inStream.readObject();
                    this.handleEvent( event );
                } catch (Exception e) {
                    System.err.println( "invalid event" );
                    e.printStackTrace();
                }

                String reply = "Thank you for the message";
                byte[] replyBytes = reply.getBytes();
                outStream.write( replyBytes );
                outStream.flush();
            }
        } catch ( Exception e ) { System.err.println( e ); }
    }

    public void handleEvent( EventObject event ) {
        this.handler.handle( event );
    }
}
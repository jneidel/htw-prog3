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
                String reply;
                try {
                    EventObject event = (EventObject) inStream.readObject();
                    reply = this.handleEvent( event );
                } catch (Exception e) {
                    reply = "error, got invalid event object";
                }

                byte[] replyBytes = reply.getBytes();
                outStream.write( replyBytes );
                outStream.flush();
            }
        } catch ( Exception e ) { e.printStackTrace(); }
    }

    public String handleEvent( EventObject event ) {
        return this.handler.handleWithFeedback( event );
    }
}
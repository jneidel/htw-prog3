package net;

import routing.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.EventObject;

public class UDPServer extends Thread implements Server {
    final int PACKET_SIZE = 8080;

    EventHandler handler;
    public UDPServer( EventHandler handler ) {
        this.handler = handler;
    }

    DatagramSocket socket;
    public int init() {
        try {
            socket = new DatagramSocket();
            return socket.getLocalPort();
        } catch ( Exception e ) { e.printStackTrace(); }
        return -1;
    }

    public void run() {
        // src: http://www.coderpanda.com/java-socket-programming-transferring-java-object-through-socket-using-udp
        try {
            byte[] incomingData = new byte[this.PACKET_SIZE];

            while( true ) {
                DatagramPacket incomingPacket = new DatagramPacket( incomingData, incomingData.length );
                socket.receive( incomingPacket );
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream( data );
                ObjectInputStream is = new ObjectInputStream( in );

                String reply;
                try {
                    EventObject event = (EventObject) is.readObject();
                    reply = this.handleEvent( event );
                } catch (Exception e) {
                    reply = "error, got invalid event object";
                }

                InetAddress clientAddress = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();
                byte[] replyBytes = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(replyBytes, replyBytes.length, clientAddress, clientPort);
                socket.send(replyPacket);
            }
        } catch ( Exception e ) { System.err.println( e ); }
    }

    public String handleEvent( EventObject event ) {
        return this.handler.handleWithFeedback( event );
    }
}
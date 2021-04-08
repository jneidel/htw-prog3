package net;

import routing.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.EventObject;

public class UDPServer extends Thread implements Server {
    final int PORT = 8088;
    final int PACKET_SIZE = 8080;

    EventHandler handler;
    public UDPServer( EventHandler handler ) {
        this.handler = handler;
    }

    public void run() {
        // src: http://www.coderpanda.com/java-socket-programming-transferring-java-object-through-socket-using-udp
        try {
            DatagramSocket socket = new DatagramSocket( this.PORT );
            byte[] incomingData = new byte[this.PACKET_SIZE];

            while( true ) {
                DatagramPacket incomingPacket = new DatagramPacket( incomingData, incomingData.length );
                socket.receive( incomingPacket );
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream( data );
                ObjectInputStream is = new ObjectInputStream( in );

                try {
                    EventObject event = (EventObject) is.readObject();
                    this.handleEvent( event );
                } catch (Exception e) {
                    // invalid event
                }

                InetAddress clientAddress = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();
                String reply = "Thank you for the message";
                byte[] replyBytes = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(replyBytes, replyBytes.length, clientAddress, clientPort);
                socket.send(replyPacket);
            }
        } catch ( Exception e ) { System.err.println( e ); }
    }

    public void handleEvent( EventObject event ) {
        this.handler.handle( event );
    }
}
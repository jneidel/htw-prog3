package net;

import gl.MediaDB;
import gl.Uploader;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.EventObject;

public class UDPServer extends Thread implements Server {
    final int PORT = 8088;
    final int PACKET_SIZE = 8080;

    MediaDB db;
    Uploader producer;

    public UDPServer( MediaDB db ) {
        this.db = db;
        this.producer = db.createProducer( "udp server" );
    }

    public void parseEvent( EventObject evt ) {

    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket( this.PORT, InetAddress.getLocalHost() );

            byte[] buffer;
            DatagramPacket request;
            DatagramPacket response;
            ByteArrayInputStream bis;
            ObjectInputStream ois;
            while ( true ) {
                buffer = new byte[this.PACKET_SIZE];
                request = new DatagramPacket( buffer, buffer.length );

                socket.receive( request );
                bis = new ByteArrayInputStream( buffer );
                ois = new ObjectInputStream( bis );
                this.parseEvent( (EventObject) ois.readObject() );
            }
        } catch ( Exception e ) { System.err.println( e ); }
    }

    @Override
    public void sendEvent(EventObject event) {

    }
}
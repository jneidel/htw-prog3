package net;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.EventObject;

public class UDPClient implements Client {
    final int SERVER_PORT = 8088;
    final int PACKET_SIZE = 8080;

    DatagramSocket socket;
    public UDPClient() {
        try {
            this.socket = new DatagramSocket();
        } catch ( SocketException e ) {
            System.err.println( "couldn't start udp socket" );
            System.exit( 1 );
        }
    }

    public String sendEvent( EventObject event ) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream( out );
            os.writeObject( event );
            os.flush();
            byte[] data = out.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), this.SERVER_PORT );
            this.socket.send( sendPacket );

            byte[] buffer = new byte[this.PACKET_SIZE];
            DatagramPacket incomingPacket = new DatagramPacket( buffer, buffer.length );
            this.socket.setSoTimeout( 4000 ); // wait 4 sec for response

            try {
                this.socket.receive( incomingPacket );
                String response = new String( incomingPacket.getData() ).trim();
                return "Response from server: " + response;
            } catch ( SocketTimeoutException e ) {} // no response, move on
        } catch ( Exception e ) { e.printStackTrace(); }
        return "";
    }

    public void sendRegister() {}
    public void sendUnregister() {}
}

package net;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.EventObject;

public class UDPClient implements Client {
    final int SERVER_PORT = 8088;
    DatagramSocket socket;

    public UDPClient() {
        try {
            this.socket = new DatagramSocket(); // anonymous port, 2nd client doesn't fail on binding
        } catch ( Exception e ) {}
    }

    public void sendEvent( EventObject event ) {
        try {
            // object serialization: https://stackoverflow.com/a/2836659
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( bos );
            oos.writeObject( event );
            oos.flush();
            byte[] buffer = bos.toByteArray();

            DatagramPacket request = new DatagramPacket( buffer, buffer.length, InetAddress.getLocalHost(), this.SERVER_PORT );
            this.socket.send( request );
            DatagramPacket response = new DatagramPacket( buffer, buffer.length );
            socket.setSoTimeout( 10000 ); // 10sec
            try {
                socket.receive( response );
            } catch ( SocketTimeoutException e ) {} // no response, move on

        } catch ( Exception e ) { System.err.println( e ); }
    }

    @Override
    public void sendRegister() {

    }

    @Override
    public void sendUnregister() {

    }

    @Override
    public void sendUploadItem() {

    }

    @Override
    public void sendDeleteItem() {

    }

    @Override
    public void sendCreateProd() {

    }

    @Override
    public void sendDeleteProd() {

    }
}

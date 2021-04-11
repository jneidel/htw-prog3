package net;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.EventObject;

public class TCPClient implements Client {
    private Socket socket;
    private InputStream inStream;
    private ObjectOutputStream outStream;
    public TCPClient( int port ) {
        this.connectSocket( port );
    }
    private void connectSocket( int port ) {
        try {
            this.socket = new Socket( InetAddress.getLocalHost(), port );
            this.inStream = socket.getInputStream();
            this.outStream = new ObjectOutputStream( socket.getOutputStream() );
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    public String sendEvent( EventObject event ) {
        try {
            if ( !socket.isConnected() ) {
                throw new RuntimeException( "init socket before sending an event" );
            }

            outStream.writeObject( event );
            outStream.flush();

            byte[] responseBuffer = new byte[1024];
            inStream.read( responseBuffer );
            String response = new String( responseBuffer, "UTF-8" ).trim();
            return response;
        } catch ( Exception e ) { e.printStackTrace(); }
        return "";
    }
}

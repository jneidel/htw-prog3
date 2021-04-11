import cli.CLI;

import static org.junit.jupiter.api.Assertions.*;

import net.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import gl.MediaDB;
import routing.EventHandler;
import routing.GLEventListener;


public class IntegrationTest {
    /*
    write to stdin

    - w/o server
    - tcp server
    - udp server
     */

    MediaDB db;
    EventHandler handler;
    @BeforeEach void setup() {
        this.db = new MediaDB();
        db.createProducer( "prod" );
        this.handler = new EventHandler();
        this.handler.add( new GLEventListener( db ) );
    }

    private void sendStdin( String cmd ) {
        // src: https://stackoverflow.com/a/3814092
        try {
            InputStream in = new ByteArrayInputStream( cmd.getBytes("UTF-8") );
            System.setIn( in );
        } catch ( UnsupportedEncodingException e ) {}
    }

    @Test void noServer() {
        sendStdin( "LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner" );
        new CLI( handler ).start();
        try { Thread.sleep(200); } catch ( Exception e ) {} // wait for cli thread to process

        assertNotNull( db.getItemByAddress( "movie.mp4" ) );
    }
    @Test void tcpServer() {
        sendStdin( "LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner" );
        Server server = new TCPServer( handler );
        int port = server.init();
        server.start();
        new CLI( new TCPClient( port ) ).start();
        try { Thread.sleep(200); } catch ( Exception e ) {}

        assertNotNull( db.getItemByAddress( "movie.mp4" ) );
    }
    @Test void udpServer() {
        sendStdin( "LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner" );
        Server server = new UDPServer( handler );
        int port = server.init();
        server.start();
        new CLI( new UDPClient( port ) ).start();
        try { Thread.sleep(400); } catch ( Exception e ) {}

        assertNotNull( db.getItemByAddress( "movie.mp4" ) );
    }
}

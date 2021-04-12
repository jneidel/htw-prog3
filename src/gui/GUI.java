package gui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.math.BigDecimal;

import gl.MediaContent;
import gl.MediaDB;
import net.*;
import routing.*;
import util.Args;
import util.MediaGenerator;
import cli.CLI;

public class GUI extends Application {
    public static String[] inputArgs;
    public void run() { launch(); }
    public void start(Stage primaryStage) throws Exception {
        Args args = new Args( inputArgs );

        MediaDB db;
        if ( args.getCapacity() > 0 )
            db = new MediaDB( new BigDecimal( args.getCapacity() ) );
        else
            db = new MediaDB();

        db.attachObserver( new cli.MediaDBObserver( db ) ); // logging

        EventHandler handler = new EventHandler();
        handler.add( new GLEventListener( db ) );
        handler.add( new IOEventListener( db ) );
        if ( args.getCountyCode() != null )
            handler.add( new LoggingEventListener( args.getCountyCode() ) );

        // configure gui
        FXMLLoader loader = new FXMLLoader( getClass().getResource("app.fxml" ) );
        Parent root = (Parent) loader.load();

        // src: https://stackoverflow.com/a/14432578
        Controller controller = loader.<Controller>getController();
        controller.setHandler( handler );

        db.attachObserver( new MediaDBObserver( db, controller ) );

        Scene scene =  new Scene( root, 500, 575 );
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new javafx.event.EventHandler<KeyEvent>() {
            // src: https://stackoverflow.com/a/42303881
            public void handle(KeyEvent ke) {
                if ( ke.getCode() == KeyCode.DELETE ) {
                    controller.removeSelection();
                    ke.consume();
                }
            }
        });
        primaryStage.setTitle( "prog3_beleg" );
        primaryStage.setScene( scene );
        primaryStage.show();

        // generate examples
        try {
            MediaGenerator gen = new MediaGenerator(db.createProducer("media generator"));
            MediaContent c1 = gen.generate();
            MediaContent c2 = gen.generate();
            MediaContent c3 = gen.generate();
            db.upload(c1);
            db.upload(c2);
            db.upload(c3);
        } catch (IllegalArgumentException e) {} // not enough capacity

        if ( args.getProtocol() == null ) { // local
            new CLI( handler ).start();
        } else { // server
            if ( args.getProtocol().equals( "udp" ) ) {
                Server server = new UDPServer( handler );
                int port = server.init();
                server.start();
                new CLI( new UDPClient( port ) ).start();
            } else {
                Server server = new TCPServer( handler );
                int port = server.init();
                server.start();
                new CLI( new TCPClient( port ) ).start();
            }
        }
    }
}
package routing;

import gl.MediaDB;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.util.EventObject;

public class EventHandler extends Thread {
    MediaDB db;
    ObjectInputStream stream;
    
    public EventHandler(MediaDB db, ObjectInputStream in ) {
        this.db = db;
        this.stream = in;
    }

    public void run() {
        InputEventHandler handler = new InputEventHandler();
        CreateInputEventListener createL = new CreateInputEventListener( this.db );
        handler.add( createL );

        System.out.println( "handler running" );
        while( true ) {
            try {
                Thread.sleep(1000); // limiter, reading more than once a sec is a waste; user i/o is the bottleneck

                EventObject event = (EventObject) stream.readObject();

                if (event instanceof InputEvent) {
                    System.out.println( "got input event" );
                    InputEvent inputEvent = (InputEvent) event;
                    handler.handle(inputEvent);
                }
            } catch ( EOFException e ) { // read nothing
            } catch ( Exception e ) {
               System.out.println( e );
            }
        }
    }
}

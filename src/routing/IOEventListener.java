package routing;

import gl.MediaDB;
import io.FileSystem;

import java.util.EventObject;

public class IOEventListener implements EventListener {
    MediaDB db;
    FileSystem fs;

    public IOEventListener( MediaDB db ) {
        this.db = db;
        this.fs = new FileSystem( db );
    }

    public void onEvent( EventObject event ) {
        switch ( event.toString() ) {
            case "media save" -> {
                String protoAddress = ((SaveEvent) event).getProtocolOrMediaAddress();
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    fs.saveDB( protoAddress );
                else
                    fs.saveInstance( protoAddress );
                db.notifyObservers( "media save" );
            }
            case "media load" -> {
                String protoAddress = ((LoadEvent) event).getProtocolOrMediaAddress();
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    fs.loadDB( protoAddress );
                else
                    fs.loadInstance( protoAddress );
                db.notifyObservers( "media load" );
            }
        }
    }
    public String onEventWithFeedback( EventObject event ) {
        switch ( event.toString() ) {
            case "media save" -> {
                String protoAddress = ((SaveEvent) event).getProtocolOrMediaAddress();
                String res = null;
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    res = fs.saveDB( protoAddress );
                else
                    res = fs.saveInstance( protoAddress );
                db.notifyObservers( "media save" );
                return res;
            }
            case "media load" -> {
                String protoAddress = ((LoadEvent) event).getProtocolOrMediaAddress();
                String res = null;
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    res = fs.loadDB( protoAddress );
                else
                    res = fs.loadInstance( protoAddress );
                db.notifyObservers( "media load" );
                return res;
            }
            default -> { return null; }
        }
    }
}
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
            }
            case "media load" -> {
                String protoAddress = ((SaveEvent) event).getProtocolOrMediaAddress();
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    fs.loadDB( protoAddress );
                else
                    fs.loadInstance( protoAddress );
            }
        }
    }
    public String onEventWithFeedback( EventObject event ) {
        switch ( event.toString() ) {
            case "media save" -> {
                String protoAddress = ((SaveEvent) event).getProtocolOrMediaAddress();
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    return fs.saveDB( protoAddress );
                else
                    return fs.saveInstance( protoAddress );
            }
            case "media load" -> {
                String protoAddress = ((LoadEvent) event).getProtocolOrMediaAddress();
                if ( protoAddress.equals( "jos" ) || protoAddress.equals( "jbp" ) )
                    return fs.loadDB( protoAddress );
                else
                    return fs.loadInstance( protoAddress );
            }
            default -> { return null; }
        }
    }
}
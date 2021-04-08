package routing;

import gl.MediaContent;
import gl.MediaDB;

import java.util.EventObject;

public class IOEventListener implements EventListener {
    MediaDB db;

    public IOEventListener( MediaDB db ) {
        this.db = db;
    }

    public void onEvent( EventObject event ) {
        switch ( event.toString() ) {
            case "media save" -> {

            }
            case "media load" -> {

            }
        }
    }
}
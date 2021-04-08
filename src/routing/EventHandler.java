package routing;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

public class EventHandler {
    private List<EventListener> listeners = new LinkedList<>();

    public void add( EventListener listener ) {
        this.listeners.add( listener );
    }
    public void remove( EventListener listener ) {
        this.listeners.remove( listener );
    }
    public void handle( EventObject event ) {
        for ( EventListener listener : this.listeners )
            listener.onEvent( event );
    }
}

package routing;

import java.util.LinkedList;
import java.util.List;

public class InputEventHandler {
    private List<InputEventListener> listeners = new LinkedList<>();
    public void add( InputEventListener listener ) {
        this.listeners.add( listener );
    }
    public void remove( InputEventListener listener ) {
        this.listeners.remove( listener );
    }
    public void handle( InputEvent event ) {
        for ( InputEventListener listener : this.listeners )
            listener.onInputEvent( event );
    }
}

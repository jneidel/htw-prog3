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
    public String handleWithFeedback( EventObject event ) {
        // pass back a msg from the executing listener to be displayed at the command line
        String msg = "did nothing";
        for ( EventListener listener : this.listeners ) {
            String potentialMsg = listener.onEventWithFeedback( event );
            if ( potentialMsg != null ) {
                if ( !msg.equals( "did nothing" ) ) // append to msg
                    msg = msg + "\n" + potentialMsg;
                else
                    msg = potentialMsg;
            }
        }
        return msg;
    }
}

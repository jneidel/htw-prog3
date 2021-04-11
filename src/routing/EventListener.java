package routing;

import java.util.EventObject;

public interface EventListener extends java.util.EventListener {
    void onEvent( EventObject event );
    String onEventWithFeedback( EventObject event );
}

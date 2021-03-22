package net;

import java.util.EventObject;

public interface Server {
    public void run();

    public void sendEvent( EventObject event );
}

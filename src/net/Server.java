package net;

import java.util.EventObject;

public interface Server {
    public void run();
    public void start();
    public int init();
    public void handleEvent( EventObject event );
}

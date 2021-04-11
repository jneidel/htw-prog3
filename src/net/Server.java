package net;

import java.util.EventObject;

public interface Server {
    public void run();
    public void start();
    public int init();
    public String handleEvent( EventObject event );
}

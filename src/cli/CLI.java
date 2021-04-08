package cli;

import net.Client;
import routing.EventHandler;

public class CLI extends Thread {
    EventHandler handler = null;
    Client client;

    public CLI( EventHandler handler ) {
        this.handler = handler;
    }
    public CLI( Client client ) {
        this.client = client;
    }

    public void run() {
        if ( client == null ) { // local
            new ConsoleReader( handler ).exec(); // same thread
        } else {
            new ConsoleReader( client ).exec(); // different thread
        }
    }
}

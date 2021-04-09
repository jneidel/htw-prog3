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
        if ( client == null ) {
            new ConsoleReader( handler ).exec(); // local
        } else {
            new ConsoleReader( client ).exec(); // server-client
        }
    }
}

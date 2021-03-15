package cli;

import gl.MediaDB;
import util.Observer;

public class MediaDBObserver implements Observer {
    Printer stdout = new Printer( "stdout" );
    MediaDB db;

    public MediaDBObserver( MediaDB db ) { this.db = db; }

    public void update() {
        stdout.printf( "%n%s%n", this.db.getStatus() );
        stdout.print( this.db.list() );
    }
}

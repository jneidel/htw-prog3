package cli;

import gl.MediaDB;
import util.Observer;

public class MediaDBObserver implements Observer {
    MediaDB db;

    public MediaDBObserver( MediaDB db ) { this.db = db; }

    public void update() {
        System.out.printf( "%n%s%n", this.db.getStatus() );
        System.out.print( this.db.list().toArray().toString() );
    }
}

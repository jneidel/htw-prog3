package cli;

import gl.MediaContent;
import gl.MediaDB;
import util.Observer;

public class MediaDBObserver implements Observer {
    MediaDB db;

    public MediaDBObserver( MediaDB db ) { this.db = db; }

    public void update() {
        System.out.printf( "%n%s%n", this.db.getStatus() );
        for (MediaContent i : this.db.list() )
            System.out.println( i );
    }
}

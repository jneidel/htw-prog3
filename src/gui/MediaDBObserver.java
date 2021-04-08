package gui;

import gl.MediaDB;
import util.Observer;

public class MediaDBObserver implements Observer {
    MediaDB db;
    Controller ctl;

    public MediaDBObserver(MediaDB db, Controller ctl ) {
        this.db = db;
        this.ctl = ctl;
    }

    public void update() {
        this.ctl.setList( this.db.list(), this.db.getProducers() );
        this.ctl.setStatus( this.db.getStatus() );
    }
}

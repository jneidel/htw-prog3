package gui;

import gl.MediaDB;
import util.Observer;

import java.util.ArrayList;

public class MediaDBObserver implements Observer {
    MediaDB db;
    Controller ctl;

    public MediaDBObserver(MediaDB db, Controller ctl ) {
        this.db = db;
        this.ctl = ctl;
    }

    public void update() {
        ArrayList items = this.db.list();
        this.ctl.setList( items );
        this.ctl.setStatus( this.db.getStatus() );
    }
}

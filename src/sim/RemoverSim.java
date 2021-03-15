package sim;

import gl.MediaContent;

import java.util.ArrayList;
import java.util.Random;

public class RemoverSim extends Thread {
    static Random rand = new Random();
    SimDB db;

    public RemoverSim( SimDB db ) {
        this.db = db;
    }

    public void run() {
        while( true ) {
            ArrayList<MediaContent> list = db.list();
            if ( list.size() > 0 ) {
                int index = rand.nextInt( list.size() );
                MediaContent item = list.get( index );
                this.db.delete( item );
            }
        }
    }
}

package sim;

import gl.Uploader;
import util.MediaGenerator;

public class UploaderSim extends Thread {
    static MediaGenerator gen;
    static SimDB db;

    public UploaderSim(SimDB db, Uploader producer) {
        this.db = db;
        this.gen = new MediaGenerator( producer );
    }

    public void run() {
        while( true ) {
            db.upload( gen.generate() );
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e){}
        }
    }
}
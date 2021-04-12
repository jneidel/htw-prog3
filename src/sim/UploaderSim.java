package sim;

import gl.MediaDB;
import gl.Uploader;
import routing.EventHandler;
import routing.UploadMediaEvent;
import util.MediaGenerator;
import util.Parser;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class UploaderSim extends Thread {
    MediaGenerator gen;
    MediaDB db;
    EventHandler handler;
    Parser parser = new Parser();
    Condition full;
    Condition removing;
    Lock lock;

    public UploaderSim(MediaDB db, EventHandler handler, Lock lock, Condition full, Condition removing, Uploader prod) {
        this.db = db;
        this.handler = handler;
        this.lock = lock;
        this.full = full;
        this.removing = removing;
        this.gen = new MediaGenerator( prod );
    }

    public void run() {
        while( true ) {
            try {
                lock.lock(); // condition requires a lock to be used

                UploadMediaEvent event = parser.parseMediaStrToEvent( gen.generateStr() );
                String res = handler.handleWithFeedback(event);

                if ( res.equals( "not enough capacity for upload" ) ) {
                    full.signal();
                    removing.await();
                }
            } catch (InterruptedException e) {}
            finally {
                lock.unlock();
            }
        }
    }
}
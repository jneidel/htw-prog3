package sim;

import gl.MediaContent;
import gl.MediaDB;
import routing.EventHandler;
import routing.IncrementAccessCounterEvent;
import routing.UploadMediaEvent;
import util.MediaGenerator;
import util.Parser;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class IncrementSim extends Thread {
    Random rand = new Random();
    MediaDB db;
    EventHandler handler;
    Lock lock;

    public IncrementSim(MediaDB db, EventHandler handler, Lock lock) {
        this.db = db;
        this.handler = handler;
        this.lock = lock;
    }

    public void run() {
        while( true ) {
            try {
                lock.lock();

                LinkedList<MediaContent> list = db.getDb();
                int index = rand.nextInt(list.size());
                String addr = list.get(index).getAddress();
                handler.handle(new IncrementAccessCounterEvent("", addr));
            } catch (IllegalArgumentException e) { // ignore bound must be positive
            } finally {
                lock.unlock();
            }
        }
    }
}
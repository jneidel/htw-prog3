package sim;

import gl.MediaContent;
import gl.MediaDB;
import javafx.event.Event;
import routing.EventHandler;
import routing.RemoveMediaEvent;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class RemoverSim extends Thread {
    Random rand = new Random();
    MediaDB db;
    EventHandler handler;
    Condition full;
    Condition removing;
    Lock lock;

    public RemoverSim(MediaDB db, EventHandler handler, Lock lock, Condition full, Condition removing ) {
        this.db = db;
        this.handler = handler;
        this.lock = lock;
        this.full = full;
        this.removing = removing;
    }

    public void run() {
        while( true ) {
            try {
                lock.lock();
                full.await();

                LinkedList<MediaContent> list = db.getDb();
                int removeCount = rand.nextInt(list.size());
                for (int i = 0; i < removeCount; i++) {
                    String addressLeastAccesses = null;
                    long lowestAccess = 100000L;
                    for ( MediaContent c : list ) {
                        if ( c.getAccessCount() < lowestAccess ) {
                            addressLeastAccesses = c.getAddressNonlogging();
                            lowestAccess = c.getAccessCount();
                        }
                    }
                    RemoveMediaEvent event = new RemoveMediaEvent( "", addressLeastAccesses );
                    handler.handle( event );
                }
                removing.signal();
            } catch (Exception e) {}
            finally {
                lock.unlock();
            }
        }
    }
}

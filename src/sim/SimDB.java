package sim;

import mediaDB.MediaContent;
import mediaDB.MediaDB;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SimDB {
    private MediaDB db;
    private final Lock lock = new ReentrantLock();
    private final Condition empty = this.lock.newCondition();

    public SimDB( MediaDB db ) {
        this.db = db;
    }
    public void upload( MediaContent item ) {
        this.lock.lock();
        try {
            this.db.upload( item );
            this.empty.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void delete( MediaContent item ) {
       this.lock.lock();
       try {
           while( this.db.list().size() < 0 ) this.empty.await();
           this.db.delete( item );
       } catch ( InterruptedException e ) {
           e.printStackTrace();
       } finally {
           this.lock.unlock();
       }
    }

    public ArrayList<MediaContent> list() {
        return this.db.list();
    }
}
package fs;

import cli.MediaDBObserver;
import mediaDB.MediaDB;
import util.Observer;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FS {
    private final Lock lock = new ReentrantLock();
    File file;

    public FS() {
         this.file = new File( "data" );
         if ( !this.file.exists() ) {
             try {
                 this.file.createNewFile();
             } catch( IOException e ) {
                 System.exit( 1 );
             }
         }
    }

    public MediaDB JOSreadDB() {
        this.lock.lock();
        try {
            ObjectInputStream stream = new ObjectInputStream( new FileInputStream( this.file ) );

            MediaDB db = (MediaDB) stream.readObject();
            stream.close();

            // db.attachObserver(  new MediaDBObserver( db ) );
            // db.notifyObservers( "read" );
            return db;
        } catch(IOException | ClassNotFoundException e ) {
            System.exit( 1 );
        } finally {
            this.lock.unlock();
        }
        return null; // java is crying, not reachable anyway
    }

    public void JOSwriteDB( MediaDB db ) {
        this.lock.lock();
        try {
            ObjectOutputStream stream = new ObjectOutputStream( new FileOutputStream( this.file ) );

            stream.writeObject( db );
            stream.close();
            db.notifyObservers( "write" );
        } catch( IOException e ) {
            System.exit( 1 );
        } finally {
            this.lock.unlock();
        }
    }
}

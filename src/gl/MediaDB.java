package gl;

import util.Observable;
import util.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

interface MediaDBI extends Observable,Serializable {
    void upload( MediaContent item );
    ArrayList<MediaContent> list();
    void update( String address, MediaContent newItem );
    void delete( String address );
}

class Uploader implements UploaderI, Serializable {
    /*
        class lives here to disallow the creation of Uploader instances through
        other means than MediaDB.createProducer(), which assures no duplicates
     */
    String name;

    public Uploader( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

public class MediaDB implements MediaDBI, Serializable {
    transient LinkedList<Observer> observers = new LinkedList<Observer>();
    public void attachObserver(Observer o) { this.observers.add( o ); }
    public void detachObserver(Observer o) { this.observers.remove( o ); }
    public void notifyObservers( String status ) {
        this.status = status;
        for ( Observer o : this.observers ) {
            o.update();
        }
    }
    transient String status;
    public String getStatus() { return this.status; }

    ArrayList<MediaContent> db = new ArrayList<MediaContent>();

    public ArrayList getdb() {
        return this.db;
    }

    public void upload(MediaContent itemToUpload) {
        for ( MediaContent item : this.db ) {
            if ( item.getAddress().equals( itemToUpload.getAddress() ) ) {
                throw new IllegalArgumentException( "Duplicate address" );
            }
        }

        // unique address
        this.db.add( itemToUpload );
        this.notifyObservers( "upload" );
    }

    public ArrayList<MediaContent> list() { return this.db; }
    public ArrayList<MediaContent> list( String mediaType ) {
        ArrayList<MediaContent> results = new ArrayList<MediaContent>();
        for ( MediaContent item : this.db ) {
            if ( item.getClassName().equals( mediaType ) ) {
                results.add( item );
            }
        }
        return results;
    }

    public void update( String address, MediaContent newItem ) {
        int matchingIndex = -1;
        for ( int i = 0; i < this.db.size(); i++ ) {
            if ( this.db.get( i ).getAddress().equals( address ) ) {
                matchingIndex = i;
                break;
            }
        }

        if ( matchingIndex < 0 ) {
            throw new IllegalArgumentException( "Passed address does not exist in database" );
        }

        db.set( matchingIndex, newItem );
        this.notifyObservers( "update" );
    }
    public void update( MediaContent item, MediaContent newItem ) {
        int index = this.db.indexOf( item );

        if ( index < 0 ) {
            throw new IllegalArgumentException( "Passed address does not exist in database" );
        }

        db.set( index, newItem );
        this.notifyObservers( "update" );
    }

    public void delete( String address ) {
        int matchingIndex = -1;
        for ( int i = 0; i < this.db.size(); i++ ) {
            if ( this.db.get( i ).getAddress().equals( address ) ) {
                matchingIndex = i;
                break;
            }
        }

        if ( matchingIndex >= 0 ) {
            this.db.remove( matchingIndex );
            this.notifyObservers( "delete" );
        }
        // if it does not exist in the first place -> do nothing
    }
    public void delete( MediaContent item ) {
        int index = this.db.indexOf( item );

        if ( index >= 0 ) {
            this.db.remove( index );
            this.notifyObservers( "delete" );
        }
        // if it does not exist in the first place -> do nothing
    }

    ArrayList<Uploader> producers = new ArrayList<Uploader>();
    public Uploader createProducer( String name ) {
        for ( Uploader prod : this.producers ) {
            if ( prod.getName().equals( name ) ) {
                throw new IllegalArgumentException( "Duplicate producer name" );
            }
        }

        Uploader prod = new Uploader( name );
        this.producers.add( prod );
        return prod;
    }
}

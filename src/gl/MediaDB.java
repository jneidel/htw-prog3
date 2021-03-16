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

    // producers
    public ArrayList<Uploader> producers = new ArrayList<Uploader>();

    private boolean hasProducer( String producerName ) {
        boolean producerExists = false;

        for ( Uploader prod : this.producers ) {
            if ( prod.getName().equals( producerName ) ) {
                producerExists = true;
            }
        }
        return producerExists;
    }

    public Uploader createProducer( String name ) {
        if ( this.hasProducer( name ) )
            throw new IllegalArgumentException( "Invalid producer: duplicate name" );

        Uploader prod = new Uploader( name );
        this.producers.add( prod );
        return prod;
    }

    private boolean hasItem( String itemAddress ) {
        boolean itemExists = false;

        for ( MediaContent item : this.db ) {
            if ( item.getAddress().equals( itemAddress ) ) {
                itemExists = true;
            }
        }

        return itemExists;
    }

    public void upload( MediaContent itemToUpload ) {
        if ( this.hasItem( itemToUpload.getAddress() ) )
            throw new IllegalArgumentException( "Invalid item: duplicate address" );

        if ( !this.hasProducer( itemToUpload.getUploader().getName() ) )
            throw new IllegalArgumentException( "Invalid producer: does not exist" );

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
}

package gl;

import util.Observable;
import util.Observer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface MediaDBI extends Observable,Serializable {
    void upload( MediaContent item );
    ArrayList<MediaContent> list();
    void update( String address, MediaContent newItem );
    void delete( String address );
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

    // capacity management
    private BigDecimal maxCapacity = new BigDecimal( 5000000 ); // in kb, 5gb
    public MediaDB( BigDecimal maxCapacity ) { this.maxCapacity = maxCapacity; }
    public MediaDB() {} // passing capacity is optional
    private BigDecimal currentCapacity = new BigDecimal( 0 );
    public BigDecimal getMaxCapacity() { return this.currentCapacity; }
    public BigDecimal getCurrentCapacity() { return this.currentCapacity; }
    private void addToCurrentSize( BigDecimal toAdd ) throws IllegalArgumentException {
        BigDecimal newSize = this.currentCapacity.add( toAdd );

        if ( newSize.max( this.maxCapacity ) != this.maxCapacity )
            throw new IllegalArgumentException( "Database error: not enough space to add item" );
        this.currentCapacity = newSize;
    }
    private void subtractFromCurrentSize( BigDecimal toSubtract ) {
        this.currentCapacity = this.currentCapacity.subtract( toSubtract )
            .max( new BigDecimal( 0 ) ); // don't go below 0
    }

    // producers
    private ArrayList<Uploader> producers = new ArrayList<Uploader>();
    public ArrayList<Uploader> getProducers() { return this.producers; }

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

    private Collection<Tag> usedTags = Collections.<Tag>emptySet();
    private void combineTags( Collection<Tag> newTags ) {
        // ref: https://www.baeldung.com/java-combine-multiple-collections
        Stream<Tag> combinedStream = Stream.concat(
                this.usedTags.stream(),
                newTags.stream()
        );
        this.usedTags = combinedStream.collect( Collectors.toSet() );
    }
    public Tag[][] getTagUsage() {
        Tag[] allTags = Tag.values();
        Tag[] used = new Tag[allTags.length];
        Tag[] unused = new Tag[allTags.length];

        for (int i = 0; i < allTags.length; i++) {
            Tag v = allTags[i];
            if ( this.usedTags.contains( v ) )
                used[i] = v;
            else
                unused[i] = v;
        }

        Tag[][] res = { used, unused };
        return res;
    }

    public void upload( MediaContent itemToUpload ) throws IllegalArgumentException {
        if ( this.hasItem( itemToUpload.getAddress() ) )
            throw new IllegalArgumentException( "Invalid item: duplicate address" );

        if ( !this.hasProducer( itemToUpload.getUploader().getName() ) )
            throw new IllegalArgumentException( "Invalid producer: does not exist" );

        itemToUpload.setUploadDateToNow();
        this.addToCurrentSize( itemToUpload.getSize() );
        itemToUpload.uploader.incrementCount();
        this.combineTags( itemToUpload.getTags() );

        this.db.add( itemToUpload );
        this.notifyObservers( "upload" );
    }

    public ArrayList<MediaContent> list() { return this.db; }
    public <T> ArrayList<T> list( String mediaType ) {
        ArrayList<T> results = new ArrayList<T>();
        for ( MediaContent item : this.db ) {
            if ( item.getClassName().equals( mediaType ) ) {
                results.add( (T) item );
            }
        }
        return results;
    }
    public MediaContent getItemByAddress( String address ) {
        for ( MediaContent item : this.db ) {
            if ( item.getAddress().equals( address ) ) {
                return item;
            }
        }
        return null;
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

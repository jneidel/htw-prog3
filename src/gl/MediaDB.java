package gl;

import util.Observable;
import util.Observer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface MediaDBI extends Observable,Serializable {
    void upload( MediaContent item );
    LinkedList<MediaContent> list();
}

public class MediaDB implements MediaDBI, Serializable {
    transient LinkedList<Observer> observers = new LinkedList<Observer>();
    public void attachObserver(Observer o) { this.observers.add( o ); }
    public void detachObserver(Observer o) { this.observers.remove( o ); }
    public void notifyObservers( String status ) {
        this.status = status;

        try {
            for (Observer o : this.observers) {
                o.update();
            }
        } catch ( NullPointerException e ) {} // transient observers list is null for io readObject instances
    }
    transient String status;
    public String getStatus() { return this.status; }

    LinkedList<MediaContent> db = new LinkedList<>();

    private final Lock dbLock = new ReentrantLock();
    private final Lock capacityLock = new ReentrantLock();
    private final Lock producerLock = new ReentrantLock();

    // capacity management
    private BigDecimal maxCapacity;
    private BigDecimal currentCapacity = new BigDecimal( 0 );
    private boolean isUsingCapacity = false;

    public MediaDB() {} // passing capacity is optional
    public MediaDB( BigDecimal maxCapacity ) {
        this.maxCapacity = maxCapacity;
        this.isUsingCapacity = true;
    }
    public BigDecimal getMaxCapacity() { return this.maxCapacity; }
    public BigDecimal getCurrentCapacity() { return this.currentCapacity; }
    public boolean isUsingCapacity() { return isUsingCapacity; }

    private void addToCurrentSize( BigDecimal toAdd ) throws IllegalArgumentException {
        if ( !isUsingCapacity )
            return;

        capacityLock.lock();
        try {
            BigDecimal newSize = this.currentCapacity.add(toAdd);

            if (newSize.max(this.maxCapacity).compareTo(this.maxCapacity) > 0)
                throw new IllegalArgumentException("Database error: not enough space to add item");
            this.currentCapacity = newSize;
        } finally {
            capacityLock.unlock();
        }
    }
    private void subtractFromCurrentSize( BigDecimal toSubtract ) {
        if ( !isUsingCapacity )
            return;

        capacityLock.lock();
        try {
            this.currentCapacity = this.currentCapacity.subtract( toSubtract )
                .max( new BigDecimal( 0 ) ); // don't go below 0
        } finally {
            capacityLock.unlock();
        }
    }

    // producers
    private LinkedList<Uploader> producers = new LinkedList<>();
    public LinkedList<Uploader> getProducers() { return this.producers; }

    private boolean hasProducer( String producerName ) {
        producerLock.lock();
        try {
            boolean producerExists = false;

            for ( Uploader prod : this.producers ) {
                if ( prod.getName().equals( producerName ) ) {
                    producerExists = true;
                }
            }
            return producerExists;
        } finally {
            producerLock.unlock();
        }
    }

    public Uploader createProducer( String name ) {
        if ( this.hasProducer( name ) )
            throw new IllegalArgumentException( "Invalid producer: duplicate name" );

        producerLock.lock();
        try {
            Uploader prod = new Uploader( name );
            this.producers.add( prod );
            this.notifyObservers( "producer create" );
            return prod;
        } finally {
            producerLock.unlock();
        }
    }
    public Uploader getProducer( String name ) {
        if ( !this.hasProducer( name ) )
            throw new IllegalArgumentException( "Invalid producer: does not exist" );

        producerLock.lock();
        try {
            for ( Uploader prod : this.producers ) {
                if ( prod.getName().equals( name ) ) {
                    return prod;
                }
            }
            return null; // just to shut up java, does not happen due to hasProducer check
        } finally {
            producerLock.unlock();
        }
    }
    public void deleteProducer( Uploader prod ) {
        producerLock.lock();
        try {
            int index = this.producers.indexOf( prod );

            if ( index >= 0 ) {
                this.producers.remove( index );
                this.notifyObservers( "producer delete" );
            }
        } finally {
            producerLock.unlock();
        }
    }
    public boolean deleteProducer( String prodName ) {
        try {
            Uploader prod = this.getProducer( prodName );
            this.deleteProducer( prod );
            return true;
        } catch ( Exception e ) {
            return false; // does not exist in the first place, do nothing
        }
    }

    private boolean hasItem( String itemAddress ) {
        dbLock.lock();
        try {
            boolean itemExists = false;

            for ( MediaContent item : this.db ) {
                if ( item.getAddressNonlogging().equals( itemAddress ) ) {
                    itemExists = true;
                }
            }

            return itemExists;
        } finally {
            dbLock.unlock();
        }
    }

    private Collection<Tag> usedTags = Collections.<Tag>emptySet();
    public Collection<Tag> getUsedTags() { return usedTags; }

    private void combineTags(Collection<Tag> newTags ) {
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
        if ( this.hasItem( itemToUpload.getAddressNonlogging() ) )
            throw new IllegalArgumentException( "Invalid item: duplicate address" );

        if ( !this.hasProducer( itemToUpload.getUploader().getName() ) )
            throw new IllegalArgumentException( "Invalid producer: does not exist" );

        dbLock.lock();
        try {
            itemToUpload.setUploadDateToNow();
            this.addToCurrentSize(itemToUpload.getSize());
            itemToUpload.uploader.incrementCount();
            this.combineTags(itemToUpload.getTags());

            this.db.add(itemToUpload);
        } finally {
            dbLock.unlock();
        }
        this.notifyObservers("media upload");
    }

    public LinkedList<MediaContent> list() {
        dbLock.lock();
        try {
            for (MediaContent c : this.db)
                c.accessCount++;
            return this.db;
        } finally {
            dbLock.unlock();
        }
    }
    public <T> LinkedList<T> list( String mediaType ) {
        dbLock.lock();
        try {
            LinkedList<T> results = new LinkedList<>();
            for (MediaContent item : this.db) {
                if (item.getClassName().equals(mediaType)) {
                    results.add((T) item);
                    item.accessCount++;
                }
            }
            return results;
        } finally {
            dbLock.unlock();
        }
    }
    public MediaContent getItemByAddress( String address ) {
        dbLock.lock();
        try {
            for (MediaContent item : this.db) {
                if (item.getAddressNonlogging().equals(address)) {
                    item.accessCount++;
                    return item;
                }
            }
            return null;
        } finally {
            dbLock.unlock();
        }
    }

    public boolean delete( String address ) {
        dbLock.lock();
        try {
            int matchingIndex = -1;
            for (int i = 0; i < this.db.size(); i++) {
                if (this.db.get(i).getAddressNonlogging().equals(address)) {
                    matchingIndex = i;
                    break;
                }
            }

            if (matchingIndex >= 0) {
                MediaContent item = this.db.get( matchingIndex );
                this.subtractFromCurrentSize( item.getSize() );
                item.getUploader().decrementCount();
                this.db.remove(matchingIndex);
                this.notifyObservers("media delete");
                return true;
            } else
                return false;
        } finally {
            dbLock.unlock();
        }
    }
    public void delete( MediaContent item ) {
        dbLock.lock();
        try {
            int index = this.db.indexOf(item);

            if (index >= 0) {
                this.subtractFromCurrentSize( item.getSize() );
                item.getUploader().decrementCount();
                this.db.remove(index);
                this.notifyObservers("media delete");
            } // if it does not exist in the first place -> do nothing
        } finally {
            dbLock.unlock();
        }
    }

    public void replaceContents( MediaDB db ) {
        dbLock.lock();
        try {
            this.db = db.getDb();
            this.maxCapacity = db.getMaxCapacity();
            this.currentCapacity = db.getCurrentCapacity();
            this.isUsingCapacity = db.isUsingCapacity();
            this.producers = db.getProducers();
            this.usedTags = db.getUsedTags();
        } finally {
            dbLock.unlock();
        }
    }

    // missing beans setters/getters
    public LinkedList<MediaContent> getDb() { return db; }
    public void setDb(LinkedList<MediaContent> db) { this.db = db; }
    public void setCurrentCapacity(BigDecimal currentCapacity) { this.currentCapacity = currentCapacity; }
    public void setMaxCapacity(BigDecimal maxCapacity) { this.maxCapacity = maxCapacity; }
    public void setUsedTags(Collection<Tag> usedTags) { this.usedTags = usedTags; }
    public void setUsingCapacity(boolean usingCapacity) { isUsingCapacity = usingCapacity; }
}
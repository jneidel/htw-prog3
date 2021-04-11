package io;

import gl.MediaContent;
import gl.MediaDB;

import java.beans.*;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileSystem {
    MediaDB db;
    private final Lock lock = new ReentrantLock();

    private String fileJBP = "db.jbp";
    private String fileJOS = "db.jos";
    private String fileInstances = "db-instances.jos";
    // for testing
    public void setFileJBP(String fileJBP) { this.fileJBP = fileJBP; }
    public void setFileJOS(String fileJOS) { this.fileJOS = fileJOS; }
    public void setFileInstances(String fileInstances) { this.fileInstances = fileInstances; }

    public FileSystem(MediaDB db) {
        this.db = db;
    }

    public FileOutputStream getWriteStream( String address ) throws IOException {
        File file = new File( address );
        if ( !file.exists() )
            file.createNewFile();

        return new FileOutputStream( address );
    }
    public FileInputStream getReadStream( String address ) throws FileNotFoundException, IOException {
        File file = new File( address );
        if ( !file.exists() )
            throw new FileNotFoundException( "can't read from nonexistant file" );

        return new FileInputStream( address );
    }

    public String saveDB( String protocol ) {
        this.lock.lock();
        try {
            switch ( protocol ) {
                case "jos" -> {
                    saveJOS();
                }
                case "jbp" -> {
                    saveJBP();
                }
                default -> {
                    return "invalid protocol for writing db: " + protocol;
                }
            }
            return "db saved";
        } catch ( Exception e ) {
            System.err.println( "io error" );
            e.printStackTrace();
            return "io error";
        } finally {
            this.lock.unlock();
        }
    }
    public String loadDB( String protocol ) {
        this.lock.lock();
        try {
            MediaDB db;
            switch ( protocol ) {
                case "jos" -> {
                    db = loadJOS();
                }
                case "jbp" -> {
                    db = loadJBP();
                }
                default -> {
                    return "invalid protocol for writing db: " + protocol;
                }
            }
            this.db.replaceContents( db );
            this.db.notifyObservers( "load db" );
            return "db loaded";
        } catch ( Exception e ) {
            System.err.println( "io error" );
            e.printStackTrace();
            return "io error";
        } finally {
            this.lock.unlock();
        }
    }

    private void saveJOS() throws Exception {
        FileOutputStream out = getWriteStream( this.fileJOS );
        ObjectOutputStream stream = new ObjectOutputStream( out );
        stream.writeObject( db );
        stream.close();
    }
    private MediaDB loadJOS() throws Exception {
        FileInputStream in = getReadStream( this.fileJOS );
        ObjectInputStream stream = new ObjectInputStream( in );
        MediaDB db = (MediaDB) stream.readObject();
        stream.close();
        return db;
    }

    private void saveJBP() throws Exception {
        FileOutputStream out = getWriteStream( this.fileJBP );
        XMLEncoder encoder = new XMLEncoder( new BufferedOutputStream( out ) );
        /*
        encoder.setPersistenceDelegate(BigDecimal.class, new DefaultPersistenceDelegate( new String[]{ "value" }));
            what am I supposed to put instead of value? the offical docs and the vl would lead me to believe it to be "value" or "val"
            https://docs.oracle.com/javase/7/docs/api/java/beans/DefaultPersistenceDelegate.html#DefaultPersistenceDelegate(java.lang.String[])
         */
        encoder.writeObject( db );

    }
    private MediaDB loadJBP() throws Exception {
        FileInputStream in = getReadStream( this.fileJBP );
        XMLDecoder decoder = new XMLDecoder( new BufferedInputStream( in ) );
        return (MediaDB) decoder.readObject();
    }

    public String saveInstance( String mediaAddress ) {
        this.lock.lock();
        try {
            MediaContent itemToBeSaved = this.db.getItemByAddress( mediaAddress );
            MediaDB dbToBeWritten;

            String res;
            try { // only this instance
                FileInputStream in = getReadStream(this.fileInstances);
                ObjectInputStream stream = new ObjectInputStream(in);
                MediaDB db = (MediaDB) stream.readObject();
                stream.close();

                db.delete( mediaAddress );
                try {
                    db.createProducer( itemToBeSaved.getUploader().getName() );
                } catch (IllegalArgumentException e) {} // prod already exists
                db.upload( itemToBeSaved );

                dbToBeWritten = db;
                res = "wrote instance";
            } catch ( FileNotFoundException e ) { // whole db
                dbToBeWritten = this.db;
                res = "wrote whole db, incl. instance";
            }

            FileOutputStream out = getWriteStream( this.fileInstances );
            ObjectOutputStream stream = new ObjectOutputStream( out );
            stream.writeObject( dbToBeWritten );
            stream.close();
            return res;
        } catch ( Exception e ) {
            System.err.println( "io error" );
            e.printStackTrace();
            return "io error";
        } finally {
            this.lock.unlock();
        }
    }
    public String loadInstance( String mediaAddress ) {
        this.lock.lock();
        try {
            FileInputStream in = getReadStream( this.fileInstances );
            ObjectInputStream stream = new ObjectInputStream( in );
            MediaDB db = (MediaDB) stream.readObject();
            stream.close();

            try {
                MediaContent item = db.getItemByAddress(mediaAddress);
                this.db.createProducer(item.getUploader().getName());
                this.db.upload(item);
                this.db.notifyObservers("load db");
            } catch (IllegalArgumentException e) { // producer already exists
                MediaContent item = db.getItemByAddress(mediaAddress);
                this.db.upload(item);
                this.db.notifyObservers("load db");
            } catch (Exception e ) {
                return "invalid media address for loading";
            }
            return "db loaded";
        } catch ( Exception e ) {
            System.err.println( "io error" );
            e.printStackTrace();
            return "io error";
        } finally {
            this.lock.unlock();
        }
    }
}
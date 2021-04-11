package io;

import static org.junit.jupiter.api.Assertions.*;

import gl.MediaContent;
import gl.MediaDB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.Parser;

import java.io.File;
import java.math.BigDecimal;


public class FileSystemTest {
    FileSystem fs;
    MediaDB db;
    MediaContent sampleItem;

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.fs = new FileSystem( db );
        db.createProducer( "prod" );
        this.sampleItem = new Parser().parseMediaStrToMediaContent( "Audio prod mediaAddr 4000 News 320 30 AAC 44000", db );
        db.upload( sampleItem );
    }
    @AfterAll static void teardown() {
        for ( String addr : new String[]{ "jos-test", "instance-test" } ) {
            File file = new File( addr );
            if ( file.exists() )
                file.delete();
        }
    }

    private void deleteFileIfExists( String address ) {
        File file = new File( address );
        if ( file.exists() )
            file.delete();
    }
    @Test void saveLoadJOS() {
        String address = "jos-test";
        deleteFileIfExists( address );

        db.setCurrentCapacity( new BigDecimal( 100 ) );
        db.setMaxCapacity( new BigDecimal( 100 ) );
        db.setUsingCapacity( true );

        fs.setFileJOS( address );
        fs.saveDB( "jos" );

        db.delete( this.sampleItem );
        assertEquals( 0, db.list().size() );
        db.deleteProducer( "prod" );
        assertEquals( 0, db.getProducers().size() );
        db.setCurrentCapacity( new BigDecimal( 5000 ) );
        db.setMaxCapacity( new BigDecimal( 5000 ) );
        db.setUsingCapacity( false );

        fs.loadDB( "jos" );
        assertEquals( 1, db.list().size() );
        assertNotNull( db.getItemByAddress( "mediaAddr" ) );
        assertNotNull( db.getProducer( "prod" ) );
        assertEquals( new BigDecimal( 100 ), db.getCurrentCapacity() );
        assertEquals( new BigDecimal( 100 ), db.getMaxCapacity() );
        assertTrue( db.isUsingCapacity() );
    }

    /*
    There is a problem serializing BigDecimal, which is not easily solvable.
    JBP requires a default constructor, BigDecimal does not have one.

    I looked into DefaultPersistenceDelegate, but I couldn't get it working. Neither the vl or the offical docs are any
    help nor are there tutorials or much of anything on the topic of Bean Persistence more generally out there.

    Also see io.FileSystems for comment.
     */
    @Disabled
    @Test void saveLoadJBP() {
        String address = "jbp-test";
        deleteFileIfExists( address );

        db.setCurrentCapacity( new BigDecimal( 100 ) );
        db.setMaxCapacity( new BigDecimal( 100 ) );
        db.setUsingCapacity( true );

        fs.setFileJBP( address );
        fs.saveDB( "jbp" );

        db.delete( this.sampleItem );
        assertEquals( 0, db.list().size() );
        db.deleteProducer( "prod" );
        assertEquals( 0, db.getProducers().size() );
        db.setCurrentCapacity( new BigDecimal( 5000 ) );
        db.setMaxCapacity( new BigDecimal( 5000 ) );
        db.setUsingCapacity( false );

        fs.loadDB( "jbp" );
        assertEquals( 1, db.list().size() );
        assertNotNull( db.getItemByAddress( "mediaAddr" ) );
        assertNotNull( db.getProducer( "prod" ) );
        assertEquals( new BigDecimal( 100 ), db.getCurrentCapacity() );
        assertEquals( new BigDecimal( 100 ), db.getMaxCapacity() );
        assertTrue( db.isUsingCapacity() );
    }

    @Test void saveLoadInstance_wholeDB() {
        String address = "instance-test";
        deleteFileIfExists( address );

        fs.setFileInstances( address );
        fs.saveInstance( "mediaAddr" );
        db.delete( this.sampleItem );
        assertEquals( 0, db.list().size() );

        MediaContent otherSample = new Parser().parseMediaStrToMediaContent( "Audio prod otherAddr 4000 News 320 30 AAC 44000", db );
        db.upload( otherSample );
        assertEquals( 1, db.list().size() );

        fs.loadInstance( "mediaAddr" );
        assertEquals( 2, db.list().size() );

        assertNotNull( db.getItemByAddress( "mediaAddr" ) );
        assertNotNull( db.getItemByAddress( "otherAddr" ) );
    }
    @Test void saveLoadInstance_onlyInstance() {
        String address = "instance-test";
        deleteFileIfExists( address );
        fs.setFileInstances( address );

        db.delete( this.sampleItem );

        // create db - save whole
        MediaContent otherSample = new Parser().parseMediaStrToMediaContent( "Audio prod otherAddr 4000 News 320 30 AAC 44000", db );
        db.upload( otherSample );
        fs.saveInstance( "otherAddr" );
        db.delete( otherSample );
        assertEquals( 0, db.list().size() );

        // add instance - only save instance
        db.upload( this.sampleItem );
        fs.saveInstance( "mediaAddr" );
        db.delete( this.sampleItem );
        assertEquals( 0, db.list().size() );

        // load instance
        fs.loadInstance( "mediaAddr" );
        assertEquals( 1, db.list().size() );

        assertNotNull( db.getItemByAddress( "mediaAddr" ) );
        assertNull( db.getItemByAddress( "otherAddr" ) );
    }
}
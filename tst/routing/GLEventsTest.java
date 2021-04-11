package routing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gl.*;
import util.Parser;

public class GLEventsTest {
    MediaDB db;
    Uploader prod;
    String sampleAddress = "music_video.mp4";
    EventHandler handler;
    GLEventListener listener;
    Parser parser = new Parser();

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.prod = this.db.createProducer( "prod" );

        String input = "Audio prod " + this.sampleAddress + " 4000 News 320 30 AAC 44000";
        MediaContent item = this.parser.parseMediaStrToMediaContent( input, this.db );
        this.db.upload( item );

        this.handler = new EventHandler();
        this.listener = new GLEventListener( this.db );
        this.handler.add(this.listener);
    }

    @Test void upload() {
        String address = "song.aac";
        String input = "Audio prod " + address + " 4000 News 320 30 AAC 44000";
        MediaContent expected = this.parser.parseMediaStrToMediaContent( input, db );

        UploadMediaEvent event = this.parser.parseMediaStrToEvent( input );
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( address );
        assertEquals( expected.toString(), res.toString() );
    }

    @Test void increment() {
        IncrementAccessCounterEvent event = new IncrementAccessCounterEvent( "", this.sampleAddress ); // 1st access
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( this.sampleAddress ); // 2nd access
        assertEquals( 2, res.getAccessCount() );
    }
    @Test void increment_nonExisting() {
        IncrementAccessCounterEvent event = new IncrementAccessCounterEvent( "", "does_not_exist" );
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( this.sampleAddress ); // 1nd access
        assertEquals( 1, res.getAccessCount() );
    }

    @Test void mediaRemove() {
        RemoveMediaEvent event = new RemoveMediaEvent( "", this.sampleAddress );
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( this.sampleAddress );
        assertNull( res );
    }
    @Test void mediaRemove_nonExisting() {
        RemoveMediaEvent event = new RemoveMediaEvent( "", "does_not_exist" );
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( this.sampleAddress );
        assertNotNull( res );
    }

    @Test void prodRemove() {
        String name = "prod-test";
        RemoveProducerEvent event = new RemoveProducerEvent( "", name );
        this.db.createProducer( name );
        this.handler.handle( event );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.db.getProducer( name )
        );
        assertEquals( "Invalid producer: does not exist", e.getMessage() );
    }
    @Test void prodRemove_nonExisting() {
        String name = "prod";
        RemoveProducerEvent event = new RemoveProducerEvent( "", "not_a-Prod" );
        this.handler.handle( event );

        Uploader res = db.getProducer( name );
        assertNotNull( res );
    }

    @Test void mediaProdRemove_media() {
        RemoveMediaOrProducerEvent event = new RemoveMediaOrProducerEvent( "", this.sampleAddress );
        this.handler.handle( event );

        MediaContent res = db.getItemByAddress( this.sampleAddress );
        assertNull( res );
    }
    @Test void mediaProdRemove_prod() {
        String name = "test-prod";
        RemoveMediaOrProducerEvent event = new RemoveMediaOrProducerEvent( "", name );
        this.db.createProducer( name );
        this.handler.handle( event );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.db.getProducer( name )
        );
        assertEquals( "Invalid producer: does not exist", e.getMessage() );
    }
    @Test void mediaProdRemove_nonExisting() {
        String name = "prod";
        RemoveMediaOrProducerEvent event = new RemoveMediaOrProducerEvent( "", "not_an_address_or_prod" );
        this.handler.handle( event );

        Uploader res = db.getProducer( name );
        MediaContent res2 = db.getItemByAddress( this.sampleAddress );
        assertNotNull( res );
        assertNotNull( res2 );
    }

    @Test void prodCreate() {
        String name = "testProd";
        CreateProducerEvent event = new CreateProducerEvent( "", name );
        this.handler.handle( event );

        Uploader res = db.getProducer( name );
        assertEquals( name, res.getName() );
    }
}
package glTest;

import static org.junit.jupiter.api.Assertions.*;

import gl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class MediaDBTest {
    MediaDB db;
    Uploader producer;

    MediaContent sampleItem;
    String sampleAddress = "testing sample item";
    long sampleBitrate = 1L;
    BigDecimal sampleSize = new BigDecimal( 1000 ); // 1mb
    Duration sampleDuration = Duration.ofSeconds( 0 + 1 * 60 + 0 * 60 * 60); // 1min

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.producer = this.db.createProducer( "default" );

        this.sampleItem = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
    }

    @Test void createUploader() { // happy path
        String[] result = { "default", "prod" };

        this.db.createProducer( "prod" );

        Object[] producerArr = this.db.getProducers().toArray();
        String[] producerStrArr = new String[2];
        for (int i = 0; i < 2; i++) {
            producerStrArr[i] = ((Uploader) producerArr[i]).getName();
        }

        // no deep equality supported, manual checking
        assertEquals( result[0], producerStrArr[0] );
        assertEquals( result[1], producerStrArr[1] );
    }
    @Test void createDuplicateUploader() {
        Exception e = assertThrows( IllegalArgumentException.class, () ->
            this.db.createProducer( "default" ) // created in BeforeEach
        );
        assertEquals( "Invalid producer: duplicate name", e.getMessage() );
    }

    @Test void upload() {
        this.db.upload( this.sampleItem );

        assertNotEquals( null, this.db.getItemByAddress( this.sampleAddress ) );
    }
    @Test void uploadWithCorrectProducer() {
        this.db.upload( this.sampleItem );

        assertEquals( this.producer, this.db.getItemByAddress( this.sampleAddress ).getUploader() );
    }
    @Test void uploadDuplicateItem() {
        this.db.upload( this.sampleItem );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
            this.db.upload( this.sampleItem )
        );
        assertEquals( "Invalid item: duplicate address", e.getMessage() );
    }
    @Test void uploadWithInvalidProducer() {
        MediaDB differentDB = new MediaDB();
        Uploader otherDBsProducer = differentDB.createProducer( "testing" );

        MediaContent itemWithInvalidProducer = new MediaContent( this.sampleAddress, otherDBsProducer, this.sampleBitrate, this.sampleDuration, this.sampleSize );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.db.upload( itemWithInvalidProducer )
        );
        assertEquals( "Invalid producer: does not exist", e.getMessage() );
    }
    @Test void uploadWithValidDate() {
        this.db.upload( this.sampleItem );

        Date uploadDate = this.db.getItemByAddress( this.sampleAddress ).getUploadDate();
        assertNotEquals( null, uploadDate );
        assertEquals( new Date().getDate(), uploadDate.getDate() );
        assertEquals( new Date().getMonth(), uploadDate.getMonth() );
        assertEquals( new Date().getHours(), uploadDate.getHours() );
    }
    @Test void uploadWithValidSize() {
        this.db.upload( this.sampleItem );

        assertEquals( this.sampleSize, this.db.getCurrentCapacity() );
    }
    @Test void uploadWithInvalidSize() {
        final int MAX_CAPACITY = 100;
        final int ITEM_CAPACITY = 500;

        MediaDB db = new MediaDB( new BigDecimal( MAX_CAPACITY ) );
        Uploader prod = db.createProducer( "size test prod" );
        MediaContent item = new MediaContent( this.sampleAddress, prod, this.sampleBitrate, this.sampleDuration, new BigDecimal( ITEM_CAPACITY ) );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
            db.upload( item )
        );
        assertEquals( "Database error: not enough space to add item", e.getMessage() );
    }

    @Test void listFiltersByMediaType() {
        Audio audio = new Audio( "audio", this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, 320000, "mp3" );
        Video video = new Video( "video", this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, 1080 ,720, "mkv" );

        this.db.upload( audio );
        this.db.upload( video );

        // only has video
        ArrayList<Video> list = this.db.list( "Video" );
        assertEquals( "video", list.get( 0 ).getAddress() );
        assertEquals( 1, list.size() );

        // only has audio
        ArrayList<Audio> list2 = this.db.list( "Audio" );
        assertEquals( "audio", list2.get( 0 ).getAddress() );
        assertEquals( 1, list.size() );
    }
    @Test void uploadWithProducerCount() {
        assertEquals( 0, this.producer.getCount() );
        this.db.upload( this.sampleItem );
        assertEquals( 1, this.producer.getCount() );
    }
}
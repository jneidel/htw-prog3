package gl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

        assertTrue( this.db.list().contains( this.sampleItem ) );
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

    @Test void getTagUsageWithoutAnyTags() {
        Tag[] allTags = Tag.values();
        Tag[] noTags = new Tag[Tag.values().length];

        Tag[][] tagUsage = this.db.getTagUsage();
        for ( int i = 0; i < Tag.values().length; i++ ) {
            assertEquals( noTags[i], tagUsage[0][i] );
            assertEquals( allTags[i], tagUsage[1][i] );
        }
    }
    @Test void getTagUsage() {
        this.sampleItem.addTag( Tag.Lifestyle );
        this.db.upload( this.sampleItem );

        Tag[] allTags = Tag.values();
        Tag[] noTags = new Tag[Tag.values().length];
        allTags[2] = null;
        noTags[2] = Tag.Lifestyle;

        Tag[][] tagUsage = this.db.getTagUsage();
        for ( int i = 0; i < Tag.values().length; i++ ) {
            assertEquals( noTags[i], tagUsage[0][i] );
            assertEquals( allTags[i], tagUsage[1][i] );
        }
    }

    @Test void getAccessCountWithGetAddress() {
        this.db.upload( this.sampleItem );
        this.sampleItem.getAddress();

        assertEquals( 1, this.sampleItem.getAccessCount() );
    }
    @Test void getAccessCountWithDbList() {
        this.db.upload( this.sampleItem );
        this.db.list();
        this.db.list( "MediaContent" );

        assertEquals( 2, this.sampleItem.getAccessCount() );
    }
    @Test void getAccessCountCombined() {
        this.db.upload( this.sampleItem );
        this.db.list();
        this.sampleItem.getAddress();
        this.db.list();
        this.db.list( "MediaContent" );

        assertEquals( 4, this.sampleItem.getAccessCount() );
    }

    @Test void deleteWithAddress() {
        this.db.upload( this.sampleItem );
        assertEquals( this.sampleItem, this.db.getItemByAddress( this.sampleAddress ) );
        this.db.delete( this.sampleAddress );
        assertEquals( null, this.db.getItemByAddress( this.sampleAddress ) );
    }
    @Test void deleteWithInstance() {
        this.db.upload( this.sampleItem );
        assertEquals( this.sampleItem, this.db.getItemByAddress( this.sampleAddress ) );
        this.db.delete( this.sampleItem );
        assertEquals( null, this.db.getItemByAddress( this.sampleAddress ) );
    }
    @Test void deleteNonExisting() {
        // no upload
        assertEquals( null, this.db.getItemByAddress( this.sampleAddress ) );
        this.db.delete( this.sampleItem );
        assertEquals( null, this.db.getItemByAddress( this.sampleAddress ) );
    }
}
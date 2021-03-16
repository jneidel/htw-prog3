package glTest;

import static org.junit.jupiter.api.Assertions.*;

import gl.Content;
import gl.MediaContent;
import gl.MediaDB;
import gl.UploaderI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;

public class MediaDBTest {
    MediaDB db;
    UploaderI producer;

    MediaContent sampleItem;
    String sampleAddress = "testing sample item";
    long sampleBitrate = 1L;
    BigDecimal sampleSize = new BigDecimal( 1 );
    Duration sampleDuration = Duration.ofSeconds( 0 + 1 * 60 + 0 * 60 * 60); // 1min

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.producer = this.db.createProducer( "default" );

        this.sampleItem = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
    }

    @Test void createUploader() { // happy path
        String[] result = { "default", "prod" };

        this.db.createProducer( "prod" );

        Object[] producerArr = this.db.producers.toArray();
        String[] producerStrArr = new String[2];
        for (int i = 0; i < 2; i++) {
            producerStrArr[i] = ((UploaderI) producerArr[i]).getName();
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

        ArrayList<MediaContent> list = this.db.list();
        assertTrue( list.contains( this.sampleItem ) );
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
        UploaderI otherDBsProducer = differentDB.createProducer( "testing" );

        MediaContent itemWithInvalidProducer = new MediaContent( this.sampleAddress, otherDBsProducer, this.sampleBitrate, this.sampleDuration, this.sampleSize );

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.db.upload( itemWithInvalidProducer )
        );
        assertEquals( "Invalid producer: does not exist", e.getMessage() );
    }
}

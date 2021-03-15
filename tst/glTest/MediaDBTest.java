package glTest;

import static org.junit.jupiter.api.Assertions.*;

import gl.MediaDB;
import gl.UploaderI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MediaDBTest {
    MediaDB db;
    UploaderI producer;

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.producer = this.db.createProducer( "default" );
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
        assertEquals( "Duplicate producer name", e.getMessage() );
    }
}

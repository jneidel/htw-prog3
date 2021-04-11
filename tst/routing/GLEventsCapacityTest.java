package routing;

import static org.junit.jupiter.api.Assertions.*;

import gl.MediaContent;
import gl.MediaDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Parser;

import java.math.BigDecimal;

public class GLEventsCapacityTest {
    MediaDB db;
    EventHandler handler;
    Parser parser = new Parser();

    @BeforeEach void setup() {
        this.db = new MediaDB( new BigDecimal( "100" ));
        db.createProducer( "prod" );
        this.handler = new EventHandler();
        this.handler.add( new GLEventListener( db ) );
    }
    private UploadMediaEvent genItem( int capacity ) {
        String input = "Audio prod tsss.mkv " + capacity + " News 320 30 AAC 44000";
        return parser.parseMediaStrToEvent( input );
    }

    @Test void above90Percent() {
        UploadMediaEvent event = genItem( 98 );
        String res = handler.handleWithFeedback( event );
        assertEquals( "uploaded item: tsss.mkv\n98% of db capacity reached", res );
    }
    @Test void exactly90Percent() {
        UploadMediaEvent event = genItem( 90 );
        String res = handler.handleWithFeedback( event );
        assertEquals( "uploaded item: tsss.mkv\n90% of db capacity reached", res );
    }
    @Test void not90Percent() {
        UploadMediaEvent event = genItem( 80 );
        String res = handler.handleWithFeedback( event );
        assertEquals( "uploaded item: tsss.mkv", res );
    }
}

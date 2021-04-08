package routing;

import gl.MediaContent;
import gl.MediaDB;
import util.Parser;

import java.util.EventObject;

public class GLEventListener implements EventListener {
    Parser parser = new Parser();
    MediaDB db;

    public GLEventListener( MediaDB db ) {
        this.db = db;
    }

    public void onEvent( EventObject event ) {
        switch ( event.toString() ) {
            case "media increment counter" -> {
                this.db.getItemByAddress( ((IncrementAccessCounterEvent) event).getMediaAddress() );
            }
            case "producer create" -> {
                this.db.createProducer( ((CreateProducerEvent) event ).getProducerStr() );
            }
            case "media remove" -> {
                this.db.delete( ((RemoveMediaEvent) event).getMediaAddress() );
            }
            case "media_producer remove" -> {
                this.db.delete( ((RemoveMediaOrProducerEvent) event).getName() );
                this.db.deleteProducer( ((RemoveMediaOrProducerEvent) event).getName() );
            }
            case "producer remove" -> {
                this.db.deleteProducer( ((RemoveProducerEvent) event).getProducerName() );
            }
            case "media upload" -> {
                try {
                    MediaContent c = this.parser.parseMediaStrToMediaContent(((UploadMediaEvent) event).getMediaStr(), this.db);
                    this.db.upload(c);
                } catch ( Exception e ) {} // disregard errMsg, already thrown on CLI
            }
        }
    }
}

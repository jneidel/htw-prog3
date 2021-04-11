package routing;

import gl.MediaContent;
import gl.MediaDB;
import gl.Tag;
import gl.Uploader;
import util.Parser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;

public class GLEventListener implements EventListener {
    Parser parser = new Parser();
    MediaDB db;
    public GLEventListener( MediaDB db ) { this.db = db; }

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
   public String onEventWithFeedback( EventObject event ) {
        switch ( event.toString() ) {
            case "media increment counter" -> {
                try {
                    return "incremented by one, new accessCounter: " +
                            this.db.getItemByAddress(((IncrementAccessCounterEvent) event).getMediaAddress()).getAccessCount();
                } catch ( Exception e ) {
                    return "failed to increment";
                }
            }
            case "producer create" -> {
                try {
                    Uploader uploader = this.db.createProducer( ((CreateProducerEvent) event ).getProducerStr() );
                    return "created new producer: " + uploader.getName();
                } catch ( Exception e ) {
                    return "failed to create";
                }
            }
            case "media remove" -> {
                String itemName = ((RemoveMediaEvent) event).getMediaAddress();
                boolean removedItem = this.db.delete( itemName );
                if ( removedItem )
                    return "removed item: " + itemName;
                else
                    return "removed nothing";
            }
            case "media_producer remove" -> {
                String itemName = ((RemoveMediaOrProducerEvent) event).getName();
                String res;
                boolean removedItem = this.db.delete( itemName );
                if ( !removedItem ) {
                    String producerName = ((RemoveMediaOrProducerEvent) event).getName();
                    boolean removedProducer = this.db.deleteProducer( producerName );

                    if ( removedProducer )
                        res = "removed producer: " + producerName;
                    else
                        res = "removed nothing";
                } else
                    res = "removed item: " + itemName;

                String notify = this.notifyOnCriticalCapacity();
                if ( notify != null )
                    res += "\n" + notify;
                return res;
            }
            case "producer remove" -> {
                String producerName = ((RemoveProducerEvent) event).getProducerName();
                boolean isRemoved = this.db.deleteProducer( producerName );
                if ( isRemoved )
                    return "removed producer: " + producerName;
                else
                    return "removed nothing";
            }
            case "media upload" -> {
                try {
                    MediaContent c = this.parser.parseMediaStrToMediaContent(((UploadMediaEvent) event).getMediaStr(), this.db);
                    this.db.upload( c );
                    String res = "uploaded item: " + c.getAddress();
                    String notify = this.notifyOnCriticalCapacity();
                    if ( notify != null )
                        res += "\n" + notify;
                    return res;
                } catch ( Exception e ) {
                    return "upload failed: " + e.getMessage();
                }
            }
            case "read content" -> {
                try {
                    String contentClass = ((ReadEvent) event).getOptions();

                    String res = "";
                    if ( contentClass == null ) {
                        for ( MediaContent c : this.db.list() )
                            res += c.toString() + "\n";
                    } else {
                        for ( Object c : this.db.list( contentClass ) )
                            res += c.toString() + "\n";
                    }
                    return res;
                } catch ( Exception e ) {
                    return "reading content failed: " + e.getMessage();
                }
            }
            case "read producer" -> {
                try {
                    String res = "";
                    for ( Uploader u : this.db.getProducers() )
                        res += u.toString() + "\n";
                    return res;
                } catch ( Exception e ) {
                    return "reading producers failed: " + e.getMessage();
                }
            }
            default -> {
                return null;
            }
            case "read tag" -> {
                try {
                    String includedExcluded = ((ReadEvent) event).getOptions();
                    Tag[][] tags = this.db.getTagUsage();

                    String res = "";
                    if ( includedExcluded.equals( "i" ) ) {
                        res += "Used tags: ";
                        for ( Tag t : tags[0] )
                            if ( t != null )
                                res += t + " ";
                    } else {
                        res += "Unused tags: ";
                        for ( Tag t : tags[1] )
                            if ( t != null )
                                res += t + " ";
                    }
                    return res;
                } catch ( Exception e) {
                    return "reading tags failed: " + e.getMessage();
                }
            }
        }
    }

    private String notifyOnCriticalCapacity() {
        if ( db.isUsingCapacity() ) {
            BigDecimal current = db.getCurrentCapacity();
            BigDecimal max = db.getMaxCapacity();
            BigDecimal percentage = current.divide(max, 2, RoundingMode.HALF_UP);
            if (percentage.compareTo(new BigDecimal("0.9")) >= 0) {
                int res = (int) (percentage.round(new MathContext(2, RoundingMode.HALF_UP)).floatValue() * 100);
                return res + "% of db capacity reached";
            }
        }
        return null;
    }

    private String notifyOnTagChange() {
        return null;
    }
}
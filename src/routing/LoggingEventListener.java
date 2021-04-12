package routing;

import io.Logger;

import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedList;

public class LoggingEventListener implements EventListener {
    static LinkedList<String> relevantEvents = new LinkedList<String>(Arrays.asList( "media save", "media load",
        "media increment counter", "media upload", "media remove", "media_producer remove", "producer create",
        "producer remove", "media read" ));
    String countryCode;
    Logger logger;

    public LoggingEventListener( String countryCode ) {
        this.countryCode = countryCode;
        this.logger = new Logger();
    }

    public void onEvent( EventObject event ) {
        String text = event.toString();
        if ( relevantEvents.contains( text ) ) {
            String msg = translate( text );
            logger.log( msg );
        }
    }
    public String onEventWithFeedback( EventObject event ) {
        // compatibility wrapper
        this.onEvent( event );
        return null;
    }
    private String translateToDE( String event ) {
        switch ( event ) {
            case "media save": return "medien speichern";
            case "media load": return "medien laden";
            case "media increment counter": return "medien zähler erhöhen";
            case "producer create": return "produzent erstellen";
            case "producer remove": return "produzent löschen";
            case "media upload": return "medien hochladen";
            case "media remove": return "medien löschen";
            case "media_producer remove": return "medien_produzent löschen";
            default: return "untranslated event: " + event.toString();
        }
    }
    private String translate( String event ) {
        switch ( this.countryCode ) {
            case "de" -> {
                return this.translateToDE( event );
            }
            case "us", "gb" -> {
                return event; // messages are in english already
            }
            default -> {
                throw new IllegalArgumentException( "invalid country code: " + countryCode );
            }
        }
    }
}

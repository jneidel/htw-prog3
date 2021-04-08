package routing;

import java.util.EventObject;

public class LoggingEventListener implements EventListener {
    String countryCode;

    public LoggingEventListener( String countryCode ) {
        this.countryCode = countryCode;
    }

    public void onEvent( EventObject event ) {
        String msg = translate( event.toString() );
        // write to log, io pkg func
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
            default: return "untranslated event: " + event;
        }
    }
    private String translate( String event ) {
        switch ( this.countryCode ) {
            case "DE" -> {
                return this.translateToDE( event );
            }
            case "US", "GB" -> {
                return event; // messages are in english already
            }
            default -> {
                throw new IllegalArgumentException( "invalid country code: " + countryCode );
            }
        }
    }
}

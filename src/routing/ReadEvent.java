package routing;

import java.util.EventObject;

public class ReadEvent extends EventObject {
    String options = null;
    private String eventType;

    public ReadEvent(Object source, String textRaw) {
        super(source);
        String[] text = textRaw.split( " " );

        try {
            switch ( text[0] ) {
                case "producer" -> {
                    this.eventType = "producer";
                }
                case "content" -> {
                    this.eventType = "content";
                    if ( text.length > 1 )
                        this.options = text[1];
                }
                case "tag" -> {
                    this.eventType = "tag";
                    this.options = text[1];
                }
                default -> {throw new IllegalArgumentException();}
            }
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "illegal format" );
        }
    }

    public String getOptions() { return options; }

    public String toString() {
        return "read " + eventType;
    }
}

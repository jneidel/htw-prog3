package routing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EventObject;

public class UploadItemEvent extends EventObject implements Serializable {
    String[] text;
    public UploadItemEvent( Object src, String[] text ) {
        super( src );
        if ( text == null )
            throw new IllegalArgumentException( "no null text" );
        this.text = text;
    }
    public String[] getText() { return this.text; }

    public String toString() { return "upload"; }
}
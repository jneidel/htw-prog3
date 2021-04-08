package routing;

import java.io.Serializable;
import java.util.EventObject;

public class IncrementAccessCounterEvent extends EventObject implements Serializable {
    String mediaAddress;
    public IncrementAccessCounterEvent(Object src, String mediaAddress) {
        super( src );
        if ( mediaAddress == null )
            throw new IllegalArgumentException( "no null address" );
        this.mediaAddress = mediaAddress;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }

    public String toString() { return "media increment counter"; }
}
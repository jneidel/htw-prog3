package routing;

import java.io.Serializable;
import java.util.EventObject;

public class RemoveMediaEvent extends EventObject implements Serializable {
    String mediaAddress;
    public RemoveMediaEvent(Object src, String mediaAddress) {
        super( src );
        if ( mediaAddress == null )
            throw new IllegalArgumentException( "no null address" );
        this.mediaAddress = mediaAddress;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }

    public String toString() { return "media remove"; }
}
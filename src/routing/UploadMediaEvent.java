package routing;

import java.io.Serializable;
import java.util.EventObject;

public class UploadMediaEvent extends EventObject implements Serializable {
    String mediaStr;
    public UploadMediaEvent( Object src, String mediaStr ) {
        super( src );
        if ( mediaStr == null )
            throw new IllegalArgumentException( "no null media" );
        this.mediaStr = mediaStr;
    }

    public String getMediaStr() { return mediaStr; }

    public String toString() { return "media upload"; }
}

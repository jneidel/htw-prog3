package routing;

import java.io.Serializable;
import java.util.EventObject;

public class SaveEvent extends EventObject implements Serializable {
    String protocolOrMediaAddress;
    public SaveEvent(Object src, String protocolOrMediaAddress) {
        super( src );
        if ( protocolOrMediaAddress == null )
            throw new IllegalArgumentException( "no null argument" );

        this.protocolOrMediaAddress = protocolOrMediaAddress;
    }

    public String getProtocolOrMediaAddress() { return protocolOrMediaAddress; }

    public String toString() { return "media save"; }
}
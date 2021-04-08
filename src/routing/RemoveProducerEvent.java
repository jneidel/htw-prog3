package routing;

import java.io.Serializable;
import java.util.EventObject;

public class RemoveProducerEvent extends EventObject implements Serializable {
    String prodName;
    public RemoveProducerEvent(Object src, String prodName) {
        super( src );
        if ( prodName == null )
            throw new IllegalArgumentException( "no null producer" );
        this.prodName = prodName;
    }

    public String getProducerName() {
        return prodName;
    }

    public String toString() { return "producer remove"; }
}
package routing;

import java.io.Serializable;
import java.util.EventObject;

public class CreateProducerEvent extends EventObject implements Serializable {
    String prodStr;
    public CreateProducerEvent(Object src, String prodStr) {
        super( src );
        if ( prodStr == null )
            throw new IllegalArgumentException( "no null producer" );
        this.prodStr = prodStr;
    }

    public String getProducerStr() {
        return prodStr;
    }

    public String toString() { return "producer create"; }
}
package routing;

import java.io.Serializable;
import java.util.EventObject;

/*
the delete mode of the cli does not differentiate between media and producer delete
*/

public class RemoveMediaOrProducerEvent extends EventObject implements Serializable {
    String name;
    public RemoveMediaOrProducerEvent(Object src, String name) {
        super( src );
        if ( name == null )
            throw new IllegalArgumentException( "no null address" );
        this.name = name;
    }

    public String getName() { return name; }

    public String toString() { return "media_producer remove"; }
}
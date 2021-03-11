package routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;

public class UpdateEvent extends EventObject implements Serializable {
    ArrayList items;
    public UpdateEvent(Object src, ArrayList items) {
        super( src );
        if ( items == null )
            throw new IllegalArgumentException( "no null items" );
        this.items = items;
    }
    public ArrayList getItems() { return items; }
}

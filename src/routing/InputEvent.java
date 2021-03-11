package routing;

import java.io.Serializable;
import java.util.EventObject;

public class InputEvent extends EventObject implements Serializable {
    String[] text;
    public InputEvent(Object src, String[] text) {
       super( src );
       if ( text == null )
           throw new IllegalArgumentException( "no null text" );
       this.text = text;
    }
    public String[] getText() { return this.text; }
}

package routing;

import java.util.EventListener;

public interface InputEventListener extends EventListener {
    void onInputEvent( InputEvent event );
}

package net;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import routing.EventHandler;
import routing.RemoveMediaEvent;

import java.util.EventObject;

public class TCPTest {
    Server server;
    Client client;
    EventHandler handlerMock;

    @BeforeEach void setup() {
        this.handlerMock = mock( EventHandler.class );
        this.server = new TCPServer( handlerMock );
        int port = this.server.init();
        this.server.start();
        this.client = new TCPClient( port );
    }

    @Test void sendEvent() {
        RemoveMediaEvent event = new RemoveMediaEvent( "", "thatMedia" );
        this.client.sendEvent( event );

        // src: https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/ArgumentCaptor.html
        ArgumentCaptor<RemoveMediaEvent> argument = ArgumentCaptor.forClass( RemoveMediaEvent.class );
        verify( this.handlerMock ).handle( argument.capture() );
        assertEquals( event.toString(), argument.getValue().toString() );
        assertEquals( event.getMediaAddress(), argument.getValue().getMediaAddress() );
    }
}

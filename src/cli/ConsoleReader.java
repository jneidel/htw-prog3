package cli;

import routing.InputEvent;
import routing.InputEventHandler;

import java.util.Scanner;

public class ConsoleReader {
    // private InputEventHandler
    public void start() {
        try ( Scanner s = new Scanner( System.in ) ) {
            do {
                System.out.print("$ ");
                String text = s.next();
                // InputEvent e = new InputEvent( this, text );
                // this.handler.handle( e );
            } while( true );
        }
    }
}

package cliTest;

import cli.ConsoleReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import routing.EventHandler;

public class ConsoleReaderTest {
    ConsoleReader cr;

    @BeforeEach void setup() {
        this.cr = new ConsoleReader( (EventHandler) null );
    }

    @Test void parseCreate() {

    }
}
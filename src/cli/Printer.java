package cli;

import java.io.PrintStream;
import java.util.ArrayList;

public class Printer {
    PrintStream stream;
    public Printer( String printStream ) {
        if ( printStream == null || printStream == "stdout" ) {
            this.stream = System.out;
        } else if ( printStream == "stderr" ) {
            this.stream = System.err;
        } else {
            throw new IllegalArgumentException( "passed invalid stream string" );
        }
    }
    public Printer( PrintStream stream ) {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        this.stream = stream;
    }

    public void print( String str ) {
        this.stream.print( str );
    }
    public void println( String str ) {
        this.stream.println( str );
    }
    public void printf( String format, String str ) {
        this.stream.printf( format, str );
    }

    public <T> void print( ArrayList<T> list ) {
        for ( T i : list ) {
            this.stream.println( i );
        }
    }
}
package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Logger {
    FileOutputStream out;
    static String logFileAddress = "log.txt";

    public Logger() {
        try {
            File file = new File( logFileAddress );
            if ( !file.exists() )
                file.createNewFile();

            out = new FileOutputStream( logFileAddress, true );
        } catch ( Exception e ) {
            System.err.println( "io error" );
            e.printStackTrace();
        }
    }
    public void log( String event ) {
        try {
            out.write( (event + "\n").getBytes() );
        } catch ( IOException e ) {
            System.err.println( "io error" );
            e.printStackTrace();
        }
    }
}

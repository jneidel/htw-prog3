import cli.MediaDBObserver;
import fs.FS;
import gl.*;
import routing.EventEmitter;
import routing.EventHandler;
import sim.RemoverSim;
import sim.SimDB;
import sim.UploaderSim;
import util.MediaGenerator;
import gui.Main;

import java.io.*;

public class App {
    public static void main( String[] args ) {
        MediaDB db = new MediaDB();
        db.attachObserver( new MediaDBObserver( db ) );
        Uploader producer = db.createProducer( "jneidel" );

        /* simulation test
        SimDB sdb = new SimDB( db );
        UploaderSim us = new UploaderSim( sdb, producer );
        RemoverSim rs = new RemoverSim( sdb );
        us.start();
        rs.start();
         */

        /* manual test */
        MediaGenerator gen = new MediaGenerator( producer );

        // MediaContent c1 = gen.generate();
        // MediaContent c2 = gen.generate();
        // MediaContent c3 = gen.generate();

        // db.update( c2, c3 );
        // db.delete( c1 );

        /*
        FS fs = new FS();

        final boolean READ = true; // toggle this for manual test
        if ( READ ) {
            db = fs.JOSreadDB();
            System.out.println( db.list() );
        } else {
            db.upload( c1 );
            db.upload( c2 );

            fs.JOSwriteDB( db ); // writes JOS to 'data' file
        }
        */

        /* gui */
        // new Main().run();

        /* net */
        File file = new File( "stream" );
        try {
            file.createNewFile();

            FileOutputStream rawOutputStream = new FileOutputStream( file );
            FileInputStream rawInputStream = new FileInputStream( file );

            ObjectOutputStream outputStream = new ObjectOutputStream( rawOutputStream );
            ObjectInputStream inputStream = new ObjectInputStream( rawInputStream );

            EventEmitter emitter = new EventEmitter( outputStream );
            EventHandler handler = new EventHandler( db, inputStream );
            emitter.start();
            handler.start();
        } catch( Exception e ) {
            e.printStackTrace();
            System.exit( 187 );
        }
    }
}
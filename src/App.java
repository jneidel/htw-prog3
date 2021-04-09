import gui.GUI;

public class App {
    public static void main( String[] args ) {
        GUI.inputArgs = args;
        new GUI().run(); // new main method

        /* simulation test
        SimDB sdb = new SimDB( db );
        UploaderSim us = new UploaderSim( sdb, producer );
        RemoverSim rs = new RemoverSim( sdb );
        us.start();
        rs.start();
         */

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
    }
}
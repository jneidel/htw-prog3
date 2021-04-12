import cli.MediaDBObserver;
import gl.MediaDB;
import gui.GUI;
import routing.EventHandler;
import routing.GLEventListener;
import sim.IncrementSim;
import sim.RemoverSim;
import sim.UploaderSim;

import java.math.BigDecimal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main( String[] args ) {
        GUI.inputArgs = args;
        new GUI().run(); // new main method

        // sim
        /*
        MediaDB simTestDB = new MediaDB( new BigDecimal( 120 ) ); // items are of size 20
        EventHandler handler = new EventHandler();
        handler.add( new GLEventListener( simTestDB ) );
        simTestDB.attachObserver( new MediaDBObserver( simTestDB ) );

        final Lock lock = new ReentrantLock();
        final Condition full = lock.newCondition();
        final Condition removing = lock.newCondition();

        UploaderSim us = new UploaderSim( simTestDB, handler, lock, full, removing, simTestDB.createProducer("up1") );
        UploaderSim us2 = new UploaderSim( simTestDB, handler, lock, full, removing, simTestDB.createProducer( "up2") );
        RemoverSim rs = new RemoverSim( simTestDB, handler, lock, full, removing );
        RemoverSim rs2 = new RemoverSim( simTestDB, handler, lock, full, removing );
        IncrementSim is = new IncrementSim( simTestDB, handler, lock );
        us.start();
        rs.start();
        is.start();
        us2.start();
        rs2.start();
        */
    }
}
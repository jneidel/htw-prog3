package routing;

import java.io.ObjectOutputStream;

public class EventEmitter extends Thread {
    ObjectOutputStream stream; 
    public EventEmitter( ObjectOutputStream out ) {
        this.stream = out;
    }
    
    public void run() {
        //while( true ) {
        System.out.println( "emitter running" );

            String[] bsp = "create Audio song.aac 320 0:2:45 aac 44000".split( " " );
            String[] bsp2 = "create AudioVideo movie.mp4 320 1:30:11 mp4 640 480 mp3 44000".split( " " );
            String[] bsp3 = "create InteractiveVideo interactive_vid.mkv 320 0:1:15 mkv 640 480 Abstimmung".split( " " );

            InputEvent event = new InputEvent( this, bsp );
            InputEvent event2 = new InputEvent( this, bsp2 );
            InputEvent event3 = new InputEvent( this, bsp3 );

            try {
                stream.writeObject(event);
                stream.flush();
                Thread.sleep( 500 );

                stream.writeObject(event2);
                stream.flush();
                Thread.sleep( 3000 );

                stream.writeObject(event3);
                stream.flush();

                Thread.sleep( 3000 );
                System.exit( 187 );
            } catch( Exception e ) {
                System.out.println( e );
            }
        // }
    }
}

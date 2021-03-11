package routing;

import mediaDB.MediaContent;
import mediaDB.MediaDB;
import cli.Parser;

import java.util.Arrays;

public class CreateInputEventListener implements InputEventListener {
    MediaDB db;
    Parser parser;

    CreateInputEventListener( MediaDB db ) {
        this.db = db;
        this.parser = new cli.Parser( this.db.createProducer( "event listener" ) );
    }

    public void onInputEvent( InputEvent event ) {
        String[] args = event.getText();
        if ( args[0].equals("create") ) {
            String[] parserArgs = Arrays.copyOfRange( args, 1, args.length ); // shift out 'create'
            MediaContent res = this.parser.parseCreate( parserArgs );
            db.upload( res );
        }
    }
}

package gui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import gl.MediaContent;
import gl.MediaDB;
import util.MediaGenerator;

public class GUI extends Application {
    public void start(Stage primaryStage) throws Exception {
        MediaDB db = new MediaDB();
        db.attachObserver( new cli.MediaDBObserver( db ) ); // logging

        FXMLLoader loader = new FXMLLoader( getClass().getResource("app.fxml" ) );
        Parent root = (Parent) loader.load();

        // src: https://stackoverflow.com/a/14432578
        Controller controller = loader.<Controller>getController();
        controller.setDb( db );

        gui.MediaDBObserver observer = new gui.MediaDBObserver( db, controller );
        db.attachObserver( observer );

        primaryStage.setTitle( "prog3_beleg" );
        Scene scene =  new Scene( root, 500, 575 );

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            // src: https://stackoverflow.com/a/42303881
            public void handle(KeyEvent ke) {
                if ( ke.getCode() == KeyCode.DELETE ) {
                    controller.removeSelection();
                    ke.consume();
                }
            }
        });

        primaryStage.setScene( scene );
        primaryStage.show();

        // generate examples
        MediaGenerator gen = new MediaGenerator( db.createProducer( "generator" ) );
        MediaContent c1 = gen.generate();
        MediaContent c2 = gen.generate();
        MediaContent c3 = gen.generate();
        MediaContent c4 = gen.generate();
        db.upload( c1 );
        db.upload( c2 );
        db.upload( c3 );
        db.upload( c4 );
    }
    public void run() {
        launch();
    }
}
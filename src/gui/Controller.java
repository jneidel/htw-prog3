package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import gl.MediaContent;
import gl.MediaDB;
import gl.UploaderI;
import cli.Parser;
import util.IllegalNumberOfArgumentsException;

public class Controller {
    MediaDB db;
    UploaderI producer;
    Parser parser;

    public void setProducer( UploaderI prod ) { this.producer = prod; }
    public void setDb( MediaDB db ) {
        this.db = db;
        this.parser = new Parser( this.db.createProducer( "manual" ) );
    }

    @FXML
    private Label lastAction;
    @FXML
    private ListView itemList;
    @FXML
    private TextField uploadField;

    // called by observer
    public void setList( ArrayList list ) {
        this.itemList.getItems().setAll( list );
    }
    public void setStatus( String status ) {
        this.lastAction.setText( status );
    }

    // on Entf
    public void removeSelection() {
        List selected = this.itemList.getSelectionModel().getSelectedItems();
        this.db.delete( (MediaContent) selected.get(0) );
    }

    @FXML
    private void handleUploadField( KeyEvent ke ) {
        if ( ke.getCode() == KeyCode.ENTER ) {
            String text = this.uploadField.getText();

            try {
                MediaContent res = this.parser.parseCreate( text.split(" ") );
                db.upload( res );
            } catch(IllegalNumberOfArgumentsException e) {}
            catch (RuntimeException e){}
            // i don't have a way to let the user know
        }
    }
}
package gui;

import gl.Tag;
import gui.comparators.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

import gl.MediaContent;
import gl.Uploader;
import javafx.scene.input.MouseEvent;
import routing.*;
import util.IllegalNumberOfArgumentsException;
import util.Parser;

public class Controller {
    Parser parser = new Parser();
    EventHandler handler;
    public void setHandler( EventHandler handler ) {
        this.handler = handler;
    }

    @FXML
    private TextField uploadField;

    // main table
    @FXML
    private TableView<MediaContentBean> tableView;
    @FXML
    private TableColumn<MediaContentBean, String> typeColumn;
    @FXML
    private TableColumn<MediaContentBean, String> addressColumn;
    @FXML
    private TableColumn<MediaContentBean, String> producerColumn;
    @FXML
    private TableColumn<MediaContentBean, Long> bitrateColumn;
    @FXML
    private TableColumn<MediaContentBean, String> durationColumn;
    @FXML
    private TableColumn<MediaContentBean, Double> sizeColumn;
    @FXML
    private TableColumn<MediaContentBean, Integer> samplingRateColumn;
    @FXML
    private TableColumn<MediaContentBean, String> audioColumn;
    @FXML
    private TableColumn<MediaContentBean, String> videoEncColumn;
    @FXML
    private TableColumn<MediaContentBean, Integer> heightColumn;
    @FXML
    private TableColumn<MediaContentBean, Integer> widthColumn;
    @FXML
    private TableColumn<MediaContentBean, String> holderColumn;
    @FXML
    private TableColumn<MediaContentBean, String> interactiveTypeColumn;
    @FXML
    private TableColumn<MediaContentBean, Long> accessColumn;

    @FXML
    private ComboBox<Comparator<MediaContentBean>> sortingBox;
    private ObservableList<Comparator<MediaContentBean>> contentComparators;

    ObservableList<MediaContentBean> content = FXCollections.observableArrayList(MediaContentBean.extractor());
    SortedList<MediaContentBean> sortedContent = content.sorted();
    @FXML
    public void onSort() {
        this.sortedContent.setComparator(sortingBox.getSelectionModel().getSelectedItem());
    }

    public void initialize() {
        tableView.setItems( sortedContent );

        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.producerColumn.setCellValueFactory(new PropertyValueFactory<>("producer"));
        this.bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        this.durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        this.sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        this.samplingRateColumn.setCellValueFactory(new PropertyValueFactory<>("samplingRate"));
        this.audioColumn.setCellValueFactory(new PropertyValueFactory<>("audio"));
        this.videoEncColumn.setCellValueFactory(new PropertyValueFactory<>("videoEnc"));
        this.heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        this.widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        this.holderColumn.setCellValueFactory(new PropertyValueFactory<>("holder"));
        this.interactiveTypeColumn.setCellValueFactory(new PropertyValueFactory<>("interactiveType"));
        this.accessColumn.setCellValueFactory(new PropertyValueFactory<>("access"));

        this.contentComparators = FXCollections.observableArrayList(
                new TypeComparator(),
                new AddressComparator(),
                new ProducerComparator(),
                new BitrateComparator(),
                new DurationComparator(),
                new SizeComparator(),
                new SamplingRateComparator(),
                new AudioEncComparator(),
                new VideoEncComparator(),
                new WidthComparator(),
                new HeightComparator(),
                new HolderComparator(),
                new InteractiveTypeComparator(),
                new AccessComparator()
        );
        this.sortingBox.setItems( this.contentComparators );

        // producers
        producerTable.setItems( this.producers );
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        // remove selection on one table when selecting the other - for delete
        // src: https://stackoverflow.com/a/32436627
        tableView.setOnMousePressed(new javafx.event.EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent me){ producerTable.getSelectionModel().clearSelection(); }
        });
        producerTable.setOnMousePressed(new javafx.event.EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent me){ tableView.getSelectionModel().clearSelection(); }
        });
    }

    public void updateProperties() {

    }

    // producers
    @FXML
    private TableView<ProducerBean> producerTable;
    @FXML
    private TableColumn<ProducerBean, String> nameColumn;
    @FXML
    private TableColumn<ProducerBean, Integer> countColumn;
    ObservableList<ProducerBean> producers = FXCollections.observableArrayList(ProducerBean.extractor());

    // tags
    @FXML private Label tagsUsed;
    @FXML private Label tagsUnused;

    // called by MediaDBObserver
    public void setList(List<MediaContent> list, List<Uploader> producerList) {
        // javafx stuff can be done from different thread unless using runnable
        // src: https://stackoverflow.com/a/32489845
        Platform.runLater(new Runnable() {
            public void run() {
                content.clear();
                for ( MediaContent m : list )
                    content.add( new MediaContentBean( m ) );

                producers.clear();
                for ( Uploader p : producerList )
                    producers.add( new ProducerBean( p ) );
            }
        });
    }

    @FXML
    private Label lastAction;
    public void setStatus( String status ) {
        Platform.runLater(new Runnable() {
            public void run() { lastAction.setText( status); }
        });
    }

    @FXML private Button saveJOS;
    @FXML private Button loadJOS;
    @FXML private TextField saveInstance;
    @FXML private TextField loadInstance;
    @FXML private TextField createProducer;

    // on Entf
    public void removeSelection() {
        TableView.TableViewSelectionModel<MediaContentBean> tableViewSelectionModel = this.tableView.getSelectionModel();
        TableView.TableViewSelectionModel<ProducerBean> producerBeanTableViewSelectionModel = this.producerTable.getSelectionModel();

        if ( !tableViewSelectionModel.isEmpty() ) {
            MediaContent selected = (MediaContent) this.tableView.getSelectionModel().getSelectedItems().get(0).src;
            this.handler.handle( new RemoveMediaEvent( selected, selected.getAddress() ) );
        } else if ( !producerBeanTableViewSelectionModel.isEmpty() ) {
            Uploader selected = (Uploader) this.producerTable.getSelectionModel().getSelectedItems().get(0).src;
            this.handler.handle( new RemoveProducerEvent( selected, selected.getName() ) );
        }
    }

    @FXML private void handleUploadField( KeyEvent ke ) {
        if ( ke.getCode() == KeyCode.ENTER ) {
            try {
                TextField field = (TextField) ke.getSource();
                String text = this.uploadField.getText();
                EventObject event = this.parser.parseMediaStrToEvent( text );
                this.handler.handle( event );
            } catch (RuntimeException e){}
        }
    }
    @FXML private void handleInstance( KeyEvent ke ) {
        if ( ke.getCode() == KeyCode.ENTER ) {
            try {
                TextField field = (TextField) ke.getSource();
                EventObject event = null;

                if ( field == saveInstance ) {
                    String text = this.saveInstance.getText();
                    event = new SaveEvent( text, text );
                } else if ( field == loadInstance ) {
                    String text = this.loadInstance.getText();
                    event = new LoadEvent( text, text );
                }

                this.handler.handle( event );
            } catch (RuntimeException e){
                e.printStackTrace();
            }
        }
    }
    @FXML private void handleCreateProducer( KeyEvent ke ) {
        if ( ke.getCode() == KeyCode.ENTER ) {
            try {
                TextField field = (TextField) ke.getSource();
                String text = this.createProducer.getText();
                EventObject event = new CreateProducerEvent( text, text );
                this.handler.handle( event );
            } catch (RuntimeException e){
                e.printStackTrace();
            }
        }
    }
    @FXML private void handleSaveJOS() {
        SaveEvent event = new SaveEvent( "", "jos" );
        this.handler.handle( event );
    }
    @FXML private void handleLoadJOS() {
        LoadEvent event = new LoadEvent( "", "jos" );
        this.handler.handle( event );
    }
}
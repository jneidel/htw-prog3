package gui;

import gui.comparators.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import gl.MediaContent;
import gl.MediaDB;
import gl.Uploader;
import cli.Parser;
import util.IllegalNumberOfArgumentsException;

public class Controller {
    MediaDB db;
    Uploader producer;
    Parser parser;

    public void setProducer( Uploader prod ) { this.producer = prod; }
    public void setDb( MediaDB db ) {
        this.db = db;
        this.parser = new Parser( this.db.createProducer( "gui manual" ) );
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
    }

    public void updateProperties() {
        this.content.clear();
        for ( MediaContent m : this.db.list() )
            this.content.add( new MediaContentBean( m ) );

        this.producers.clear();
        for ( Uploader p : this.db.getProducers() )
            this.producers.add( new ProducerBean( p ) );
    }

    // producers
    @FXML
    private TableView<ProducerBean> producerTable;
    @FXML
    private TableColumn<ProducerBean, String> nameColumn;
    @FXML
    private TableColumn<ProducerBean, Integer> countColumn;
    ObservableList<ProducerBean> producers = FXCollections.observableArrayList(ProducerBean.extractor());

    // called by observer
    public void setList( ArrayList list ) {
        this.updateProperties();
    }

    @FXML
    private Label lastAction;

    public void setStatus( String status ) {
        this.lastAction.setText( status );
    }

    // on Entf
    public void removeSelection() {
        ObservableList<MediaContentBean> selected = this.tableView.getSelectionModel().getSelectedItems();
        this.db.delete( (MediaContent) selected.get(0).src );
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
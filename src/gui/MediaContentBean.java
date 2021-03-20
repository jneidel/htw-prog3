package gui;

import gl.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;

public class MediaContentBean {
    MediaContent src;

    public MediaContentBean( MediaContent underlyingMediaContent ) {
        this.src = underlyingMediaContent;
        this.setProps();
    }

    private void setProps() {
        this.type = new SimpleStringProperty( this.src.getClassName() );
        this.address = new SimpleStringProperty( this.src.getAddress() );
        this.producer = new SimpleStringProperty( this.src.getUploader().getName() );
        this.bitrate = new SimpleLongProperty( this.src.getBitrate() );
        this.duration = new SimpleStringProperty( this.src.getLength().toString() );
        this.size = new SimpleDoubleProperty( this.src.getSize().doubleValue() );
        try { // audio
            this.samplingRate = new SimpleIntegerProperty(((Audio) this.src).getSamplingRate());
            this.audio = new SimpleStringProperty(((Audio) this.src).getEncoding());
            this.holder = new SimpleStringProperty(((LicensedAudio) this.src).getHolder());
        } catch( Exception e ) {}
        try { // video
            this.videoEnc = new SimpleStringProperty(((Video) this.src).getEncoding());
            this.height = new SimpleIntegerProperty(((Video) this.src).getHeight());
            this.width = new SimpleIntegerProperty(((Video) this.src).getWidth());
            this.holder = new SimpleStringProperty(((LicensedVideo) this.src).getHolder());
        } catch( Exception e ) {}
        try { // audio video
            this.samplingRate = new SimpleIntegerProperty(((AudioVideo) this.src).getSamplingRate());
            this.audio = new SimpleStringProperty(((AudioVideo) this.src).getEncoding());
            this.holder = new SimpleStringProperty(((LicensedAudioVideo) this.src).getHolder());
        } catch( Exception e ) {}
    }

    private StringProperty type = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty producer = new SimpleStringProperty();
    private LongProperty bitrate = new SimpleLongProperty();
    private StringProperty duration = new SimpleStringProperty();
    private DoubleProperty size = new SimpleDoubleProperty();
    private IntegerProperty samplingRate = new SimpleIntegerProperty();
    private StringProperty audio = new SimpleStringProperty();
    private StringProperty videoEnc = new SimpleStringProperty();
    private IntegerProperty height = new SimpleIntegerProperty();
    private IntegerProperty width = new SimpleIntegerProperty();
    private StringProperty holder = new SimpleStringProperty();

    public StringProperty typeProperty() { return this.type; }
    public StringProperty addressProperty() { return this.address; }
    public StringProperty producerProperty() { return this.producer; }
    public LongProperty bitrateProperty() { return this.bitrate; }
    public StringProperty durationProperty() { return this.duration; }
    public DoubleProperty sizeProperty() { return this.size; }
    public IntegerProperty samplingRateProperty() { return this.samplingRate; }
    public StringProperty audioProperty() { return this.audio; }
    public StringProperty videoEncProperty() { return this.videoEnc; }
    public IntegerProperty heightProperty() { return this.height; }
    public IntegerProperty widthProperty() { return this.width; }
    public StringProperty holderProperty() { return this.holder; }

    public static Callback<MediaContentBean, Observable[]> extractor() {
        return (MediaContentBean m) -> new Observable[]{
                m.typeProperty(),
                m.addressProperty(),
                m.producerProperty(),
                m.bitrateProperty(),
                m.durationProperty(),
                m.sizeProperty(),
                m.samplingRateProperty(),
                m.audioProperty(),
                m.videoEncProperty(),
                m.heightProperty(),
                m.widthProperty(),
                m.holderProperty()
        };
    }
}

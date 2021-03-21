package gui;

import gl.Uploader;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

public class ProducerBean {
    public Uploader src;

    public ProducerBean( Uploader underlyingProducer ) {
        this.src = underlyingProducer;
        this.name = new SimpleStringProperty( underlyingProducer.getName() );
        this.count = new SimpleIntegerProperty( underlyingProducer.getCount() );
    }

    private StringProperty name;
    private IntegerProperty count;

    public StringProperty nameProperty() { return this.name; }
    public IntegerProperty countProperty() { return this.count; }

    public static Callback<ProducerBean, Observable[]> extractor() {
        return (ProducerBean p) -> new Observable[]{
                p.nameProperty(),
                p.countProperty()
        };
    }
}

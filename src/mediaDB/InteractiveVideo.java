package mediaDB;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface InteractiveVideoI extends VideoI,InteractiveI,Serializable {}

public class InteractiveVideo extends Video implements InteractiveVideoI {
    static final long serialVersionUID = 1L;
    Interactive interactive;

    public InteractiveVideo( String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int width, int height, String encoding, String type ) {
        super(address, bitrate, length, size, uploader, width, height, encoding);
        this.interactive = new Interactive( address, type );
    }

    public String getType() {
        return this.interactive.getType();
    }
}

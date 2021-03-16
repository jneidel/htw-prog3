package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface MediaContentI extends ContentI,Serializable {
    long getBitrate();
    Duration getLength();
    BigDecimal getSize();
}

public class MediaContent extends Content implements MediaContentI {
    long bitrate;
    Duration length;
    BigDecimal size;

    public MediaContent( String address, UploaderI uploader, long bitrate, Duration length, BigDecimal size ) {
        super( address, uploader );
        this.bitrate = bitrate;
        this.length = length;
        this.size = size;
    }

    public long getBitrate() {
        return this.bitrate;
    }

    public Duration getLength() {
        return this.length;
    }

    public BigDecimal getSize() { return this.size; }
}

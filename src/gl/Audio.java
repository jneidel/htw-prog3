package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

interface AudioI extends MediaContentI,UploadableI,Serializable {
    int getSamplingRate();
    String getEncoding();
}

public class Audio extends MediaContent implements AudioI {
    static final long serialVersionUID = 1L;

    int samplingRate;
    String encoding;

    public Audio( String address, long bitrate, Duration length, BigDecimal size, Uploader uploader, int samplingRate, String encoding ) {
        super( address, uploader, bitrate, length, size );
        this.samplingRate = samplingRate;
        this.encoding = encoding;
    }

    public int getSamplingRate() {
        return this.samplingRate;
    }

    public String getEncoding() {
        return this.encoding;
    }
}

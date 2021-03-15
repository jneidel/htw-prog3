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
    private Uploadable uploadable;

    int samplingRate;
    String encoding;

    public Audio( String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int samplingRate, String encoding ) {
        super( address, bitrate, length, size );
        Uploadable uploadable = new Uploadable( uploader );
        this.samplingRate = samplingRate;
        this.encoding = encoding;
    }

    public UploaderI getUploader() {
        return this.uploadable.getUploader();
    }
    public Date getUploadDate() {
        return this.uploadable.getUploadDate();
    }

    public int getSamplingRate() {
        return this.samplingRate;
    }

    public String getEncoding() {
        return this.encoding;
    }
}

package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

interface VideoI extends MediaContentI,UploadableI,Serializable {
    int getWidth();
    int getHeight();
    String getEncoding();
}

public class Video extends MediaContent implements VideoI {
    static final long serialVersionUID = 1L;
    private Uploadable uploadable;

    int width;
    int height;
    String encoding;

    public Video(String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int width, int height, String encoding) {
        super(address, bitrate, length, size);
        Uploadable uploadable = new Uploadable( uploader );
        this.width = width;
        this.height = height;
        this.encoding = encoding;
    }

    public UploaderI getUploader() {
        return this.uploadable.getUploader();
    }
    public Date getUploadDate() {
        return this.uploadable.getUploadDate();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getEncoding() {
        return this.encoding;
    }
}

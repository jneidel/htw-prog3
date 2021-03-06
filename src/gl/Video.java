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

    int width;
    int height;
    String encoding;

    public Video() {}
    public Video(String address, long bitrate, Duration length, BigDecimal size, Uploader uploader, int width, int height, String encoding) {
        super(address, uploader, bitrate, length, size);
        Uploadable uploadable = new Uploadable( uploader );
        this.width = width;
        this.height = height;
        this.encoding = encoding;
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

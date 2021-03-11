package mediaDB;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface LicensedVideoI extends LicensedI,VideoI,Serializable {}

public class LicensedVideo extends Video implements LicensedVideoI {
    static final long serialVersionUID = 1L;
    Licensed licensed;

    public LicensedVideo(String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int width, int height, String encoding, String holderName) {
       super( address, bitrate, length, size, uploader, width, height, encoding );
       this.licensed = new Licensed( address, holderName );
    }

    public String getHolder() {
        return this.licensed.getHolder();
    }
}

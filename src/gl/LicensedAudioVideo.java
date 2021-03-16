package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface LicensedAudioVideoI extends LicensedI,AudioVideoI,Serializable {}

public class LicensedAudioVideo extends AudioVideo implements LicensedAudioVideoI {
    static final long serialVersionUID = 1L;
    Licensed licensed;

    public LicensedAudioVideo(String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int samplingRate, String audioEncoding, int width, int height, String videoEncoding, String holderName ) {
        super(address, bitrate, length, size, uploader, samplingRate, audioEncoding, width, height, videoEncoding);
        this.licensed = new Licensed( address, uploader, holderName );
    }

    public String getHolder() {
        return this.licensed.getHolder();
    }
}

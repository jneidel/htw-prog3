package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface LicensedAudioI extends LicensedI,AudioI,Serializable {}

public class LicensedAudio extends Audio implements LicensedAudioI {
    static final long serialVersionUID = 1L;
    Licensed licensed;

    public LicensedAudio() {}
    public LicensedAudio( String address, long bitrate, Duration length, BigDecimal size, Uploader uploader, int samplingRate, String encoding, String holderName ) {
        super( address, bitrate, length, size, uploader, samplingRate, encoding );
        this.licensed = new Licensed( address, uploader, holderName );
    }

    public String getHolder() {
        return this.licensed.getHolder();
    }
}

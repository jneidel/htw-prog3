package gl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

interface AudioVideoI extends AudioI,VideoI,Serializable {}

public class AudioVideo extends Video implements AudioVideoI {
    static final long serialVersionUID = 1L;
    Audio audio;

    public AudioVideo(String address, long bitrate, Duration length, BigDecimal size, UploaderI uploader, int samplingRate, String audioEncoding, int width, int height, String videoEncoding) {
        super( address, bitrate, length, size, uploader, width, height, videoEncoding );
        this.audio = new Audio( address, bitrate, length, size, uploader, samplingRate, audioEncoding );
    }

    public int getSamplingRate() {
        return this.audio.getSamplingRate();
    }
    public String getAudioEncoding() {
        return this.audio.getEncoding();
    }
}

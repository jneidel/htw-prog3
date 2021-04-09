package util;

import gl.*;
import org.junit.jupiter.api.BeforeEach;
import routing.UploadMediaEvent;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

public class ParserTest {
    Parser parser = new Parser();
    MediaDB db;
    Uploader prod;

    @BeforeEach void setup() {
        this.db = new MediaDB();
        this.prod = db.createProducer( "prod" );
    }

    @Test void parseMediaStrToMediaContent() { // happy path
        String args = "Audio prod song.aac 4000 News 320 30 AAC 44000";

        Audio res = (Audio) this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "prod", res.getUploader().getName() );
        assertEquals( "song.aac", res.getAddress() );
        assertEquals( new BigDecimal( 4000 ), res.getSize() );
        assertTrue( res.getTags().contains( Tag.News ) );
        assertEquals( 320L, res.getBitrate() );
        assertEquals( Duration.ofSeconds( 30 ), res.getLength() );
        assertEquals( "AAC", res.getEncoding() );
        assertEquals( 44000, res.getSamplingRate() );
    }

    @Test void parseMediaStrToEvent_Audio() {
        String args = "Audio prod song.aac 4000 News 320 30 AAC 44000";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }
    @Test void parseMediaStrToEvent_Audio_InvalidSize() {
        String args = "Audio prod song.aac NOTASIZE News 320 30 AAC 44000";

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.parser.parseMediaStrToEvent( args )
        );
        assertEquals( "invalid size: NOTASIZE", e.getMessage() );
    }
    @Test void parseMediaStrToEvent_Audio_InvalidTag() {
        String args = "Audio prod song.aac 5000 NOTATAG 320 30 AAC 44000";

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.parser.parseMediaStrToEvent( args )
        );
        assertEquals( "invalid tag: NOTATAG", e.getMessage() );
    }
    @Test void parseMediaStrToEvent_Audio_InvalidBitRate() {
        String args = "Audio prod song.aac 5000 , NOTABITRATE 30 AAC 44000";

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.parser.parseMediaStrToEvent( args )
        );
        assertEquals( "invalid bitrate: NOTABITRATE", e.getMessage() );
    }
    @Test void parseMediaStrToEvent_Audio_InvalidLength() {
        String args = "Audio prod song.aac 5000 , 320 NOTALENGTH AAC 44000";

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.parser.parseMediaStrToEvent( args )
        );
        assertEquals( "invalid length: NOTALENGTH", e.getMessage() );
    }
    @Test void parseMediaStrToEvent_Audio_InvalidSamplingRate() {
        String args = "Audio prod song.aac 5000 , 320 30 AAC NOTARATE";

        Exception e = assertThrows( IllegalArgumentException.class, () ->
                this.parser.parseMediaStrToEvent( args )
        );
        assertEquals( "invalid sampling rate: NOTARATE", e.getMessage() );
    }

    @Test void parseMediaStringToMediaContent_AudioVideo() {
        String args = "AudioVideo prod movie.mp4 4000 Lifestyle 320 200 MP4 720 480 44000";
        MediaContent res = this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "movie.mp4", res.getAddress() );
    }
    @Test void parseMediaStrToEvent_AudioVideo() {
        String args = "AudioVideo prod movie.mp4 4000 Lifestyle 320 200 MP4 720 480 44000";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }

    @Test void parseMediaStringToMediaContent_InteractiveVideo() {
        String args = "InteractiveVideo prod interactive_vid.mkv 8000 , 320 80 MKV 640 480 Performance";
        MediaContent res = this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "interactive_vid.mkv", res.getAddress() );
    }
    @Test void parseMediaStrToEvent_InteractiveVideo() {
        String args = "InteractiveVideo prod interactive_vid.mkv 8000 , 320 80 MKV 640 480 Performance";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }

    @Test void parseMediaStringToMediaContent_LicensedAudio() {
        String args = "LicensedAudio prod song.mp3 2000 , 320 800 MP3 44000 UMG";
        MediaContent res = this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "song.mp3", res.getAddress() );
    }
    @Test void parseMediaStrToEvent_LicensedAudio() {
        String args = "LicensedAudio prod song.mp3 2000 , 320 800 MP3 44000 UMG";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }
    @Test void parseMediaStringToMediaContent_LicensedAudioVideo() {
        String args = "LicensedAudioVideo prod music_video.mp4 16000 , 320 40 MP4 1080 720 44000 Warner";
        MediaContent res = this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "music_video.mp4", res.getAddress() );
    }
    @Test void parseMediaStrToEvent_LicensedAudioVideo() {
        String args = "LicensedAudioVideo prod music_video.mp4 16000 , 320 40 MP4 1080 720 44000 Warner";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }
    @Test void parseMediaStringToMediaContent_LicensedVideo() {
        String args = "LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner";
        MediaContent res = this.parser.parseMediaStrToMediaContent( args, db );

        assertEquals( "movie.mp4", res.getAddress() );
    }
    @Test void parseMediaStrToEvent_LicensedVideo() {
        String args = "LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner";
        UploadMediaEvent res = this.parser.parseMediaStrToEvent( args );

        assertEquals( args, res.getMediaStr() );
    }
}

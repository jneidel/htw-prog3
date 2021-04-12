package util;

import gl.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class MediaGenerator {
    static String[] audioExtensions = { "mp3", "aac", "opus" };
    static String[] videoExtensions = { "mp4", "mkv" };
    static Number[] bitrates = { 320000, 124000, 50000 };
    static Number[] samplingRates = { 44000, 22000 };
    static BigDecimal[] sizes = { new BigDecimal( "20" ), new BigDecimal( "10" ) };
    static Number[] widths = { 480, 720, 1080, 1920 };
    static String[] licenseHolders = { "Universal", "BMG", "GPLv3" };
    static LocalTime now = LocalTime.now();
    static Duration[] durations = { Duration.between( now, now.plusMinutes( 20 ) ), Duration.between( now, now.plusMinutes( 60 ) ) };

    static Random rand = new Random();

    Uploader producer;
    public MediaGenerator( Uploader producer ) {
        this.producer = producer;
    }

    // pick random entry of array
    private <T> T random(T[] array) {
        int randomIndex = this.rand.nextInt( array.length );
        return array[randomIndex];
    }

    private String generateAddress() {
        // src: https://www.baeldung.com/java-random-string
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        String address = this.rand.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return address;
    }

    Audio generateAudio() {
        String ext = random( audioExtensions );
        return new Audio( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( samplingRates ), ext.toUpperCase() );
    }
    AudioVideo generateAudioVideo() {
        String ext = random( videoExtensions );
        return new AudioVideo( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( samplingRates ), random( audioExtensions ).toUpperCase(), (int) random( widths ), (int) random( widths ), ext.toUpperCase() );
    }
    InteractiveVideo generateInteractiveVideo() {
        String ext = random( videoExtensions );
        return new InteractiveVideo( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( widths ), (int) random( widths ), ext.toUpperCase(), "Interactive" );
    }
    LicensedAudio generateLicesedAudio() {
        String ext = random( audioExtensions );
        return new LicensedAudio( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( samplingRates ), ext.toUpperCase(), random( licenseHolders ) );
    }
    LicensedAudioVideo generateLicesedAudioVideo() {
        String ext = random( videoExtensions );
        return new LicensedAudioVideo( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( samplingRates ), random( audioExtensions ).toUpperCase(), (int) random( widths ), (int) random( widths ), ext.toUpperCase(), random( licenseHolders ) );
    }
    LicensedVideo generateLicesedVideo() {
        String ext = random( videoExtensions );
        return new LicensedVideo( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( widths ), (int) random( widths ), ext.toUpperCase(), random( licenseHolders ));
    }
    Video generateVideo() {
        String ext = random( videoExtensions );
        return new Video( generateAddress() + '.' + ext, random( bitrates ).longValue(),  random( durations ), random( sizes ), this.producer, (int) random( widths ), (int) random( widths ), ext.toUpperCase() );
    }

    String generateAudioStr() {
        return String.join( " ",
                "Audio", producer.getName(),
                generateAddress(),
                "20 News 320 30 AAC 44000"
        );
    }
    String generateAudioVideoStr() {
        return String.join( " ",
                "AudioVideo", producer.getName(),
                generateAddress(),
                "20 Lifestyle 320 200 MP4 720 480 44000"
        );
    }
    String generateInteractiveVideoStr() {
        return String.join( " ",
                "InteractiveVideo", producer.getName(),
                generateAddress(),
                "20 , 320 80 MKV 640 480 P"
        );
    }
    String generateLicesedAudioStr() {
        return String.join( " ",
                "LicensedAudio", producer.getName(),
                generateAddress(),
                "20 , 320 800 MP3 44000 UMG"
        );
    }
    String generateLicesedAudioVideoStr() {
        return String.join( " ",
                "LicensedAudioVideo", producer.getName(),
                generateAddress(),
                "20 , 320 40 MP4 1080 720 44000 Warner"
        );
    }
    String generateLicesedVideoStr() {
        return String.join( " ",
                "LicensedVideo", producer.getName(),
                generateAddress(),
                "20 Animal,Lifestyle 320 45 MP4 720 480 Warner"
        );
    }
    String generateVideoStr() {
        return String.join( " ",
                "Video", producer.getName(),
                generateAddress(),
                "20 Animal 320 200 MP4 720 480"
        );
    }

    public MediaContent generate() {
        switch ( rand.nextInt( 7 ) ) { // number of functions
            case 0: return generateAudio();
            case 1: return generateAudioVideo();
            case 2: return generateInteractiveVideo();
            case 3: return generateLicesedAudio();
            case 4: return generateLicesedAudioVideo();
            case 5: return generateLicesedVideo();
            case 6: return generateVideo();
            default: return null; // java complains
        }
    }
    public String generateStr() {
        switch ( rand.nextInt( 7 ) ) { // number of functions
            case 0: return generateAudioStr();
            case 1: return generateAudioVideoStr();
            case 2: return generateInteractiveVideoStr();
            case 3: return generateLicesedAudioStr();
            case 4: return generateLicesedAudioVideoStr();
            case 5: return generateLicesedVideoStr();
            case 6: return generateVideoStr();
            default: return null; // java complains
        }
    }
}

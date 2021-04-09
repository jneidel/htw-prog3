package gui;

import static org.junit.jupiter.api.Assertions.*;

import gl.*;
import gui.comparators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;

public class ComparatorTests {
    MediaDB db;
    Uploader producer;

    MediaContent sampleItem;
    String sampleAddress = "ttt";
    long sampleBitrate = 1L;
    BigDecimal sampleSize = new BigDecimal( 1000 ); // 1mb
    Duration sampleDuration = Duration.ofSeconds( 0 + 1 * 60 + 0 * 60 * 60); // 1min
    int sampleSamplingRate = 32000;
    String sampleAudioEnc = "MP3";
    String sampleVideoEnc = "MP4";
    String sampleHolder = "UMG";
    int sampleHeight = 720;
    int sampleWidth = 1080;

    @BeforeEach
    void setup() {
        this.db = new MediaDB();
        this.producer = this.db.createProducer( "default" );
        this.sampleItem = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
    }

    @Test
    void compareAddress() {
        MediaContent ac = new MediaContent( "ttt", this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
        MediaContent bc = new MediaContent( "aaa", this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new AddressComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // ttt > aaa
        assertTrue( c.compare( bb, ab ) < 0 ); // aaa < ttt
    }
    @Test
    void compareAddressSameAddress() {
        MediaContent ac = this.sampleItem;
        MediaContent bc = this.sampleItem;
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new AddressComparator();
        assertTrue( c.compare( ab, bb ) == 0 );
    }

    @Test
    void compareProducer() {
        MediaContent ac = new MediaContent( this.sampleAddress, this.db.createProducer( "io" ), this.sampleBitrate, this.sampleDuration, this.sampleSize );
        MediaContent bc = new MediaContent( this.sampleAddress, this.db.createProducer( "aaa" ), this.sampleBitrate, this.sampleDuration, this.sampleSize );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new ProducerComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // io > aaa
    }

    @Test
    void compareDuration() {
        Duration acDur = Duration.ofSeconds( 0 + 1 * 60 + 0 * 60 * 60 ); // 1min
        Duration bcDur = Duration.ofSeconds( 0 + 2 * 60 + 0 * 60 * 60 ); // 2min

        MediaContent ac = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, acDur, this.sampleSize );
        MediaContent bc = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, bcDur, this.sampleSize );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new DurationComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 1min < 2min
    }

    @Test
    void compareSize() {
        BigDecimal acSize = new BigDecimal( 1000 ); // 1mb
        BigDecimal bcSize = new BigDecimal( 2000 ); // 2mb

        MediaContent ac = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, this.sampleDuration, acSize );
        MediaContent bc = new MediaContent( this.sampleAddress, this.producer, this.sampleBitrate, this.sampleDuration, bcSize );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new SizeComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 1mb < 2mb
    }

    @Test
    void compareBitrate() {
        long acRate = 1L;
        long bcRate = 2L;

        MediaContent ac = new MediaContent( this.sampleAddress, this.producer, acRate, this.sampleDuration, this.sampleSize );
        MediaContent bc = new MediaContent( this.sampleAddress, this.producer, bcRate, this.sampleDuration, this.sampleSize );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new BitrateComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 1L < 2L
    }

    @Test
    void compareSamplingRateForAudio() {
        int acRate = 32000;
        int bcRate = 48000;

        MediaContent ac = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  acRate, this.sampleAudioEnc );
        MediaContent bc = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  bcRate, this.sampleAudioEnc );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new SamplingRateComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 32k < 48k
    }
    @Test
    void compareSamplingRateForAudioVideo() {
        int acRate = 32000;
        int bcRate = 48000;

        MediaContent ac = new AudioVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  acRate, this.sampleAudioEnc, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc );
        MediaContent bc = new AudioVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  bcRate, this.sampleAudioEnc, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new SamplingRateComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 32k < 48k
    }
    @Test
    void compareSamplingRateForAudioVideoAndAudio() {
        int acRate = 32000;
        int bcRate = 48000;

        MediaContent ac = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  acRate, this.sampleAudioEnc );
        MediaContent bc = new AudioVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  bcRate, this.sampleAudioEnc, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new SamplingRateComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 32k < 48k
    }
    @Test
    void compareSamplingRateForAudioAndInvalid() {
        MediaContent ac = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer,  this.sampleSamplingRate, this.sampleAudioEnc );
        MediaContent bc = this.sampleItem; // not Audio, does not have sampling rate
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new SamplingRateComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // valid < invalid
    }
    @Test
    void compareAudioEnc() {
        String av = "MP3";
        String bv = "OPUS";

        MediaContent ac = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleSamplingRate, av );
        MediaContent bc = new Audio( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleSamplingRate, bv );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new AudioEncComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // MP3 > OPUS
    }
    @Test
    void compareVideoEnc() {
        String av = "MP4";
        String bv = "MKV";

        MediaContent ac = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, av );
        MediaContent bc = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, bv );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new VideoEncComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // MP4 > MKV
    }
    @Test
    void compareVideoWidth() {
        int av = 720;
        int bv = 1080;

        MediaContent ac = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, av, this.sampleHeight, this.sampleVideoEnc );
        MediaContent bc = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, bv, this.sampleHeight, this.sampleVideoEnc );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new WidthComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 720 < 1080
    }
    @Test
    void compareVideoHeight() {
        int av = 720;
        int bv = 1080;

        MediaContent ac = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, av, this.sampleVideoEnc );
        MediaContent bc = new Video( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, bv, this.sampleVideoEnc );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new HeightComparator();
        assertTrue( c.compare( ab, bb ) < 0 ); // 720 < 1080
    }
    @Test
    void compareLicenseHolder() {
        String av = "UMG";
        String bv = "BMG";

        MediaContent ac = new LicensedVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc, av );
        MediaContent bc = new LicensedVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc, bv );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new HolderComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // UMG > BMG
    }
    @Test
    void compareInteractiveType() {
        String av = "Performace";
        String bv = "Game";

        MediaContent ac = new InteractiveVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc, av );
        MediaContent bc = new InteractiveVideo( this.sampleAddress, this.sampleBitrate, this.sampleDuration, this.sampleSize, this.producer, this.sampleWidth, this.sampleHeight, this.sampleVideoEnc, bv );
        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new HolderComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // Performance > Game
    }
    @Test
    void compareAccessCount() {
        MediaContent ac = new MediaContent( "abc", this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
        MediaContent bc = new MediaContent( "123", this.producer, this.sampleBitrate, this.sampleDuration, this.sampleSize );
        this.db.upload( ac );
        this.db.upload( bc );
        this.db.getItemByAddress( "abc" );

        MediaContentBean ab = new MediaContentBean( ac );
        MediaContentBean bb = new MediaContentBean( bc );

        Comparator c = new AccessComparator();
        assertTrue( c.compare( ab, bb ) > 0 ); // 2 > 1
    }
}
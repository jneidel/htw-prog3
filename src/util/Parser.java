package util;

import gl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigDecimal;
import java.time.Duration;

public class Parser {
    UploaderI producer;
    public Parser( UploaderI prod ) {
        this.producer = prod;
    }

    private final static int MIN_REQUIRED_NR_OF_ARGS = 4;
    private final static ArrayList<String> TYPES = new ArrayList<>( Arrays.asList(
            "Audio", "AudioVideo", "InteractiveVideo", "LicensedAudio", "LicensedAudioVideo", "LicensedVideo"
    ) );

    public MediaContent parseData( String data ) throws IllegalNumberOfArgumentsException, IllegalArgumentException {

        String[] args = data.split( " " );

        if (args.length >= MIN_REQUIRED_NR_OF_ARGS) {
            String mediaType = args[0];
            if ( !TYPES.contains( mediaType ) ) {
                throw new IllegalArgumentException( "Invalid media type" );
            }

            String address = args[1]; // check duplicate
            long bitrate = Long.parseLong(args[2]);
            BigDecimal size = new BigDecimal( 0 );

            Duration length;
            String[] lengthTokens = args[3].split( ":" );
            int lengthHrs = Integer.parseInt(lengthTokens[0]);
            int lengthMin = Integer.parseInt(lengthTokens[1]);
            int lengthSec = Integer.parseInt(lengthTokens[2]);
            length = Duration.ofSeconds(lengthSec + lengthMin * 60 + lengthHrs * 60 * 60);

            switch (mediaType) {
                case "Audio" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 2;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String audioEncoding = args[4];
                    int samplingRate = Integer.parseInt(args[5]);
                    // size?

                    // Audio song.aac 320 0:2:45 aac 44000
                    return new Audio( address, bitrate, length, size, this.producer, samplingRate, audioEncoding );
                }
                case "AudioVideo" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 5;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String videoEncoding = args[4];
                    int height = Integer.parseInt(args[5]);
                    int width = Integer.parseInt(args[6]);
                    String audioEncoding = args[7];
                    int samplingRate = Integer.parseInt(args[8]);
                    // size?

                    // AudioVideo movie.mp4 320 1:30:11 mp4 640 480 mp3 44000
                    return new AudioVideo( address, bitrate, length, size, this.producer, samplingRate, audioEncoding, width, height, videoEncoding );
                }
                case "InteractiveVideo" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 4;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String videoEncoding = args[4];
                    int height = Integer.parseInt(args[5]);
                    int width = Integer.parseInt(args[6]);
                    String type = args[7];
                    // size?

                    // InteractiveVideo interactive_vid.mkv 320 0:1:15 mkv 640 480 Abstimmung
                    return new InteractiveVideo( address, bitrate, length, size, this.producer, width, height, videoEncoding, type );
                }
                case "LicensedAudio" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 3;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String audioEncoding = args[4];
                    int samplingRate = Integer.parseInt(args[5]);
                    String holderName = args[6];
                    // size?

                    // LicensedAudio song.mp3 320 0:2:45 mp3 44000 UMG
                    return new LicensedAudio( address, bitrate, length, size, this.producer, samplingRate, audioEncoding, holderName );
                }
                case "LicensedAudioVideo" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 6;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String videoEncoding = args[4];
                    int height = Integer.parseInt(args[5]);
                    int width = Integer.parseInt(args[6]);
                    String audioEncoding = args[7];
                    int samplingRate = Integer.parseInt(args[8]);
                    String holderName = args[9];
                    // size?

                    // LicensedAudioVideo music_video.mp4 320 0:2:45 mp4 480 720 aac 44000 Warner
                    return new LicensedAudioVideo( address, bitrate, length, size, this.producer, samplingRate, audioEncoding, width, height, videoEncoding, holderName );
                }
                case "LicensedVideo" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS + 4;
                    if (args.length != REQUIRED_NR_OF_ARGS) {
                        throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);
                    }
                    String videoEncoding = args[4];
                    int height = Integer.parseInt(args[5]);
                    int width = Integer.parseInt(args[6]);
                    String holderName = args[7];
                    // size?

                    // LicensedVideo movie.mp4 320 0:2:45 mp4 480 720 Warner
                    return new LicensedVideo( address, bitrate, length, size, this.producer, width, height, videoEncoding, holderName );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( MIN_REQUIRED_NR_OF_ARGS + "+ required" );
        }
        return new MediaContent( "", this.producer, 0L, Duration.ofSeconds(0), new BigDecimal( "0 " ) );
    }
}

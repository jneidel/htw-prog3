package cli;

import gl.*;
import fs.FS;

import util.IllegalNumberOfArgumentsException;
import util.Observer;

import java.math.BigDecimal;
import java.time.Duration;

public class Parser {
    Printer printer = new Printer( "stdout" );
    FS fs = new FS();
    char status = 'r';
    UploaderI producer;

    public Parser( UploaderI prod ) {
        this.producer = prod;
    }

    public MediaContent parseCreate(String[] args ) throws IllegalNumberOfArgumentsException {
        final String HELP_MENU = "create mode:\n\n" +
            "  PRODUCER - add a new producer\n" +
            "  MEDIA_TYPE PRODUCER TAGS BITRATE LENGTH\n" +
            "    [VIDEO_ENCODING HEIGHT WIDTH AUDIO_ENCODING\n" +
            "    SAMPLING_RATE INTERACTION_TYPE LICENSE_HOLDER]\n" +
            "      - add a new media file (specific arguments depend on type)\n\n" +
            "Examples:\n" +
            "  InteractiveVideo Produzent1 Lifestyle,News 5000 3600 DWT 640 480 Abstimmung\n\n" +
            "  LicensedAudioVideo Produzent1 , 8000 600 DCT 1400 900 MDCT 44100 EdBangerRecords\n";
        final int MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE = 4;

        if ( args.length == 0 ) {
            // print help menu
        } else if (args.length == 1) {
            switch (args[0]) {
                case "help" -> {
                    // print help menu
                }
                default -> {
                    String producer = args[0];
                    // add producer
                }
            }
        } else if (args.length >= MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE) {
            String mediaType = args[0]; // validate
            String address = args[1]; // check duplicate
            long bitrate = Long.parseLong(args[2]);
            BigDecimal size = new BigDecimal(0);

            Duration length;
            String[] lengthTokens = args[3].split(":");
            int lengthHrs = Integer.parseInt(lengthTokens[0]);
            int lengthMin = Integer.parseInt(lengthTokens[1]);
            int lengthSec = Integer.parseInt(lengthTokens[2]);
            length = Duration.ofSeconds(lengthSec + lengthMin * 60 + lengthHrs * 60 * 60);

            switch (mediaType) {
                case "Audio" -> {
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 2;
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
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 5;
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
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
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
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 3;
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
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 6;
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
                    final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
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
            throw new IllegalNumberOfArgumentsException( "1 or " + MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + "+" );
        }
        return new MediaContent( "", 0L, Duration.ofSeconds(0), new BigDecimal( "0 " ) );
    }

    void parseRead( String[] args ) {
        final String HELP_MENU = "read mode:\n\n" +
            "  uploader PRODUCER - display number of uploaded files\n" +
            "  content [MEDIA_TYPE] - display files (filtered by type if given)\n" +
            "  tag [i|e|included|excluded] - show list of tag (filter by whether or not they have been included)\n" +
            "\nExamples:\n" +
            "  uploader jneidel\n" +
            "  content\n" +
            "  content LicensedVideo\n" +
            "  tag i\n";

        if ( args.length == 0 ) {
            // print help menu
        } else if ( args.length < 3 ) {
            switch (args[0]) {
                case "help" -> {
                    // print help menu
                }
                case "uploader" -> {
                    String producerName = args[0];
                    // print count of files
                }
                case "content" -> {
                    if ( args.length == 2 ) {
                        String mediaType = args[1];
                        // print filtered list
                    } else {
                        // print list
                        // incl. address, upload date, accessCount
                    }
                }
                case "tag" -> {
                    if ( args.length == 2 ) {
                        boolean included;
                        switch( args[1] ) {
                            case "e":
                            case "excluded":
                                included = false;
                                break;
                            case "i":
                            case "included":
                                included = true;
                                break;
                            default:
                                throw new IllegalArgumentException( "Invalid parameter passed to tag.\nSee 'help' for help." );
                        }
                        // print tags depending on bool
                    } else {
                        // print tags
                    }
                }
                default -> {
                    throw new IllegalArgumentException( "Invalid command passed.\nSee 'help' for help." );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( "1-2" );
        }
    }

    void parseDelete( String[] args ) {
        final String HELP_MENU = "delete mode:\n\n" +
            "  PRODUCER - delete a producer\n" +
            "  ADDRESS - delete a file\n";

        if ( args.length == 0 ) {
            // print help menu
        } else if ( args.length == 1 ) {
            // check if valid producer
            // or check if valid address
            // else
            throw new IllegalArgumentException( "Invalid producer or address passed.\nSee 'help' for help." );
        } else {
            throw new IllegalNumberOfArgumentsException( 1 );
        }
    }

    void parseUpdate( String[] args ) {
        final String HELP_MENU = "update mode:\n\n" +
                "  ADDRESS - increment the access counter by one\n";

        if ( args.length == 0 ) {
            // print help menu
        } else if ( args.length == 1 ) {
            // check if valid address
            // else
            throw new IllegalArgumentException( "Invalid address passed.\nSee 'help' for help." );
        } else {
            throw new IllegalNumberOfArgumentsException( 1 );
        }
    }

    void parsePersist( String[] args ) {
        final String HELP_MENU = "persist mode:\n\n" +
            "  saveJOS - save with JOS\n" +
            "  loadJOS - load from JOS\n" +
            "  saveJBP - save with JBP\n" +
            "  loadJBP - load from JBP\n" +
            "  save ADDRESS - save instance to file\n" +
            "  load ADDRESS - load instance from file\n";

        if ( args.length == 0 ) {
            // print help menu
        } else if ( args.length == 1 ) {
            switch ( args[0]) {
                case "saveJOS":
                    // fs.writeDB( db );
                    // break;
                case "loadJOS":
                    // MediaDB db = fs.readDB();
                    // break;
                case "saveJBP":
                case "loadJBP":
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command passed.\nSee 'help' for help.");
            }
        } else if ( args.length == 2 ) {
            String className = args[1];
            // validate class
            switch( args[0] ) {
                case "add" -> {
                    // add
                }
                case "remove" -> {
                    // remove
                }
                default -> {
                    throw new IllegalArgumentException("Invalid command passed.\nSee 'help' for help.");
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( "1-2" );
        }
    }

    void parseConfig( String[] args ) {
        final String HELP_MENU = "config mode:\n\n" +
            "  add CLASS - register observer\n" +
            "  remove CLASS - unregister observer\n";

        if ( args.length == 0 ) {
            // print help menu
        } else if ( args.length == 2 ) {
            switch( args[0] ) {
                case "add" -> {
                    // validate that class is observer
                    // add class as an observer to db
                }
                case "remove" -> {
                    // remove as observer
                }
                default -> {
                    throw new IllegalArgumentException( "Invalid command passed.\nSee 'help' for help." );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( 2 );
        }
    }

    // integration of parse requires events to be setup
    public void parse( String input ) throws IllegalNumberOfArgumentsException {
        switch( input ) {
            case ":r": // read
                this.status = 'r';
                return;
            case ":c": // create
                this.status = 'c';
                return;
            case ":d": // delete
                this.status = 'd';
                return;
            case ":u": // update
                this.status = 'u';
                return;
            case ":p": // persist
                this.status = 'p';
                return;
            case ":config": // config
                this.status = 'g';
                return;
        }

        String[] args = input.split( " " );
        switch( this.status ) {
            case 'c' -> parseCreate( args );
            case 'r' -> parseRead( args );
            case 'd' -> parseDelete( args );
            case 'u' -> parseUpdate( args );
            case 'p' -> parsePersist( args );
            //case 'g' -> parseConfig( args );
        }
    }
}

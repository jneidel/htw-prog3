package util;

import gl.*;
import routing.UploadMediaEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;

public class Parser {
    private MediaContent addTags( LinkedList<Tag> tags, MediaContent content ) {
        for ( Tag t : tags ) {
            content.addTag( t );
        }
        return content;
    }

    public MediaContent parseMediaStrToMediaContent( String text, MediaDB db ) throws IllegalArgumentException {
        final int MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE = 7;

        String[] args = text.split( " " );
        if ( args.length < MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE )
            throw new IllegalNumberOfArgumentsException( MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE );

        String mediaType = args[0].toLowerCase();
        if ( !this.validateMediaType( mediaType ) )
            throw new IllegalArgumentException( "invalid media type: " + args[0] );

        Uploader producer = db.getProducer( args[1] );

        String address = args[2];

        BigDecimal size;
        try {
            size = new BigDecimal( args[3] );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid size: " + args[3] );
        }

        String tagsStr = args[4];
        LinkedList<Tag> tags = new LinkedList<Tag>();
        if ( !tagsStr.equals( "," ) )
            tags = this.parseTags( tagsStr.split( "," ) );

        long bitrate;
        try {
            bitrate = Long.parseLong( args[5] );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid bitrate: " + args[5] );
        }

        Duration length;
        try {
            int lengthSec = Integer.parseInt(args[6]);
            length = Duration.ofSeconds(lengthSec);
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid length: " + args[6] );
        }

        switch (mediaType) {
            case "audio" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 2;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[8] );
                }

                // Audio prod song.aac 4000 News 320 30 AAC 44000
                Audio a = new Audio( address, bitrate, length, size, producer, samplingRate, encoding );
                return this.addTags( tags, a );
            }
            case "audiovideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[10]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[10] );
                }

                // AudioVideo prod movie.mp4 4000 Lifestyle 320 200 MP4 720 480 44000
                AudioVideo av = new AudioVideo( address, bitrate, length, size, producer, samplingRate, encoding, width, height, encoding );
                return this.addTags( tags, av );
            }
            case "interactivevideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                String type = args[10];

                // InteractiveVideo prod interactive_vid.mkv 8000 , 320 80 MKV 640 480 Performance
                InteractiveVideo iv = new InteractiveVideo( address, bitrate, length, size, producer, width, height, encoding, type );
                return this.addTags( tags, iv );
            }
            case "licensedaudio" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 3;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[8] );
                }

                String holderName = args[9];

                // LicensedAudio prod song.mp3 2000 , 320 800 MP3 44000 UMG
                LicensedAudio la = new LicensedAudio( address, bitrate, length, size, producer, samplingRate, encoding, holderName );
                return this.addTags( tags, la );
            }
            case "licensedaudiovideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 5;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[9] );
                }

                String holderName = args[10];

                // LicensedAudioVideo prod music_video.mp4 16000 , 320 40 MP4 1080 720 44000 Warner
                LicensedAudioVideo lav = new LicensedAudioVideo( address, bitrate, length, size, producer, samplingRate, encoding, width, height, encoding, holderName );
                return this.addTags( tags, lav );
            }
            case "licensedvideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                String holderName = args[10];

                // LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner
                LicensedVideo lv = new LicensedVideo( address, bitrate, length, size, producer, width, height, encoding, holderName );
                return this.addTags( tags, lv );
            }
            case "video" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 3;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                Video v = new Video( address, bitrate, length, size, producer, width, height, encoding );
                return this.addTags( tags, v );
            }
            default -> {
                return null;
            }
        }
    }
    public UploadMediaEvent parseMediaStrToEvent( String text ) throws IllegalNumberOfArgumentsException {
        final int MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE = 7;

        String[] args = text.split( " " );
        if ( args.length < MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE )
            throw new IllegalNumberOfArgumentsException( MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE );

        String mediaType = args[0].toLowerCase();
        if ( !this.validateMediaType( mediaType ) )
            throw new IllegalArgumentException( "invalid media type: " + args[0] );

        String producer = args[1];

        String address = args[2];

        BigDecimal size;
        try {
            size = new BigDecimal( args[3] );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid size: " + args[3] );
        }

        String tagsStr = args[4];
        LinkedList<Tag> tags = new LinkedList<Tag>();
        if ( !tagsStr.equals( "," ) )
            tags = this.parseTags( tagsStr.split( "," ) );

        long bitrate;
        try {
            bitrate = Long.parseLong( args[5] );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid bitrate: " + args[5] );
        }

        Duration length;
        try {
            int lengthSec = Integer.parseInt(args[6]);
            length = Duration.ofSeconds(lengthSec);
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid length: " + args[6] );
        }

        switch (mediaType) {
            case "audio" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 2;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[8] );
                }

                // Audio prod song.aac 4000 News 320 30 AAC 44000
                return new UploadMediaEvent( text, text );
            }
            case "audiovideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[10]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[10] );
                }

                // AudioVideo prod movie.mp4 4000 Lifestyle 320 200 MP4 720 480 44000
                return new UploadMediaEvent( text, text );
            }
            case "interactivevideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                String type = args[10];

                // InteractiveVideo prod interactive_vid.mkv 8000 , 320 80 MKV 640 480 Performance
                return new UploadMediaEvent( text, text );
            }
            case "licensedaudio" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 3;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[8] );
                }

                String holderName = args[9];

                // LicensedAudio prod song.mp3 2000 , 320 800 MP3 44000 UMG
                return new UploadMediaEvent( text, text );
            }
            case "licensedaudiovideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 5;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                int samplingRate;
                try {
                    samplingRate = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid sampling rate: " + args[9] );
                }

                String holderName = args[10];

                // LicensedAudioVideo prod music_video.mp4 16000 , 320 40 MP4 1080 720 44000 Warner
                return new UploadMediaEvent( text, text );
            }
            case "licensedvideo" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 4;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                String holderName = args[10];

                // LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner
                return new UploadMediaEvent( text, text );
            }
            case "video" -> {
                final int REQUIRED_NR_OF_ARGS = MIN_REQUIRED_NR_OF_ARGS_FOR_CREATE + 3;
                if (args.length != REQUIRED_NR_OF_ARGS)
                    throw new IllegalNumberOfArgumentsException(mediaType, REQUIRED_NR_OF_ARGS);

                String encoding = args[7];

                int height;
                try {
                    height = Integer.parseInt(args[8]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid height: " + args[8] );
                }

                int width;
                try {
                    width = Integer.parseInt(args[9]);
                } catch ( Exception e ) {
                    throw new IllegalArgumentException( "invalid width: " + args[9] );
                }

                return new UploadMediaEvent( text, text );
            }
            default -> {
                return null;
            }
        }
    }
    public boolean validateMediaType( String type ) {
        switch ( type ) {
            case "audio":
            case "audiovideo":
            case "interactivevideo":
            case "licensedaudio":
            case "licensedaudiovideo":
            case "licensedvideo":
            case "video": return true;
            default: return false;
        }
    }

    public LinkedList<Tag> parseTags( String[] tags ) throws IllegalArgumentException {
        LinkedList<Tag> list = new LinkedList<Tag>();

        for ( String tag : tags ) {
            switch ( tag ) {
                case "News": list.add( Tag.News ); break;
                case "Lifestyle": list.add( Tag.Lifestyle ); break;
                case "Animal": list.add( Tag.Animal ); break;
                case "Tutorial": list.add( Tag.Tutorial ); break;
                default: throw new IllegalArgumentException( "invalid tag: " + tag );
            }
        }

        return list;
    }
}
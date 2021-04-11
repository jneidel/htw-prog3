package cli;

import routing.*;
import net.Client;
import util.*;

import java.util.EventObject;
import java.util.Scanner;

public class ConsoleReader {
    char mode = 'c';
    Parser parser = new Parser();
    Client client = null;
    EventHandler handler = null;

    public ConsoleReader( EventHandler handler ) {
        this.handler = handler;
    }
    public ConsoleReader( Client client ) {
        this.client = client;
    }

    public void print( String msg ) { System.out.println( msg ); }
    public void printErr( String msg ) { System.out.println( msg ); }

    public void exec() {
        try ( Scanner s = new Scanner( System.in ) ) {
            do {
                System.out.print("$ ");
                String text = s.nextLine();

                if ( Character.compare( text.charAt( 0 ), ':' ) == 0 ) {
                    this.print( this.changeMode( text ) );
                } else {
                    try {
                        EventObject event = this.parseToEvent( text );
                        if ( event != null ) {
                            String res = this.handleEvent( event );
                            this.print( res );
                        }
                    } catch ( IllegalArgumentException e ) {
                        this.printErr( e.toString() ); // user error
                    }
                }
            } while( true );
        }
    }

    public String handleEvent( EventObject event ) {
        if ( this.client == null ) { // local
            return this.handler.handleWithFeedback( event );
        } else { // server-client
            return this.client.sendEvent( event );
        }
    }

    public String changeMode( String text ) {
        switch ( text ) {
            case ":c": this.mode = 'c';
                return "Switched to insert mode";
            case ":d": this.mode = 'd';
                return "Switched to deletion mode";
            case ":r": this.mode = 'r';
                return "Switched to read mode";
            case ":u": this.mode = 'u';
                return "Switched to update mode";
            case ":p": this.mode = 'p';
                return "Switched to persistent mode";
            case ":config": this.mode = 'g';
                return "Switched to config mode";
            default:
                return "Error: invalid mode specified";
        }
    }
    private EventObject parseToEvent( String text ) throws IllegalArgumentException {
        switch ( this.mode ) {
            case 'c': return this.parseCreate( text );
            case 'r': return this.parseRead( text );
            case 'd': return this.parseDelete( text );
            case 'g': return this.parseConfig( text );
            case 'u': return this.parseUpdate( text );
            case 'p': return this.parsePersist( text );
            default: return null; // for java to shut up, invalid mode state
        }
    }

    public EventObject parseCreate( String text ) throws IllegalNumberOfArgumentsException {
        String[] args = text.split( " " );
        final String HELP_MENU = "create mode:\n\n" +
                "  PRODUCER - add a new producer\n" +
                "  MEDIA_TYPE PRODUCER ADDRESS SIZE\n" +
                "    TAGS BITRATE LENGTH\n" +
                "    [ENCODING HEIGHT WIDTH \n" +
                "    SAMPLING_RATE INTERACTION_TYPE LICENSE_HOLDER]\n" +
                "      - add a new media file (specific arguments depend on type)\n\n" +
                "Examples:\n" +
                "  InteractiveVideo prod interactive_vid.mkv 8000 , 320 80 MKV 640 480 Performance\n\n" +
                "  AudioVideo prod movie.mp4 4000 Lifestyle 320 200 MP4 720 480 44000\n\n" +
                "  LicensedVideo prod movie.mp4 700 Animal,Lifestyle 320 45 MP4 720 480 Warner\n";
        if ( args.length == 0 ) {
            this.print( HELP_MENU );
            return null;
        } else if (args.length == 1) {
            switch (args[0]) {
                case "help" -> {
                    this.print( HELP_MENU );
                    return null;
                }
                default -> {
                    String name = args[0];
                    return new CreateProducerEvent( name, name );
                }
            }
        } else {
            return this.parser.parseMediaStrToEvent( text );
        }
    }

    public EventObject parseRead( String text ) {
        String[] args = text.split( " " );
        final String HELP_MENU = "read mode:\n\n" +
                "  uploader - display producers & number of uploaded files\n" +
                "  content [MEDIA_TYPE] - display files (filtered by type if given)\n" +
                "  tag [i|e|included|excluded] - show list of tag (filter by whether or not they have been included)\n" +
                "\nExamples:\n" +
                "  uploader\n" +
                "  content\n" +
                "  content LicensedVideo\n" +
                "  tag i\n";

        if ( args.length == 0 ) {
            this.print( HELP_MENU );
            return null;
        } else if ( args.length < 3 ) {
            switch (args[0]) {
                case "help" -> {
                    this.print( HELP_MENU );
                    return null;
                }
                case "uploader", "producer" -> {
                    return new ReadEvent( "producer", "producer" );
                }
                case "content" -> {
                    return new ReadEvent( "content", text );
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
                                throw new IllegalArgumentException( "Invalid tag parameter passed to tag.\nSee 'help' for help." );
                        }
                        if ( included )
                            return new ReadEvent( "tag", "tag " + "i" );
                        else
                            return new ReadEvent( "tag", "tag " + "e" );
                    } else {
                        throw new IllegalArgumentException( "Invalid tag parameter passed.\nSee 'help' for help." );
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

    public EventObject parseDelete( String text ) {
        String[] args = text.split( " " );
        final String HELP_MENU = "delete mode:\n\n" +
                "  PRODUCER - delete a producer\n" +
                "  ADDRESS - delete a file\n";

        if ( args.length == 0 ) {
            this.print( HELP_MENU );
            return null;
        } else if ( args.length == 1 ) {
            switch (args[0]) {
                case "help" -> {
                    this.print( HELP_MENU );
                    return null;
                }
                default -> {
                    return new RemoveMediaOrProducerEvent( args[0], args[0] );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( 1 );
        }
    }

    public EventObject parseUpdate( String text ) {
        String[] args = text.split( " " );
        final String HELP_MENU = "update mode:\n\n" +
                "  ADDRESS - increment the access counter by one\n";

        if ( args.length == 0 ) {
            this.print( HELP_MENU );
            return null;
        } else if ( args.length == 1 ) {
            switch (args[0]) {
                case "help" -> {
                    this.print( HELP_MENU );
                    return null;
                }
                default -> {
                    return new IncrementAccessCounterEvent( args[0], args[0] );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( 1 );
        }
    }

    public EventObject parsePersist( String text ) {
        String[] args = text.split( " " );
        final String HELP_MENU = "persist mode:\n\n" +
                "  saveJOS - save with JOS\n" +
                "  loadJOS - load from JOS\n" +
                "  saveJBP - save with JBP - DISABLED\n" +
                "  loadJBP - load from JBP - DISABLED\n" +
                "  save ADDRESS - save instance to file\n" +
                "  load ADDRESS - load instance from file\n";

        if ( args.length == 0 ) {
            this.print( HELP_MENU );
            return null;
        } else if ( args.length == 1 ) {
            switch ( args[0]) {
                case "help" -> {
                    this.print( HELP_MENU );
                    return null;
                }
                case "saveJOS" -> {
                    return new SaveEvent( "jos", "jos" );
                }
                case "loadJOS" -> {
                    return new LoadEvent( "jos", "jos" );
                }
                case "saveJBP", "loadJBP" -> {
                    //return new SaveEvent( "jbp", "jbp" );
                    //return new LoadEvent( "jbp", "jbp" );
                    throw new RuntimeException("Disabled");
                }
                default -> {
                    throw new IllegalArgumentException("Invalid command passed.\nSee 'help' for help.");
                }
            }
        } else if ( args.length == 2 ) {
            switch( args[0] ) {
                case "save" -> {
                    return new SaveEvent( args[1], args[1] );
                }
                case "load" -> {
                    return new LoadEvent( args[1], args[1] );
                }
                default -> {
                    throw new IllegalArgumentException("Invalid command passed.\nSee 'help' for help.");
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( "1-2" );
        }
    }

    public EventObject parseConfig( String text ) {
        String[] args = text.split( " " );
        final String HELP_MENU = "config mode:\n\n" +
                "  add CLASS - register observer\n" +
                "  remove CLASS - unregister observer\n";

        if ( args.length == 0 ) {
            this.print(HELP_MENU);
            return null;
        } else if ( args.length == 1 ) {
            this.print(HELP_MENU);
            return null;
        } else if ( args.length == 2 ) {
            switch( args[0] ) {
                case "add", "remove" -> {
                    /*
                     it isn't clear to me what's to do here, the pdf says:
                        add [Klassenname] registriert einen benannten Beobachter bzw. listener
                        remove [Klassenname] de-registriert einen benannten Beobachter bzw. listener
                     but what is the use of those listeners? this is the only reference I found to them and it's not helpful
                     */
                    return null;
                }
                default -> {
                    throw new IllegalArgumentException( "Invalid command passed.\nSee 'help' for help." );
                }
            }
        } else {
            throw new IllegalNumberOfArgumentsException( 2 );
        }
    }
}
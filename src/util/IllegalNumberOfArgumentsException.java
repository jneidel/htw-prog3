package util;

public class IllegalNumberOfArgumentsException extends IllegalArgumentException {
    String type = null;
    String exepctedNr;
    public IllegalNumberOfArgumentsException( int expectedNr ) { this.exepctedNr = "" + expectedNr; }
    public IllegalNumberOfArgumentsException( String expectedNr ) { this.exepctedNr = expectedNr; }
    public IllegalNumberOfArgumentsException( String type, String expectedNr ) {
        this.type = type;
        this.exepctedNr = expectedNr;
    }
    public IllegalNumberOfArgumentsException( String type, int expectedNr ) {
        this.type = type;
        this.exepctedNr = "" + expectedNr;
    }

    public String toString() {
        final String PREFIX = "Got wrong amount of arguments";
        final String EXPECTED = ", expected " + this.exepctedNr + ".\n";
        final String SUFFIX = "See 'help' for help.\n";
        if ( null != this.type ) {
            return PREFIX + " for type '" + this.type + "'" + EXPECTED + SUFFIX;
        } else {
            return PREFIX + EXPECTED + SUFFIX;
        }
    }
}

package util;

import static java.lang.Integer.parseInt;

public class Args {
    int capacity = 0;
    String protocol = null;
    String countyCode = null;

    public Args( String[] args ) {
        for ( String arg : args ) {
            arg = arg.toLowerCase();
            System.out.println( arg );
            if ( arg.equals( "udp" ) || arg.equals( "tcp" ) ) {
                this.protocol = arg;
            } else {
                try {
                    int cap = parseInt( arg );
                    if ( cap > 0 )
                        this.capacity = cap;
                } catch (Exception e) { // not a number
                    if ( arg.length() == 2 && this.isSupportedCountyCode( arg ) ) {
                        this.countyCode = arg;
                    }
                }
            }
        }
    }

    private boolean isSupportedCountyCode( String countyCode ) {
        countyCode = countyCode.toLowerCase();
        return countyCode.equals( "de" ) || countyCode.equals( "gb" ) || countyCode.equals( "us" );
    }

    public int getCapacity() { return capacity; }
    public String getProtocol() { return protocol; }
    public String getCountyCode() { return countyCode; }
}

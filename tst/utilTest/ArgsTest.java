package utilTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import util.Args;

public class ArgsTest {
    @Test public void ArgsDE() {
        String[] argsArr = { "DE" };

        Args args = new Args( argsArr );
        assertEquals( argsArr[0].toLowerCase(), args.getCountyCode() );
    }
    @Test public void ArgsUS() {
        String[] argsArr = { "US" };

        Args args = new Args( argsArr );
        assertEquals( argsArr[0].toLowerCase(), args.getCountyCode() );
    }
    @Test public void ArgsGB() {
        String[] argsArr = { "gb" };

        Args args = new Args( argsArr );
        assertEquals( argsArr[0].toLowerCase(), args.getCountyCode() );
    }
    @Test public void ArgsEN() {
        String[] argsArr = { "EN" }; // en not a valid county code

        Args args = new Args( argsArr );
        assertEquals( null, args.getCountyCode() );
    }
    @Test public void ArgsFR() {
        String[] argsArr = { "FR" }; // french is not supported

        Args args = new Args( argsArr );
        assertEquals( null, args.getCountyCode() );
    }
    @Test public void ArgsNoLogging() {
        String[] argsArr = {};

        Args args = new Args( argsArr );
        assertEquals( null, args.getCountyCode() );
    }

    @Test public void ArgsTCP() {
        String[] argsArr = { "tcp" };

        Args args = new Args( argsArr );
        assertEquals( argsArr[0].toLowerCase(), args.getProtocol() );
    }
    @Test public void ArgsUDP() {
        String[] argsArr = { "UDP" };

        Args args = new Args( argsArr );
        assertEquals( argsArr[0].toLowerCase(), args.getProtocol() );
    }
    @Test public void ArgsLocal() {
        String[] argsArr = {};

        Args args = new Args( argsArr );
        assertEquals( null, args.getProtocol() );
    }

    @Test public void ArgsCapacity() {
        String[] argsArr = { "100" };

        Args args = new Args( argsArr );
        assertEquals( 100, args.getCapacity() );
    }
    @Test public void ArgsNegativeCapacity() {
        String[] argsArr = { "-100" };

        Args args = new Args( argsArr );
        assertEquals( 0, args.getCapacity() );
    }
    @Test public void ArgsFloatCapacity() {
        String[] argsArr = { "10.01" };

        Args args = new Args( argsArr );
        assertEquals( 0, args.getCapacity() );
    }
}

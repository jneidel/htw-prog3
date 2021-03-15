package gl;

interface LicensedI extends ContentI {
    String getHolder();
}

public class Licensed extends Content implements LicensedI {
    String holder;
    public Licensed( String address, String holder ) {
        super( address );
        this.holder = holder;
    }

    public String getHolder() {
        return this.holder;
    }
}
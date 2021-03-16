package gl;

interface LicensedI extends ContentI {
    String getHolder();
}

public class Licensed extends Content implements LicensedI {
    String holder;
    public Licensed( String address, Uploader uploader, String holder ) {
        super( address, uploader );
        this.holder = holder;
    }

    public String getHolder() {
        return this.holder;
    }
}
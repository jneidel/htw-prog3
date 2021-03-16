package gl;

interface InteractiveI extends ContentI {
    String getType();
}

public class Interactive extends Content implements InteractiveI {
    String type;

    public Interactive( String address, UploaderI uploader, String type ) {
        super( address, uploader );
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
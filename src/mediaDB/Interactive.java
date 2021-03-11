package mediaDB;

interface InteractiveI extends ContentI {
    String getType();
}

public class Interactive extends Content implements InteractiveI {
    String type;

    public Interactive( String address, String type ) {
        super( address );
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
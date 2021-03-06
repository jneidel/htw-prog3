package gl;

import java.io.Serializable;
import java.util.*;

interface ContentI {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();
}

public class Content extends Uploadable implements ContentI,Serializable {
    private String address;
    private LinkedList<Tag> tags = new LinkedList<Tag>();
    public long accessCount = 0; // how often the content (represented by it's address) has been accessed

    public Content() { super(); }
    public Content( String address, Uploader uploader ) {
        super( uploader );
        this.address = address;
    }

    public String getAddress() {
        this.accessCount++;
        return this.address;
    }
    public String getAddressNonlogging() {
        // for non-user interaction (MediaDB internal calls)
        // required for stable testing
        return this.address;
    }

    public Collection<Tag> getTags() { return this.tags; }
    public void addTag( Tag tag ) { this.tags.add( tag ); }

    public long getAccessCount() {
        return this.accessCount;
    }

    public String getClassName() { return this.getClass().getSimpleName(); }

    public String toString() {
        return  "[" + this.getClassName() + "]: " + this.address;
    }
}
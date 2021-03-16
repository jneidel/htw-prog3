package gl;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

interface ContentI {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();
}

public class Content extends Uploadable implements ContentI,Serializable {
    String address;
    Collection<Tag> tags = new LinkedList<Tag>();
    long accessCount = 0; // how often the content (represented by it's address) has been accessed

    public Content( String address, UploaderI uploader ) {
        super( uploader );
        this.address = address;
    }

    public String getAddress() {
        this.accessCount++;
        return this.address;
    }

    public Collection<Tag> getTags() {
        return this.tags;
    }
    public void addTag( Tag tag ) {
        this.tags.add( tag );
    }

    public long getAccessCount() {
        return this.accessCount;
    }

    public String getClassName() { return this.getClass().getSimpleName(); }

    public String toString() {
        return  "[" + this.getClassName() + "]: " + this.address;
    }
}
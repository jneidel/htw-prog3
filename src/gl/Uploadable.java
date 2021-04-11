package gl;

import java.io.Serializable;
import java.util.Date;

interface UploadableI extends Serializable {
    UploaderI getUploader();
    Date getUploadDate();
}

public class Uploadable implements UploadableI {
    Uploader uploader;
    Date date = null;

    public Uploadable() {}
    public Uploadable( Uploader uploader ) {
        this.uploader = uploader;
    }

    public Uploader getUploader() {
        return this.uploader;
    }

    public Date getUploadDate() { return this.date; }
    public void setUploadDateToNow() { this.date = new Date(); }
}
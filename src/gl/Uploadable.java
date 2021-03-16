package gl;

import java.util.Date;

interface UploadableI {
    UploaderI getUploader();
    Date getUploadDate();
}

public class Uploadable implements UploadableI {
    Uploader uploader;
    Date date = null;

    public Uploadable( Uploader uploader ) {
        this.uploader = uploader;
    }

    public Uploader getUploader() {
        return this.uploader;
    }

    public Date getUploadDate() { return this.date; }
    public void setUploadDateToNow() { this.date = new Date(); }
}
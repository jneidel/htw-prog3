package gl;

import java.util.Date;

interface UploadableI {
    UploaderI getUploader();
    Date getUploadDate();
}

public class Uploadable implements UploadableI {
    UploaderI uploader;
    Date date = null;

    public Uploadable( UploaderI uploader ) {
        this.uploader = uploader;
    }

    public UploaderI getUploader() {
        return this.uploader;
    }

    public Date getUploadDate() { return this.date; }
    public void setUploadDateToNow() { this.date = new Date(); }
}
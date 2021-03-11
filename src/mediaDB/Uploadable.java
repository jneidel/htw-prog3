package mediaDB;

import java.util.Date;

interface UploadableI {
    UploaderI getUploader();
    Date getUploadDate();
}

public class Uploadable implements UploadableI {
    UploaderI uploader;
    Date date;

    public Uploadable( UploaderI uploader ) {
        this.uploader = uploader;
        this.date = new Date();
    }

    public UploaderI getUploader() {
        return this.uploader;
    }

    public Date getUploadDate() {
        return this.date;
    }
}
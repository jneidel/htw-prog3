package fs;

import gl.MediaContent;
import gl.MediaDB;

import java.util.ArrayList;
import java.util.Random;

public class DBWriter extends Thread {
    FS fs;

    public DBWriter( FS fs ) {
        this.fs = fs;
    }

    public void run() {
    }
}

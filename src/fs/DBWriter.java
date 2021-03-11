package fs;

import mediaDB.MediaContent;
import mediaDB.MediaDB;

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

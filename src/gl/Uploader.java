package gl;

import java.io.Serializable;

interface UploaderI extends Serializable {
    String getName();
}

public class Uploader implements UploaderI {
    private String name;
    private int count = 0;

    public Uploader( String name ) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() { return this.count; }
    public void incrementCount() { this.count++; }
    public void decrementCount() { this.count--; }

    public String toString() {
        return "producer " + name + ": " + count;
    }
}



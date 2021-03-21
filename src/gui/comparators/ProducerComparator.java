package gui.comparators;

import gui.MediaContentBean;

import java.util.Comparator;

public class ProducerComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            return a.src.getUploader().getName().compareTo(b.src.getUploader().getName());
        }
        return 0;
    }

    public String toString() {
        return "Producer";
    }
}
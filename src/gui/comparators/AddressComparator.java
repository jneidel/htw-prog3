package gui.comparators;

import gui.MediaContentBean;
import java.util.Comparator;

public class AddressComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            return a.src.getAddress().compareTo(b.src.getAddress());
        }
        return 0;
    }

    public String toString() {
        return "Address";
    }
}
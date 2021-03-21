package gui.comparators;

import gl.Audio;
import gl.AudioVideo;
import gl.InteractiveVideo;
import gui.MediaContentBean;

import java.util.Comparator;

public class InteractiveTypeComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            String av = null;
            try {
                av = ((InteractiveVideo) a.src).getType();
            } catch (Exception e) {
                return 1;
            }

            String bv = null;
            try {
                bv = ((InteractiveVideo) b.src).getType();
            } catch (Exception e) {
                return -1;
            }

            return av.compareTo( bv );
        }
        return 0;
    }

    public String toString() { return "Interactive Type"; }
}
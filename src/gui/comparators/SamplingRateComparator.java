package gui.comparators;

import gl.Audio;
import gl.AudioVideo;
import gui.MediaContentBean;

import java.util.Comparator;

public class SamplingRateComparator implements Comparator<MediaContentBean> {
    public int compare( MediaContentBean a, MediaContentBean b ) {
        if (a != null && b != null) {
            int av = 9999;
            try {
                av = ((Audio) a.src).getSamplingRate();
            } catch (Exception e) {}
            try {
                av = ((AudioVideo) a.src).getSamplingRate();
            } catch (Exception e) {}
            if ( av == 9999 )
                return 1;

            int bv = 9999;
            try {
                bv = ((Audio) b.src).getSamplingRate();
            } catch (Exception e) {}
            try {
                bv = ((AudioVideo) b.src).getSamplingRate();
            } catch (Exception e) {}
            if ( bv == 9999 )
                return -1;

            return Integer.compare( av, bv );
        }
        return 0;
    }

    public String toString() {
        return "Sampling Rate";
    }
}
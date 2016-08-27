package com.alexstyl.gsc;

import java.util.List;

public class SoundComparer {
    private static final SoundRules sRules = SoundRules.getInstance();

    public SoundComparer() {
    }

    public static boolean areSame(String first, String second) {
        if(first == null) {
            throw new IllegalArgumentException("first");
        } else if(second == null) {
            throw new IllegalArgumentException("second");
        } else {
            return sRules.compare(first, second, false);
        }
    }

    public static boolean startsWith(String first, String second) {
        if(first == null) {
            throw new IllegalArgumentException("first");
        } else if(second == null) {
            throw new IllegalArgumentException("second");
        } else {
            return sRules.compare(first, second, true);
        }
    }

    public static List<Sound> soundsLike(String first) {
        if(first == null) {
            throw new IllegalArgumentException("first");
        } else {
            return sRules.soundsLike(first);
        }
    }
}
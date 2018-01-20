package com.alexstyl.android;

import android.os.Build;

/**
 * This class contains static utility methods.
 */
public final class Version {

    // Prevents instantiation.
    private Version() {
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Emulate the as operator of C#. If the object can be cast to type it will
     * be casted. If not this returns null.
     */
    public static <T> T as(Class<T> type, Object o) {
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        return null;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * Lollipop or later.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}

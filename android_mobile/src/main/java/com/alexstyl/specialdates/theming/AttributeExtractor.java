package com.alexstyl.specialdates.theming;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import com.alexstyl.specialdates.R;

public class AttributeExtractor {

    private static final int[] SECONDARY = new int[]{R.attr.colorSecondary};

    private static final int[] PRIMARY_DARK = new int[]{R.attr.colorPrimaryDark};
    private static final int[] ACCENT = new int[]{R.attr.colorAccent};
    private static final int[] DARK_ICONS = new int[]{R.attr.useDarkIcons};
    private static final int[] TOOLBAR_ICONS_COLOR = new int[]{R.attr.toolbarIconColor};

    @ColorInt
    public int extractPrimaryColorFrom(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, PRIMARY_DARK);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    @ColorInt
    public int extractSecondaryColorFrom(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, SECONDARY);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public boolean extractDarkIconsFrom(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, DARK_ICONS);
        boolean useDarkColors = a.getBoolean(0, false);
        a.recycle();
        return useDarkColors;
    }

    @ColorInt
    public int extractAccentColorFrom(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, ACCENT);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    @ColorInt
    public int extractToolbarIconColors(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, TOOLBAR_ICONS_COLOR);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}

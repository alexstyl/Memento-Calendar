package com.alexstyl.specialdates.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.view.Window;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class LolipopHideStatusBarListener implements AppBarLayout.OnOffsetChangedListener {

    private static final int STATE_DEFAULT = 1;
    private static final int STATE_TRANSPARENT = 2;
    private static final int MAX_OFFSET = -800;

    private final Window window;
    @ColorInt
    private final int defaultColor;
    @ColorInt
    private final int targetColor;

    private int state = 0;

    public LolipopHideStatusBarListener(Window window) {
        this.window = window;
        this.defaultColor = window.getStatusBarColor();
        this.targetColor = Color.TRANSPARENT;

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (state != STATE_DEFAULT && verticalOffset == MAX_OFFSET) {
            window.setStatusBarColor(defaultColor);
            state = STATE_DEFAULT;
        } else if (state != STATE_TRANSPARENT) {
            window.setStatusBarColor(targetColor);
            state = STATE_TRANSPARENT;
        }
    }
}

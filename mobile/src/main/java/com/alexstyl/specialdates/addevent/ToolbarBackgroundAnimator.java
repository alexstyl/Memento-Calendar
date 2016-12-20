package com.alexstyl.specialdates.addevent;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.Toolbar;

final class ToolbarBackgroundAnimator {

    private static final int FADING_DURATION = 400;
    private TransitionDrawable transitionDrawable;
    private boolean visible = true;

    static ToolbarBackgroundAnimator setupOn(Toolbar toolbar) {
        ColorDrawable colorDrawable = new ColorDrawable(toolbar.getResources().getColor(android.R.color.transparent));
        TransitionDrawable transitionDrawable = new TransitionDrawable(layersFrom(toolbar.getBackground(), colorDrawable));
        transitionDrawable.setCrossFadeEnabled(true);
        toolbar.setBackground(transitionDrawable);
        return new ToolbarBackgroundAnimator(transitionDrawable);
    }

    private ToolbarBackgroundAnimator(TransitionDrawable transitionDrawable) {
        this.transitionDrawable = transitionDrawable;
    }

    void fadeOut() {
        if (visible) {
            transitionDrawable.startTransition(FADING_DURATION);
            visible = false;
        }
    }

    private static Drawable[] layersFrom(Drawable from, Drawable to) {
        Drawable[] layers = new Drawable[2];
        layers[0] = from;
        layers[1] = to;
        return layers;
    }

    void fadeIn() {
        if (!visible) {
            transitionDrawable.reverseTransition(FADING_DURATION);
            visible = true;
        }
    }
}

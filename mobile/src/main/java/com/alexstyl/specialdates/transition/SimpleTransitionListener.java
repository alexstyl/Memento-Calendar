package com.alexstyl.specialdates.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

import static android.transition.Transition.*;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SimpleTransitionListener implements TransitionListener {
    @Override
    public void onTransitionStart(Transition transition) {
        // do nothing
    }

    @Override
    public void onTransitionPause(Transition transition) {
        // do nothing
    }

    @Override
    public void onTransitionResume(Transition transition) {
        // do nothing
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        // do nothing
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        // do nothing
    }
}

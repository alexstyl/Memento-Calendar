package com.alexstyl.specialdates.search;

import android.view.View;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

class ToggleVisibilityOnFocus implements View.OnFocusChangeListener {

    private final View view;

    ToggleVisibilityOnFocus(View view) {
        this.view = view;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }
}

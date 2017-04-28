package com.alexstyl.specialdates.search;

import android.view.View;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ToggleVisibilityOnFocus implements View.OnFocusChangeListener {
    private final View view;

    public ToggleVisibilityOnFocus(View view) {
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

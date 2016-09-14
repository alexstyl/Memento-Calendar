package com.alexstyl.specialdates.ui;

import android.view.ViewGroup;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class ViewFader {

    public void hideContentOf(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(GONE);
        }
    }

    public void showContent(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(VISIBLE);
        }
    }

}

package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.widget.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class CardActionRecycler {

    private final List<ActionButton> freeViews = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public CardActionRecycler(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public ActionButton getOrCreateCardAction(ViewGroup viewGroup) {
        if (freeViews.isEmpty()) {
            return (ActionButton) layoutInflater.inflate(R.layout.row_card_action, viewGroup, false);
        } else {
            return freeViews.remove(0);
        }

    }

    public void clearActions(LinearLayout cardActions) {
        int viewCount = cardActions.getChildCount();
        for (int i = viewCount - 1; i > 0; i--) {
            ActionButton view = (ActionButton) cardActions.getChildAt(i);
            cardActions.removeView(view);
            freeViews.add(view);
        }
    }
}

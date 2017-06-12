package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ui.widget.ActionButton;

final class CardActionFactory {

    private final LayoutInflater layoutInflater;

    CardActionFactory(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    ActionButton inflateActionButton(ViewGroup viewGroup) {
        return (ActionButton) layoutInflater.inflate(R.layout.row_card_action, viewGroup, false);
    }
}

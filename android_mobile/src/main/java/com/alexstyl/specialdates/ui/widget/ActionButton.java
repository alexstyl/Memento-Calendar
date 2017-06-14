package com.alexstyl.specialdates.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.alexstyl.specialdates.datedetails.actions.LabeledAction;

public class ActionButton extends ForegroundImageView {

    public ActionButton(Context context) {
        super(context);
    }

    public ActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(LabeledAction action) {
        setImageResource(action.getIconRes());
    }
}

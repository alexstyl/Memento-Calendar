package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

final class CustomEventTextView extends TextInputLayout implements EventLabelLayout {

    public CustomEventTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void showEventName(String title) {
        getEditText().setText(title);
    }
}

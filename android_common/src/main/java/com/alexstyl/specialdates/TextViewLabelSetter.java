package com.alexstyl.specialdates;

import android.widget.TextView;

public class TextViewLabelSetter implements LabelSetter {
    private final TextView textView;

    public TextViewLabelSetter(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void setLabel(String text) {
        textView.setText(text);
    }
}

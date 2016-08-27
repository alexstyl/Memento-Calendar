package com.alexstyl.specialdates.upcoming.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

public class TitledTextView extends LinearLayout {

    private final TextView title;
    private final TextView text;

    public TitledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_events_card_view, this, true);
        super.setOrientation(VERTICAL);

        title = Views.findById(this, R.id.titled_textview_title);
        text = Views.findById(this, R.id.titled_textview_text);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TitledTextView, 0, 0);

        CharSequence eventTitle = typedArray.getText(R.styleable.TitledTextView_eventTitle);
        title.setText(eventTitle);

        int titleColor = typedArray.getColor(R.styleable.TitledTextView_eventTitleColor, -1);
        title.setTextColor(titleColor);

        typedArray.recycle();

        text.setEllipsize(TextUtils.TruncateAt.END);

    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setTextLines(int lines) {
        this.text.setLines(lines);
    }
}

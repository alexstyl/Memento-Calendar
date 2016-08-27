package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.events.DateDisplayStringCreator;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

public class BirthdayLabelView extends LinearLayout {

    private static final DateDisplayStringCreator DATE_DISPLAY_STRING_CREATOR = DateDisplayStringCreator.getInstance();

    private TextView label;
    private Birthday birthday;
    private OnEditListener listener = OnEditListener.NO_OP;

    public BirthdayLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.merge_birthdaypicker_label_view, this);
        label = Views.findById(this, R.id.birthdaypicker_label);
        label.setOnClickListener(onLabelClickedListener);
    }

    private OnClickListener onLabelClickedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onEdit();
        }
    };

    public void displayBirthday(Birthday birthday) {
        this.birthday = birthday;
        String text = DATE_DISPLAY_STRING_CREATOR.fullyFormattedBirthday(birthday);
        label.setText(text);
    }

    public void displayNoBirthday() {
        this.birthday = null;
        label.setText(null);
    }

    public Birthday getDisplayingBirthday() {
        return birthday;
    }

    public void setOnEditListener(OnEditListener listener) {
        this.listener = listener;
    }

    public interface OnEditListener {
        void onEdit();

        OnEditListener NO_OP = new OnEditListener() {
            @Override
            public void onEdit() {
                Log.w("onEdit() called with no set listener");
            }
        };
    }
}

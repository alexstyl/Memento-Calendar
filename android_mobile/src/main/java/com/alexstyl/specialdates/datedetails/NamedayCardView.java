package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.ui.MementoCardView;
import com.novoda.notils.caster.Views;

import java.util.List;

public class NamedayCardView extends MementoCardView {

    private OnShareClickListener shareClickListener;

    private TextView namesLabelView;
    private Button shareButton;
    private Optional<NamesInADate> namedays = new Optional<>(null);

    public NamedayCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.merge_namedaycardview, this);

        this.namesLabelView = (TextView) findViewById(R.id.namedaycard_names);
        this.shareButton = Views.findById(this, R.id.namedays_card_share);

        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClickListener.onNamedaysShared(namedays.get());
            }
        });

    }

    public void setDisplayingNames(NamesInADate names) {
        if (areDifferentNames(names)) {
            this.namedays = new Optional<>(names);
            List<String> displayingNames = names.getNames();
            if (displayingNames.size() > 0) {
                namesLabelView.setText(TextUtils.join(", ", displayingNames));
                shareButton.setVisibility(VISIBLE);
            } else {
                namesLabelView.setText(R.string.namedays_no_names);
                shareButton.setVisibility(GONE);
            }
        }
    }

    private boolean areDifferentNames(NamesInADate names) {
        return !namedays.contains(names);
    }

    interface OnShareClickListener {
        void onNamedaysShared(NamesInADate namedays);
    }

    public void setOnShareClickListener(OnShareClickListener l) {
        this.shareClickListener = l;
    }

}

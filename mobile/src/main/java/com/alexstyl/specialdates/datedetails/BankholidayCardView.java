package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.ui.MementoCardView;

class BankHolidayCardView extends MementoCardView {

    private final TextView text;

    public BankHolidayCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.merge_bankholidaycardview, this);
        text = (TextView) findViewById(R.id.bankholiday_text);
    }

    public void display(BankHoliday bankholiday) {
        text.setText(bankholiday.getHolidayName());
    }
}

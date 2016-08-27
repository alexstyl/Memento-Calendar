package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexstyl.specialdates.datedetails.BankholidayCardView;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

class BankHolidayCardViewHolder extends RecyclerView.ViewHolder {

    private final BankholidayCardView bankholidayCardView;

    public BankHolidayCardViewHolder(View itemView) {
        super(itemView);
        bankholidayCardView = (BankholidayCardView) itemView;
    }

    void bind(BankHoliday holiday) {
        bankholidayCardView.display(holiday);
    }

}

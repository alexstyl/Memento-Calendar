package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

class BankHolidayCardViewHolder extends RecyclerView.ViewHolder {

    private final BankHolidayCardView bankHolidayCardView;

    BankHolidayCardViewHolder(View itemView) {
        super(itemView);
        bankHolidayCardView = (BankHolidayCardView) itemView;
    }

    void bind(BankHoliday holiday) {
        bankHolidayCardView.display(holiday);
    }

}

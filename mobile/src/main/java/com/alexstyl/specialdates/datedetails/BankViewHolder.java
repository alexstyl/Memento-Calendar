package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

class BankViewHolder extends RecyclerView.ViewHolder {

    private final BankCardView bankCardView;

    BankViewHolder(View itemView) {
        super(itemView);
        bankCardView = (BankCardView) itemView;
    }

    void bind(BankHoliday holiday) {
        bankCardView.display(holiday);
    }

}

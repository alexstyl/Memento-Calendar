package com.alexstyl.specialdates.datedetails;

import android.view.View;

class BankViewHolder extends DateDetailsViewHolder<BankHolidayViewModel> {

    private final BankCardView bankCardView;

    BankViewHolder(View itemView) {
        super(itemView);
        bankCardView = (BankCardView) itemView;
    }

    @Override
    void bind(BankHolidayViewModel viewModel, DateDetailsClickListener listener) {
        bankCardView.display(viewModel.getBankHoliday());

    }

}

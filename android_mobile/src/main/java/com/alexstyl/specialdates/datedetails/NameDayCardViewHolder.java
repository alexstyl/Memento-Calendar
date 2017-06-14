package com.alexstyl.specialdates.datedetails;

import android.view.View;

import com.alexstyl.specialdates.events.namedays.NamesInADate;

class NameDayCardViewHolder extends DateDetailsViewHolder<NamedaysViewModel> {

    private final NamedayCardView namedayCard;

    NameDayCardViewHolder(View itemView, NamedayCardView namedayCard) {
        super(itemView);
        this.namedayCard = namedayCard;

    }

    @Override
    void bind(NamedaysViewModel viewModel, final DateDetailsClickListener listener) {
        namedayCard.setDisplayingNames(viewModel.getNamedays());
        namedayCard.setOnShareClickListener(new NamedayCardView.OnShareClickListener() {
            @Override
            public void onNamedaysShared(NamesInADate namedays) {
                listener.onNamedayShared(namedays);
            }
        });
    }
}

package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

class NameDayCardViewHolder extends RecyclerView.ViewHolder {

    private final NamedayCardView namedayCard;

    NameDayCardViewHolder(View itemView, NamedayCardView.OnShareClickListener shareListener) {
        super(itemView);
        this.namedayCard = (NamedayCardView) itemView.findViewById(R.id.nameday_card);
        this.namedayCard.setOnShareClickListener(shareListener);

    }

    public void bind(NamesInADate namedays) {
        namedayCard.setDisplayingNames(namedays);
    }
}

package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingContactEventViewModel;
import com.alexstyl.specialdates.upcoming.NamedaysViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;
import com.novoda.notils.caster.Views;

import java.util.List;

public class UpcomingEventsView extends FrameLayout {

    private final TitledTextView bankholidaysCard;
    private final TitledTextView namedaysCard;
    private final ContactEventView contactEventCardViewOne;
    private final ContactEventView contactEventCardViewTwo;
    private final Button moreContacts;
    private final View showMoreDivider;

    public UpcomingEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_events_view, this, true);
        bankholidaysCard = Views.findById(this, R.id.upcoming_events_bankholidays);
        bankholidaysCard.setTextLines(1);
        namedaysCard = Views.findById(this, R.id.upcoming_events_namedays);
        contactEventCardViewOne = Views.findById(this, R.id.upcoming_events_contact_one);
        contactEventCardViewTwo = Views.findById(this, R.id.upcoming_events_contact_two);
        moreContacts = Views.findById(this, R.id.upcoming_events_show_more);
        showMoreDivider = Views.findById(this, R.id.upcoming_events_contact_to_more_divider);

    }

    public void bind(final UpcomingEventsViewModel viewModel, final OnUpcomingEventClickedListener listener, ImageLoader imageLoader) {
        bindBankHolidays(viewModel.getBankHolidayViewModel());
        bindNamedays(viewModel.getNamedaysViewModel());
        bindContactEvents(viewModel.getContactViewModels(), listener, imageLoader);
        moreContacts.setText(viewModel.getMoreButtonLabe());
        moreContacts.setVisibility(viewModel.getMoreButtonVisibility());
        showMoreDivider.setVisibility(viewModel.getMoreButtonVisibility());

        moreContacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMoreEventsClicked(viewModel);
            }
        });
    }

    private void bindBankHolidays(BankHolidayViewModel viewModel) {
        bankholidaysCard.setVisibility(viewModel.getBankHolidaysVisibility());
        bankholidaysCard.setText(viewModel.getBankHolidayName());
    }

    private void bindNamedays(NamedaysViewModel viewModel) {
        namedaysCard.setText(viewModel.getNamesLabel());
        namedaysCard.setVisibility(viewModel.getNamedaysVisibility());
        namedaysCard.setTextLines(viewModel.getMaxLines());
    }

    private void bindContactEvents(List<UpcomingContactEventViewModel> viewModels, final OnUpcomingEventClickedListener listener, ImageLoader imageLoader) {
        if (viewModels.size() >= 1) {
            contactEventCardViewOne.bind(viewModels.get(0), listener, imageLoader);
        } else {
            contactEventCardViewOne.setVisibility(GONE);
        }
        if (viewModels.size() >= 2) {
            contactEventCardViewTwo.bind(viewModels.get(1), listener, imageLoader);
        } else {
            contactEventCardViewTwo.setVisibility(GONE);
        }
    }
}

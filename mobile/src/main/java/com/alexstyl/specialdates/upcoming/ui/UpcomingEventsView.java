package com.alexstyl.specialdates.upcoming.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.ContactEvents;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.images.ImageLoader;
import com.novoda.notils.caster.Views;

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

    public void displayBankholidays(BankHoliday bankHoliday) {
        bankholidaysCard.setText(bankHoliday.toString());
        bankholidaysCard.setVisibility(VISIBLE);
    }

    public void hideBankHolidays() {
        bankholidaysCard.setVisibility(GONE);
        bankholidaysCard.setOnClickListener(null);
    }

    public void displayNamedays(NamesInADate daysNamedays) {
        namedaysCard.setText(TextUtils.join(", ", daysNamedays.getNames()));
        namedaysCard.setVisibility(VISIBLE);
    }

    public void hideNamedays() {
        namedaysCard.setVisibility(GONE);
        namedaysCard.setOnClickListener(null);
    }

    public void displayContactEventsFor(final ContactEvents contactEvents, final OnUpcomingEventClickedListener listener, ImageLoader imageLoader) {
        ContactEvent firstEvent = contactEvents.getEvent(0);
        displayContactEventFor(contactEventCardViewOne, listener, firstEvent, imageLoader);

        if (hasAtLeastTwoContacts(contactEvents)) {
            displayContactEventFor(contactEventCardViewTwo, listener, contactEvents.getEvent(1), imageLoader);
        } else {
            contactEventCardViewTwo.setVisibility(GONE);
        }

        if (contactEvents.size() <= 2) {
            moreContacts.setVisibility(View.GONE);
            showMoreDivider.setVisibility(View.GONE);
        } else {
            moreContacts.setVisibility(View.VISIBLE);
            moreContacts.setText(getResources().getString(R.string.plus_x_more, contactEvents.size() - 2));
            showMoreDivider.setVisibility(View.VISIBLE);
            moreContacts.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardPressed(contactEvents.getDate());
                }
            });
        }
    }

    private boolean hasAtLeastTwoContacts(ContactEvents contactEvents) {
        return contactEvents.size() > 1;
    }

    private void displayContactEventFor(ContactEventView view, final OnUpcomingEventClickedListener listener, final ContactEvent firstEvent, ImageLoader imageLoader) {
        view.setVisibility(VISIBLE);
        view.displayEvent(firstEvent, imageLoader);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactEventPressed(v, firstEvent);
            }
        });
    }

    public void hideContactEvents() {
        moreContacts.setVisibility(GONE);
        showMoreDivider.setVisibility(GONE);
        contactEventCardViewOne.setVisibility(GONE);
        contactEventCardViewTwo.setVisibility(GONE);
    }

    public void markAsToday() {
        namedaysCard.setTextLines(3);
        contactEventCardViewOne.setNameTypeface(Typeface.DEFAULT_BOLD);
        contactEventCardViewTwo.setNameTypeface(Typeface.DEFAULT_BOLD);
    }

    public void markAsFutureEvent() {
        namedaysCard.setTextLines(1);
        contactEventCardViewOne.setNameTypeface(Typeface.DEFAULT);
        contactEventCardViewTwo.setNameTypeface(Typeface.DEFAULT);
    }
}

package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.android.AndroidColorResources;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.novoda.notils.caster.Views;

public class ContactEventView extends LinearLayout {

    private final TextView contactNameView;
    private final TextView eventTypeView;
    private final ColorImageView avatarView;
    private final StringResources stringResources;
    private final ColorResources colorResources;

    public ContactEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_events_contact_card_view, this, true);

        contactNameView = Views.findById(this, R.id.upcoming_event_contact_card_contactname);
        eventTypeView = Views.findById(this, R.id.upcoming_event_contact_card_event_type);
        avatarView = Views.findById(this, R.id.upcoming_event_contact_card_avatar);

        setGravity(Gravity.CENTER_VERTICAL);
        stringResources = new AndroidStringResources(getResources());
        colorResources = new AndroidColorResources(getResources());
    }

    public void displayEvent(ContactEvent event, ImageLoader imageLoader) {
        Contact contact = event.getContact();
        contactNameView.setText(contact.getDisplayName().toString());
        displayEventFor(event);
        avatarView.setBackgroundVariant(event.hashCode());
        avatarView.setLetter(event.getContact().getDisplayName().toString());

        imageLoader.loadThumbnail(contact.getImagePath(), avatarView.getImageView());
    }

    private void displayEventFor(ContactEvent event) {
        EventType eventType = event.getType();
        String eventLabel = event.getLabel(stringResources);
        eventTypeView.setText(eventLabel);
        eventTypeView.setTextColor(colorResources.getColor(eventType.getColorRes()));
    }

    public void setNameTypeface(Typeface typeface) {
        contactNameView.setTypeface(typeface);
    }
}

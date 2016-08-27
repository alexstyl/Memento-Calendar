package com.alexstyl.specialdates.datedetails;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.EventType;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

abstract class DateDetailsViewHolder extends RecyclerView.ViewHolder {
    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final ImageLoader imageLoader;
    private final Resources resources;

    DateDetailsViewHolder(View convertView, ImageLoader imageLoader, Resources resources) {
        super(convertView);
        this.imageLoader = imageLoader;
        this.resources = resources;
        this.displayName = (TextView) convertView.findViewById(R.id.contact_name);
        this.eventLabel = (TextView) convertView.findViewById(R.id.event_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.avatar);
    }

    public void bind(ContactEvent event, final ContactCardListener listener) {
        final Contact contact = event.getContact();
        avatar.setBackgroundVariant((int) contact.getContactID());
        String displayNameString = contact.getDisplayName().toString();
        avatar.setLetter(displayNameString);
        displayName.setText(displayNameString);
        imageLoader.loadThumbnail(contact.getImagePath(), avatar.getImageView());
        eventLabel.setVisibility(View.GONE);
        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCardClicked(v, contact);
                    }
                }
        );
        updateEventLabel(event);
        bindActionsFor(contact, listener);
    }

    abstract void bindActionsFor(Contact contact, ContactCardListener listener);

    private void updateEventLabel(ContactEvent event) {
        eventLabel.setTextColor(resources.getColor(event.getType().getColorRes()));
        eventLabel.setVisibility(View.VISIBLE);
        String label = getLabelFor(event);
        eventLabel.setText(label);
    }

    private String getLabelFor(ContactEvent event) {
        EventType eventType = event.getType();
        if (eventType == EventType.BIRTHDAY) {
            Birthday birthday = event.getContact().getBirthday();
            if (birthday.includesYear()) {
                int age = birthday.getAgeOnYear(event.getYear());
                if (age > 0) {
                    return resources.getString(R.string.turns_age, age);
                }
            }
        }
        return resources.getString(eventType.nameRes());
    }

    abstract void clearActions();

}

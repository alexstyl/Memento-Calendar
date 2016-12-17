package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.resources.StringResources;
import com.alexstyl.resources.ColorResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

abstract class DateDetailsViewHolder extends RecyclerView.ViewHolder {
    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final ImageLoader imageLoader;
    private final StringResources stringResources;
    private final ColorResources colorResources;

    DateDetailsViewHolder(View convertView, ImageLoader imageLoader, StringResources stringResources, ColorResources colorResources) {
        super(convertView);
        this.imageLoader = imageLoader;
        this.stringResources = stringResources;
        this.displayName = (TextView) convertView.findViewById(R.id.contact_name);
        this.eventLabel = (TextView) convertView.findViewById(R.id.event_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.avatar);
        this.colorResources = colorResources;
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
        eventLabel.setTextColor(colorResources.getColor(event.getType().getColorRes()));
        eventLabel.setVisibility(View.VISIBLE);
        String label = event.getLabel(stringResources);
        eventLabel.setText(label);
    }

    abstract void clearActions();

}

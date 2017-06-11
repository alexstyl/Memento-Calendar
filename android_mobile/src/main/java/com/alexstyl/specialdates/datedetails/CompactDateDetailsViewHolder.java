package com.alexstyl.specialdates.datedetails;

import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

class CompactDateDetailsViewHolder extends DateDetailsViewHolder<ContactEventViewModel> {

    private final ImageLoader imageLoader;
    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final View actions;

    CompactDateDetailsViewHolder(View itemView, ImageLoader imageLoader, ColorImageView avatar, TextView displayName, TextView eventLabel, View actions) {
        super(itemView);
        this.imageLoader = imageLoader;
        this.avatar = avatar;
        this.displayName = displayName;
        this.eventLabel = eventLabel;
        this.actions = actions;
    }

    @Override
    void bind(final ContactEventViewModel viewModel, final DateDetailsClickListener listener) {
        final Contact contact = viewModel.getContact();
        avatar.setBackgroundVariant((int) contact.getContactID());
        String displayNameString = contact.getDisplayName().toString();
        avatar.setLetter(displayNameString);
        displayName.setText(displayNameString);
        imageLoader.loadImage(contact.getImagePath(), avatar.getImageView());
        eventLabel.setVisibility(View.GONE);
        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCardClicked(contact);
                    }
                }
        );

        eventLabel.setTextColor(viewModel.getEventLabelColor());
        eventLabel.setVisibility(viewModel.getEventLabelVisibility());
        eventLabel.setText(viewModel.getEventLabel());

        actions.setVisibility(viewModel.getActionsVisibility());
        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactActionsMenuClicked(v, contact, viewModel.getActions());
            }
        });
    }

}

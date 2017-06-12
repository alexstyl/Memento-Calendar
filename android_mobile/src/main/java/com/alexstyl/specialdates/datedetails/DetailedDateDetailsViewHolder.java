package com.alexstyl.specialdates.datedetails;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ActionButton;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

class DetailedDateDetailsViewHolder extends DateDetailsViewHolder<ContactEventViewModel> {

    private final ImageLoader imageLoader;
    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final LinearLayout actionsLayout;
    private final CardActionFactory factory;

    DetailedDateDetailsViewHolder(View itemView,
                                  ImageLoader imageLoader,
                                  ColorImageView avatar,
                                  TextView displayName,
                                  TextView eventLabel,
                                  LinearLayout actionsLayout,
                                  CardActionFactory factory) {
        super(itemView);
        this.imageLoader = imageLoader;
        this.avatar = avatar;
        this.displayName = displayName;
        this.eventLabel = eventLabel;
        this.actionsLayout = actionsLayout;
        this.factory = factory;
    }

    @Override
    void bind(ContactEventViewModel viewModel, final DateDetailsClickListener listener) {
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
        eventLabel.setVisibility(View.VISIBLE);
        eventLabel.setText(viewModel.getEventLabel());

        actionsLayout.setVisibility(viewModel.getActionsVisibility());
        actionsLayout.removeAllViews();

        bindActions(viewModel, listener);
    }

    private void bindActions(ContactEventViewModel viewModel, final DateDetailsClickListener listener) {
        for (final LabeledAction action : viewModel.getActions()) {
            ActionButton button = factory.inflateActionButton(actionsLayout);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onActionClicked(action);
                }
            });
            button.bind(action);
            actionsLayout.addView(button);
        }
    }
}

package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.alexstyl.specialdates.upcoming.ContactEventViewModel;
import com.novoda.notils.caster.Views;

public class ContactEventView extends LinearLayout {

    private final TextView contactNameView;
    private final TextView eventTypeView;
    private final ColorImageView avatarView;

    public ContactEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.merge_upcoming_events_contact_card_view, this, true);

        contactNameView = Views.findById(this, R.id.upcoming_event_contact_card_contactname);
        eventTypeView = Views.findById(this, R.id.upcoming_event_contact_card_event_type);
        avatarView = Views.findById(this, R.id.upcoming_event_contact_card_avatar);

        super.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void bind(final ContactEventViewModel contactEventViewModel, final OnUpcomingEventClickedListener listener, ImageLoader imageLoader) {
        contactNameView.setText(contactEventViewModel.getContactName());
        eventTypeView.setText(contactEventViewModel.getEventLabel());
        eventTypeView.setTextColor(contactEventViewModel.getEventColor());
        avatarView.setBackgroundVariant(contactEventViewModel.getBackgroundVariant());
        avatarView.setLetter(contactEventViewModel.getContactName());
        contactNameView.setTypeface(contactEventViewModel.getTypeface());
        imageLoader.displayThumbnail(contactEventViewModel.getContactImagePath(), avatarView.getImageView());
        setVisibility(contactEventViewModel.getContactEventVisibility());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactEventPressed(v, contactEventViewModel.getContact());
            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        throw new UnsupportedOperationException("The gravity for this view is pre-set");
    }
}

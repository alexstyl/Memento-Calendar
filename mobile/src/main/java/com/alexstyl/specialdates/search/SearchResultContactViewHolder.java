package com.alexstyl.specialdates.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

import java.util.List;

class SearchResultContactViewHolder extends RecyclerView.ViewHolder {

    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final ImageLoader imageLoader;
    private final StringResources stringResources;

    public static SearchResultContactViewHolder createFor(ViewGroup parent,
                                                          ImageLoader imageLoader,
                                                          StringResources stringResources) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_search_result_contact, parent, false);
        return new SearchResultContactViewHolder(view, imageLoader, stringResources);
    }

    private SearchResultContactViewHolder(View convertView, ImageLoader imageLoader, StringResources stringResources) {
        super(convertView);
        this.imageLoader = imageLoader;
        this.displayName = (TextView) convertView.findViewById(R.id.contact_name);
        this.eventLabel = (TextView) convertView.findViewById(R.id.search_result_event_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.avatar);
        this.stringResources = stringResources;
    }

    void bind(final ContactWithEvents contactWithEvents, final SearchResultAdapter.SearchResultClickListener mListener) {
        final Contact contact = contactWithEvents.getContact();
        avatar.setBackgroundVariant((int) contact.getContactID());
        String displayNameString = contact.getDisplayName().toString();
        this.displayName.setText(displayNameString);
        avatar.setLetter(displayNameString, true);

        imageLoader.loadThumbnail(contact.getImagePath(), avatar.getImageView());

        bindBirthday(contactWithEvents);

        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onContactClicked(v, contact);
                    }
                }
        );
    }

    private void bindBirthday(ContactWithEvents contactWithEvents) {
        List<ContactEvent> events = contactWithEvents.getEvents();
        if (events.size() > 0) {
            ContactEvent contactEvent = events.get(0);
            String message = getBirthdayString(getContext(), contactEvent);
            this.eventLabel.setVisibility(View.VISIBLE);
            this.eventLabel.setText(message);
        } else {
            this.eventLabel.setVisibility(View.GONE);
        }
    }

    private String getBirthdayString(Context context, ContactEvent birthday) {
        String eventLabel = birthday.getType().getEventName(stringResources);
        return getEventString(context, eventLabel, birthday.getDate());
    }

    private static String getEventString(Context context, String stringRes, Date date) {
        String message =
                DateFormatUtils.formatTimeStampString(
                        context,
                        date.toMillis(),
                        false
                );
        return stringRes + " " + message;
    }

    private Context getContext() {
        return itemView.getContext();
    }
}

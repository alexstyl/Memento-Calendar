package com.alexstyl.specialdates.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.Optional;
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
    private final TextView birthday;
    private final TextView nameday;

    private final ImageLoader imageLoader;

    public static SearchResultContactViewHolder createFor(ViewGroup parent,
                                                          ImageLoader imageLoader) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_search_result_contact, parent, false);
        return new SearchResultContactViewHolder(view, imageLoader);
    }

    private SearchResultContactViewHolder(View convertView, ImageLoader imageLoader) {
        super(convertView);
        this.imageLoader = imageLoader;
        this.displayName = (TextView) convertView.findViewById(R.id.contact_name);
        this.birthday = (TextView) convertView.findViewById(R.id.birthday_label);
        this.nameday = (TextView) convertView.findViewById(R.id.nameday_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.avatar);
    }

    void bind(final ContactWithEvents contactWithEvents, final SearchResultAdapter.SearchResultClickListener mListener) {
        final Contact contact = contactWithEvents.getContact();
        avatar.setBackgroundVariant((int) contact.getContactID());
        String displayNameString = contact.getDisplayName().toString();
        this.displayName.setText(displayNameString);
        avatar.setLetter(displayNameString, true);

        imageLoader.loadThumbnail(contact.getImagePath(), avatar.getImageView());

        bindBirthday(contactWithEvents);
        bindNamedays(contactWithEvents);

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
        Optional<ContactEvent> contactEvent = contactWithEvents.getBirthday();
        if (contactEvent.isPresent()) {
            Date date = contactEvent.get().getDate();
            String message = getBirthdayString(getContext(), date);
            this.birthday.setVisibility(View.VISIBLE);
            this.birthday.setText(message);
        } else {
            this.birthday.setVisibility(View.GONE);
        }
    }

    private void bindNamedays(ContactWithEvents contact) {
        List<ContactEvent> namedays = contact.getNamedays();
        if (namedays.isEmpty()) {
            nameday.setVisibility(View.GONE);
        } else {
            // we are only displaying 1 date instead of all of them
            // with Person details this is fixed though
            String message = getNamedayString(getContext(), namedays.get(0).getDate());
            nameday.setVisibility(View.VISIBLE);
            nameday.setText(message);
        }

    }

    private static String getBirthdayString(Context context, Date birthday) {
        return getEventString(context, R.string.birthday, birthday);
    }

    private static String getNamedayString(Context context, Date date) {
        return getEventString(context, R.string.nameday, date);
    }

    private static String getEventString(Context context, int stringRes, Date date) {
        String message =
                DateFormatUtils.formatTimeStampString(
                        context,
                        date.toMillis(),
                        false
                );
        return context.getString(stringRes) + " " + message;
    }

    private Context getContext() {
        return itemView.getContext();
    }
}

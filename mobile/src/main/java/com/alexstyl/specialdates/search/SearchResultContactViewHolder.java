package com.alexstyl.specialdates.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

class SearchResultContactViewHolder extends RecyclerView.ViewHolder {
    private final NamedayCalendar namedayCalendar;
    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView birthday;
    private final TextView nameday;

    private final ImageLoader imageLoader;
    private boolean namedayEnabled;

    public static SearchResultContactViewHolder createFor(ViewGroup parent,
                                                          NamedayCalendar namedayCalendar,
                                                          ImageLoader imageLoader,
                                                          boolean namedayEnabled) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_search_result_contact, parent, false);
        return new SearchResultContactViewHolder(view, namedayCalendar, imageLoader, namedayEnabled);
    }

    SearchResultContactViewHolder(View convertView, NamedayCalendar namedayCalendar, ImageLoader imageLoader, boolean namedayEnabled) {
        super(convertView);
        this.namedayCalendar = namedayCalendar;
        this.imageLoader = imageLoader;
        this.namedayEnabled = namedayEnabled;
        this.displayName = (TextView) convertView.findViewById(R.id.contact_name);
        this.birthday = (TextView) convertView.findViewById(R.id.birthday_label);
        this.nameday = (TextView) convertView.findViewById(R.id.nameday_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.avatar);
    }

    void bind(final Contact contact, final SearchResultAdapter.SearchResultClickListener mListener) {
        avatar.setBackgroundVariant((int) contact.getContactID());
        String displayNameString = contact.getDisplayName().toString();
        this.displayName.setText(displayNameString);
        avatar.setLetter(displayNameString, true);

        imageLoader.loadThumbnail(contact.getImagePath(), avatar.getImageView());

        bindBirthday(contact);
        bindNamedays(contact);

        itemView.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        mListener.onContactClicked(v, contact);
                    }
                }

        );
    }

    private void bindBirthday(Contact contact) {
        if (contact.hasBirthday()) {
            Birthday birthday = contact.getBirthday();
            String message = getBirthdayString(getContext(), birthday);
            this.birthday.setVisibility(View.VISIBLE);
            this.birthday.setText(message);
        } else {
            this.birthday.setVisibility(View.GONE);
        }
    }

    private void bindNamedays(Contact contact) {
        NameCelebrations namedays = namedayCalendar.getAllNamedays(contact.getGivenName());
        if (noEventsToDisplay(namedays)) {
            nameday.setVisibility(View.GONE);
        } else {
            // we are only displaying 1 date instead of all of them
            // with Person details this is fixed though
            String message = getNamedayString(getContext(), namedays.getDate(0));
            nameday.setVisibility(View.VISIBLE);
            nameday.setText(message);
        }

    }

    public static String getBirthdayString(Context context, Birthday birthday) {
        DayDate date1 = DayDate.newInstance(birthday.getDayOfMonth(), birthday.getMonth(), DayDate.today().getYear());
        return getEventString(context, R.string.birthday, date1);
    }

    /**
     * Returns a 'Nameday on X' string
     *
     * @param context The context to use
     * @param date    The date of the event
     */
    public static String getNamedayString(Context context, DayDate date) {
        return getEventString(context, R.string.nameday, date);
    }

    private static String getEventString(Context context, int stringRes, DayDate date) {
        String message =
                DateFormatUtils.formatTimeStampString(
                        context,
                        date.toMillis(),
                        false
                );
        return context.getString(stringRes) + " " + message;
    }

    private boolean noEventsToDisplay(NameCelebrations namedays) {
        return !namedayEnabled || namedays.containsNoDate();
    }

    private Context getContext() {
        return itemView.getContext();
    }
}

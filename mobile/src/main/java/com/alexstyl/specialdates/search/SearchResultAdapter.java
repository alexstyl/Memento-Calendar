package com.alexstyl.specialdates.search;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Contact> contacts = new ArrayList<>();

    private NamedayCard namedayCard = new NamedayCard();

    private boolean isLoadingMore;
    private final boolean namedayEnabled;
    private boolean canLoadMore = false;
    private final ImageLoader imageLoader;
    private final NamedayCalendar namedayCalendar;
    private String searchQuery;

    public static SearchResultAdapter newInstance(Context context, boolean loadNamedays) {
        Resources resources = context.getResources();
        ImageLoader imageLoader = ImageLoader.createSquareThumbnailLoader(resources);

        int year = DayDate.today().getYear();
        NamedayLocale locale = NamedayPreferences.newInstance(context).getSelectedLanguage();
        NamedayCalendar namedayCalendar = NamedayCalendarProvider.newInstance(context)
                .loadNamedayCalendarForLocale(locale, year);
        return new SearchResultAdapter(loadNamedays, imageLoader, namedayCalendar);
    }

    private SearchResultAdapter(boolean loadNamedays, ImageLoader imageLoader, NamedayCalendar namedayCalendar) {
        this.namedayEnabled = loadNamedays;
        this.imageLoader = imageLoader;
        this.namedayCalendar = namedayCalendar;
    }

    public void updateSearchResults(SearchResults searchResults) {
        this.searchQuery = searchResults.getSearchQuery();
        this.contacts.clear();
        this.contacts.addAll(searchResults.getContacts());

        if (this.canLoadMore != searchResults.canLoadMore()) {
            this.canLoadMore = searchResults.canLoadMore();
        }
        notifyDataSetChanged();
    }

    public void setNamedays(NameCelebrations namedays) {
        namedayCard.setNameday(namedays);
        notifyDataSetChanged();
    }

    public void notifyIsLoadingMore() {
        isLoadingMore = true;
        notifyDataSetChanged();
    }

    public void clearResults() {
        this.contacts.clear();
        this.namedayCard.clear();
        this.isLoadingMore = false;
        this.canLoadMore = false;
        notifyDataSetChanged();
    }

    public interface SearchResultClickListener {

        /**
         * Called when a contact has been selected.
         *
         * @param v       The view the user clicked
         * @param contact The contact represented by the view
         */
        void onContactClicked(View v, Contact contact);

        /**
         * Called when the user has selected a specific day of a nameday
         *
         * @param v          The view the user clicked
         * @param month      The month of the nameday
         * @param dayOfMonth The day of the month of the nameday
         */
        void onNamedayClicked(View v, int month, int dayOfMonth);
    }

    private SearchResultClickListener listener;

    public void setSearchResultClickListener(SearchResultClickListener l) {
        this.listener = l;
    }

    private static final int VIEWTYPE_CONTACTVIEW = 0;
    private static final int VIEWTYPE_NAMEDAYS_VIEW = 1;
    private static final int VIEWTYPE_LOAD_MORE = 2;
    private static final int VIEWTYPE_NO_RESULTS = 3;

    @Override
    public int getItemViewType(int position) {

        if (loadMoreCardAt(position)) {
            return VIEWTYPE_LOAD_MORE;
        }
        if (namedayCardAt(position)) {
            return VIEWTYPE_NAMEDAYS_VIEW;
        }
        if (containsNoResults()) {
            return VIEWTYPE_NO_RESULTS;
        }
        return VIEWTYPE_CONTACTVIEW;
    }

    private boolean loadMoreCardAt(int position) {
        return canLoadMore && position == getItemCount() - 1;
    }

    private boolean namedayCardAt(int position) {
        return namedayCard.isAvailable() && position == 0;
    }

    private boolean containsNoResults() {
        return contacts.size() == 0 && !namedayCard.isAvailable();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEWTYPE_CONTACTVIEW) {
            return SearchResultContactViewHolder.createFor(parent, namedayCalendar, imageLoader, namedayEnabled);
        }
        if (viewType == VIEWTYPE_NAMEDAYS_VIEW) {
            return SearchResultNamedayViewHolder.createFor(parent);
        }

        if (viewType == VIEWTYPE_LOAD_MORE) {
            return MoreViewHolder.createFor(parent);
        }
        if (viewType == VIEWTYPE_NO_RESULTS) {
            return NoResultsViewHolder.createFor(parent);
        }

        throw new DeveloperError("No Holder created for type " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

        int type = getItemViewType(position);
        if (type == VIEWTYPE_CONTACTVIEW) {
            if (isDisplayingNamedayCard()) {
                // nameday adds one more row
                position = position - 1;
            }
            Contact contact = contacts.get(position);
            SearchResultContactViewHolder viewHolder = (SearchResultContactViewHolder) vh;
            viewHolder.bind(contact, listener);
        } else if (type == VIEWTYPE_NAMEDAYS_VIEW) {
            ((SearchResultNamedayViewHolder) vh).bind(namedayCard, listener);
        } else if (type == VIEWTYPE_LOAD_MORE) {
            ((MoreViewHolder) vh).bind(isLoadingMore);
        } else if (type == VIEWTYPE_NO_RESULTS) {
            // no special binding needed
        } else {
            throw new DeveloperError("Unhandled type " + type);
        }

    }

    private boolean isDisplayingNamedayCard() {
        return namedayCard.isAvailable();
    }

    @Override
    public int getItemCount() {
        return contacts.size()
                + (namedayCard.isAvailable() ? 1 : 0)
                + (canLoadMore ? 1 : 0)
                + (shouldDisplayEmptyRow() ? 1 : 0);
    }

    private boolean shouldDisplayEmptyRow() {
        return hasSearchQuery() && (!canLoadMore && containsNoResults());
    }

    private boolean hasSearchQuery() {
        return !TextUtils.isEmpty(searchQuery);
    }

}

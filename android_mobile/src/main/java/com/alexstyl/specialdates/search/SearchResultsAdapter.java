package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.images.ImageLoader;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

public final class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ContactEventViewModel> searchResults = new ArrayList<>();

    private NamedayCard namedayCard = new NamedayCard();

    private boolean isLoadingMore;
    private boolean canLoadMore = false;
    private final ImageLoader imageLoader;
    private final DateLabelCreator labelCreator;

    SearchResultsAdapter(ImageLoader imageLoader, DateLabelCreator labelCreator) {
        this.imageLoader = imageLoader;
        this.labelCreator = labelCreator;
    }

    void updateSearchResults(SearchResults searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults.getViewModels());

        if (this.canLoadMore != searchResults.getCanLoadMore()) {
            this.canLoadMore = searchResults.getCanLoadMore();
        }
        notifyDataSetChanged();
    }

    public void setNamedays(NameCelebrations namedays) {
        namedayCard.setNameday(namedays);
        notifyDataSetChanged();
    }

    void notifyIsLoadingMore() {
        isLoadingMore = true;
        notifyDataSetChanged();
    }

    void clearResults() {
        this.searchResults.clear();
        this.namedayCard.clear();
        this.isLoadingMore = false;
        this.canLoadMore = false;
        notifyDataSetChanged();
    }

    interface SearchResultClickListener {

        void onContactClicked(Contact contact);

        void onNamedayClicked(Date date);
    }

    private SearchResultClickListener listener;

    void setSearchResultClickListener(SearchResultClickListener l) {
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
        return searchResults.size() == 0 && !namedayCard.isAvailable();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEWTYPE_CONTACTVIEW) {
            return SearchResultContactViewHolder.createFor(parent, imageLoader);
        }
        if (viewType == VIEWTYPE_NAMEDAYS_VIEW) {
            return SearchResultNamedayViewHolder.createFor(parent, labelCreator);
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
            ContactEventViewModel viewModel = searchResults.get(position);
            SearchResultContactViewHolder viewHolder = (SearchResultContactViewHolder) vh;
            viewHolder.bind(viewModel, listener);
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
        // TODO move all this to presenter's viewmodel logic
        return searchResults.size()
                + (namedayCard.isAvailable() ? 1 : 0)
                + (canLoadMore ? 1 : 0)
                + (shouldDisplayEmptyRow() ? 1 : 0);
    }

    private boolean shouldDisplayEmptyRow() {
        return hasSearchQuery() && (!canLoadMore && containsNoResults());
    }

    private boolean hasSearchQuery() {
        return !TextUtils.isEmpty("");
    }

}

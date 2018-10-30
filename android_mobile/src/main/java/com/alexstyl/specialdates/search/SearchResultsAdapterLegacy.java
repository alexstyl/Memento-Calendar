package com.alexstyl.specialdates.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alexstyl.specialdates.images.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapterLegacy extends RecyclerView.Adapter<SearchResultViewHolder> {

    private static final int VIEWTYPE_CONTACT = 0;
    private static final int VIEWTYPE_NAMEDAY = 1;
    private static final int VIEWTYPE_PERMISSION = 2;

    private final ImageLoader imageLoader;
    private final SearchResultClickListener listener;

    public SearchResultsAdapterLegacy(ImageLoader imageLoader, SearchResultClickListener listener) {
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    private List<SearchResultViewModel> searchResults = new ArrayList<>();


    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResultViewModel viewModel = searchResults.get(position);
        holder.bind(viewModel, listener);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }


    @Override
    public int getItemViewType(int position) {

        SearchResultViewModel searchResultViewModel = searchResults.get(position);
        if (searchResultViewModel instanceof SearchResultViewModel.NamedaySearchResultViewModel) {
            return VIEWTYPE_NAMEDAY;
        } else if (searchResultViewModel instanceof SearchResultViewModel.ContactSearchResultViewModel) {
            return VIEWTYPE_CONTACT;
        } else if (searchResultViewModel == SearchResultViewModel.ContactReadPermissionRequestViewModel.INSTANCE) {
            return VIEWTYPE_PERMISSION;
        }
        throw new IllegalStateException("Cannot create ViewHolder for viewType [$parent]");
    }
}


package com.alexstyl.specialdates.upcoming.view;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;

import java.util.ArrayList;
import java.util.List;

class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingRowViewHolder> {

    private final UpcomingViewHolderFactory viewHolderFactory;

    private final List<UpcomingRowViewModel> viewModels = new ArrayList<>();
    private OnUpcomingEventClickedListener listener;

    UpcomingEventsAdapter(UpcomingViewHolderFactory viewHolderFactory) {
        this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public UpcomingRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.createFor(viewType, parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(UpcomingRowViewHolder holder, int position) {
        holder.bind(viewModels.get(position), listener);
    }

    @Override
    @UpcomingRowViewType
    public int getItemViewType(int position) {
        UpcomingRowViewModel viewModel = viewModels.get(position);
        return viewModel.getViewType();
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    void displayUpcomingEvents(List<UpcomingRowViewModel> upcomingEventRows, OnUpcomingEventClickedListener listener) {
        this.listener = listener;

        final UpcomingDiffCallback diffCallback = new UpcomingDiffCallback(viewModels, upcomingEventRows);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.viewModels.clear();
        this.viewModels.addAll(upcomingEventRows);
        diffResult.dispatchUpdatesTo(this);
    }

}

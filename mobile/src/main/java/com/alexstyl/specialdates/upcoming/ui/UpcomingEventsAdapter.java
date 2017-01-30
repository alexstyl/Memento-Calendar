package com.alexstyl.specialdates.upcoming.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;

import java.util.List;

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingRowViewHolder> {

    private final UpcomingViewHolderFactory viewHolderFactory;

    private List<UpcomingRowViewModel> viewModels;
    private OnUpcomingEventClickedListener listener;

    public UpcomingEventsAdapter(UpcomingViewHolderFactory viewHolderFactory) {
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

    public void displayUpcomingEvents(List<UpcomingRowViewModel> upcomingEventRows, OnUpcomingEventClickedListener listener) {
        this.listener = listener;
        this.viewModels = upcomingEventRows;
        notifyDataSetChanged();
    }

    public int getClosestDayPosition() {
        // the first item in the list is always the closest one to the today date
        return 0;
    }
}

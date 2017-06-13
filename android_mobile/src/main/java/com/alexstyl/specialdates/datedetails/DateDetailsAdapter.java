package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class DateDetailsAdapter extends RecyclerView.Adapter<DateDetailsViewHolder> {

    private final DateDetailsViewHolderFactory viewHolderFactory;
    private final DateDetailsClickListener dateDetailsClickListener;
    private final List<DateDetailsViewModel> viewModels = new ArrayList<>();

    DateDetailsAdapter(DateDetailsViewHolderFactory viewHolderFactory, DateDetailsClickListener dateDetailsClickListener) {
        this.viewHolderFactory = viewHolderFactory;
        this.dateDetailsClickListener = dateDetailsClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public DateDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.createFor(viewType, parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(DateDetailsViewHolder holder, int position) {
        holder.bind(viewModels.get(position), dateDetailsClickListener);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        DateDetailsViewModel viewModel = viewModels.get(position);
        return viewModel.getViewType();
    }

    void displayEvents(List<DateDetailsViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        updateHeaderCount();
        notifyDataSetChanged();
    }

    private int headerCount = 0;

    private void updateHeaderCount() {
        int sum = 0;
        for (DateDetailsViewModel viewModel : viewModels) {
            int viewType = viewModel.getViewType();
            if (viewType == DateDetailsViewType.BANKHOLIDAY
                    || viewType == DateDetailsViewType.NAMEDAY) {
                sum++;
            }
        }
        this.headerCount = sum;
    }

    int getHeaderCount() {
        return headerCount;
    }
}

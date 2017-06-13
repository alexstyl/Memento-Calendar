package com.alexstyl.specialdates.upcoming;

import android.support.v7.util.DiffUtil;

import java.util.List;

final class UpcomingDiffCallback extends DiffUtil.Callback {
    private final List<UpcomingRowViewModel> oldModels;
    private final List<UpcomingRowViewModel> newModels;

    UpcomingDiffCallback(List<UpcomingRowViewModel> oldModels, List<UpcomingRowViewModel> newModels) {
        this.oldModels = oldModels;
        this.newModels = newModels;
    }

    @Override
    public int getOldListSize() {
        return oldModels.size();
    }

    @Override
    public int getNewListSize() {
        return newModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areOfSameType(oldItemPosition, newItemPosition)
                && oldModels.get(oldItemPosition).getId() == newModels.get(newItemPosition).getId();
    }

    private boolean areOfSameType(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).getViewType() == newModels.get(newItemPosition).getViewType();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).equals(newModels.get(newItemPosition));
    }
}

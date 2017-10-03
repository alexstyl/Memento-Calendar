package com.alexstyl.specialdates.events.namedays.activity;

import android.support.v7.util.DiffUtil;

import java.util.List;

final class NamedaysViewModelDiff extends DiffUtil.Callback {

    private final List<NamedayScreenViewModel> oldModels;
    private final List<NamedayScreenViewModel> newModels;

    NamedaysViewModelDiff(List<NamedayScreenViewModel> oldModels, List<NamedayScreenViewModel> newModels) {
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
        return oldModels.get(oldItemPosition).getViewType() == newModels.get(newItemPosition).getViewType()
                && oldModels.get(oldItemPosition).getId() == newModels.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).equals(newModels.get(newItemPosition));
    }
}

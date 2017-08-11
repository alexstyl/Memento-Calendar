package com.alexstyl.specialdates.events.namedays.activity;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class NamedaysViewModelDiff extends DiffUtil.Callback {

    private final List<NamedaysViewModel> oldModels;
    private final List<NamedaysViewModel> newModels;

    public NamedaysViewModelDiff(List<NamedaysViewModel> oldModels, List<NamedaysViewModel> newModels) {
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
        // the identifier here is the name itself
        return oldModels.get(oldItemPosition).getName()
                .equals(newModels.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).getContacts()
                .equals(newModels.get(newItemPosition).getContacts());
    }
}

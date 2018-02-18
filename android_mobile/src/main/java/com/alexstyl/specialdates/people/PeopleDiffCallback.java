package com.alexstyl.specialdates.people;

import android.support.v7.util.DiffUtil;

import java.util.List;

class PeopleDiffCallback extends DiffUtil.Callback {

    private final List<PeopleViewModel> oldViewModels;
    private final List<PeopleViewModel> newViewModels;

    PeopleDiffCallback(List<PeopleViewModel> oldViewModels, List<PeopleViewModel> newViewModels) {
        this.oldViewModels = oldViewModels;
        this.newViewModels = newViewModels;
    }

    @Override
    public int getOldListSize() {
        return oldViewModels.size();
    }

    @Override
    public int getNewListSize() {
        return newViewModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        PeopleViewModel oldViewModel = oldViewModels.get(oldItemPosition);
        PeopleViewModel newViewModel = newViewModels.get(newItemPosition);

        return (oldViewModel.getPersonId() == newViewModel.getPersonId()) &&
                oldViewModel.getPersonSource() == newViewModel.getPersonSource();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        PeopleViewModel oldViewModel = oldViewModels.get(oldItemPosition);
        PeopleViewModel newViewModel = newViewModels.get(newItemPosition);

        return (oldViewModel.getPersonName().equals(newViewModel.getPersonName())) &&
                oldViewModel.getAvatarURI() == newViewModel.getAvatarURI();
    }
}

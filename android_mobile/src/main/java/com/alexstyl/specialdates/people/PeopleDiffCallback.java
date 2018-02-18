package com.alexstyl.specialdates.people;

import android.support.v7.util.DiffUtil;

import java.util.List;

class PeopleDiffCallback extends DiffUtil.Callback {

    private final List<PeopleRowViewModel> oldViewModels;
    private final List<PeopleRowViewModel> newViewModels;

    PeopleDiffCallback(List<PeopleRowViewModel> oldViewModels, List<PeopleRowViewModel> newViewModels) {
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
        PeopleRowViewModel oldViewModel = oldViewModels.get(oldItemPosition);
        PeopleRowViewModel newViewModel = newViewModels.get(newItemPosition);

        if (oldViewModel instanceof FacebookViewModel && newViewModel instanceof FacebookViewModel) {
            return true;
        }
        if (oldViewModel instanceof PersonViewModel && newViewModel instanceof PersonViewModel) {
            PersonViewModel oldPersonViewModel = (PersonViewModel) oldViewModels.get(oldItemPosition);
            PersonViewModel newPersonViewModel = (PersonViewModel) newViewModels.get(newItemPosition);
            return (oldPersonViewModel.getPersonId() == newPersonViewModel.getPersonId()) &&
                    oldPersonViewModel.getPersonSource() == newPersonViewModel.getPersonSource();
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        PeopleRowViewModel oldViewModel = oldViewModels.get(oldItemPosition);
        PeopleRowViewModel newViewModel = newViewModels.get(newItemPosition);

        if (oldViewModel instanceof FacebookViewModel && newViewModel instanceof FacebookViewModel) {
            return true;
        }

        if (oldViewModel instanceof PersonViewModel && newViewModel instanceof PersonViewModel) {
            PersonViewModel oldPersonViewModel = (PersonViewModel) oldViewModels.get(oldItemPosition);
            PersonViewModel newPersonViewModel = (PersonViewModel) newViewModels.get(newItemPosition);

            return (oldPersonViewModel.getPersonName().equals(newPersonViewModel.getPersonName())) &&
                    oldPersonViewModel.getAvatarURI() == newPersonViewModel.getAvatarURI();
        }

        return false;
    }
}

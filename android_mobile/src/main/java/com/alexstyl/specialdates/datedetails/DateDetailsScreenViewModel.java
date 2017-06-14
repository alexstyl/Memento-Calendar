package com.alexstyl.specialdates.datedetails;

import java.util.List;

class DateDetailsScreenViewModel {

    private final List<DateDetailsViewModel> viewModels;
    private final int spanCount;
    private final DateDetailsViewHolderFactory viewHolderFactory;

    DateDetailsScreenViewModel(List<DateDetailsViewModel> viewModels, int spanCount, DateDetailsViewHolderFactory viewHolderFactory) {
        this.viewModels = viewModels;
        this.spanCount = spanCount;
        this.viewHolderFactory = viewHolderFactory;
    }

    public List<DateDetailsViewModel> getViewModels() {
        return viewModels;
    }

    int getSpanCount() {
        return spanCount;
    }

    DateDetailsViewHolderFactory getViewHolderFactory() {
        return viewHolderFactory;
    }
}

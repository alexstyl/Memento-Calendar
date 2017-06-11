package com.alexstyl.specialdates.datedetails;

import java.util.List;

class DateDetailsScreenViewModel {

    private final List<DateDetailsViewModel> viewModels;
    private final int spanCount;

    DateDetailsScreenViewModel(List<DateDetailsViewModel> viewModels, int spanCount) {
        this.viewModels = viewModels;
        this.spanCount = spanCount;
    }

    public List<DateDetailsViewModel> getViewModels() {
        return viewModels;
    }

    int getSpanCount() {
        return spanCount;
    }
}

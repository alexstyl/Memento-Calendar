package com.alexstyl.specialdates.upcoming;

import java.util.List;

interface UpcomingListMVPView {

    void showLoading();

    void display(List<UpcomingRowViewModel> events);
}

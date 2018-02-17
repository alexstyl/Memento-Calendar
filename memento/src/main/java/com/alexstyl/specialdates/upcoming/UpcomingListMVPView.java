package com.alexstyl.specialdates.upcoming;

import java.util.List;

public interface UpcomingListMVPView {

    void showLoading();

    void display(List<UpcomingRowViewModel> events);

    boolean isEmpty();

    void askForContactPermission();
}

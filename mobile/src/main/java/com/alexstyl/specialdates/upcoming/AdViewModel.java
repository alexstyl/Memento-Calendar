package com.alexstyl.specialdates.upcoming;

final public class AdViewModel implements UpcomingRowViewModel {
    @Override
    public int getViewType() {
        return UpcomingRowViewType.AD;
    }

    @Override
    public long getId() {
        return 1337;
    }
}

package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.GridLayoutManager;

final class DateDetailsSpanLookup extends GridLayoutManager.SpanSizeLookup {
    public static final int FULL_SPAN = 2;
    public static final int HALF_SPAN = 1;
    private final DateDetailsAdapter adapter;

    DateDetailsSpanLookup(DateDetailsAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize(int position) {
        int viewType = adapter.getItemViewType(position);
        if (viewType == DateDetailsViewType.BANKHOLIDAY ||
                viewType == DateDetailsViewType.NAMEDAY) {
            return FULL_SPAN;
        }
        return HALF_SPAN;
    }
}

package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.GridLayoutManager;

final class DateDetailsSpanLookup extends GridLayoutManager.SpanSizeLookup {
    public static final int HALF_SPAN = 1;
    public static final int FULL_SPAN = 2;
    private final DateDetailsAdapter adapter;
    private final GridLayoutManager layoutManager;

    DateDetailsSpanLookup(DateDetailsAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        int viewType = adapter.getItemViewType(position);
        if (viewType == DateDetailsViewType.BANKHOLIDAY ||
                viewType == DateDetailsViewType.NAMEDAY ||
                viewType == DateDetailsViewType.RATE_APP) {
            return layoutManager.getSpanCount();
        }
        return HALF_SPAN;
    }
}

package com.alexstyl.specialdates.util;

import android.widget.Filter;

/**
 * <p>Created by alexstyl on 17/05/15.</p>
 */
public class DeviceContactsFilter extends Filter {


    @Override
    public String convertResultToString(Object resultValue) {
        return resultValue.toString();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {


    }
}

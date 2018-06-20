package com.alexstyl.specialdates.search;

import android.widget.Filter;

import com.alexstyl.specialdates.Optional;

import java.util.ArrayList;
import java.util.List;

final class NamesFilter extends Filter {

    private static final Optional<String> NO_IGNORED_RESULT = Optional.Companion.absent();
    private final NameSuggestionsAdapter adapter;
    private final NameFilter nameFilter;
    private Optional<String> ignoredResult = NO_IGNORED_RESULT;

    NamesFilter(NameSuggestionsAdapter adapter, NameFilter nameFilter) {
        this.adapter = adapter;
        this.nameFilter = nameFilter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint == null) {
            List<String> allNames = nameFilter.getAllNames();
            results.values = allNames;
            results.count = allNames.size();
        } else {
            List<String> names = nameFilter.performFiltering(constraint);
            results.count = names.size();
            results.values = names;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence typedName, FilterResults results) {
        if (results.values == null) {
            // This is a fallback in case anything breaks the filtering
            results.values = nameFilter.getAllNames();
        }
        ArrayList<String> names = (ArrayList<String>) results.values;
        if (ignoredResult.isPresent()) {
            names.remove(ignoredResult.get());
        }
        adapter.updateNames(names);
    }

    @Override
    public CharSequence convertResultToString(Object resultValue) {
        return resultValue.toString();
    }

    void ignoreResults(String ignoredResult) {
        if (ignoredResult.length() > 0) {
            this.ignoredResult = new Optional<>(ignoredResult);
        } else {
            this.ignoredResult = NO_IGNORED_RESULT;
        }
    }
}

package com.alexstyl.specialdates.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.alexstyl.specialdates.SoundWordComparator;
import com.alexstyl.specialdates.WordComparator;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;

import java.util.ArrayList;
import java.util.List;

import static com.alexstyl.specialdates.date.DateExtKt.todaysDate;

final class NameSuggestionsAdapter extends RecyclerView.Adapter<SuggstedNameViewHolder> implements Filterable {

    private final OnNameSelectedListener listener;
    private final NamesFilter filter;
    private final List<String> displayingNames;

    public static NameSuggestionsAdapter newInstance(OnNameSelectedListener onNameSelectedListener,
                                                     NamedayUserSettings namedayPreferences,
                                                     NamedayCalendarProvider namedayCalendarProvider) {
        NamedayLocale locale = namedayPreferences.getSelectedLanguage();
        WordComparator compatator;
        if (locale.isComparedBySound()) {
            compatator = new SoundWordComparator();
        } else {
            compatator = new CaseInsensitiveComparator();
        }
        int year = todaysDate().getYear();
        NamedayCalendar namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(locale, year);
        List<String> names = namedayCalendar.getAllNames();
        return new NameSuggestionsAdapter(new NameFilter(names, compatator), onNameSelectedListener);
    }

    private NameSuggestionsAdapter(NameFilter nameFilter, OnNameSelectedListener onNameSelectedListener) {
        this.listener = onNameSelectedListener;
        this.displayingNames = new ArrayList<>();
        this.filter = new NamesFilter(this, nameFilter);
    }

    @Override
    public SuggstedNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SuggstedNameViewHolder.createFor(parent);
    }

    @Override
    public void onBindViewHolder(SuggstedNameViewHolder holder, int position) {
        holder.bind(displayingNames.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return displayingNames.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    void clearNames() {
        this.displayingNames.clear();
        notifyDataSetChanged();
    }

    void updateNames(ArrayList<String> names) {
        displayingNames.clear();
        displayingNames.addAll(names);
        notifyDataSetChanged();
    }

    void setTextTyped(String textTyped) {
        filter.ignoreResults(textTyped);
    }

    interface OnNameSelectedListener {
        void onNameSelected(String name);
    }

}

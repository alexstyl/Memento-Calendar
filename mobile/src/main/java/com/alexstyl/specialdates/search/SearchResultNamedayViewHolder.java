package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.date.DayDate;

class SearchResultNamedayViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final LinearLayout datesLayout;
    private final LayoutInflater inflater;

    static SearchResultNamedayViewHolder createFor(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_nameday_single, parent, false);
        return new SearchResultNamedayViewHolder(view, layoutInflater);
    }

    private SearchResultNamedayViewHolder(View convertView, LayoutInflater layoutInflater) {
        super(convertView);
        this.name = (TextView) convertView.findViewById(R.id.name_celebrating);
        this.datesLayout = (LinearLayout) convertView.findViewById(R.id.dates);

        this.inflater = layoutInflater;
    }

    public void bind(NamedayCard dates, final SearchResultAdapter.SearchResultClickListener searchResultListener) {
        name.setText(dates.getName());
        this.datesLayout.removeAllViews();
        for (int i = 0; i < dates.size(); i++) {
            View view = inflater.inflate(R.layout.nameday_date, datesLayout, false);
            TextView dateView = (TextView) view.findViewById(android.R.id.text1);

            final DayDate date = dates.getDate(i);

            String prettyDate = DateFormatUtils.formatTimeStampString(view.getContext(), date.toMillis(), false);
            dateView.setText(prettyDate);
            if (searchResultListener != null) {
                view.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchResultListener.onNamedayClicked(v, date.getMonth(), date.getDayOfMonth());
                            }
                        }
                );
            }

            datesLayout.addView(dateView);
        }
    }
}

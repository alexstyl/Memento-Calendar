package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;

final class NoResultsViewHolder extends RecyclerView.ViewHolder {

    static NoResultsViewHolder createFor(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_no_search_results, parent, false);
        return new NoResultsViewHolder(view);
    }

    private NoResultsViewHolder(final View convertView) {
        super(convertView);
    }
}

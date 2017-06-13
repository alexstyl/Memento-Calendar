package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

class MoreViewHolder extends RecyclerView.ViewHolder {

    private final TextView mLabel;
    private final ProgressBar mProgress;

    static MoreViewHolder createFor(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_load_more, parent, false);
        return new MoreViewHolder(view);
    }

    private MoreViewHolder(View convertView) {
        super(convertView);
        this.mLabel = (TextView) convertView.findViewById(R.id.label_more);
        this.mProgress = (ProgressBar) convertView.findViewById(R.id.progress);
    }

    void bind(boolean isLoadingMore) {
        if (isLoadingMore) {
            mProgress.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
        }
    }
}


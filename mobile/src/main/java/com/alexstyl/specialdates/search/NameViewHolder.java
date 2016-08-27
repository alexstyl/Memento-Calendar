package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

class NameViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameView;

    public static NameViewHolder createFor(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_name, parent, false);
        return new NameViewHolder(view);
    }

    private NameViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(android.R.id.text1);
    }

    public void bind(final String name, final NameSuggestionsAdapter.OnNameSelectedListener listener) {
        nameView.setText(name);
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNameSelected(itemView, name);
            }
        });
    }
}

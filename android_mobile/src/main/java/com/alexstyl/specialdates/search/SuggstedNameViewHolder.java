package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

final class SuggstedNameViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameView;

    public static SuggstedNameViewHolder createFor(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_suggested_name, parent, false);
        return new SuggstedNameViewHolder(view);
    }

    private SuggstedNameViewHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.suggested_name_text);
    }

    public void bind(final String name, final NameSuggestionsAdapter.OnNameSelectedListener listener) {
        nameView.setText(name);
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNameSelected(name);
            }
        });
    }
}

package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class CallViewHolder extends RecyclerView.ViewHolder {
    private final ImageView iconView;
    private final TextView callTypeView;
    private final TextView numberView;

    CallViewHolder(View view, TextView callTypeView, TextView numberView, ImageView iconView) {
        super(view);
        this.callTypeView = callTypeView;
        this.numberView = numberView;
        this.iconView = iconView;
    }

    public void bind(final ContactCallViewModel viewModel, final EventPressedListener listener) {
        callTypeView.setText(viewModel.getPhoneLabel());
        numberView.setText(viewModel.getPhoneNumber());
        iconView.setImageDrawable(viewModel.getPhoneicon());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactMethodPressed(viewModel.getStartIntent());
            }
        });
    }
}

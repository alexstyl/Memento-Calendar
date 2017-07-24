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

    public void bind(final ContactActionViewModel viewModel, final EventPressedListener listener) {
        callTypeView.setText(viewModel.getLabel());
        numberView.setText(viewModel.getIdentifier());
        iconView.setImageDrawable(viewModel.getIcon());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactActionPressed(viewModel);
            }
        });
    }
}

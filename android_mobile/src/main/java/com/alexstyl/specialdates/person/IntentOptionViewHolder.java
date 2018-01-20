package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener;

final class IntentOptionViewHolder extends RecyclerView.ViewHolder {

    private final ImageView iconView;
    private final TextView labelView;

    IntentOptionViewHolder(View view, ImageView iconView, TextView labelView) {
        super(view);
        this.iconView = iconView;
        this.labelView = labelView;
    }

    void bind(final IntentOptionViewModel viewModel, final BottomSheetIntentListener listener) {
        iconView.setImageDrawable(viewModel.getActivityIcon());
        labelView.setText(viewModel.getLabel());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActivitySelected(viewModel.getIntent());
            }
        });
    }
}

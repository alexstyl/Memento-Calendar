package com.alexstyl.specialdates.addevent.bottomsheet;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.addevent.BottomSheetPicturesDialog.OnImageOptionPickedListener;

final class IntentOptionViewHolder extends ImageOptionViewHolder {

    private final ImageView iconView;
    private final TextView labelView;

    IntentOptionViewHolder(View view, ImageView iconView, TextView labelView) {
        super(view);
        this.iconView = iconView;
        this.labelView = labelView;
    }

    @Override
    public void bind(final IntentOptionViewModel viewModel, final OnImageOptionPickedListener listener) {
        iconView.setImageDrawable(viewModel.getActivityIcon());
        labelView.setText(viewModel.getLabel());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onIntentSelected(viewModel.getIntent());
            }
        });
    }
}

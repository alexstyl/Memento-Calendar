package com.alexstyl.specialdates.addevent.bottomsheet;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexstyl.specialdates.addevent.BottomSheetPicturesDialog.OnImageOptionPickedListener;

abstract class ImageOptionViewHolder extends RecyclerView.ViewHolder {

    ImageOptionViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(IntentOptionViewModel viewModel, OnImageOptionPickedListener listener);
}

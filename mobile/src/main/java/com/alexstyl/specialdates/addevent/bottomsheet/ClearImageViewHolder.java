package com.alexstyl.specialdates.addevent.bottomsheet;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.BottomSheetPicturesDialog;

final class ClearImageViewHolder extends ImageOptionViewHolder {

    private final ImageView imageView;
    private final TextView textView;

    public ClearImageViewHolder(View itemView, ImageView imageView, TextView textView) {
        super(itemView);
        this.imageView = imageView;
        this.textView = textView;
    }

    @Override
    void bind(IntentOptionViewModel viewModel, final BottomSheetPicturesDialog.OnImageOptionPickedListener listener) {
        // ignore the viewmodel. we are always displays 'clear'
        imageView.setImageResource(R.drawable.ic_clear);
        textView.setText(R.string.add_event_remove_photo);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClearSelected();
            }
        });
    }
}

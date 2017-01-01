package com.alexstyl.specialdates.addevent.bottomsheet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.BottomSheetPicturesDialog.OnImageOptionPickedListener;

import java.util.ArrayList;
import java.util.List;

public class IntentOptionsAdapter extends RecyclerView.Adapter<ImageOptionViewHolder> {

    private static final int TYPE_CLEAR = 0;
    private static final int TYPE_OPTION = 1;

    private final List<IntentOptionViewModel> viewModels;
    private final OnImageOptionPickedListener listener;
    private boolean includeClear;

    public static IntentOptionsAdapter newInstance(OnImageOptionPickedListener listener) {
        return new IntentOptionsAdapter(listener, false);
    }

    public static IntentOptionsAdapter createWithClear(OnImageOptionPickedListener listener) {
        return new IntentOptionsAdapter(listener, true);
    }

    private IntentOptionsAdapter(OnImageOptionPickedListener listener, boolean includeClear) {
        this.includeClear = includeClear;
        this.viewModels = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (includeClear && position == 0) {
            return TYPE_CLEAR;
        }
        return TYPE_OPTION;
    }

    @Override
    public ImageOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_option, parent, false);
        ImageView iconView = (ImageView) view.findViewById(R.id.pick_image_activity_icon);
        TextView labelView = (TextView) view.findViewById(R.id.pick_image_activity_label);
        if (viewType == TYPE_CLEAR) {
            return new ClearImageViewHolder(view, iconView, labelView);
        } else if (viewType == TYPE_OPTION) {
            return new IntentOptionViewHolder(view, iconView, labelView);
        }
        throw new IllegalStateException("Illegal view type " + viewType);
    }

    @Override
    public void onBindViewHolder(ImageOptionViewHolder holder, int position) {
        holder.bind(viewModels.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void updateWith(List<IntentOptionViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
    }
}

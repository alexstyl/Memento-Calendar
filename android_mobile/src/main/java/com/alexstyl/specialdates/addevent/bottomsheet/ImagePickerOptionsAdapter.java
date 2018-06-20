package com.alexstyl.specialdates.addevent.bottomsheet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.bottomsheet.BottomSheetPicturesDialog.Listener;

import java.util.ArrayList;
import java.util.List;

final class ImagePickerOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CLEAR = 0;
    private static final int TYPE_OPTION = 1;

    private final List<PhotoPickerViewModel> viewModels;
    private final Listener listener;
    private boolean includeClear;
    private final int includeClearCount;

    public static ImagePickerOptionsAdapter newInstance(Listener listener) {
        return new ImagePickerOptionsAdapter(listener, false);
    }

    static ImagePickerOptionsAdapter createWithClear(Listener listener) {
        return new ImagePickerOptionsAdapter(listener, true);
    }

    private ImagePickerOptionsAdapter(Listener listener, boolean includeClear) {
        this.includeClear = includeClear;
        this.viewModels = new ArrayList<>();
        this.listener = listener;
        this.includeClearCount = includeClear ? 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (includeClear && position == 0) {
            return TYPE_CLEAR;
        }
        return TYPE_OPTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_option, parent, false);
        ImageView iconView = view.findViewById(R.id.pick_image_activity_icon);
        TextView labelView = view.findViewById(R.id.pick_image_activity_label);
        if (viewType == TYPE_CLEAR) {
            return new ClearImageViewHolder(view, iconView, labelView);
        } else if (viewType == TYPE_OPTION) {
            return new ImagePickerOptionViewHolder(view, iconView, labelView);
        } else {
            throw new IllegalStateException("Illegal view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == TYPE_CLEAR) {
            ((ClearImageViewHolder) holder).bind(listener);
        } else if (itemViewType == TYPE_OPTION) {
            PhotoPickerViewModel viewModel = viewModels.get(position - includeClearCount);
            ((ImagePickerOptionViewHolder) holder).bind(viewModel, listener);
        } else {
            throw new IllegalStateException("Illegal view type " + itemViewType);
        }
    }

    @Override
    public int getItemCount() {
        return viewModels.size() + includeClearCount;
    }

    void updateWith(List<PhotoPickerViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyDataSetChanged();
    }
}

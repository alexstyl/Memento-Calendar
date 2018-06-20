package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;

import java.util.ArrayList;
import java.util.List;

public final class BottomSheetIntentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<IntentOptionViewModel> viewModels;
    private final BottomSheetIntentListener listener;

    BottomSheetIntentAdapter(BottomSheetIntentListener listener, List<IntentOptionViewModel> viewModels) {
        this.viewModels = new ArrayList<>();
        this.viewModels.addAll(viewModels);
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_option, parent, false);
        ImageView iconView = view.findViewById(R.id.pick_image_activity_icon);
        TextView labelView = view.findViewById(R.id.pick_image_activity_label);
        return new IntentOptionViewHolder(view, iconView, labelView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IntentOptionViewModel viewModel = viewModels.get(position);
        ((IntentOptionViewHolder) holder).bind(viewModel, listener);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

}


package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

public class ContactActionsAdapter extends RecyclerView.Adapter<CallViewHolder> {

    private final List<ContactActionViewModel> viewModels = new ArrayList<>();
    private final EventPressedListener listener;

    public ContactActionsAdapter(EventPressedListener listener) {
        this.listener = listener;
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_person_action, parent, false);
        TextView callTypeView = Views.findById(view, R.id.row_person_action_type);
        TextView numberView = Views.findById(view, R.id.row_person_call_number);
        ImageView iconView = Views.findById(view, R.id.row_person_call_icon);
        return new CallViewHolder(view, callTypeView, numberView, iconView);
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, int position) {
        holder.bind(viewModels.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void displayCallMethods(List<ContactActionViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyItemRangeChanged(0, viewModels.size());
    }
}

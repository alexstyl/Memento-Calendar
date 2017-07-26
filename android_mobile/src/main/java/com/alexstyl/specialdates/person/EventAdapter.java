package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final List<ContactEventViewModel> viewModels = new ArrayList<>();

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_person_event, parent, false);
        TextView nameView = Views.findById(view, R.id.row_person_event_title);
        TextView dateView = Views.findById(view, R.id.row_person_event_date);
        return new EventViewHolder(view, nameView, dateView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bind(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void displayEvents(@NotNull List<ContactEventViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyItemRangeChanged(0, viewModels.size());
    }
}

package com.alexstyl.specialdates.addevent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

final class ContactEventsAdapter extends RecyclerView.Adapter<ContactEventViewHolder> {

    private final List<ContactEventViewModel> viewModels = new ArrayList<>();
    private final ContactEventsListener contactEventsListener;

    ContactEventsAdapter(ContactEventsListener contactEventsListener) {
        this.contactEventsListener = contactEventsListener;
    }

    @Override
    public ContactEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_add_event_contact_event, parent, false);
        ImageView icon = Views.findById(view, R.id.add_event_event_icon);
        TextView datePicker = Views.findById(view, R.id.add_event_date_picker);
        ImageButton clear = Views.findById(view, R.id.add_event_remove_event);
        return new ContactEventViewHolder(view, icon, datePicker, clear);
    }

    @Override
    public void onBindViewHolder(ContactEventViewHolder holder, int position) {
        ContactEventViewModel contactEventViewModel = viewModels.get(position);
        holder.bind(contactEventViewModel, contactEventsListener);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    void replace(List<ContactEventViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyDataSetChanged();
    }

    void replace(ContactEventViewModel viewModel) {
        for (int i = 0; i < viewModels.size(); i++) {
            ContactEventViewModel vm = viewModels.get(i);
            if (vm.getEventType() == viewModel.getEventType()) {
                viewModels.remove(vm);
                viewModels.add(i, viewModel);
                notifyItemChanged(i);
                break;
            }
        }
    }
}

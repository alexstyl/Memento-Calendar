package com.alexstyl.specialdates.addevent;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

public final class ContactDetailsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int HEADER_COUNT = 1;
    private static final int TYPE_CONTACT_SUGGESTION = 0;
    private static final int TYPE_EVENT = 1;

    private final List<AddEventContactEventViewModel> viewModels = new ArrayList<>();
    private final ContactDetailsListener contactDetailsListener;

    ContactDetailsAdapter(ContactDetailsListener contactDetailsListener) {
        this.contactDetailsListener = contactDetailsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CONTACT_SUGGESTION : TYPE_EVENT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTACT_SUGGESTION) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_add_event_contact_suggestion, parent, false);
            ContactSuggestionView suggestionView = Views.findById(view, R.id.add_event_contact_autocomplete);
            return new ContactSuggestionViewHolder(suggestionView);
        } else if (viewType == TYPE_EVENT) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_add_event_contact_event, parent, false);
            ImageView icon = Views.findById(view, R.id.add_event_event_icon);
            TextView datePicker = Views.findById(view, R.id.add_event_date_picker);
            ImageButton clear = Views.findById(view, R.id.add_event_remove_event);
            return new ContactEventViewHolder(view, icon, datePicker, clear);
        } else {
            throw new IllegalStateException("Received viewType " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_CONTACT_SUGGESTION) {
            ((ContactSuggestionViewHolder) holder).bind(contactDetailsListener);
        } else if (viewType == TYPE_EVENT) {
            AddEventContactEventViewModel addEventContactEventViewModel = viewModels.get(position - HEADER_COUNT);
            ((ContactEventViewHolder) holder).bind(addEventContactEventViewModel, contactDetailsListener);
        } else {
            throw new IllegalStateException("Unable to bind view type " + viewType);
        }
    }

    @Override
    public int getItemCount() {
        return viewModels.size() + HEADER_COUNT;
    }

    void replace(List<AddEventContactEventViewModel> viewModels) {
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        notifyDataSetChanged();
    }

    void replace(AddEventContactEventViewModel viewModel) {
        for (int i = 0; i < viewModels.size(); i++) {
            AddEventContactEventViewModel vm = viewModels.get(i);
            if (vm.getEventType() == viewModel.getEventType()) {
                viewModels.remove(vm);
                viewModels.add(i, viewModel);
                notifyItemChanged(i + HEADER_COUNT);
                break;
            }
        }
    }
}

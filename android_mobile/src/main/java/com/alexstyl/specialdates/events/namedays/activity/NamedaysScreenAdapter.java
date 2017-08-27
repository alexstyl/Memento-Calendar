package com.alexstyl.specialdates.events.namedays.activity;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class NamedaysScreenAdapter extends RecyclerView.Adapter<NamedayScreenViewHolder> {

    private final List<NamedayScreenViewModel> viewModels = new ArrayList<>();

    private final NamedaysScreenViewHolderFactory viewholderFactory;
    private final Function1<Contact, Unit> onContactClicked;

    public NamedaysScreenAdapter(NamedaysScreenViewHolderFactory viewholderFactory, Function1<Contact, Unit> onContactClicked) {
        this.viewholderFactory = viewholderFactory;
        this.onContactClicked = onContactClicked;
    }

    @Override
    public int getItemViewType(int position) {
        return viewModels.get(position).getViewType();
    }

    @Override
    public NamedayScreenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewholderFactory.viewHolderFor(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(NamedayScreenViewHolder holder, int position) {
        holder.bind(viewModels.get(position), onContactClicked);
    }

    void display(List<NamedayScreenViewModel> viewModels) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NamedaysViewModelDiff(this.viewModels, viewModels));
        this.viewModels.clear();
        this.viewModels.addAll(viewModels);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }
}

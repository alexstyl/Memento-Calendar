package com.alexstyl.specialdates.people;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

import java.util.ArrayList;
import java.util.List;

class PeopleAdapter extends RecyclerView.Adapter<PeopleViewHolder> {

    private final List<PeopleViewModel> people = new ArrayList<>();
    private final LayoutInflater inflater;
    private final PeopleViewHolderListener listener;
    private final ImageLoader imageLoader;

    PeopleAdapter(ImageLoader imageLoader, LayoutInflater inflater, PeopleViewHolderListener listener) {
        this.imageLoader = imageLoader;
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(R.layout.row_people, parent, false);

        ColorImageView imageView = rowView.findViewById(R.id.people_avatar);
        TextView nameView = rowView.findViewById(R.id.people_name);
        return new PeopleViewHolder(rowView, imageLoader, imageView, nameView);
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        holder.bind(people.get(position), listener);
    }

    void updateWith(final List<PeopleViewModel> viewModels) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PeopleDiffCallback(people, viewModels));
        this.people.clear();
        this.people.addAll(viewModels);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

}

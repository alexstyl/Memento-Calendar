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

class PeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PERSON = 0;
    private static final int VIEW_TYPE_IMPORT_FACEBOOK = 1;
    private static final int VIEW_TYPE_NO_CONTACTS = 2;

    private final List<PeopleRowViewModel> people = new ArrayList<>();
    private final LayoutInflater inflater;
    private final PeopleViewHolderListener listener;
    private final ImageLoader imageLoader;

    PeopleAdapter(ImageLoader imageLoader, LayoutInflater inflater, PeopleViewHolderListener listener) {
        this.imageLoader = imageLoader;
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        PeopleRowViewModel personViewModel = people.get(position);
        if (personViewModel instanceof PersonViewModel) {
            return VIEW_TYPE_PERSON;
        }
        if (personViewModel instanceof FacebookImportViewModel) {
            return VIEW_TYPE_IMPORT_FACEBOOK;
        }
        if (personViewModel instanceof NoContactsViewModel) {
            return VIEW_TYPE_NO_CONTACTS;
        }
        throw new IllegalStateException("Unhandled view type for " + personViewModel.getClass());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PERSON) {
            View rowView = inflater.inflate(R.layout.row_people, parent, false);

            ColorImageView imageView = rowView.findViewById(R.id.people_avatar);
            TextView nameView = rowView.findViewById(R.id.people_name);
            return new PeopleViewHolder(rowView, imageLoader, imageView, nameView);
        } else if (viewType == VIEW_TYPE_IMPORT_FACEBOOK) {
            View rowView = inflater.inflate(R.layout.row_people_import_from_facebook, parent, false);
            return new ImportFromFacebookViewHolder(rowView, (TextView) rowView.findViewById(R.id.row_people_facebook_import));
        } else if (viewType == VIEW_TYPE_NO_CONTACTS) {
            View rowView = inflater.inflate(R.layout.row_people_no_contacts, parent, false);
            return new NoContactViewHolder(rowView);
        }
        throw new IllegalArgumentException("Cannot create holder for viewType " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == VIEW_TYPE_PERSON) {
            ((PeopleViewHolder) holder).bind((PersonViewModel) people.get(position), listener);
        } else if (itemViewType == VIEW_TYPE_IMPORT_FACEBOOK) {
            ((ImportFromFacebookViewHolder) holder).bind((FacebookImportViewModel) people.get(position), listener);
        } else if (itemViewType == VIEW_TYPE_NO_CONTACTS) {
            ((NoContactViewHolder) holder).bind(listener);
        }
    }

    void updateWith(List<PeopleRowViewModel> viewModels) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PeopleDiffCallback(people, viewModels));
        this.people.clear();
        this.people.addAll(viewModels);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

}

package com.alexstyl.specialdates.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

class SearchResultContactViewHolder extends RecyclerView.ViewHolder {

    private final ColorImageView avatar;
    private final TextView displayName;
    private final TextView eventLabel;
    private final ImageLoader imageLoader;

    public static SearchResultContactViewHolder createFor(ViewGroup parent, ImageLoader imageLoader) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_search_result_contact_event, parent, false);
        return new SearchResultContactViewHolder(view, imageLoader);
    }

    private SearchResultContactViewHolder(View convertView, ImageLoader imageLoader) {
        super(convertView);
        this.imageLoader = imageLoader;
        this.displayName = (TextView) convertView.findViewById(R.id.search_result_contact_name);
        this.eventLabel = (TextView) convertView.findViewById(R.id.search_result_event_label);
        this.avatar = (ColorImageView) convertView.findViewById(R.id.search_result_avatar);
    }

    void bind(final ContactEventViewModel viewModel, final SearchResultAdapter.SearchResultClickListener listener) {
        avatar.setBackgroundVariant(viewModel.getBackgroundVariant());
        avatar.setLetter(viewModel.getDisplayName(), true);
        displayName.setText(viewModel.getDisplayName());
        imageLoader.displayThumbnail(viewModel.getContactAvatarURI(), avatar.getImageView());
        eventLabel.setText(viewModel.getEventLabel());
        eventLabel.setTextColor(eventLabel.getResources().getColor(viewModel.getEventColor()));
        itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onContactClicked(viewModel.getContact());
                    }
                }
        );
    }

}

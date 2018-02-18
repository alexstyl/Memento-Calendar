package com.alexstyl.specialdates.people;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;

class PeopleViewHolder extends RecyclerView.ViewHolder {

    private final ColorImageView imageView;
    private final TextView nameView;
    private final ImageLoader imageLoader;

    PeopleViewHolder(View rowView, ImageLoader imageLoader, ColorImageView imageView, TextView nameView) {
        super(rowView);
        this.imageLoader = imageLoader;
        this.imageView = imageView;
        this.nameView = nameView;
    }

    public void bind(final PersonViewModel viewModel, final PeopleViewHolderListener listener) {
        nameView.setText(viewModel.getPersonName());
        imageView.setCircleColorVariant((int) viewModel.getPersonId());
        imageView.setLetter(viewModel.getPersonName(), true);
        imageLoader.load(viewModel.getAvatarURI())
                .asCircle()
                .into(imageView.getImageView());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPersonClicked(viewModel.getContact());
            }
        });
    }
}

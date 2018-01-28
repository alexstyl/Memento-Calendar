package com.alexstyl.specialdates.people;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class ImportFromFacebookViewHolder extends RecyclerView.ViewHolder {

    ImportFromFacebookViewHolder(View rowView) {
        super(rowView);
    }

    public void bind(final PeopleViewHolderListener listener) {
     itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             listener.onFacebookImport();
         }
     });
    }
}

package com.alexstyl.specialdates.datedetails;

import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.images.ImageLoader;

import java.util.List;

class CompactDateDetailsViewHolder extends DateDetailsViewHolder {

    private final FrameLayout more;
    private final View cardView;

    static CompactDateDetailsViewHolder newInstance(View view, ImageLoader imageLoader) {
        Resources resources = view.getResources();
        return new CompactDateDetailsViewHolder(view, imageLoader, resources);
    }

    private CompactDateDetailsViewHolder(View convertView, ImageLoader imageLoader, Resources resources) {
        super(convertView, imageLoader, resources);
        this.cardView = convertView;
        this.more = (FrameLayout) convertView.findViewById(R.id.more_actions);
    }

    @Override
    void bindActionsFor(final Contact contact, final ContactCardListener listener) {
        List<LabeledAction> userActions = contact.getUserActions(cardView.getContext());
        if (userActions.size() > 0) {
            more.setVisibility(View.VISIBLE);
            more.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onContactActionsMenuClicked(v, contact);
                        }
                    }
            );
        } else {
            more.setVisibility(View.GONE);
        }
    }

    @Override
    public void clearActions() {
        // we are not displaying any actions
    }

}

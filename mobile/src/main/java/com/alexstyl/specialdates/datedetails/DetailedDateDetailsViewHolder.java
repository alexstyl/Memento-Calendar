package com.alexstyl.specialdates.datedetails;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ActionButton;

import java.util.List;

class DetailedDateDetailsViewHolder extends DateDetailsViewHolder {
    private final ContactCardListener listener;
    private final LinearLayout cardActions;
    private final CardActionRecycler cardActionRecycler;
    private final int actionMarginHorizontal;

    static DetailedDateDetailsViewHolder newInstance(View view, ContactCardListener listener, ImageLoader imageLoader, CardActionRecycler cardActionRecycler) {
        Resources resources = view.getResources();
        int actionHorizontalMargin = resources.getDimensionPixelSize(R.dimen.card_action_horizontal_margin);
        return new DetailedDateDetailsViewHolder(view, resources, listener, imageLoader, cardActionRecycler, actionHorizontalMargin);
    }

    DetailedDateDetailsViewHolder(View itemView, Resources resources, ContactCardListener listener, ImageLoader imageLoader, CardActionRecycler cardActionRecycler, int actionMarginHorizontal) {
        super(itemView, imageLoader, resources);

        this.listener = listener;
        this.actionMarginHorizontal = actionMarginHorizontal;
        this.cardActions = (LinearLayout) itemView.findViewById(R.id.card_actions);
        this.cardActionRecycler = cardActionRecycler;
    }

    @Override
    public void bind(ContactEvent event, ContactCardListener listener) {
        super.bind(event, listener);
    }

    @Override
    void bindActionsFor(Contact contact, ContactCardListener listener) {
        removeAllActions();
        List<LabeledAction> actions = contact.getUserActions(itemView.getContext());
        if (containsNoActions(actions)) {
            cardActions.setVisibility(View.GONE);
        } else {
            cardActions.setVisibility(View.VISIBLE);

            int size = actions.size();
            for (int i = 0; i < size; i++) {
                LabeledAction action = actions.get(i);
                ActionButton button = cardActionRecycler.getOrCreateCardAction(cardActions);
                ((LinearLayout.LayoutParams) button.getLayoutParams()).setMargins(actionMarginHorizontal, 0, actionMarginHorizontal, 0);
                bindActionToButton(action, button);
                cardActions.addView(button);
            }
        }
    }

    private void removeAllActions() {
        for (int i = 0; i < cardActions.getChildCount(); i++) {
            View childAt = cardActions.getChildAt(i);
            cardActions.removeView(childAt);
        }
    }

    private void bindActionToButton(final LabeledAction action, ActionButton button) {
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onActionClicked(v, action);
                    }
                }
        );
        button.bind(action);
    }

    private boolean containsNoActions(List<LabeledAction> actions) {
        return actions.size() == 0;
    }

    public void clearActions() {
        cardActionRecycler.clearActions(cardActions);
    }

}

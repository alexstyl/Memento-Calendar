package com.alexstyl.specialdates.datedetails;

import android.view.View;
import android.widget.LinearLayout;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.ui.widget.ActionButton;

import java.util.List;

class DetailedDateDetailsViewHolder extends DateDetailsViewHolder {
    private final ContactCardListener listener;
    private final LinearLayout cardActions;
    private final CardActionRecycler cardActionRecycler;
    private final int actionMarginHorizontal;

    DetailedDateDetailsViewHolder(View itemView,
                                  StringResources stringResources,
                                  ColorResources colorResources,
                                  ContactCardListener listener,
                                  ImageLoader imageLoader,
                                  CardActionRecycler cardActionRecycler
    ) {
        super(itemView, imageLoader, stringResources, colorResources);

        this.listener = listener;
        this.actionMarginHorizontal = itemView.getResources().getDimensionPixelSize(R.dimen.card_action_horizontal_margin);
        this.cardActions = (LinearLayout) itemView.findViewById(R.id.card_actions);
        this.cardActionRecycler = cardActionRecycler;
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

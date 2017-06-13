package com.alexstyl.specialdates.datedetails.actions;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.util.AppUtils;

/**
 * An action that has a label. In full contact cards, the actions that can be performed are labeled on the card.
 * <p>Created by Alex on 9/5/2014.</p>
 */
public class LabeledAction {

    @StringRes
    private final int nameResId;
    @DrawableRes
    private final int iconResId;
    private final boolean slowStart;
    private final IntentAction action;

    public LabeledAction(int nameResId, IntentAction action, @DrawableRes int iconResId) {
        this(nameResId, action, iconResId, false);
    }

    public LabeledAction(int nameResId, IntentAction action, @DrawableRes int iconResId, boolean slowStart) {
        this.nameResId = nameResId;
        this.action = action;
        this.iconResId = iconResId;
        this.slowStart = slowStart;
    }

    public IntentAction getAction() {
        return action;
    }

    /**
     * Fires the given action
     *
     * @param context The context to use
     */
    public boolean fire(Context context) {
        return AppUtils.openIntentSafely(context, action);
    }

    @DrawableRes
    public int getIconRes() {
        return iconResId;
    }

    public boolean hasSlowStart() {
        return slowStart;
    }

    @StringRes
    public int getName() {
        return nameResId;
    }
}

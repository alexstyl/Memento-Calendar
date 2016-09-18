package com.alexstyl.specialdates.upcoming.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.upcoming.view.UpcomingEventsView;
import com.novoda.notils.caster.Views;

public class UpcomingEventsViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final TextView title;
    private final UpcomingEventsView eventsView;
    private final TransitionDrawable backgroundDrawable;
    private final ImageLoader imageLoader;

    public static UpcomingEventsViewHolder createFor(ViewGroup parent, ImageLoader imageLoader) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_upcoming_day, parent, false);
        return new UpcomingEventsViewHolder(view, imageLoader);
    }

    private UpcomingEventsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        this.context = itemView.getContext();
        this.title = Views.findById(itemView, R.id.upcoming_events_title);
        this.eventsView = Views.findById(itemView, R.id.upcoming_events_view);
        this.backgroundDrawable = createTransitionDrawable(itemView, itemView.getResources());

        View background = Views.findById(itemView, R.id.upcoming_events_background);
        background.setBackgroundDrawable(backgroundDrawable);
    }

    private TransitionDrawable createTransitionDrawable(View view, Resources resources) {
        AttributeExtractor attributeExtractor = new AttributeExtractor();
        int accentColor = attributeExtractor.extractSecondaryColorFrom(context);

        TransitionDrawable drawable = new TransitionDrawable(new Drawable[]{
                view.getBackground() == null ? new ColorDrawable(resources.getColor(android.R.color.transparent)) : view.getBackground(),
                new ColorDrawable(accentColor)
        });
        drawable.setCrossFadeEnabled(true);
        return drawable;
    }

    public void bind(final CelebrationDate celebrationDate, DayDate today, final OnUpcomingEventClickedListener listener) {
        backgroundDrawable.resetTransition();
        String text = DateFormatUtils.formatTimeStampString(context, celebrationDate.getDate().toMillis(), true);
        title.setText(text);
        if (canOpenDetailsView(celebrationDate)) {
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardPressed(celebrationDate.getDate());
                }
            });
        } else {
            itemView.setClickable(false);
        }

        if (celebrationDate.getDate().equals(today)) {
            title.setTypeface(Typeface.DEFAULT_BOLD);
            eventsView.markAsToday();
        } else {
            eventsView.markAsFutureEvent();
            title.setTypeface(Typeface.DEFAULT);
        }
        if (celebrationDate.hasBankholiday()) {
            eventsView.displayBankholidays(celebrationDate.getBankHoliday());
        } else {
            eventsView.hideBankHolidays();
        }
        if (celebrationDate.hasNamedays()) {
            eventsView.displayNamedays(celebrationDate.getDaysNamedays());
        } else {
            eventsView.hideNamedays();
        }
        if (celebrationDate.hasContactEvents()) {
            eventsView.displayContactEventsFor(celebrationDate.getContactEvents(), listener, imageLoader);
        } else {
            eventsView.hideContactEvents();
        }

    }

    private boolean canOpenDetailsView(CelebrationDate celebrationDate) {
        return celebrationDate.getContactCount() > 2 || celebrationDate.hasNamedays();
    }

    private boolean isAnimating = false;
    private static int PLAY_ANIMATION = 250;
    private static final int STAY_ANIMATION = 500;

    public void playShowMeAnimation() {
        if (isAnimating) {
            return;
        }
        backgroundDrawable.startTransition(PLAY_ANIMATION);
        isAnimating = true;
        itemView.postDelayed(reverseAnimationRunnable, STAY_ANIMATION);

    }

    private final Runnable reverseAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            playReverseAnimation();
            isAnimating = false;
        }

        private void playReverseAnimation() {
            backgroundDrawable.setCrossFadeEnabled(true);
            backgroundDrawable.reverseTransition(PLAY_ANIMATION);
        }
    };
}

package com.alexstyl.specialdates.upcoming.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.PauseImageLoadingScrollListener;
import com.alexstyl.specialdates.ui.widget.ScrollingLinearLayoutManager;

import java.util.List;

public class UpcomingEventsListView extends RecyclerView {

    private UpcomingEventsAdapter adapter;
    private ScrollingLinearLayoutManager layoutManager;

    private OnUpcomingEventClickedListener listener;

    public UpcomingEventsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setHasFixedSize(true);
        layoutManager = new ScrollingLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 600);
        setLayoutManager(layoutManager);

        Resources resources = getResources();
        ImageLoader imageLoader = ImageLoader.createCircleThumbnailLoader(resources);

        if (isInEditMode()) {
            return;
        }
        adapter = UpcomingEventsAdapter.newInstance(imageLoader);
        setAdapter(adapter);

        addOnScrollListener(PauseImageLoadingScrollListener.newInstance(imageLoader));
    }

    public void updateWith(List<CelebrationDate> dates, OnUpcomingEventClickedListener listener) {
        this.listener = listener;
        this.adapter.setUpcomingEvents(dates, listener);
    }

    public void scrollToToday(final boolean smoothScroll) {
        post(
                new Runnable() {
                    public void run() {
                        int pos = adapter.getClosestDayPosition();
                        if (pos == RecyclerView.NO_POSITION) {
                            pos = getLastDayPosition();
                        }

                        if (isPositionVisible(pos)) {
                            onFinishedScrolling(pos);
                            return;
                        }

                        if (smoothScroll) {
                            smoothScrollTo(pos);
                        } else {
                            layoutManager.scrollToPositionWithOffset(pos, 0);
                        }
                    }

                    private int getLastDayPosition() {
                        return adapter.getItemCount() - 1;
                    }
                }
        );
    }

    private void smoothScrollTo(int pos) {
        // see if today is visible
        final int finalPos = pos;
        addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            onFinishedScrolling(finalPos);
                            // our job here is done
                            removeOnScrollListener(this);
                        }
                    }
                }
        );
        smoothScrollToPosition(pos);
    }

    private void onFinishedScrolling(final int pos) {

        UpcomingEventsViewHolder upcomingViewHolder = (UpcomingEventsViewHolder) findViewHolderForAdapterPosition(pos);
        if (upcomingViewHolder != null && isPositionVisible(pos)) {
            upcomingViewHolder.playShowMeAnimation();
        }

    }

    boolean isPositionVisible(int pos) {
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastCompletelyVisibleItemPosition();
        return first <= pos && pos <= last;
    }

    public boolean isDisplayingEvents() {
        return adapter.getItemCount() > 0;
    }

}

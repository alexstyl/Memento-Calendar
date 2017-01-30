package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.PauseImageLoadingScrollListener;
import com.alexstyl.specialdates.ui.widget.ScrollingLinearLayoutManager;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;

import java.util.List;

public class UpcomingEventsListView extends RecyclerView {

    private UpcomingEventsAdapter adapter;
    private ScrollingLinearLayoutManager layoutManager;

    public UpcomingEventsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        layoutManager = new ScrollingLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 600);
        setLayoutManager(layoutManager);

        Resources resources = getResources();
        ImageLoader imageLoader = ImageLoader.createCircleThumbnailLoader(resources);

        if (isInEditMode()) {
            return;
        }
        adapter = new UpcomingEventsAdapter(new UpcomingViewHolderFactory(LayoutInflater.from(context), imageLoader));
        setAdapter(adapter);

        addOnScrollListener(PauseImageLoadingScrollListener.newInstance(imageLoader));
    }

    public void updateWith(List<UpcomingRowViewModel> dates, OnUpcomingEventClickedListener listener) {
        adapter.displayUpcomingEvents(dates, listener);
    }

    public void scrollToToday(final boolean smoothScroll) {
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

    private void onFinishedScrolling(int position) {
        UpcomingEventsViewHolder upcomingViewHolder = (UpcomingEventsViewHolder) findViewHolderForAdapterPosition(position);
        if (upcomingViewHolder != null && isPositionVisible(position)) {
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

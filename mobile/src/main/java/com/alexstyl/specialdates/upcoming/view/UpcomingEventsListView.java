package com.alexstyl.specialdates.upcoming.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.PauseImageLoadingScrollListener;
import com.alexstyl.specialdates.ui.widget.ScrollingLinearLayoutManager;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;

import java.util.List;

public class UpcomingEventsListView extends RecyclerView {

    private UpcomingEventsAdapter adapter;

    public UpcomingEventsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ScrollingLinearLayoutManager layoutManager = new ScrollingLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false, 600);
        setLayoutManager(layoutManager);

        Resources resources = getResources();
        ImageLoader imageLoader = ImageLoader.createCircleThumbnailLoader(resources);

        if (isInEditMode()) {
            return;
        }
        adapter = new UpcomingEventsAdapter(new UpcomingViewHolderFactory(LayoutInflater.from(context), imageLoader));
        setAdapter(adapter);

        addOnScrollListener(PauseImageLoadingScrollListener.newInstance(imageLoader));
        addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.upcoming_vertical_padding_between_cards), 1));
    }

    public void updateWith(List<UpcomingRowViewModel> dates, OnUpcomingEventClickedListener listener) {
        adapter.displayUpcomingEvents(dates, listener);
    }

    public boolean isDisplayingEvents() {
        return adapter.getItemCount() > 0;
    }

}

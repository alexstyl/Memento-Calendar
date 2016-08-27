package com.alexstyl.specialdates.upcoming;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class BoundariesScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public BoundariesScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (isScrollingUp(dy)) {
            if (hasReachedTop()) {
                onScrolledToTop();
            }
        } else if (hasReachedBottom()) {
            onScrolledToBottom();
        }
    }

    private boolean isScrollingUp(int dy) {
        return dy < 0;
    }

    private boolean hasReachedTop() {
        return layoutManager.findFirstVisibleItemPosition() == 0;
    }

    private boolean hasReachedBottom() {
        return layoutManager.findLastVisibleItemPosition() == lastItemPosition();
    }

    private int lastItemPosition() {
        return layoutManager.getChildCount() - 1;
    }

    protected abstract void onScrolledToTop();

    protected abstract void onScrolledToBottom();

}

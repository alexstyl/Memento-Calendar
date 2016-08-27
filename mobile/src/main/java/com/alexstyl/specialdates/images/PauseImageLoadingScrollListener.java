package com.alexstyl.specialdates.images;

import android.support.v7.widget.RecyclerView;

public class PauseImageLoadingScrollListener extends RecyclerView.OnScrollListener {

    private final ImageLoader imageLoader;

    public PauseImageLoadingScrollListener(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public static RecyclerView.OnScrollListener newInstance(ImageLoader imageLoader) {
        return new PauseImageLoadingScrollListener(imageLoader);
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
            case RecyclerView.SCROLL_STATE_DRAGGING:
                imageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                imageLoader.pause();
                break;
            default:
                break;
        }
    }

}

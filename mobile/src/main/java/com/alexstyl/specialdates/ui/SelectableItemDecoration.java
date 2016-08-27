package com.alexstyl.specialdates.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.alexstyl.specialdates.R;

/**
 * <p>Created by alexstyl on 14/09/15.</p>
 */
public class SelectableItemDecoration extends RecyclerView.ItemDecoration {
    public SelectableItemDecoration(Context context) {

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Drawable background = parent.getResources().getDrawable(R.drawable.selectable_back);
        if (background != null) {
            background.draw(c);
        }
    }
}

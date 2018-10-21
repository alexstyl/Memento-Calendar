package com.alexstyl.specialdates.ui.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(private val space: Int, private val columns: Int = 1) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State?) {

        outRect.bottom = space

        if (parent.getChildLayoutPosition(view) < columns) {
            outRect.top = space
        }

    }
}

package com.alexstyl.specialdates.ui.widget

import android.graphics.Rect
import android.support.annotation.Px
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(@Px private val space: Int, private val columns: Int = 1) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State?) {

        outRect.bottom = space

        if (parent.getChildLayoutPosition(view) < columns) {
            outRect.top = space
        }

    }
}

class HorizontalSpacesItemDecoration(@Px private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State?) {

        val childLayoutPosition = parent.getChildLayoutPosition(view)
        if (isNotLastItem(childLayoutPosition, parent)) {
            outRect.right = space
        }
    }

    private fun isNotLastItem(childLayoutPosition: Int, parent: RecyclerView) =
            childLayoutPosition != parent.childCount - 1
}

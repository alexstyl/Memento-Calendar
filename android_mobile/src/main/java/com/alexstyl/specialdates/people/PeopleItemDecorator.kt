package com.alexstyl.specialdates.people

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class PeopleItemDecorator(private val categoryMargin: Int,
                          private val itemInBetweenMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (parent.getChildViewHolder(view) is ImportFromFacebookViewHolder) {
            outRect.bottom = categoryMargin
        } else {
            outRect.bottom = itemInBetweenMargin
        }
    }
}

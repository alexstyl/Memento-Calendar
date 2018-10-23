package com.alexstyl.specialdates.ui

import android.view.ViewGroup

import android.view.View.GONE
import android.view.View.VISIBLE

object ViewFader {

    fun hideContentOf(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            viewGroup.getChildAt(i).visibility = GONE
        }
    }

    fun showContent(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            viewGroup.getChildAt(i).visibility = VISIBLE
        }
    }

}

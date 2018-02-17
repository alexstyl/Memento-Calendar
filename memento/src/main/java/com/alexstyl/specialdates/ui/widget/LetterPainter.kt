package com.alexstyl.specialdates.ui.widget

import android.support.annotation.ColorInt

interface LetterPainter {
    @ColorInt
    fun getVariant(i2: Int): Int
}

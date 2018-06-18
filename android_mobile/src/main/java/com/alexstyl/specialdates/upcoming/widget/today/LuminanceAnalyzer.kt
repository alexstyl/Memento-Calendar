package com.alexstyl.specialdates.upcoming.widget.today

import android.graphics.Bitmap
import android.graphics.Color
import io.reactivex.Observable
import io.reactivex.Scheduler

class LuminanceAnalyzer(private val workScheduler: Scheduler, private val resultScheduler: Scheduler) {

    fun analyse(bitmap: Bitmap, result: (Boolean) -> Unit) {
        Observable.fromCallable {
            isLight(bitmap)
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe {
                    result(it)
                }
    }

    private fun isLight(bitmap: Bitmap): Boolean {
        val darkThreshold = bitmap.width.toFloat() * bitmap.height.toFloat() * 0.90f
        var lightPixels = 0

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in pixels) {
            val r = Color.red(pixel)
            val g = Color.green(pixel)
            val b = Color.blue(pixel)
            val luminance = 0.299 * r + 0.0 + 0.587 * g + 0.0 + 0.114 * b + 0.0
            if (luminance > 150) {
                lightPixels++
            }
        }

        return lightPixels >= darkThreshold
    }
}

package com.alexstyl.specialdates

import android.widget.Toast
import com.alexstyl.specialdates.ui.base.MementoActivity
import com.alexstyl.specialdates.ui.base.MementoFragment

fun MementoActivity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun MementoFragment.toast(text: String) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}
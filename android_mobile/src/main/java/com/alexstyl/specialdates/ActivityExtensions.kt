package com.alexstyl.specialdates

import android.support.v4.app.Fragment
import android.widget.Toast
import com.alexstyl.specialdates.ui.base.MementoActivity

fun MementoActivity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}
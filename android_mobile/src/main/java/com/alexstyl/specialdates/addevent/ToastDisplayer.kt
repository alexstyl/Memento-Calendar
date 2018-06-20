package com.alexstyl.specialdates.addevent

import android.content.Context
import android.widget.Toast

class ToastDisplayer(private val context: Context) : MessageDisplayer {

    override fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}

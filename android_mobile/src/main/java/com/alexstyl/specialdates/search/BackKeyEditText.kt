package com.alexstyl.specialdates.search

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText

class BackKeyEditText(context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatEditText(context, attrs) {

    private var listener: OnBackKeyPressedListener? = null

    fun setOnBackKeyPressedListener(listener: OnBackKeyPressedListener) {
        this.listener = listener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean =
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onBackButtonPressed() ?: false
            } else super.onKeyPreIme(keyCode, event)
}

package com.alexstyl.specialdates.search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.DrawableTinter
import com.novoda.notils.caster.Views
import com.novoda.notils.text.SimpleTextWatcher

class SearchToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {

    private lateinit var editText: BackKeyEditText

    private val onEditorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.requestFocus()
            return@OnEditorActionListener true
        }
        false
    }

    init {
        View.inflate(getContext(), R.layout.merge_searchbar, this)
        editText = Views.findById(this, R.id.toolbar_search_edittext)
        setBackgroundColor(Color.WHITE)
        addTintedUpNavigation()

        editText.setOnEditorActionListener(onEditorActionListener)
    }

    private fun addTintedUpNavigation() {
        val drawableTinter = DrawableTinter(AttributeExtractor())
        val backDrawable = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp).mutate()
        val tintedUpDrawable = drawableTinter.tintWithAccentColor(backDrawable, context)
        navigationIcon = tintedUpDrawable
    }

    fun addTextWatcher(watcher: (String) -> Unit) {
        editText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                watcher(s.toString())
            }
        })
    }

    fun setText(text: String) {
        editText.setText(text)
        editText.setSelection(text.length)
    }

    fun setInputType(typeTextFlag: Int) {
        editText.inputType = typeTextFlag
    }

    fun setOnBackKeyPressedListener(listener: OnBackKeyPressedListener) {
        editText.setOnBackKeyPressedListener(listener)
    }

    fun clearText() {
        editText.text = null
    }

    fun hasText(): Boolean {
        return editText.length() > 0
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }


}

package com.alexstyl.specialdates.search

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.alexstyl.specialdates.MementoApplication

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.DrawableTinter
import com.novoda.notils.text.SimpleTextWatcher
import javax.inject.Inject

class Searchbar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private lateinit var editText: BackKeyEditText
    private lateinit var navigationIcon: ImageButton
    private lateinit var clearButton: ImageButton

    @Inject lateinit var namedayPreferences: NamedayUserSettings


    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_searchbar, this)

        editText = findViewById(R.id.toolbar_search_edittext)
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                editText.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        clearButton = findViewById(R.id.searchbar_clear)
        navigationIcon = findViewById(R.id.searchbar_back_button)

        minimumHeight = resources.getDimensionPixelSize(R.dimen.toolbar_minHeight)
        tintIcons(navigationIcon, clearButton)
        setBackgroundColor(Color.WHITE)

        editText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }
        })

        if (!isInEditMode) {
            (context.applicationContext as MementoApplication).applicationModule.inject(this)

            val enabled = namedayPreferences.isEnabled
            editText.hint = if (enabled)
                resources.getString(R.string.search_hint_contacts_and_namedays)
            else
                resources.getString(R.string.search_hint_contacts)
        } else {
            editText.hint = "Search in Edit Mode"
        }
    }

    private fun tintIcons(vararg icons: ImageButton) {
        val drawableTinter = DrawableTinter(AttributeExtractor())
        icons.forEach { icon ->
            val backDrawable = icon.drawable.mutate()
            val tintedUpDrawable = drawableTinter.tintWithAccentColor(backDrawable, context)
            icon.setImageDrawable(tintedUpDrawable)
        }
    }

    fun addTextWatcher(watcher: (String) -> Unit) {
        editText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                watcher(s.toString())
            }
        })
    }

    fun setText(text: String) {
        editText.setText(text)
        editText.setSelection(text.length)
    }

    var onSoftBackKeyPressed: (() -> Boolean)? = null
        set(value) {
            editText.setOnBackKeyPressedListener(OnBackKeyPressedListener { value?.invoke()!! })
        }
    val text: Editable
        get() = editText.text


    fun setOnNavigateBackButtonPressed(function: (View) -> Unit) {
        navigationIcon.setOnClickListener(function)
    }

    fun setOnClearButtonPressed(function: (View) -> Unit) {
        clearButton.setOnClickListener(function)
    }

    fun clearText() {
        editText.text = null
    }

}

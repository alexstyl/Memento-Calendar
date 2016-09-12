package com.alexstyl.specialdates.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.alexstyl.specialdates.R;

public class SearchBar extends Toolbar {

    private EditText editText;

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.merge_searchbar, this);
        editText = (EditText) findViewById(R.id.toolbar_search_edittext);
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }
}

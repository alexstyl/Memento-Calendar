package com.alexstyl.specialdates.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.theming.ViewTinter;
import com.novoda.notils.caster.Views;

public class SearchBar extends Toolbar {

    private MyEditText editText;

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.merge_searchbar, this);
        editText = Views.findById(this, R.id.toolbar_search_edittext);
        setBackgroundColor(Color.WHITE);
        addTintedUpNavigation();

        editText.setOnEditorActionListener(onEditorActionListener);
    }

    private void addTintedUpNavigation() {
        ViewTinter viewTinter = new ViewTinter(new AttributeExtractor());
        Drawable backDrawable = getResources().getDrawable(R.drawable.ic_action_arrow_light_back).mutate();
        Drawable tintedUpDrawable = viewTinter.createAccentTintedDrawable(backDrawable, getContext());
        setNavigationIcon(tintedUpDrawable);
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }

    public void setInputType(int typeTextFlag) {
        editText.setInputType(typeTextFlag);
    }

    private final TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.requestFocus();
                return true;
            }
            return false;
        }
    };

    public void setOnBackKeyPressedListener(OnBackKeyPressedListener listener) {
        editText.setOnBackKeyPressedListener(listener);
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void clearText() {
        editText.setText(null);
    }

    public boolean hasText() {
        return editText.length() > 0;
    }
}

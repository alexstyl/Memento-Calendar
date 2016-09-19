package com.alexstyl.specialdates.ui.base;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.Themer;

public class ThemedActivity extends MementoActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        Themer.get(this).initialiseActivity(this);
        super.setContentView(layoutResID);
    }

    public void setContentView(@LayoutRes int layoutResID, MementoTheme theme) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this, theme.androidTheme());
        View inflate = LayoutInflater.from(wrapper).inflate(layoutResID, null, false);
        setContentView(inflate);
    }

    public void reapplyTheme() {
        Intent intent = getIntent();
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

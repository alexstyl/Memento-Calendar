package com.alexstyl.specialdates.ui.base;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.alexstyl.specialdates.theming.Themer;

public class ThemedMementoActivity extends MementoActivity {

    private Themer themer;
    private AttributeExtractor attributeExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themer = Themer.get(this);
        attributeExtractor = new AttributeExtractor();
        themer.initialiseActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
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

    protected final void inflateThemedMenu(@MenuRes int menuResId, Menu menu) {
        getMenuInflater().inflate(menuResId, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable wrappedDrawable = DrawableCompat.wrap(item.getIcon());
            int color = attributeExtractor.extractToolbarIconColors(this);
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        }
    }
}

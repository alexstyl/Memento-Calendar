package com.alexstyl.specialdates.settings;

import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.theming.MementoTheme;
import com.novoda.notils.caster.Views;

public class ThemeViewHolder {

    private final AttributeExtractor extractor;
    private final TextView themeName;
    private final View primaryColorView;
    private final View accentColorView;

    public ThemeViewHolder(View convertView, AttributeExtractor extractor) {
        this.extractor = extractor;
        this.themeName = Views.findById(convertView, R.id.themeSelect_name);
        this.primaryColorView = Views.findById(convertView, R.id.themeSelect_primary_color);
        this.accentColorView = Views.findById(convertView, R.id.themeSelect_accent_color);
    }

    public void bind(MementoTheme theme) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(themeName.getContext(), theme.androidTheme());

        int primaryColor = extractor.extractPrimaryColorFrom(wrapper);
        primaryColorView.setBackgroundColor(primaryColor);

        int accentColor = extractor.extractAccentColorFrom(wrapper);

        boolean darkIcons = extractor.extractDarkIconsFrom(wrapper);
        themeName.setTextColor(wrapper.getResources().getColor(darkIcons ? android.R.color.black : android.R.color.white));

        accentColorView.setBackgroundColor(accentColor);

        themeName.setText(theme.getThemeName());
    }
}
